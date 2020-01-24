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
package org.camunda.bpm.engine.rest.history.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.history.HistoricCaseActivityInstance;
import org.camunda.bpm.engine.history.HistoricCaseActivityInstanceQuery;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricCaseActivityInstanceDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricCaseActivityInstanceQueryDto;
import org.camunda.bpm.engine.rest.history.HistoricCaseActivityInstanceRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(HistoryRestService.PATH + HistoricCaseActivityInstanceRestService.PATH)
public class HistoricCaseActivityInstanceRestConroller extends AbstractRestProcessEngineAware implements HistoricCaseActivityInstanceRestService {

//	public HistoricCaseActivityInstanceResource getHistoricCaseInstance(String caseActivityInstanceId) {
//		return new HistoricCaseActivityInstanceRestController(processEngine, caseActivityInstanceId);
//	}

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricCaseActivityInstanceDto> getHistoricCaseActivityInstances(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		HistoricCaseActivityInstanceQueryDto queryHistoricCaseActivityInstanceDto = new HistoricCaseActivityInstanceQueryDto(
				objectMapper, request.getParameterMap());
		return queryHistoricCaseActivityInstances(queryHistoricCaseActivityInstanceDto, firstResult, maxResults);
	}

	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getHistoricCaseActivityInstancesCount(HttpServletRequest request) {
		HistoricCaseActivityInstanceQueryDto queryDto = new HistoricCaseActivityInstanceQueryDto(objectMapper,
				request.getParameterMap());
		return queryHistoricCaseActivityInstancesCount(queryDto);
	}

	private List<HistoricCaseActivityInstanceDto> queryHistoricCaseActivityInstances(
			HistoricCaseActivityInstanceQueryDto queryDto, Integer firstResult, Integer maxResults) {
		HistoricCaseActivityInstanceQuery query = queryDto.toQuery(processEngine);

		List<HistoricCaseActivityInstance> matchingHistoricCaseActivityInstances;
		if (firstResult != null || maxResults != null) {
			matchingHistoricCaseActivityInstances = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingHistoricCaseActivityInstances = query.list();
		}

		List<HistoricCaseActivityInstanceDto> historicCaseActivityInstanceResults = new ArrayList<HistoricCaseActivityInstanceDto>();
		for (HistoricCaseActivityInstance historicCaseActivityInstance : matchingHistoricCaseActivityInstances) {
			HistoricCaseActivityInstanceDto resultHistoricCaseActivityInstance = HistoricCaseActivityInstanceDto
					.fromHistoricCaseActivityInstance(historicCaseActivityInstance);
			historicCaseActivityInstanceResults.add(resultHistoricCaseActivityInstance);
		}
		return historicCaseActivityInstanceResults;
	}

	private List<HistoricCaseActivityInstance> executePaginatedQuery(HistoricCaseActivityInstanceQuery query,
			Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}

	private CountResultDto queryHistoricCaseActivityInstancesCount(HistoricCaseActivityInstanceQueryDto queryDto) {
		HistoricCaseActivityInstanceQuery query = queryDto.toQuery(processEngine);

		long count = query.count();

		return new CountResultDto(count);
	}
}
