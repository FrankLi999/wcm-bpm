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

package com.bpwizard.gateway.admin.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.gateway.admin.dto.SpringCloudRegisterDTO;
import com.bpwizard.gateway.admin.dto.SpringMvcRegisterDTO;
import com.bpwizard.gateway.admin.service.GatewayClientRegisterService;

/**
 * The type Gateway client controller.
 */
@RestController
@RequestMapping("/gateway-admin/api/gateway-client")
public class GatewayClientController {
    
    private final GatewayClientRegisterService gatewayClientRegisterService;
    
    /**
     * Instantiates a new Gateway client controller.
     *
     * @param getewayClientRegisterService the gateway client register service
     */
    public GatewayClientController(final GatewayClientRegisterService getewayClientRegisterService) {
        this.gatewayClientRegisterService = getewayClientRegisterService;
    }
    
    /**
     * Register spring mvc string.
     *
     * @param springMvcRegisterDTO the spring mvc register dto
     * @return the string
     */
    @PostMapping("/springmvc-register")
    public String registerSpringMvc(@RequestBody final SpringMvcRegisterDTO springMvcRegisterDTO) {
        return this.gatewayClientRegisterService.registerSpringMvc(springMvcRegisterDTO);
    }
    
    /**
     * Register spring cloud string.
     *
     * @param springCloudRegisterDTO the spring cloud register dto
     * @return the string
     */
    @PostMapping("/springcloud-register")
    public String registerSpringCloud(@RequestBody final SpringCloudRegisterDTO springCloudRegisterDTO) {
        return this.gatewayClientRegisterService.registerSpringCloud(springCloudRegisterDTO);
    }
}
