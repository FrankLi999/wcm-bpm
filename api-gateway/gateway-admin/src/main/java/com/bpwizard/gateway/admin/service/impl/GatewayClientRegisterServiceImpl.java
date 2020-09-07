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

package com.bpwizard.gateway.admin.service.impl;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpwizard.gateway.admin.dto.RuleConditionDTO;
import com.bpwizard.gateway.admin.dto.RuleDTO;
import com.bpwizard.gateway.admin.dto.SelectorConditionDTO;
import com.bpwizard.gateway.admin.dto.SelectorDTO;
import com.bpwizard.gateway.admin.dto.SpringCloudRegisterDTO;
import com.bpwizard.gateway.admin.dto.SpringMvcRegisterDTO;
import com.bpwizard.gateway.admin.entity.MetaDataDO;
import com.bpwizard.gateway.admin.entity.RuleDO;
import com.bpwizard.gateway.admin.entity.SelectorDO;
import com.bpwizard.gateway.admin.listener.DataChangedEvent;
import com.bpwizard.gateway.admin.mapper.MetaDataMapper;
import com.bpwizard.gateway.admin.mapper.RuleMapper;
import com.bpwizard.gateway.admin.mapper.SelectorMapper;
import com.bpwizard.gateway.admin.service.GatewayClientRegisterService;
import com.bpwizard.gateway.admin.service.RuleService;
import com.bpwizard.gateway.admin.service.SelectorService;
import com.bpwizard.gateway.admin.transfer.MetaDataTransfer;
import com.bpwizard.gateway.common.dto.SelectorData;
import com.bpwizard.gateway.common.dto.convert.DivideUpstream;
import com.bpwizard.gateway.common.dto.convert.rule.DivideRuleHandle;
import com.bpwizard.gateway.common.dto.convert.rule.SpringCloudRuleHandle;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.common.enums.DataEventTypeEnum;
import com.bpwizard.gateway.common.enums.LoadBalanceEnum;
import com.bpwizard.gateway.common.enums.MatchModeEnum;
import com.bpwizard.gateway.common.enums.OperatorEnum;
import com.bpwizard.gateway.common.enums.ParamTypeEnum;
import com.bpwizard.gateway.common.enums.RpcTypeEnum;
import com.bpwizard.gateway.common.enums.SelectorTypeEnum;
import com.bpwizard.gateway.common.utils.GsonUtils;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.common.utils.UUIDUtils;

/**
 * The type Gateway client register service.
 */
@Service("gatewayClientRegisterService")
public class GatewayClientRegisterServiceImpl implements GatewayClientRegisterService {
    
    private final MetaDataMapper metaDataMapper;
    
    private final ApplicationEventPublisher eventPublisher;
    
    private final SelectorService selectorService;
    
    private final RuleService ruleService;
    
    private final RuleMapper ruleMapper;
    
    private final UpstreamCheckService upstreamCheckService;
    
    private final SelectorMapper selectorMapper;
    
    /**
     * Instantiates a new Meta data service.
     *
     * @param metaDataMapper       the meta data mapper
     * @param eventPublisher       the event publisher
     * @param selectorService      the selector service
     * @param ruleService          the rule service
     * @param ruleMapper           the rule mapper
     * @param upstreamCheckService the upstream check service
     * @param selectorMapper       the selector mapper
     */
    @Autowired(required = false)
    public GatewayClientRegisterServiceImpl(final MetaDataMapper metaDataMapper,
                                         final ApplicationEventPublisher eventPublisher,
                                         final SelectorService selectorService,
                                         final RuleService ruleService,
                                         final RuleMapper ruleMapper,
                                         final UpstreamCheckService upstreamCheckService,
                                         final SelectorMapper selectorMapper) {
        this.metaDataMapper = metaDataMapper;
        this.eventPublisher = eventPublisher;
        this.selectorService = selectorService;
        this.ruleService = ruleService;
        this.ruleMapper = ruleMapper;
        this.upstreamCheckService = upstreamCheckService;
        this.selectorMapper = selectorMapper;
    }
    
    @Override
    @Transactional
    public String registerSpringMvc(final SpringMvcRegisterDTO dto) {
        String selectorId = handlerSpringMvcSelector(dto);
        handlerSpringMvcRule(selectorId, dto);
        return "success";
    }
    
