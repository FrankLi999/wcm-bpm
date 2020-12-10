package com.bpwizard.gateway.plugin.base;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;

import com.bpwizard.gateway.common.dto.PluginData;
import com.bpwizard.gateway.common.dto.RuleData;
import com.bpwizard.gateway.common.dto.SelectorData;
import com.bpwizard.gateway.common.enums.PluginEnum;
import com.bpwizard.gateway.common.enums.SelectorTypeEnum;
import com.bpwizard.gateway.plugin.api.GatewayPlugin;
import com.bpwizard.gateway.plugin.api.GatewayPluginChain;
import com.bpwizard.gateway.plugin.base.cache.BaseDataCache;
import com.bpwizard.gateway.plugin.base.utils.CheckUtils;
import com.bpwizard.gateway.plugin.base.utils.MatchStrategyUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * abstract gateway plugin please extends.
 */
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractGatewayPlugin implements GatewayPlugin {

    /**
     * this is Template Method child has Implement your own logic.
     *
     * @param exchange exchange the current server exchange {@linkplain ServerWebExchange}
     * @param chain    chain the current chain  {@linkplain ServerWebExchange}
     * @param selector selector    {@linkplain SelectorData}
     * @param rule     rule    {@linkplain RuleData}
     * @return {@code Mono<Void>} to indicate when request handling is complete
     */
    protected abstract Mono<Void> doExecute(ServerWebExchange exchange, GatewayPluginChain chain, SelectorData selector, RuleData rule);

    /**
     * Process the Web request and (optionally) delegate to the next
     * {@code GatewayPlugin} through the given {@link GatewayPluginChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next plugin
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    @Override
    public Mono<Void> execute(final ServerWebExchange exchange, final GatewayPluginChain chain) {
        String pluginName = named();
        final PluginData pluginData = BaseDataCache.getInstance().obtainPluginData(pluginName);
        if (pluginData != null && pluginData.getEnabled()) {
            final Collection<SelectorData> selectors = BaseDataCache.getInstance().obtainSelectorData(pluginName);
            if (ObjectUtils.isEmpty(selectors)) {
                return CheckUtils.checkSelector(pluginName, exchange, chain);
            }
            final SelectorData selectorData = matchSelector(exchange, selectors);
            if (Objects.isNull(selectorData)) {
                if (PluginEnum.WAF.getName().equals(pluginName)) {
                    return doExecute(exchange, chain, null, null);
                }
                return CheckUtils.checkSelector(pluginName, exchange, chain);
            }
            if (selectorData.getLoged()) {
                log.info("{} selector success match , selector name :{}", pluginName, selectorData.getName());
            }
            final List<RuleData> rules = BaseDataCache.getInstance().obtainRuleData(selectorData.getId());
            if (ObjectUtils.isEmpty(rules)) {
                if (PluginEnum.WAF.getName().equals(pluginName)) {
                    return doExecute(exchange, chain, null, null);
                }
                return CheckUtils.checkRule(pluginName, exchange, chain);
            }
            RuleData rule;
            if (selectorData.getType() == SelectorTypeEnum.FULL_FLOW.getCode()) {
                //get last
                rule = rules.get(rules.size() - 1);
            } else {
                rule = matchRule(exchange, rules);
            }
            if (Objects.isNull(rule)) {
                return CheckUtils.checkRule(pluginName, exchange, chain);
            }
            if (rule.getLoged()) {
                log.info("{} rule success match ,rule name :{}", pluginName, rule.getName());
            }
            return doExecute(exchange, chain, selectorData, rule);
        }
        return chain.execute(exchange);
    }

    private SelectorData matchSelector(final ServerWebExchange exchange, final Collection<SelectorData> selectors) {
        return selectors.stream()
                .filter(selector -> selector.getEnabled() && filterSelector(selector, exchange))
                .findFirst().orElse(null);
    }

    private Boolean filterSelector(final SelectorData selector, final ServerWebExchange exchange) {
        if (selector.getType() == SelectorTypeEnum.CUSTOM_FLOW.getCode()) {
            if (ObjectUtils.isEmpty(selector.getConditionList())) {
                return false;
            }
            return MatchStrategyUtils.match(selector.getMatchMode(), selector.getConditionList(), exchange);
        }
        return true;
    }

    private RuleData matchRule(final ServerWebExchange exchange, final Collection<RuleData> rules) {
        return rules.stream()
                .filter(rule -> filterRule(rule, exchange))
                .findFirst().orElse(null);
    }

    private Boolean filterRule(final RuleData ruleData, final ServerWebExchange exchange) {
        return ruleData.getEnabled() && MatchStrategyUtils.match(ruleData.getMatchMode(), ruleData.getConditionDataList(), exchange);
    }

}
