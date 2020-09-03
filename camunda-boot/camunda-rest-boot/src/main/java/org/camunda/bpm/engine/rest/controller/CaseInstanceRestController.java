/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.rest.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.rest.CaseInstanceRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.runtime.CaseInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.CaseInstanceQueryDto;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.runtime.CaseInstanceQuery;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Roman Smirnov
 *
 */
@RestController(value="caseInstanceApi")
@RequestMapping(CaseInstanceRestService.PATH)
public class CaseInstanceRestController extends AbstractRestProcessEngineAware implements CaseInstanceRestService {

//	public CaseInstanceResource getCaseInstance(String caseInstanceId) {
//		return new CaseInstanceRestController(getProcessEngine(), caseInstanceId, getObjectMapper());
//	}

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<CaseInstanceDto> getCaseInstances(HttpServletRequest request, @RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		CaseInstanceQueryDto queryDto = new CaseInstanceQueryDto(this.getObjectMapper(), request.getParameterMap());
		return queryCaseInstances(queryDto, firstResult, maxResults);
	}

	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<CaseInstanceDto> queryCaseInstances(@RequestBody CaseInstanceQueryDto queryDto, @RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		queryDto.setObjectMapper(this.getObjectMapper());
		CaseInstanceQuery query = queryDto.toQuery(this.processEngine);

		List<CaseInstance> matchingInstances;
		if (firstResult != null || maxResults != null) {
			matchingInstances = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingInstances = query.list();
		}

		List<CaseInstanceDto> instanceResults = new ArrayList<CaseInstanceDto>();
		for (CaseInstance instance : matchingInstances) {
			CaseInstanceDto resultInstance = CaseInstanceDto.fromCaseInstance(instance);
			instanceResults.add(resultInstance);
		}
		return instanceResults;
	}

	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getCaseInstancesCount(HttpServletRequest request) {
		CaseInstanceQueryDto queryDto = new CaseInstanceQueryDto(this.getObjectMapper(), request.getParameterMap());
		return queryCaseInstancesCount(queryDto);
	}

	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryCaseInstancesCount(@RequestBody CaseInstanceQueryDto queryDto) {
		queryDto.setObjectMapper(this.getObjectMapper());
		CaseInstanceQuery query = queryDto.toQuery(this.processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}
	
	private List<CaseInstance> executePaginatedQuery(CaseInstanceQuery query, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}
}
