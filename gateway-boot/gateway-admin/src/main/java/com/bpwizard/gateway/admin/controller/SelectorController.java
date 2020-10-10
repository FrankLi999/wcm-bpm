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

import com.bpwizard.gateway.admin.dto.SelectorDTO;
import com.bpwizard.gateway.admin.page.CommonPager;
import com.bpwizard.gateway.admin.page.PageParameter;
import com.bpwizard.gateway.admin.query.SelectorQuery;
import com.bpwizard.gateway.admin.result.GatewayAdminResult;
import com.bpwizard.gateway.admin.service.SelectorService;
import com.bpwizard.gateway.admin.vo.SelectorVO;

import java.util.List;
import java.util.Objects;

/**
 * this is selector controller.
 */
@RestController
@RequestMapping("/gateway-admin/api/selector")
public class SelectorController {

    private final SelectorService selectorService;

    @Autowired(required = false)
    public SelectorController(final SelectorService selectorService) {
        this.selectorService = selectorService;
    }

    /**
     * query Selectors.
     *
     * @param pluginId    plugin id.
     * @param currentPage current page.
     * @param pageSize    page size.
     * @return {@linkplain GatewayAdminResult}
     */
    @GetMapping("")
    public GatewayAdminResult querySelectors(final String pluginId, final Integer currentPage, final Integer pageSize) {
        CommonPager<SelectorVO> commonPager = selectorService.listByPage(new SelectorQuery(pluginId, new PageParameter(currentPage, pageSize)));
        return GatewayAdminResult.success("query selectors success", commonPager);
    }

    /**
     * detail selector.
     *
     * @param id selector id.
     * @return {@linkplain GatewayAdminResult}
     */
    @GetMapping("/{id}")
    public GatewayAdminResult detailSelector(@PathVariable("id") final String id) {
        SelectorVO selectorVO = selectorService.findById(id);
        return GatewayAdminResult.success("detail selector success", selectorVO);
    }

    /**
     * create selector.
     *
     * @param selectorDTO selector.
     * @return {@linkplain GatewayAdminResult}
     */
    @PostMapping("")
    public GatewayAdminResult createSelector(@RequestBody final SelectorDTO selectorDTO) {
        Integer createCount = selectorService.createOrUpdate(selectorDTO);
        return GatewayAdminResult.success("create selector success", createCount);
    }

    /**
     * update Selector.
     *
     * @param id          primary key.
     * @param selectorDTO selector.
     * @return {@linkplain GatewayAdminResult}
     */
    @PutMapping("/{id}")
    public GatewayAdminResult updateSelector(@PathVariable("id") final String id, @RequestBody final SelectorDTO selectorDTO) {
        Objects.requireNonNull(selectorDTO);
        selectorDTO.setId(id);
        Integer updateCount = selectorService.createOrUpdate(selectorDTO);
        return GatewayAdminResult.success("update selector success", updateCount);
    }

    /**
     * delete Selectors.
     *
     * @param ids primary key.
     * @return {@linkplain GatewayAdminResult}
     */
    @DeleteMapping("/batch")
    public GatewayAdminResult deleteSelector(@RequestBody final List<String> ids) {
        Integer deleteCount = selectorService.delete(ids);
        return GatewayAdminResult.success("delete selectors success", deleteCount);
    }
}
