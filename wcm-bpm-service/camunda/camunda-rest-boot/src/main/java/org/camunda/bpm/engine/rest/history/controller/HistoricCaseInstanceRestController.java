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

import org.camunda.bpm.engine.history.HistoricCaseInstance;
import org.camunda.bpm.engine.history.HistoricCaseInstanceQuery;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricCaseInstanceDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricCaseInstanceQueryDto;
import org.camunda.bpm.engine.rest.history.HistoricCaseInstanceRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(HistoryRestService.PATH + HistoricCaseInstanceRestService.PATH)
public class HistoricCaseInstanceRestController extends AbstractRestProcessEngineAware implements HistoricCaseInstanceRestService {

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricCaseInstanceDto> getHistoricCaseInstances(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		HistoricCaseInstanceQueryDto queryHistoricCaseInstanceDto = new HistoricCaseInstanceQueryDto(objectMapper,
				request.getParameterMap());
		return queryHistoricCaseInstances(queryHistoricCaseInstanceDto, firstResult, maxResults);
	}

	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricCaseInstanceDto> queryHistoricCaseInstances(
			@RequestBody HistoricCaseInstanceQueryDto queryDto,
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		HistoricCaseInstanceQuery query = queryDto.toQuery(processEngine);

		List<HistoricCaseInstance> matchingHistoricCaseInstances;
		if (firstResult != null || maxResults != null) {
			matchingHistoricCaseInstances = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingHistoricCaseInstances = query.list();
		}

		List<HistoricCaseInstanceDto> historicCaseInstanceDtoResults = new ArrayList<HistoricCaseInstanceDto>();
		for (HistoricCaseInstance historicCaseInstance : matchingHistoricCaseInstances) {
			HistoricCaseInstanceDto resultHistoricCaseInstanceDto = HistoricCaseInstanceDto
					.fromHistoricCaseInstance(historicCaseInstance);
			historicCaseInstanceDtoResults.add(resultHistoricCaseInstanceDto);
		}
		return historicCaseInstanceDtoResults;
	}

	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getHistoricCaseInstancesCount(HttpServletRequest request) {
		HistoricCaseInstanceQueryDto queryDto = new HistoricCaseInstanceQueryDto(objectMapper,
				request.getParameterMap());
		return queryHistoricCaseInstancesCount(queryDto);
	}

	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryHistoricCaseInstancesCount(
			@RequestBody HistoricCaseInstanceQueryDto queryDto) {
		HistoricCaseInstanceQuery query = queryDto.toQuery(processEngine);

		long count = query.count();

		return new CountResultDto(count);
	}

	private List<HistoricCaseInstance> executePaginatedQuery(HistoricCaseInstanceQuery query, Integer firstResult,
			Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}
}
