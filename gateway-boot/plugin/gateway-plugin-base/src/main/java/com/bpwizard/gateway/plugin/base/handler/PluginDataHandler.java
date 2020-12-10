package com.bpwizard.gateway.plugin.base.handler;

import com.bpwizard.gateway.common.dto.PluginData;
import com.bpwizard.gateway.common.dto.RuleData;
import com.bpwizard.gateway.common.dto.SelectorData;

/**
 * The interface Plugin data handler.
 */
public interface PluginDataHandler {
    
    /**
     * Handler plugin.
     *
     * @param pluginData the plugin data
     */
    default void handlerPlugin(PluginData pluginData) {
    }
    
    /**
     * Remove plugin.
     *
     * @param pluginData the plugin data
     */
    default void removePlugin(PluginData pluginData) {
    }
    
    /**
     * Handler selector.
     *
     * @param selectorData the selector data
     */
    default void handlerSelector(SelectorData selectorData) {
    }
    
    /**
     * Remove selector.
     *
     * @param selectorData the selector data
     */
    default void removeSelector(SelectorData selectorData) {
    }
    
    /**
     * Handler rule.
     *
     * @param ruleData the rule data
     */
    default void handlerRule(RuleData ruleData) {
    }
    
    /**
     * Remove rule.
     *
     * @param ruleData the rule data
     */
    default void removeRule(RuleData ruleData) {
    }
    
    /**
     * Plugin named string.
     *
     * @return the string
     */
    String pluginNamed();
    
}
