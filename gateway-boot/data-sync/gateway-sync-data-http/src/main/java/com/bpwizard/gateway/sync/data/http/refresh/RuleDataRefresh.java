package com.bpwizard.gateway.sync.data.http.refresh;

import java.util.List;

import org.springframework.util.ObjectUtils;

import com.bpwizard.gateway.common.dto.ConfigData;
import com.bpwizard.gateway.common.dto.RuleData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Rule data refresh.
 */
@Slf4j
@RequiredArgsConstructor
public class RuleDataRefresh extends AbstractDataRefresh<RuleData> {

    private final PluginDataSubscriber pluginDataSubscriber;

    @Override
    protected JsonNode convert(final JsonNode data) {
        return data.get(ConfigGroupEnum.RULE.name());
    }

    @Override
    protected ConfigData<RuleData> fromJson(final JsonNode data) {
    	return JsonUtils.fromJson(data,new TypeReference<ConfigData<RuleData>>() { });
    }

    @Override
    protected boolean updateCacheIfNeed(final ConfigData<RuleData> result) {
        return updateCacheIfNeed(result, ConfigGroupEnum.RULE);
    }

    @Override
    public ConfigData<?> cacheConfigData() {
        return GROUP_CACHE.get(ConfigGroupEnum.RULE);
    }

    @Override
    protected void refresh(final List<RuleData> data) {
        if (ObjectUtils.isEmpty(data)) {
            log.info("clear all rule cache, old cache");
            data.forEach(pluginDataSubscriber::unRuleSubscribe);
            pluginDataSubscriber.refreshRuleDataAll();
        } else {
            // update cache for UpstreamCacheManager
            pluginDataSubscriber.refreshRuleDataAll();
            data.forEach(pluginDataSubscriber::onRuleSubscribe);
        }
    }
}
