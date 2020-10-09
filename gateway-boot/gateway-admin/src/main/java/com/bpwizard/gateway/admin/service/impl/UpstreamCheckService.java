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

import com.bpwizard.gateway.admin.entity.SelectorDO;
import com.bpwizard.gateway.admin.listener.DataChangedEvent;
import com.bpwizard.gateway.admin.mapper.SelectorMapper;
import com.bpwizard.gateway.admin.service.SelectorService;
import com.bpwizard.gateway.common.dto.SelectorData;
import com.bpwizard.gateway.common.dto.convert.DivideUpstream;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.common.enums.DataEventTypeEnum;
import com.bpwizard.gateway.common.utils.GsonUtils;
import com.bpwizard.gateway.common.utils.UpstreamCheckUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import com.bpwizard.gateway.common.concurrent.GatewayThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * this is divide  http url upstream.
 */
@Slf4j
@Component
public class UpstreamCheckService {
    
    private static final Map<String, List<DivideUpstream>> UPSTREAM_MAP = Maps.newConcurrentMap();
    
    @Value("${gateway.upstream.check:true}")
    private boolean check;
    
    @Value("${gateway.upstream.scheduledTime:10}")
    private int scheduledTime;
    
    private final SelectorService selectorService;
    
    private final SelectorMapper selectorMapper;
    
    private final ApplicationEventPublisher eventPublisher;
    
    /**
     * Instantiates a new Upstream check service.
     *
     * @param selectorService the selector service
     * @param selectorMapper  the selector mapper
     * @param eventPublisher  the event publisher
     */
    @Autowired(required = false)
    public UpstreamCheckService(final SelectorService selectorService, final SelectorMapper selectorMapper, final ApplicationEventPublisher eventPublisher) {
        if (log.isDebugEnabled()) {
        	log.debug("Entering");
        }
    	this.selectorService = selectorService;
        this.selectorMapper = selectorMapper;
        this.eventPublisher = eventPublisher;
    }
    
    /**
     * Sets .
     */
    @PostConstruct
    public void setup() {
        List<SelectorDO> selectorDOList = selectorMapper.findByPluginId("5");
        for (SelectorDO selectorDO : selectorDOList) {
            List<DivideUpstream> divideUpstreams = GsonUtils.getInstance().fromList(selectorDO.getHandle(), DivideUpstream.class);
            if (CollectionUtils.isNotEmpty(divideUpstreams)) {
                UPSTREAM_MAP.put(selectorDO.getName(), divideUpstreams);
            }
        }
        if (check) {
            new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), GatewayThreadFactory.create("scheduled-upstream-task", false))
                    .scheduleWithFixedDelay(this::scheduled, 10, scheduledTime, TimeUnit.SECONDS);
        }
    }
    
    /**
     * Remove by key.
     *
     * @param selectorName the selector name
     */
    public static void removeByKey(final String selectorName) {
        UPSTREAM_MAP.remove(selectorName);
    }
    
    /**
     * Submit.
     *
     * @param selectorName   the selector name
     * @param divideUpstream the divide upstream
     */
    public void submit(final String selectorName, final DivideUpstream divideUpstream) {
        if (UPSTREAM_MAP.containsKey(selectorName)) {
            UPSTREAM_MAP.get(selectorName).add(divideUpstream);
        } else {
            UPSTREAM_MAP.put(selectorName, Lists.newArrayList(divideUpstream));
        }
        
    }
    
    private void scheduled() {
        if (UPSTREAM_MAP.size() > 0) {
            UPSTREAM_MAP.forEach(this::check);
        }
    }
    
    private void check(final String selectorName, final List<DivideUpstream> upstreamList) {
        List<DivideUpstream> successList = Lists.newArrayListWithCapacity(upstreamList.size());
        for (DivideUpstream divideUpstream : upstreamList) {
            final boolean pass = UpstreamCheckUtils.checkUrl(divideUpstream.getUpstreamUrl());
            if (pass) {
                successList.add(divideUpstream);
            }
        }
        if (successList.size() == upstreamList.size()) {
            return;
        }
        if (successList.size() > 0) {
            UPSTREAM_MAP.put(selectorName, successList);
            updateSelectorHandler(selectorName, successList);
        } else {
            UPSTREAM_MAP.remove(selectorName);
            updateSelectorHandler(selectorName, null);
        }
    }
    
    private void updateSelectorHandler(final String selectorName, final List<DivideUpstream> upstreams) {
        SelectorDO selector = selectorService.findByName(selectorName);
        if (Objects.nonNull(selector)) {
            SelectorData selectorData = selectorService.buildByName(selectorName);
            if (upstreams == null) {
                selector.setHandle("");
                selectorData.setHandle("");
            } else {
                String handler = GsonUtils.getInstance().toJson(upstreams);
                selector.setHandle(handler);
                selectorData.setHandle(handler);
            }
            selectorMapper.updateSelective(selector);
            //发送更新事件
            // publish change event.
            eventPublisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.SELECTOR, DataEventTypeEnum.UPDATE,
                    Collections.singletonList(selectorData)));
        }
    }
    
}