    @Override
    @Transactional
    public synchronized String registerSpringCloud(final SpringCloudRegisterDTO dto) {
        MetaDataDO metaDataDO = metaDataMapper.findByPath(dto.getContext() + "/**");
        if (Objects.isNull(metaDataDO)) {
            saveSpringCloudMetaData(dto);
        }
        String selectorId = handlerSpringCloudSelector(dto);
        handlerSpringCloudRule(selectorId, dto);
        return "success";
    }
    
    private void saveSpringCloudMetaData(final SpringCloudRegisterDTO dto) {
        MetaDataDO metaDataDO = new MetaDataDO();
        metaDataDO.setAppName(dto.getAppName());
        metaDataDO.setPath(dto.getContext() + "/**");
        metaDataDO.setPathDesc(dto.getAppName() + "spring cloud meta data info");
        metaDataDO.setServiceName(dto.getAppName());
        metaDataDO.setMethodName(dto.getContext());
        metaDataDO.setRpcType(dto.getRpcType());
        metaDataDO.setEnabled(dto.isEnabled());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        metaDataDO.setId(UUIDUtils.getInstance().generateShortUuid());
        metaDataDO.setDateCreated(currentTime);
        metaDataDO.setDateUpdated(currentTime);
        metaDataMapper.insert(metaDataDO);
        // publish AppAuthData's event
        eventPublisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.META_DATA, DataEventTypeEnum.CREATE,
                Collections.singletonList(MetaDataTransfer.INSTANCE.mapToData(metaDataDO))));
    }
    
