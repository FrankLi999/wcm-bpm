package com.bpwizard.gateway.sync.data.http.refresh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.bpwizard.gateway.common.dto.ConfigData;
import com.bpwizard.gateway.common.dto.MetaData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.MetaDataSubscriber;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test cases for {@link MetaDataRefresh}.
 */
public class MetaDataRefreshTest {
    
    private final MetaDataRefresh mockMetaDataRefresh = this.buildMockMetaDataRefresh();
    
    /**
     * test case for {@link MetaDataRefresh#convert(JsonObject)}.
     */
    @Test
    public void testConvert() {
        ObjectNode jsonObject = JsonUtils.createObjectNode();
        ObjectNode expectJsonObject = JsonUtils.createObjectNode();
        jsonObject.set(ConfigGroupEnum.META_DATA.name(), expectJsonObject);
        Assertions.assertEquals(expectJsonObject, mockMetaDataRefresh.convert(jsonObject));
    }
    
    /**
     * test case for {@link MetaDataRefresh#fromJson(JsonObject)}.
     */
    @Test
    public void testFromJson() {
        ConfigData<MetaData> metaDataConfigData = new ConfigData<>();
        MetaData metaData = new MetaData();
        metaDataConfigData.setData(Collections.singletonList(metaData));
        JsonNode jsonObject = JsonUtils.toJsonNode(metaDataConfigData);
        Assertions.assertEquals(metaDataConfigData, mockMetaDataRefresh.fromJson(jsonObject));
    }
    
    /**
     * This case coverages the following method:
     * {@link MetaDataRefresh#cacheConfigData()}
     * {@link MetaDataRefresh#updateCacheIfNeed(ConfigData)}.
     * For {@link SelectorDataRefresh} inherits from {@link AbstractDataRefresh}, the {@link AbstractDataRefresh#GROUP_CACHE} was initialized when the class of
     * {@link AbstractDataRefresh} load, in two different test methods in this class, the the {@link AbstractDataRefresh#GROUP_CACHE} class only load once, so
     * the method which manipulate the {@link AbstractDataRefresh#GROUP_CACHE} invocation has aftereffects to the other methods.
     */
    @Test
    public void testUpdateCacheIfNeed() {
        final MetaDataRefresh metaDataRefresh = mockMetaDataRefresh;
        // update cache, then assert equals
        ConfigData<MetaData> expect = new ConfigData<>();
        expect.setLastModifyTime(System.currentTimeMillis());
        metaDataRefresh.updateCacheIfNeed(expect);
        Assertions.assertEquals(expect, metaDataRefresh.cacheConfigData());
    }
    
    /**
     * This case is only for {@link MetaDataRefresh} code coverage.
     */
    @Test
    public void testRefreshCoverage() {
        final MetaDataRefresh metaDataRefresh = mockMetaDataRefresh;
        MetaData metaData = new MetaData();
        List<MetaData> metaDataList = new ArrayList<>();
        metaDataRefresh.refresh(metaDataList);
        metaDataList.add(metaData);
        metaDataRefresh.refresh(metaDataList);
        
    }
    
    private MetaDataRefresh buildMockMetaDataRefresh() {
        List<MetaDataSubscriber> metaDataSubscribers = new ArrayList<>();
        metaDataSubscribers.add(new MetaDataSubscriber() {
            @Override
            public void onSubscribe(final MetaData metaData) {
            
            }
            
            @Override
            public void unSubscribe(final MetaData metaData) {
            
            }
        });
        return new MetaDataRefresh(metaDataSubscribers);
    }
    
}
