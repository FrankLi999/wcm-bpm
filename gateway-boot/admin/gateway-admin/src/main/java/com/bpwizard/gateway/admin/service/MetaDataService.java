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

package com.bpwizard.gateway.admin.service;

import com.bpwizard.gateway.admin.dto.MetaDataDTO;
import com.bpwizard.gateway.admin.page.CommonPager;
import com.bpwizard.gateway.admin.query.MetaDataQuery;
import com.bpwizard.gateway.admin.vo.MetaDataVO;
import com.bpwizard.gateway.common.dto.MetaData;

import java.util.List;
import java.util.Map;

/**
 * this is application authority service.
 */
public interface MetaDataService {

    /**
     * Create or update int.
     *
     * @param metaDataDTO the meta data dto
     * @return the String
     */
    String createOrUpdate(MetaDataDTO metaDataDTO);
    
    /**
     * delete application authorities.
     *
     * @param ids primary key.
     * @return rows int
     */
    int delete(List<String> ids);
    
    /**
     * Find by id meta data vo.
     *
     * @param id the id
     * @return the meta data vo
     */
    MetaDataVO findById(String id);
    
    /**
     * List by page common pager.
     *
     * @param metaDataQuery the meta data query
     * @return the common pager
     */
    CommonPager<MetaDataVO> listByPage(MetaDataQuery metaDataQuery);
    
    /**
     * Find all list.
     *
     * @return the list
     */
    List<MetaDataVO> findAll();
    
    /**
     * Find all group map.
     *
     * @return the map
     */
    Map<String, List<MetaDataVO>> findAllGroup();
    
    /**
     * List all list.
     *
     * @return the list
     */
    List<MetaData> listAll();
    
    /**
     * Enabled string.
     *
     * @param ids     the ids
     * @param enabled the enable
     * @return the string
     */
    String enabled(List<String> ids, Boolean enabled);

    /**
     * Sync data.
     */
    void syncData();
}
