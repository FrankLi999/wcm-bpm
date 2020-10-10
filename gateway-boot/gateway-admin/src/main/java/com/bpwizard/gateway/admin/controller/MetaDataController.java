/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * Contributor license agreements.See the NOTICE file distributed with
 * This work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * he License.You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bpwizard.gateway.admin.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.gateway.admin.dto.BatchCommonDTO;
import com.bpwizard.gateway.admin.dto.MetaDataDTO;
import com.bpwizard.gateway.admin.page.CommonPager;
import com.bpwizard.gateway.admin.page.PageParameter;
import com.bpwizard.gateway.admin.query.MetaDataQuery;
import com.bpwizard.gateway.admin.result.GatewayAdminResult;
import com.bpwizard.gateway.admin.service.MetaDataService;
import com.bpwizard.gateway.admin.vo.MetaDataVO;

import java.util.List;

/**
 * The type Meta data controller.
 */
@RestController
@RequestMapping("/gateway-admin/api/meta-data")
public class MetaDataController {

    private final MetaDataService metaDataService;

    /**
     * Instantiates a new Meta data controller.
     *
     * @param metaDataService the meta data service
     */
    @Autowired(required = false)
    public MetaDataController(final MetaDataService metaDataService) {
        this.metaDataService = metaDataService;
    }

    /**
     * Query list gateway result.
     *
     * @param appName     the app name
     * @param currentPage the current page
     * @param pageSize    the page size
     * @return the gateway result
     */
    @GetMapping("/queryList")
    public GatewayAdminResult queryList(final String appName, final Integer currentPage, final Integer pageSize) {
        CommonPager<MetaDataVO> commonPager = metaDataService.listByPage(new MetaDataQuery(appName, new PageParameter(currentPage, pageSize)));
        return GatewayAdminResult.success("query  success", commonPager);
    }

    /**
     * Find all gateway result.
     *
     * @return the gateway result
     */
    @GetMapping("/findAll")
    public GatewayAdminResult findAll() {
        return GatewayAdminResult.success("query success", metaDataService.findAll());
    }

    /**
     * Find all group gateway result.
     *
     * @return the gateway result
     */
    @GetMapping("/findAllGroup")
    public GatewayAdminResult findAllGroup() {
        return GatewayAdminResult.success("query success", metaDataService.findAllGroup());
    }
    
    /**
     * Detail app auth gateway result.
     *
     * @param id the id
     * @return the gateway result
     */
    @GetMapping("/{id}")
    public GatewayAdminResult editor(@PathVariable("id") final String id) {
        MetaDataVO metaDataVO = metaDataService.findById(id);
        return GatewayAdminResult.success("detail success", metaDataVO);
    }

    /**
     * Create or update gateway result.
     *
     * @param metaDataDTO the meta data dto
     * @return the gateway result
     */
    @PostMapping("/createOrUpdate")
    public GatewayAdminResult createOrUpdate(@RequestBody final MetaDataDTO metaDataDTO) {
        String result = metaDataService.createOrUpdate(metaDataDTO);
        if (StringUtils.isNoneBlank(result)) {
            return GatewayAdminResult.error(result);
        }
        return GatewayAdminResult.success("create success");
    }
    
    /**
     * Batch deleted gateway result.
     *
     * @param ids the ids
     * @return the gateway result
     */
    @PostMapping("/batchDeleted")
    public GatewayAdminResult batchDeleted(@RequestBody final List<String> ids) {
        Integer deleteCount = metaDataService.delete(ids);
        return GatewayAdminResult.success("delete  success", deleteCount);
    }
    
    /**
     * Batch enabled gateway result.
     *
     * @param batchCommonDTO the batch common dto
     * @return the gateway result
     */
    @PostMapping("/batchEnabled")
    public GatewayAdminResult batchEnabled(@RequestBody final BatchCommonDTO batchCommonDTO) {
        final String result = metaDataService.enabled(batchCommonDTO.getIds(), batchCommonDTO.getEnabled());
        if (StringUtils.isNoneBlank(result)) {
            return GatewayAdminResult.error(result);
        }
        return GatewayAdminResult.success("enable success");
    }


    /**
     * Sync data gateway result.
     *
     * @return the gateway result
     */
    @PostMapping("/syncData")
    public GatewayAdminResult syncData() {
        metaDataService.syncData();
        return GatewayAdminResult.success();
    }
}
