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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.gateway.admin.dto.BatchCommonDTO;
import com.bpwizard.gateway.admin.dto.PluginDTO;
import com.bpwizard.gateway.admin.page.CommonPager;
import com.bpwizard.gateway.admin.page.PageParameter;
import com.bpwizard.gateway.admin.query.PluginQuery;
import com.bpwizard.gateway.admin.result.GatewayAdminResult;
import com.bpwizard.gateway.admin.service.PluginService;
import com.bpwizard.gateway.admin.service.SyncDataService;
import com.bpwizard.gateway.admin.vo.PluginVO;
import com.bpwizard.gateway.common.enums.DataEventTypeEnum;

import java.util.List;
import java.util.Objects;

/**
 * this is plugin controller.
 */
@RestController
@RequestMapping("/plugin")
public class PluginController {

    private final PluginService pluginService;

    private final SyncDataService syncDataService;

    /**
     * Instantiates a new Plugin controller.
     *
     * @param pluginService   the plugin service
     * @param syncDataService the sync data service
     */
    @Autowired(required = false)
    public PluginController(final PluginService pluginService,
                            final SyncDataService syncDataService) {
        this.pluginService = pluginService;
        this.syncDataService = syncDataService;
    }

    /**
     * query plugins.
     *
     * @param name        plugin name.
     * @param currentPage current page.
     * @param pageSize    page size.
     * @return {@linkplain GatewayAdminResult}
     */
    @GetMapping("")
    public GatewayAdminResult queryPlugins(final String name, final Integer currentPage, final Integer pageSize) {
        CommonPager<PluginVO> commonPager = pluginService.listByPage(new PluginQuery(name, new PageParameter(currentPage, pageSize)));
        return GatewayAdminResult.success("query plugins success", commonPager);
    }

    /**
     * detail plugin.
     *
     * @param id plugin id.
     * @return {@linkplain GatewayAdminResult}
     */
    @GetMapping("/{id}")
    public GatewayAdminResult detailPlugin(@PathVariable("id") final String id) {
        PluginVO pluginVO = pluginService.findById(id);
        return GatewayAdminResult.success("detail plugin success", pluginVO);
    }

    /**
     * create plugin.
     *
     * @param pluginDTO plugin.
     * @return {@linkplain GatewayAdminResult}
     */
    @PostMapping("")
    public GatewayAdminResult createPlugin(@RequestBody final PluginDTO pluginDTO) {
        String result = pluginService.createOrUpdate(pluginDTO);
        if (StringUtils.isNoneBlank()) {
            return GatewayAdminResult.error(result);
        }
        return GatewayAdminResult.success("create plugin success");
    }

    /**
     * update plugin.
     *
     * @param id        primary key.
     * @param pluginDTO plugin.
     * @return {@linkplain GatewayAdminResult}
     */
    @PutMapping("/{id}")
    public GatewayAdminResult updatePlugin(@PathVariable("id") final String id, @RequestBody final PluginDTO pluginDTO) {
        Objects.requireNonNull(pluginDTO);
        pluginDTO.setId(id);
        final String result = pluginService.createOrUpdate(pluginDTO);
        if (StringUtils.isNoneBlank(result)) {
            return GatewayAdminResult.error(result);
        }
        return GatewayAdminResult.success("update plugin success");
    }

    /**
     * delete plugins.
     *
     * @param ids primary key.
     * @return {@linkplain GatewayAdminResult}
     */
    @DeleteMapping("/batch")
    public GatewayAdminResult deletePlugins(@RequestBody final List<String> ids) {
        final String result = pluginService.delete(ids);
        if (StringUtils.isNoneBlank(result)) {
            return GatewayAdminResult.error(result);
        }
        return GatewayAdminResult.success("delete plugins success");
    }

    /**
     * Enable mono.
     *
     * @param batchCommonDTO the batch common dto
     * @return the mono
     */
    @PostMapping("/enabled")
    public GatewayAdminResult enabled(@RequestBody final BatchCommonDTO batchCommonDTO) {
        final String result = pluginService.enabled(batchCommonDTO.getIds(), batchCommonDTO.getEnabled());
        if (StringUtils.isNoneBlank(result)) {
            return GatewayAdminResult.error(result);
        }
        return GatewayAdminResult.success("enable plugins success");
    }


    /**
     * sync plugins.
     *
     * @return {@linkplain GatewayAdminResult}
     */
    @PostMapping("/syncPluginAll")
    public GatewayAdminResult syncPluginAll() {
        boolean success = syncDataService.syncAll(DataEventTypeEnum.REFRESH);
        if (success) {
            return GatewayAdminResult.success("sync plugins success");
        } else {
            return GatewayAdminResult.error("sync plugins fail");
        }
    }

    /**
     * Sync plugin data.
     *
     * @param id the id
     * @return the mono
     */
    @PutMapping("/syncPluginData/{id}")
    public GatewayAdminResult syncPluginData(@PathVariable("id") final String id) {
        boolean success = syncDataService.syncPluginData(id);
        if (success) {
            return GatewayAdminResult.success("sync plugins success");
        } else {
            return GatewayAdminResult.error("sync plugins fail");
        }
    }
}
