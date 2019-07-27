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

import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricActivityInstanceQuery;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricActivityInstanceDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricActivityInstanceQueryDto;
import org.camunda.bpm.engine.rest.history.HistoricActivityInstanceRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(HistoryRestService.PATH + HistoricActivityInstanceRestService.PATH)
public class HistoricActivityInstanceRestController extends AbstractRestProcessEngineAware implements HistoricActivityInstanceRestService {

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricActivityInstanceDto> getHistoricActivityInstances(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		HistoricActivityInstanceQueryDto queryHistoricActivityInstanceDto = new HistoricActivityInstanceQueryDto(
				objectMapper, request.getParameterMap());
		return queryHistoricActivityInstances(queryHistoricActivityInstanceDto, firstResult, maxResults);
	}

	@Override
	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricActivityInstanceDto> queryHistoricActivityInstances(
			@RequestBody HistoricActivityInstanceQueryDto queryDto,
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		queryDto.setObjectMapper(objectMapper);
		HistoricActivityInstanceQuery query = queryDto.toQuery(processEngine);

		List<HistoricActivityInstance> matchingHistoricActivityInstances;
		if (firstResult != null || maxResults != null) {
			matchingHistoricActivityInstances = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingHistoricActivityInstances = query.list();
		}

		List<HistoricActivityInstanceDto> historicActivityInstanceResults = new ArrayList<HistoricActivityInstanceDto>();
		for (HistoricActivityInstance historicActivityInstance : matchingHistoricActivityInstances) {
			HistoricActivityInstanceDto resultHistoricActivityInstance = HistoricActivityInstanceDto
					.fromHistoricActivityInstance(historicActivityInstance);
			historicActivityInstanceResults.add(resultHistoricActivityInstance);
		}
		return historicActivityInstanceResults;
	}

	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getHistoricActivityInstancesCount(HttpServletRequest request) {
		HistoricActivityInstanceQueryDto queryDto = new HistoricActivityInstanceQueryDto(objectMapper,
				request.getParameterMap());
		return queryHistoricActivityInstancesCount(queryDto);
	}

	@Override
	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryHistoricActivityInstancesCount(@RequestBody HistoricActivityInstanceQueryDto queryDto) {
		queryDto.setObjectMapper(objectMapper);
		HistoricActivityInstanceQuery query = queryDto.toQuery(processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}
	
	private List<HistoricActivityInstance> executePaginatedQuery(HistoricActivityInstanceQuery query,
			Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}
}
