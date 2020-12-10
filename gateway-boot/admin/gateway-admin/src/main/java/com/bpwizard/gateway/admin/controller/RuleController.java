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

import com.bpwizard.gateway.admin.dto.RuleDTO;
import com.bpwizard.gateway.admin.page.CommonPager;
import com.bpwizard.gateway.admin.page.PageParameter;
import com.bpwizard.gateway.admin.query.RuleQuery;
import com.bpwizard.gateway.admin.result.GatewayAdminResult;
import com.bpwizard.gateway.admin.service.RuleService;
import com.bpwizard.gateway.admin.vo.RuleVO;

import java.util.List;
import java.util.Objects;

/**
 * this is rule controller.
 */
@RestController
@RequestMapping("/gateway-admin/api/rule")
public class RuleController {

    private final RuleService ruleService;

    @Autowired(required = false)
    public RuleController(final RuleService ruleService) {
        this.ruleService = ruleService;
    }

    /**
     * query rules.
     *
     * @param selectorId  selector id.
     * @param currentPage current page.
     * @param pageSize    page size.
     * @return {@linkplain GatewayAdminResult}
     */
    @GetMapping("")
    public GatewayAdminResult queryRules(final String selectorId, final Integer currentPage, final Integer pageSize) {
        CommonPager<RuleVO> commonPager = ruleService.listByPage(new RuleQuery(selectorId, new PageParameter(currentPage, pageSize)));
        return GatewayAdminResult.success("query rules success", commonPager);
    }

    /**
     * detail rule.
     *
     * @param id rule id.
     * @return {@linkplain GatewayAdminResult}
     */
    @GetMapping("/{id}")
    public GatewayAdminResult detailRule(@PathVariable("id") final String id) {
        RuleVO ruleVO = ruleService.findById(id);
        return GatewayAdminResult.success("detail rule success", ruleVO);
    }

    /**
     * create rule.
     *
     * @param ruleDTO rule.
     * @return {@linkplain GatewayAdminResult}
     */
    @PostMapping("")
    public GatewayAdminResult createRule(@RequestBody final RuleDTO ruleDTO) {
        Integer createCount = ruleService.createOrUpdate(ruleDTO);
        return GatewayAdminResult.success("create rule success", createCount);
    }

    /**
     * update rule.
     *
     * @param id      primary key.
     * @param ruleDTO rule.
     * @return {@linkplain GatewayAdminResult}
     */
    @PutMapping("/{id}")
    public GatewayAdminResult updateRule(@PathVariable("id") final String id, @RequestBody final RuleDTO ruleDTO) {
        Objects.requireNonNull(ruleDTO);
        ruleDTO.setId(id);
        Integer updateCount = ruleService.createOrUpdate(ruleDTO);
        return GatewayAdminResult.success("update rule success", updateCount);
    }

    /**
     * delete rules.
     *
     * @param ids primary key.
     * @return {@linkplain GatewayAdminResult}
     */
    @DeleteMapping("/batch")
    public GatewayAdminResult deleteRules(@RequestBody final List<String> ids) {
        Integer deleteCount = ruleService.delete(ids);
        return GatewayAdminResult.success("delete rule success", deleteCount);
    }
}
