/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bpwizard.gateway.sync.data.http.refresh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.bpwizard.gateway.common.dto.ConfigData;
import com.bpwizard.gateway.common.dto.PluginData;
import com.bpwizard.gateway.common.dto.RuleData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test cases for {@link RuleDataRefresh}.
 */
public class RuleDataRefreshTest {
    
    private final RuleDataRefresh mockRuleDataRefresh = new RuleDataRefresh(new PluginDataSubscriber() {
        @Override
        public void onSubscribe(final PluginData pluginData) {
        
        }
    });
    
    /**
     * test case for {@link RuleDataRefresh#convert(JsonObject)}.
     */
    @Test
    public void testConvert() {
        ObjectNode jsonObject = JsonUtils.createObjectNode();
        ObjectNode expectJsonObject = JsonUtils.createObjectNode();
        jsonObject.set(ConfigGroupEnum.RULE.name(), expectJsonObject);
        Assertions.assertEquals(expectJsonObject, mockRuleDataRefresh.convert(jsonObject));
    }
    
    /**
     * test case for {@link RuleDataRefresh#fromJson(JsonObject)}.
     */
    @Test
    public void testFromJson() {
        ConfigData<RuleData> ruleDataConfigData = new ConfigData<>();
        RuleData ruleData = new RuleData();
        ruleDataConfigData.setData(Collections.singletonList(ruleData));
        JsonNode jsonObject = JsonUtils.toJsonNode(ruleDataConfigData);
        Assertions.assertEquals(ruleDataConfigData, mockRuleDataRefresh.fromJson(jsonObject));
    }
    
    /**
     * This case coverages the following method:
     * {@link RuleDataRefresh#cacheConfigData()}
     * {@link RuleDataRefresh#updateCacheIfNeed(ConfigData)}.
     * For {@link RuleDataRefresh} inherits from {@link AbstractDataRefresh}, the {@link AbstractDataRefresh#GROUP_CACHE} was initialized when the class of
     * {@link AbstractDataRefresh} load, in two different test methods in this class, the the {@link AbstractDataRefresh#GROUP_CACHE} class only load once, so
     * the method which manipulate the {@link AbstractDataRefresh#GROUP_CACHE} invocation has aftereffects to the other methods.
     */
    @Test
    public void testUpdateCacheIfNeed() {
        final RuleDataRefresh ruleDataRefresh = mockRuleDataRefresh;
        // update cache, then assert equals
        ConfigData<RuleData> expect = new ConfigData<>();
        expect.setLastModifyTime(System.currentTimeMillis());
        ruleDataRefresh.updateCacheIfNeed(expect);
        Assertions.assertEquals(expect, ruleDataRefresh.cacheConfigData());
    }
    
    /**
     * This case is only for {@link RuleDataRefresh} code coverage.
     */
    @Test
    public void testRefresh() {
        final RuleDataRefresh ruleDataRefresh = mockRuleDataRefresh;
        RuleData ruleData = new RuleData();
        List<RuleData> ruleDataList = new ArrayList<>();
        ruleDataRefresh.refresh(ruleDataList);
        ruleDataList.add(ruleData);
        ruleDataRefresh.refresh(ruleDataList);
        
    }
}
