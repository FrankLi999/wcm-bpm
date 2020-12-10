package com.bpwizard.gateway.sync.data.http.refresh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.bpwizard.gateway.common.dto.ConfigData;
import com.bpwizard.gateway.common.dto.PluginData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test cases for {@link PluginDataRefresh}.
 */
public class PluginDataRefreshTest {
    
    private final PluginDataRefresh mockPluginDataRefresh = new PluginDataRefresh(new PluginDataSubscriber() {
        @Override
        public void onSubscribe(final PluginData pluginData) {
        
        }
    });
    
    /**
     * test case for {@link PluginDataRefresh#convert(JsonObject)}.
     */
    @Test
    public void testConvert() {
        ObjectNode jsonObject = JsonUtils.createObjectNode();
        ObjectNode expectJsonObject = JsonUtils.createObjectNode();
        jsonObject.set(ConfigGroupEnum.PLUGIN.name(), expectJsonObject);
        Assertions.assertEquals(expectJsonObject, mockPluginDataRefresh.convert(jsonObject));
    }
    
    /**
     * test case for {@link PluginDataRefresh#fromJson(JsonObject)}.
     */
    @Test
    public void testFromJson() {
        ConfigData<PluginData> pluginDataConfigData = new ConfigData<>();
        PluginData pluginData = new PluginData();
        pluginDataConfigData.setData(Collections.singletonList(pluginData));
        JsonNode jsonObject = JsonUtils.toJsonNode(pluginDataConfigData);
        Assertions.assertEquals(pluginDataConfigData, mockPluginDataRefresh.fromJson(jsonObject));

    }
    
    /**
     * This case coverages the following method:
     * {@link PluginDataRefresh#cacheConfigData()}
     * {@link PluginDataRefresh#updateCacheIfNeed(ConfigData)}.
     * For {@link SelectorDataRefresh} inherits from {@link AbstractDataRefresh}, the {@link AbstractDataRefresh#GROUP_CACHE} was initialized when the class of
     * {@link AbstractDataRefresh} load, in two different test methods in this class, the the {@link AbstractDataRefresh#GROUP_CACHE} class only load once, so
     * the method which manipulate the {@link AbstractDataRefresh#GROUP_CACHE} invocation has aftereffects to the other methods
     */
    @Test
    public void testUpdateCacheIfNeed() {
        final PluginDataRefresh pluginDataRefresh = mockPluginDataRefresh;
        // update cache, then assert equals
        ConfigData<PluginData> expect = new ConfigData<>();
        expect.setLastModifyTime(System.currentTimeMillis());
        pluginDataRefresh.updateCacheIfNeed(expect);
        Assertions.assertEquals(expect, pluginDataRefresh.cacheConfigData());
    }
    
    /**
     * This case is only for {@link PluginDataRefresh} code coverage.
     */
    @Test
    public void testRefreshCoverage() {
        final PluginDataRefresh pluginDataRefresh = mockPluginDataRefresh;
        PluginData selectorData = new PluginData();
        List<PluginData> selectorDataList = new ArrayList<>();
        pluginDataRefresh.refresh(selectorDataList);
        selectorDataList.add(selectorData);
        pluginDataRefresh.refresh(selectorDataList);
        
    }
}
