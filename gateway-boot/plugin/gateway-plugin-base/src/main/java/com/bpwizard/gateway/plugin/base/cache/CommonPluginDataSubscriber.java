package com.bpwizard.gateway.plugin.base.cache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.bpwizard.gateway.common.dto.PluginData;
import com.bpwizard.gateway.common.dto.RuleData;
import com.bpwizard.gateway.common.dto.SelectorData;
import com.bpwizard.gateway.plugin.base.handler.PluginDataHandler;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;

/**
 * The type Common plugin data subscriber.
 */
public class CommonPluginDataSubscriber implements PluginDataSubscriber {
    
    private final Map<String, PluginDataHandler> handlerMap;
    
    /**
     * Instantiates a new Common plugin data subscriber.
     *
     * @param pluginDataHandlerList the plugin data handler list
     */
    public CommonPluginDataSubscriber(final List<PluginDataHandler> pluginDataHandlerList) {
        this.handlerMap = pluginDataHandlerList.stream().collect(Collectors.toConcurrentMap(PluginDataHandler::pluginNamed, e -> e));
    }
    
    @Override
    public void onSubscribe(final PluginData pluginData) {
        BaseDataCache.getInstance().cachePluginData(pluginData);
        Optional.ofNullable(handlerMap.get(pluginData.getName())).ifPresent(handler -> handler.handlerPlugin(pluginData));
    }
    
    @Override
    public void unSubscribe(final PluginData pluginData) {
        BaseDataCache.getInstance().removePluginData(pluginData);
        Optional.ofNullable(handlerMap.get(pluginData.getName())).ifPresent(handler -> handler.removePlugin(pluginData));
    }
    
    @Override
    public void refreshPluginDataAll() {
        BaseDataCache.getInstance().cleanPluginData();
    }
    
    @Override
    public void refreshPluginDataSelf(final List<PluginData> pluginDataList) {
        if (ObjectUtils.isEmpty(pluginDataList)) {
            return;
        }
        BaseDataCache.getInstance().cleanPluginDataSelf(pluginDataList);
    }
    
    @Override
    public void onSelectorSubscribe(final SelectorData selectorData) {
        BaseDataCache.getInstance().cacheSelectData(selectorData);
        Optional.ofNullable(handlerMap.get(selectorData.getPluginName())).ifPresent(handler -> handler.handlerSelector(selectorData));
    }
    
    @Override
    public void unSelectorSubscribe(final SelectorData selectorData) {
        BaseDataCache.getInstance().removeSelectData(selectorData);
        Optional.ofNullable(handlerMap.get(selectorData.getPluginName())).ifPresent(handler -> handler.removeSelector(selectorData));
    }
    
    @Override
    public void refreshSelectorDataAll() {
        BaseDataCache.getInstance().cleanSelectorData();
    }
    
    @Override
    public void refreshSelectorDataSelf(final List<SelectorData> selectorDataList) {
        if (CollectionUtils.isEmpty(selectorDataList)) {
            return;
        }
        BaseDataCache.getInstance().cleanSelectorDataSelf(selectorDataList);
    }
    
    @Override
    public void onRuleSubscribe(final RuleData ruleData) {
        BaseDataCache.getInstance().cacheRuleData(ruleData);
        Optional.ofNullable(handlerMap.get(ruleData.getPluginName())).ifPresent(handler -> handler.handlerRule(ruleData));
    }
    
    @Override
    public void unRuleSubscribe(final RuleData ruleData) {
        BaseDataCache.getInstance().removeRuleData(ruleData);
        Optional.ofNullable(handlerMap.get(ruleData.getPluginName())).ifPresent(handler -> handler.removeRule(ruleData));
    }
    
    @Override
    public void refreshRuleDataAll() {
        BaseDataCache.getInstance().cleanRuleData();
    }
    
    @Override
    public void refreshRuleDataSelf(final List<RuleData> ruleDataList) {
        if (CollectionUtils.isEmpty(ruleDataList)) {
            return;
        }
        BaseDataCache.getInstance().cleanRuleDataSelf(ruleDataList);
    }
}
