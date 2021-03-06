/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.bpwizard.gateway.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.gateway.admin.listener.http.HttpLongPollingDataChangedListener;
import com.bpwizard.gateway.admin.result.GatewayAdminResult;
import com.bpwizard.gateway.common.dto.ConfigData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
// import com.esotericsoftware.kryo.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * This Controller only when HttpLongPollingDataChangedListener exist, will take effect.
 */
@ConditionalOnBean(HttpLongPollingDataChangedListener.class)
@RestController
@RequestMapping("/gateway-admin/api/configs")
@Slf4j
public class ConfigController {

    @Resource
    private HttpLongPollingDataChangedListener longPollingListener;

    /**
     * Fetch configs gateway result.
     *
     * @param groupKeys the group keys
     * @return the gateway result
     */
    @GetMapping("/fetch")
    public GatewayAdminResult fetchConfigs(@NotNull final String[] groupKeys) {
        Map<String, ConfigData<?>> result = new HashMap<>();//Maps.newHashMap();
        for (String groupKey : groupKeys) {
            ConfigData<?> data = longPollingListener.fetchConfig(ConfigGroupEnum.valueOf(groupKey));
            result.put(groupKey, data);
        }
        return GatewayAdminResult.success("success", result);
    }

    /**
     * Listener.
     *
     * @param request  the request
     * @param response the response
     */
    @PostMapping(value = "/listener")
    public void listener(final HttpServletRequest request, final HttpServletResponse response) {
        longPollingListener.doLongPolling(request, response);
    }

}
