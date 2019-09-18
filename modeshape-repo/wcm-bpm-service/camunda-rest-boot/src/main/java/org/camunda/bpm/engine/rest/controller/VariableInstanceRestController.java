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

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.VariableInstanceRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.runtime.VariableInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.VariableInstanceQueryDto;
import org.camunda.bpm.engine.rest.sub.VariableResource;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.camunda.bpm.engine.runtime.VariableInstanceQuery;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(VariableInstanceRestService.PATH)
public class VariableInstanceRestController extends AbstractRestProcessEngineAware
		implements VariableInstanceRestService {

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<VariableInstanceDto> getVariableInstances(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults, 
			@RequestParam(name=VariableResource.DESERIALIZE_VALUES_QUERY_PARAM, defaultValue="true") boolean deserializeObjectValues) {
		VariableInstanceQueryDto queryDto = new VariableInstanceQueryDto(getObjectMapper(),
				request.getParameterMap());
		return queryVariableInstances(queryDto, firstResult, maxResults, deserializeObjectValues);
	}

	@Override
	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<VariableInstanceDto> queryVariableInstances(
			@RequestBody VariableInstanceQueryDto queryDto, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults, 
			@RequestParam(name=VariableResource.DESERIALIZE_VALUES_QUERY_PARAM, defaultValue="true") boolean deserializeObjectValues) {
		ProcessEngine engine = getProcessEngine();
		queryDto.setObjectMapper(getObjectMapper());
		VariableInstanceQuery query = queryDto.toQuery(engine);

		// disable binary fetching by default.
		query.disableBinaryFetching();

		// disable custom object fetching by default. Cannot be done to not break
		// existing API
		if (!deserializeObjectValues) {
			query.disableCustomObjectDeserialization();
		}

		List<VariableInstance> matchingInstances;
		if (firstResult != null || maxResults != null) {
			matchingInstances = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingInstances = query.list();
		}

		List<VariableInstanceDto> instanceResults = new ArrayList<VariableInstanceDto>();
		for (VariableInstance instance : matchingInstances) {
			VariableInstanceDto resultInstance = VariableInstanceDto.fromVariableInstance(instance);
			instanceResults.add(resultInstance);
		}
		return instanceResults;
	}

	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getVariableInstancesCount(HttpServletRequest request) {
		VariableInstanceQueryDto queryDto = new VariableInstanceQueryDto(getObjectMapper(),
				request.getParameterMap());
		return queryVariableInstancesCount(queryDto);
	}

	@Override
	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryVariableInstancesCount(@RequestBody VariableInstanceQueryDto queryDto) {
		ProcessEngine engine = getProcessEngine();
		queryDto.setObjectMapper(getObjectMapper());
		VariableInstanceQuery query = queryDto.toQuery(engine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}

	private List<VariableInstance> executePaginatedQuery(VariableInstanceQuery query, Integer firstResult,
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
