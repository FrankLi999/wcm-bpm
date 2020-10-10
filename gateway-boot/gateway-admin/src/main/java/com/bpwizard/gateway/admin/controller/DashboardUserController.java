/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.bpwizard.gateway.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.gateway.admin.dto.DashboardUserDTO;
import com.bpwizard.gateway.admin.page.CommonPager;
import com.bpwizard.gateway.admin.page.PageParameter;
import com.bpwizard.gateway.admin.query.DashboardUserQuery;
import com.bpwizard.gateway.admin.result.GatewayAdminResult;
import com.bpwizard.gateway.admin.service.DashboardUserService;
import com.bpwizard.gateway.admin.vo.DashboardUserVO;

import java.util.List;
import java.util.Objects;

/**
 * this is dashboard user controller.
 */
@RestController
@RequestMapping("/gateway-admin/api/dashboardUser")
public class DashboardUserController {

    private final DashboardUserService dashboardUserService;

    @Autowired(required = false)
    public DashboardUserController(final DashboardUserService dashboardUserService) {
        this.dashboardUserService = dashboardUserService;
    }

    /**
     * query dashboard users.
     *
     * @param userName    user name
     * @param currentPage current page
     * @param pageSize    page size
     * @return {@linkplain GatewayAdminResult}
     */
    @GetMapping("")
    public GatewayAdminResult queryDashboardUsers(final String userName, final Integer currentPage, final Integer pageSize) {
        CommonPager<DashboardUserVO> commonPager = dashboardUserService.listByPage(new DashboardUserQuery(userName, new PageParameter(currentPage, pageSize)));
        return GatewayAdminResult.success("query dashboard users success", commonPager);
    }

    /**
     * detail dashboard user.
     *
     * @param id dashboard user id.
     * @return {@linkplain GatewayAdminResult}
     */
    @GetMapping("/{id}")
    public GatewayAdminResult detailDashboardUser(@PathVariable("id") final String id) {
        DashboardUserVO dashboardUserVO = dashboardUserService.findById(id);
        return GatewayAdminResult.success("detail dashboard user success", dashboardUserVO);
    }

    /**
     * create dashboard user.
     *
     * @param dashboardUserDTO dashboard user.
     * @return {@linkplain GatewayAdminResult}
     */
    @PostMapping("")
    public GatewayAdminResult createDashboardUser(@RequestBody final DashboardUserDTO dashboardUserDTO) {
        Integer createCount = dashboardUserService.createOrUpdate(dashboardUserDTO);
        return GatewayAdminResult.success("create dashboard user success", createCount);
    }

    /**
     * update dashboard user.
     *
     * @param id               primary key.
     * @param dashboardUserDTO dashboard user.
     * @return {@linkplain GatewayAdminResult}
     */
    @PutMapping("/{id}")
    public GatewayAdminResult updateDashboardUser(@PathVariable("id") final String id, @RequestBody final DashboardUserDTO dashboardUserDTO) {
        Objects.requireNonNull(dashboardUserDTO);
        dashboardUserDTO.setId(id);
        Integer updateCount = dashboardUserService.createOrUpdate(dashboardUserDTO);
        return GatewayAdminResult.success("update dashboard user success", updateCount);
    }

    /**
     * delete dashboard users.
     *
     * @param ids primary key.
     * @return {@linkplain GatewayAdminResult}
     */
    @DeleteMapping("/batch")
    public GatewayAdminResult deleteDashboardUser(@RequestBody final List<String> ids) {
        Integer deleteCount = dashboardUserService.delete(ids);
        return GatewayAdminResult.success("delete dashboard users success", deleteCount);
    }
}
