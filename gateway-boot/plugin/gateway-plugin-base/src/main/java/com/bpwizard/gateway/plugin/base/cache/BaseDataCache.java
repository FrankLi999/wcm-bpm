package com.bpwizard.gateway.plugin.base.cache;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.bpwizard.gateway.common.dto.PluginData;
import com.bpwizard.gateway.common.dto.RuleData;
import com.bpwizard.gateway.common.dto.SelectorData;

/**
 * The type Base data cache.
 */
public final class BaseDataCache {
    
    private static final BaseDataCache INSTANCE = new BaseDataCache();
    
    /**
     * pluginName -> PluginData.
     */
    private static final ConcurrentMap<String, PluginData> PLUGIN_MAP = new ConcurrentHashMap<>();
    
    /**
     * pluginName -> SelectorData.
     */
    private static final ConcurrentMap<String, List<SelectorData>> SELECTOR_MAP = new ConcurrentHashMap<>();
    
    /**
     * selectorId -> RuleData.
     */
    private static final ConcurrentMap<String, List<RuleData>> RULE_MAP = new ConcurrentHashMap<>();
    
    private BaseDataCache() {
    }
    
    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static BaseDataCache getInstance() {
        return INSTANCE;
    }
    
    /**
     * Cache plugin data.
     *
     * @param data the data
     */
    public void cachePluginData(final PluginData data) {
        PLUGIN_MAP.put(data.getName(), data);
    }
    
    /**
     * Remove plugin data.
     *
     * @param data the data
     */
    public void removePluginData(final PluginData data) {
        PLUGIN_MAP.remove(data.getName());
    }
    
    /**
     * Clean plugin data.
     */
    public void cleanPluginData() {
        PLUGIN_MAP.clear();
    }
    
    /**
     * Clean plugin data self.
     *
     * @param pluginDataList the plugin data list
     */
    public void cleanPluginDataSelf(final List<PluginData> pluginDataList) {
        pluginDataList.forEach(this::removePluginData);
    }
    
    /**
     * Obtain plugin data plugin data.
     *
     * @param pluginName the plugin name
     * @return the plugin data
     */
    public PluginData obtainPluginData(final String pluginName) {
        return PLUGIN_MAP.get(pluginName);
    }
    
    /**
     * Cache select data.
     *
     * @param data the data
     */
    public void cacheSelectData(final SelectorData data) {
        String key = data.getPluginName();
        if (SELECTOR_MAP.containsKey(key)) {
            List<SelectorData> existList = SELECTOR_MAP.get(key);
            final List<SelectorData> resultList = existList.stream().filter(r -> !r.getId().equals(data.getId())).collect(Collectors.toList());
            resultList.add(data);
            final List<SelectorData> collect = resultList.stream().sorted(Comparator.comparing(SelectorData::getSort)).collect(Collectors.toList());
            SELECTOR_MAP.put(key, collect);
        } else {
            SELECTOR_MAP.put(key, Arrays.asList(data));
        }
    }
    
    /**
     * Remove select data.
     *
     * @param data the data
     */
    public void removeSelectData(final SelectorData data) {
        final List<SelectorData> selectorDataList = SELECTOR_MAP.get(data.getPluginName());
        Optional.ofNullable(selectorDataList).ifPresent(list -> list.removeIf(e -> e.getId().equals(data.getId())));
    }
    
    /**
     * Clean selector data.
     */
    public void cleanSelectorData() {
        SELECTOR_MAP.clear();
    }
    
    /**
     * Clean selector data self.
     *
     * @param selectorDataList the selector data list
     */
    public void cleanSelectorDataSelf(final List<SelectorData> selectorDataList) {
        selectorDataList.forEach(this::removeSelectData);
    }
    
    /**
     * Obtain selector data list list.
     *
     * @param pluginName the plugin name
     * @return the list
     */
    public List<SelectorData> obtainSelectorData(final String pluginName) {
        return SELECTOR_MAP.get(pluginName);
    }
    
    /**
     * Cache rule data.
     *
     * @param ruleData the rule data
     */
    public void cacheRuleData(final RuleData ruleData) {
        String selectorId = ruleData.getSelectorId();
        if (RULE_MAP.containsKey(selectorId)) {
            List<RuleData> existList = RULE_MAP.get(selectorId);
            final List<RuleData> resultList = existList.stream().filter(r -> !r.getId().equals(ruleData.getId())).collect(Collectors.toList());
            resultList.add(ruleData);
            final List<RuleData> collect = resultList.stream().sorted(Comparator.comparing(RuleData::getSort)).collect(Collectors.toList());
            RULE_MAP.put(selectorId, collect);
        } else {
            RULE_MAP.put(selectorId, Arrays.asList(ruleData));
        }
    }
    
    /**
     * Remove rule data.
     *
     * @param ruleData the rule data
     */
    public void removeRuleData(final RuleData ruleData) {
        final List<RuleData> ruleDataList = RULE_MAP.get(ruleData.getSelectorId());
        Optional.ofNullable(ruleDataList).ifPresent(list -> list.removeIf(rule -> rule.getId().equals(ruleData.getId())));
    }
    
    /**
     * Clean rule data.
     */
    public void cleanRuleData() {
        RULE_MAP.clear();
    }
    
    /**
     * Clean rule data self.
     *
     * @param ruleDataList the rule data list
     */
    public void cleanRuleDataSelf(final List<RuleData> ruleDataList) {
        ruleDataList.forEach(this::removeRuleData);
    }
    
    /**
     * Obtain rule data list list.
     *
     * @param selectorId the selector id
     * @return the list
     */
    public List<RuleData> obtainRuleData(final String selectorId) {
        return RULE_MAP.get(selectorId);
    }
}
