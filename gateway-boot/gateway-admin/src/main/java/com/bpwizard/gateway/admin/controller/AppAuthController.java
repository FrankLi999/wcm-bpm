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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.gateway.admin.dto.AppAuthDTO;
import com.bpwizard.gateway.admin.dto.AuthApplyDTO;
import com.bpwizard.gateway.admin.dto.AuthPathWarpDTO;
import com.bpwizard.gateway.admin.dto.BatchCommonDTO;
import com.bpwizard.gateway.admin.page.CommonPager;
import com.bpwizard.gateway.admin.page.PageParameter;
import com.bpwizard.gateway.admin.query.AppAuthQuery;
import com.bpwizard.gateway.admin.query.AppAuthQueryDTO;
import com.bpwizard.gateway.admin.result.GatewayAdminResult;
import com.bpwizard.gateway.admin.service.AppAuthService;
import com.bpwizard.gateway.admin.vo.AppAuthVO;

import java.util.List;

/**
 * this is application authority controller.
 */
@RestController
@RequestMapping("/appAuth")
public class AppAuthController {

    private final AppAuthService appAuthService;

    /**
     * Instantiates a new App auth controller.
     *
     * @param appAuthService the app auth service
     */
    @Autowired(required = false)
    public AppAuthController(final AppAuthService appAuthService) {
        this.appAuthService = appAuthService;
    }

    /**
     * Apply gateway result.
     *
     * @param authApplyDTO the auth apply dto
     * @return the gateway result
     */
    @PostMapping("/apply")
    public GatewayAdminResult apply(@RequestBody final AuthApplyDTO authApplyDTO) {
        if (StringUtils.isNoneBlank(authApplyDTO.getAppKey())) {
            return appAuthService.applyUpdate(authApplyDTO);
        } else {
            return appAuthService.applyCreate(authApplyDTO);
        }
    }

    /**
     * Update sk gateway result.
     *
     * @param appKey    the app key
     * @param appSecret the app secret
     * @return the gateway result
     */
    @GetMapping("/updateSk")
    public GatewayAdminResult updateSk(@RequestParam("appKey") final String appKey, @RequestParam("appSecret") final String appSecret) {
        return appAuthService.updateAppSecretByAppKey(appKey, appSecret);
    }


    /**
     * Find page by query gateway result.
     *
     * @param dto the dto
     * @return the gateway result
     */
    @PostMapping("/findPageByQuery")
    public GatewayAdminResult findPageByQuery(@RequestBody final AppAuthQueryDTO dto) {
        AppAuthQuery query = new AppAuthQuery();
        query.setPhone(dto.getPhone());
        query.setAppKey(dto.getAppKey());
        query.setPageParameter(new PageParameter(dto.getCurrentPage(), dto.getPageSize()));
        CommonPager<AppAuthVO> commonPager = appAuthService.listByPage(query);
        return GatewayAdminResult.success("query application authorities success", commonPager);
    }

    /**
     * Detail gateway result.
     *
     * @param id the id
     * @return the gateway result
     */
    @GetMapping("/detail")
    public GatewayAdminResult detail(@RequestParam("id") final String id) {
        return GatewayAdminResult.success("detail application authority success", appAuthService.findById(id));
    }

    /**
     * Update detail gateway result.
     *
     * @param appAuthDTO the app auth dto
     * @return the gateway result
     */
    @PostMapping("/updateDetail")
    public GatewayAdminResult updateDetail(@RequestBody final AppAuthDTO appAuthDTO) {
        return appAuthService.updateDetail(appAuthDTO);
    }

    /**
     * Detail path gateway result.
     *
     * @param id the id
     * @return the gateway result
     */
    @GetMapping("/detailPath")
    public GatewayAdminResult detailPath(@RequestParam("id") final String id) {
        return GatewayAdminResult.success("detailPath application success", appAuthService.detailPath(id));
    }


    /**
     * Update detail path gateway result.
     *
     * @param authPathWarpDTO the auth path warp dto
     * @return the gateway result
     */
    @PostMapping("/updateDetailPath")
    public GatewayAdminResult updateDetailPath(@RequestBody final AuthPathWarpDTO authPathWarpDTO) {
        return appAuthService.updateDetailPath(authPathWarpDTO);
    }


    /**
     * delete application authorities.
     *
     * @param ids primary key.
     * @return {@linkplain GatewayAdminResult}
     */
    @PostMapping("/batchDelete")
    public GatewayAdminResult batchDelete(@RequestBody final List<String> ids) {
        Integer deleteCount = appAuthService.delete(ids);
        return GatewayAdminResult.success("delete application authorities success", deleteCount);
    }

    /**
     * Batch enabled gateway result.
     *
     * @param batchCommonDTO the batch common dto
     * @return the gateway result
     */
    @PostMapping("/batchEnabled")
    public GatewayAdminResult batchEnabled(@RequestBody final BatchCommonDTO batchCommonDTO) {
        final String result = appAuthService.enabled(batchCommonDTO.getIds(), batchCommonDTO.getEnabled());
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
        return appAuthService.syncData();
    }

}
