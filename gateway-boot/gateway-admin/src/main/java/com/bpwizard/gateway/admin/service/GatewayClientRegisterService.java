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

package com.bpwizard.gateway.admin.service;

import com.bpwizard.gateway.admin.dto.SpringCloudRegisterDTO;
import com.bpwizard.gateway.admin.dto.SpringMvcRegisterDTO;

/**
 * The interface gateway client service.
 */
public interface GatewayClientRegisterService {
    
    /**
     * Register http string.
     *
     * @param springMvcRegisterDTO the http register dto
     * @return the string
     */
    String registerSpringMvc(SpringMvcRegisterDTO springMvcRegisterDTO);
    
    /**
     * Register spring cloud string.
     *
     * @param springCloudRegisterDTO the spring cloud register dto
     * @return the string
     */
    String registerSpringCloud(SpringCloudRegisterDTO springCloudRegisterDTO);
    
}