//    private void saveOrUpdateMetaData(final MetaDataDO exist, final MetaDataDTO metaDataDTO) {
//        DataEventTypeEnum eventType;
//        MetaDataDO metaDataDO = MetaDataTransfer.INSTANCE.mapToEntity(metaDataDTO);
//        if (Objects.isNull(exist)) {
//            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
//            metaDataDO.setId(UUIDUtils.getInstance().generateShortUuid());
//            metaDataDO.setDateCreated(currentTime);
//            metaDataDO.setDateUpdated(currentTime);
//            metaDataMapper.insert(metaDataDO);
//            eventType = DataEventTypeEnum.CREATE;
//        } else {
//            metaDataDO.setId(exist.getId());
//            metaDataMapper.update(metaDataDO);
//            eventType = DataEventTypeEnum.UPDATE;
//        }
//        // publish AppAuthData's event
//        eventPublisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.META_DATA, eventType,
//                Collections.singletonList(MetaDataTransfer.INSTANCE.mapToData(metaDataDTO))));
//    }
    
    private String handlerSpringMvcSelector(final SpringMvcRegisterDTO dto) {
        String contextPath = dto.getContext();
        SelectorDO selectorDO = selectorService.findByName(contextPath);
        String selectorId;
        String uri = String.join(":", dto.getHost(), String.valueOf(dto.getPort()));
        if (Objects.isNull(selectorDO)) {
            selectorId = registerSelector(contextPath, dto.getRpcType(), dto.getAppName(), uri);
        } else {
            selectorId = selectorDO.getId();
            //update upstream
            String handle = selectorDO.getHandle();
            String handleAdd;
            DivideUpstream addDivideUpstream = buildDivideUpstream(uri);
            SelectorData selectorData = selectorService.buildByName(contextPath);
            if (StringUtils.isBlank(handle)) {
                handleAdd = GsonUtils.getInstance().toJson(Collections.singletonList(addDivideUpstream));
            } else {
                List<DivideUpstream> exist = GsonUtils.getInstance().fromList(handle, DivideUpstream.class);
                for (DivideUpstream upstream : exist) {
                    if (upstream.getUpstreamUrl().equals(addDivideUpstream.getUpstreamUrl())) {
                        return selectorId;
                    }
                }
                exist.add(addDivideUpstream);
                handleAdd = GsonUtils.getInstance().toJson(exist);
            }
            selectorDO.setHandle(handleAdd);
            selectorData.setHandle(handleAdd);
            //更新数据库
            selectorMapper.updateSelective(selectorDO);
            //提交过去检查
            upstreamCheckService.submit(contextPath, addDivideUpstream);
            //发送更新事件
            // publish change event.
            eventPublisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.SELECTOR, DataEventTypeEnum.UPDATE,
                    Collections.singletonList(selectorData)));
        }
        return selectorId;
    }
    
    private void handlerSpringMvcRule(final String selectorId, final SpringMvcRegisterDTO dto) {
        RuleDO ruleDO = ruleMapper.findByName(dto.getRuleName());
        if (Objects.isNull(ruleDO)) {
            registerRule(selectorId, dto.getPath(), dto.getRpcType(), dto.getRuleName());
        }
    }
    
    private String handlerSpringCloudSelector(final SpringCloudRegisterDTO dto) {
        String contextPath = dto.getContext();
        SelectorDO selectorDO = selectorService.findByName(contextPath);
        if (Objects.isNull(selectorDO)) {
            return registerSelector(contextPath, dto.getRpcType(), dto.getAppName(), "");
        } else {
            return selectorDO.getId();
        }
    }
    
    private void handlerSpringCloudRule(final String selectorId, final SpringCloudRegisterDTO dto) {
        RuleDO ruleDO = ruleMapper.findByName(dto.getRuleName());
        if (Objects.isNull(ruleDO)) {
            registerRule(selectorId, dto.getPath(), dto.getRpcType(), dto.getRuleName());
        }
    }
    
    private String registerSelector(final String contextPath, final String rpcType, final String appName, final String uri) {
        SelectorDTO selectorDTO = new SelectorDTO();
        selectorDTO.setName(contextPath);
        selectorDTO.setType(SelectorTypeEnum.CUSTOM_FLOW.getCode());
        selectorDTO.setMatchMode(MatchModeEnum.AND.getCode());
        selectorDTO.setEnabled(Boolean.TRUE);
        selectorDTO.setLoged(Boolean.TRUE);
        selectorDTO.setContinued(Boolean.TRUE);
        selectorDTO.setSort(1);
        if (RpcTypeEnum.SPRING_CLOUD.getName().equals(rpcType)) {
            selectorDTO.setPluginId("8");
            selectorDTO.setHandle(appName);
        } else {
            //is divide
            DivideUpstream divideUpstream = buildDivideUpstream(uri);
            String handler = GsonUtils.getInstance().toJson(Collections.singletonList(divideUpstream));
            selectorDTO.setHandle(handler);
            selectorDTO.setPluginId("5");
            upstreamCheckService.submit(selectorDTO.getName(), divideUpstream);
        }
        SelectorConditionDTO selectorConditionDTO = new SelectorConditionDTO();
        selectorConditionDTO.setParamType(ParamTypeEnum.URI.getName());
        selectorConditionDTO.setParamName("/");
        selectorConditionDTO.setOperator(OperatorEnum.MATCH.getAlias());
        selectorConditionDTO.setParamValue(contextPath + "/**");
        selectorDTO.setSelectorConditions(Collections.singletonList(selectorConditionDTO));
        return selectorService.register(selectorDTO);
    }
    
    private DivideUpstream buildDivideUpstream(final String uri) {
        DivideUpstream divideUpstream = new DivideUpstream();
        divideUpstream.setUpstreamHost("localhost");
        divideUpstream.setProtocol("http://");
        divideUpstream.setUpstreamUrl(uri);
        divideUpstream.setWeight(50);
        return divideUpstream;
    }
    
    private void registerRule(final String selectorId, final String path, final String rpcType, final String ruleName) {
        RuleDTO ruleDTO = new RuleDTO();
        ruleDTO.setSelectorId(selectorId);
        ruleDTO.setName(ruleName);
        ruleDTO.setMatchMode(MatchModeEnum.AND.getCode());
        ruleDTO.setEnabled(Boolean.TRUE);
        ruleDTO.setLoged(Boolean.TRUE);
        ruleDTO.setSort(1);
        RuleConditionDTO ruleConditionDTO = new RuleConditionDTO();
        ruleConditionDTO.setParamType(ParamTypeEnum.URI.getName());
        ruleConditionDTO.setParamName("/");
        if (path.indexOf("*") > 1) {
            ruleConditionDTO.setOperator(OperatorEnum.MATCH.getAlias());
        } else {
            ruleConditionDTO.setOperator(OperatorEnum.EQ.getAlias());
        }
        ruleConditionDTO.setParamValue(path);
        ruleDTO.setRuleConditions(Collections.singletonList(ruleConditionDTO));
        if (rpcType.equals(RpcTypeEnum.HTTP.getName())) {
            DivideRuleHandle divideRuleHandle = new DivideRuleHandle();
            divideRuleHandle.setLoadBalance(LoadBalanceEnum.RANDOM.getName());
            divideRuleHandle.setRetry(0);
            ruleDTO.setHandle(JsonUtils.toJson(divideRuleHandle));
        } else {
            SpringCloudRuleHandle springCloudRuleHandle = new SpringCloudRuleHandle();
            springCloudRuleHandle.setPath(path);
            ruleDTO.setHandle(JsonUtils.toJson(springCloudRuleHandle));
        }
        ruleService.register(ruleDTO);
    }
}
