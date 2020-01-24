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

import org.camunda.bpm.engine.rest.IncidentRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.runtime.IncidentDto;
import org.camunda.bpm.engine.rest.dto.runtime.IncidentQueryDto;
import org.camunda.bpm.engine.runtime.Incident;
import org.camunda.bpm.engine.runtime.IncidentQuery;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Roman Smirnov
 *
 */
@RestController
@RequestMapping(IncidentRestService.PATH)
public class IncidentRestController extends AbstractRestProcessEngineAware implements IncidentRestService {

	@Override
	@GetMapping(path="/", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IncidentDto> getIncidents(HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		IncidentQueryDto queryDto = new IncidentQueryDto(this.objectMapper, request.getParameterMap());
		IncidentQuery query = queryDto.toQuery(processEngine);

		List<Incident> queryResult;
		if (firstResult != null || maxResults != null) {
			queryResult = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			queryResult = query.list();
		}

		List<IncidentDto> result = new ArrayList<IncidentDto>();
		for (Incident incident : queryResult) {
			IncidentDto dto = IncidentDto.fromIncident(incident);
			result.add(dto);
		}

		return result;
	}

	@Override
	@GetMapping(path="/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getIncidentsCount(HttpServletRequest request) {
		IncidentQueryDto queryDto = new IncidentQueryDto(this.objectMapper, request.getParameterMap());
		IncidentQuery query = queryDto.toQuery(processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}

	private List<Incident> executePaginatedQuery(IncidentQuery query, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}

//	@Override
//	public IncidentResource getIncident(String incidentId) {
//		return new IncidentRestController(getProcessEngine(), incidentId, getObjectMapper());
//	}
}
