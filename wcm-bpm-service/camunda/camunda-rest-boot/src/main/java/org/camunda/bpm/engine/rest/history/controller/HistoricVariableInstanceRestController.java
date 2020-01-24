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

import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstanceQuery;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricVariableInstanceDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricVariableInstanceQueryDto;
import org.camunda.bpm.engine.rest.history.HistoricVariableInstanceRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.rest.sub.VariableResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(HistoryRestService.PATH + HistoricVariableInstanceRestService.PATH)
public class HistoricVariableInstanceRestController extends AbstractRestProcessEngineAware implements HistoricVariableInstanceRestService {

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricVariableInstanceDto> getHistoricVariableInstances(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults, 
			@RequestParam(name=VariableResource.DESERIALIZE_VALUES_QUERY_PARAM, defaultValue="true") boolean deserializeObjectValues) {
		HistoricVariableInstanceQueryDto queryDto = new HistoricVariableInstanceQueryDto(objectMapper,
				request.getParameterMap());
		return queryHistoricVariableInstances(queryDto, firstResult, maxResults, deserializeObjectValues);
	}

	@Override
	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricVariableInstanceDto> queryHistoricVariableInstances(
			@RequestBody HistoricVariableInstanceQueryDto queryDto,
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults, 
			@RequestParam(name=VariableResource.DESERIALIZE_VALUES_QUERY_PARAM, defaultValue="true") boolean deserializeObjectValues) {
		queryDto.setObjectMapper(objectMapper);
		HistoricVariableInstanceQuery query = queryDto.toQuery(processEngine);
		query.disableBinaryFetching();

		if (!deserializeObjectValues) {
			query.disableCustomObjectDeserialization();
		}

		List<HistoricVariableInstance> matchingHistoricVariableInstances;
		if (firstResult != null || maxResults != null) {
			matchingHistoricVariableInstances = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingHistoricVariableInstances = query.list();
		}

		List<HistoricVariableInstanceDto> historicVariableInstanceDtoResults = new ArrayList<HistoricVariableInstanceDto>();
		for (HistoricVariableInstance historicVariableInstance : matchingHistoricVariableInstances) {
			HistoricVariableInstanceDto resultHistoricVariableInstance = HistoricVariableInstanceDto
					.fromHistoricVariableInstance(historicVariableInstance);
			historicVariableInstanceDtoResults.add(resultHistoricVariableInstance);
		}
		return historicVariableInstanceDtoResults;
	}

	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getHistoricVariableInstancesCount(HttpServletRequest request) {
		HistoricVariableInstanceQueryDto queryDto = new HistoricVariableInstanceQueryDto(objectMapper,
				request.getParameterMap());
		return queryHistoricVariableInstancesCount(queryDto);
	}

	@Override
	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryHistoricVariableInstancesCount(@RequestBody HistoricVariableInstanceQueryDto queryDto) {
		queryDto.setObjectMapper(objectMapper);
		HistoricVariableInstanceQuery query = queryDto.toQuery(processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}
	
	private List<HistoricVariableInstance> executePaginatedQuery(HistoricVariableInstanceQuery query,
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
