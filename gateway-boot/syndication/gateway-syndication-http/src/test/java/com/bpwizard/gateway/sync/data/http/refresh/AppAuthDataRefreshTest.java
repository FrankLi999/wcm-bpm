package com.bpwizard.gateway.sync.data.http.refresh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.bpwizard.gateway.common.dto.AppAuthData;
import com.bpwizard.gateway.common.dto.ConfigData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.AuthDataSubscriber;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test cases for {@link AppAuthDataRefresh}.
 */
public class AppAuthDataRefreshTest {
    
    private final AppAuthDataRefresh mockAppAuthDataRefresh = this.buildMockAppAuthDataRefresh();
    
    /**
     * test case for {@link AppAuthDataRefresh#convert(JsonObject)}.
     */
    @Test
    public void testConvert() {
    	ObjectNode jsonObject = JsonUtils.createObjectNode();
    	ObjectNode expectJsonObject  = JsonUtils.createObjectNode();
        jsonObject.set(ConfigGroupEnum.APP_AUTH.name(), expectJsonObject);
        Assertions.assertEquals(expectJsonObject, mockAppAuthDataRefresh.convert(jsonObject));
    }
    
    /**
     * test case for {@link AppAuthDataRefresh#fromJson(JsonObject)}.
     */
    @Test
    public void testFromJson() {
        ConfigData<AppAuthData> appAuthDataConfigData = new ConfigData<>();
        AppAuthData appAuthData = new AppAuthData();
        appAuthDataConfigData.setData(Collections.singletonList(appAuthData));
        JsonNode jsonObject = JsonUtils.toJsonNode(appAuthDataConfigData);
        Assertions.assertEquals(appAuthDataConfigData, mockAppAuthDataRefresh.fromJson(jsonObject));
    }
    
    /**
     * This case coverages the following method:
     * {@link AppAuthDataRefresh#cacheConfigData()}
     * {@link AppAuthDataRefresh#updateCacheIfNeed(ConfigData)}.
     * For {@link SelectorDataRefresh} inherits from {@link AbstractDataRefresh}, the {@link AbstractDataRefresh#GROUP_CACHE} was initialized when the class of
     * {@link AbstractDataRefresh} load, in two different test methods in this class, the the {@link AbstractDataRefresh#GROUP_CACHE} class only load once, so
     * the method which manipulate the {@link AbstractDataRefresh#GROUP_CACHE} invocation has aftereffects to the other methods.
     */
    @Test
    public void testUpdateCacheIfNeed() {
        final AppAuthDataRefresh appAuthDataRefresh = mockAppAuthDataRefresh;
        // update cache, then assert equals
        ConfigData<AppAuthData> expect = new ConfigData<>();
        expect.setLastModifyTime(System.currentTimeMillis());
        appAuthDataRefresh.updateCacheIfNeed(expect);
        Assertions.assertEquals(expect, appAuthDataRefresh.cacheConfigData());
    }
    
    /**
     * This case is only for {@link AppAuthDataRefresh} code coverage.
     */
    @Test
    public void testRefreshCoverage() {
        final AppAuthDataRefresh appAuthDataRefresh = mockAppAuthDataRefresh;
        AppAuthData appAuthData = new AppAuthData();
        List<AppAuthData> appAuthDataList = new ArrayList<>();
        appAuthDataRefresh.refresh(appAuthDataList);
        appAuthDataList.add(appAuthData);
        appAuthDataRefresh.refresh(appAuthDataList);
        
    }
    
    private AppAuthDataRefresh buildMockAppAuthDataRefresh() {
        List<AuthDataSubscriber> authDataSubscribers = new ArrayList<>();
        authDataSubscribers.add(new AuthDataSubscriber() {
            @Override
            public void onSubscribe(final AppAuthData appAuthData) {
            
            }
            
            @Override
            public void unSubscribe(final AppAuthData appAuthData) {
            
            }
        });
        return new AppAuthDataRefresh(authDataSubscribers);
    }
    
}
