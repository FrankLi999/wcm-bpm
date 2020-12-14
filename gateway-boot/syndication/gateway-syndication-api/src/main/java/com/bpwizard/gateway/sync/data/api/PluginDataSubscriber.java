package com.bpwizard.gateway.sync.data.api;

import java.util.List;
import com.bpwizard.gateway.common.dto.PluginData;
import com.bpwizard.gateway.common.dto.RuleData;
import com.bpwizard.gateway.common.dto.SelectorData;

/**
 * The interface Plugin data subscriber.
 */
public interface PluginDataSubscriber {
    
    /**
     * On subscribe.
     *
     * @param pluginData the plugin data
     */
    default void onSubscribe(PluginData pluginData) {
    }
    
    /**
     * Un subscribe plugin data.
     *
     * @param pluginData the plugin data
     */
    default void unSubscribe(PluginData pluginData) {
    }
    
    /**
     * Refresh  all plugin data.
     */
    default void refreshPluginDataAll() {
    }
    
    /**
     * Refresh plugin data self.
     *
     * @param pluginDataList the plugin data list
     */
    default void refreshPluginDataSelf(List<PluginData> pluginDataList) {
    }
    
    /**
     * On selector subscribe.
     *
     * @param selectorData the selector data
     */
    default void onSelectorSubscribe(SelectorData selectorData) {
    }
    
    /**
     * Un selector subscribe.
     *
     * @param selectorData the selector data
     */
    default void unSelectorSubscribe(SelectorData selectorData) {
    }
    
    /**
     * Refresh all selector data.
     */
    default void refreshSelectorDataAll() {
    }
    
    /**
     * Refresh selector data.
     *
     * @param selectorDataList the selector data list
     */
    default void refreshSelectorDataSelf(List<SelectorData> selectorDataList) {
    }
    
    /**
     * On rule subscribe.
     *
     * @param ruleData the rule data
     */
    default void onRuleSubscribe(RuleData ruleData) {
    }
    
    /**
     * On rule subscribe.
     *
     * @param ruleData the rule data
     */
    default void unRuleSubscribe(RuleData ruleData) {
    }
    
    /**
     * Refresh rule data.
     */
    default void refreshRuleDataAll() {
    }
    
    /**
     * Refresh rule data self.
     *
     * @param ruleDataList the rule data list
     */
    default void refreshRuleDataSelf(List<RuleData> ruleDataList) {
    }
}
