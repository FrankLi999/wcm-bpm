package com.bpwizard.gateway.sync.data.http.refresh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.bpwizard.gateway.common.dto.ConfigData;
import com.bpwizard.gateway.common.dto.PluginData;
import com.bpwizard.gateway.common.dto.SelectorData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test cases for {@link SelectorDataRefresh}.
 */
public class SelectorDataRefreshTest {
    
    private final SelectorDataRefresh mockSelectorDataRefresh = new SelectorDataRefresh(new PluginDataSubscriber() {
        @Override
        public void onSubscribe(final PluginData pluginData) {
        
        }
    });
    
    
    /**
     * test case for {@link SelectorDataRefresh#convert(JsonObject)}.
     */
    @Test
    public void testConvert() {
        ObjectNode jsonObject = JsonUtils.createObjectNode();
        ObjectNode expectJsonObject = JsonUtils.createObjectNode();
        jsonObject.set(ConfigGroupEnum.SELECTOR.name(), expectJsonObject);
        Assertions.assertEquals(expectJsonObject, mockSelectorDataRefresh.convert(jsonObject));
    }
    
    /**
     * test case for {@link SelectorDataRefresh#fromJson(JsonObject)}.
     */
    @Test
    public void testFromJson() {
        ConfigData<SelectorData> selectorDataConfigData = new ConfigData<>();
        SelectorData selectorData = new SelectorData();
        selectorDataConfigData.setData(Collections.singletonList(selectorData));
        JsonNode jsonObject = JsonUtils.toJsonNode(selectorDataConfigData);
        Assertions.assertEquals(selectorDataConfigData, mockSelectorDataRefresh.fromJson(jsonObject));
    }
    
    /**
     * This case coverages the following method:
     * {@link SelectorDataRefresh#cacheConfigData()}
     * {@link SelectorDataRefresh#updateCacheIfNeed(ConfigData)}.
     * For {@link SelectorDataRefresh} inherits from {@link AbstractDataRefresh}, the {@link AbstractDataRefresh#GROUP_CACHE} was initialized when the class of
     * {@link AbstractDataRefresh} load, in two different test methods in this class, the the {@link AbstractDataRefresh#GROUP_CACHE} class only load once, so
     * the method which manipulate the {@link AbstractDataRefresh#GROUP_CACHE} invocation has aftereffects to the other methods.
     */
    @Test
    public void testUpdateCacheIfNeed() {
        final SelectorDataRefresh selectorDataRefresh = mockSelectorDataRefresh;
        // update cache, then assert equals
        ConfigData<SelectorData> expect = new ConfigData<>();
        expect.setLastModifyTime(System.currentTimeMillis());
        selectorDataRefresh.updateCacheIfNeed(expect);
        Assertions.assertEquals(expect, selectorDataRefresh.cacheConfigData());
    }
    
    /**
     * This case is only for {@link SelectorDataRefresh} code coverage.
     */
    @Test
    public void testRefreshCoverage() {
        final SelectorDataRefresh selectorDataRefresh = mockSelectorDataRefresh;
        SelectorData selectorData = new SelectorData();
        List<SelectorData> selectorDataList = new ArrayList<>();
        selectorDataRefresh.refresh(selectorDataList);
        selectorDataList.add(selectorData);
        selectorDataRefresh.refresh(selectorDataList);
        
    }
}
