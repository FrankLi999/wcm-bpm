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

import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.management.ProcessDefinitionStatistics;
import org.camunda.bpm.engine.management.ProcessDefinitionStatisticsQuery;
import org.camunda.bpm.engine.repository.DeleteProcessDefinitionsBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.rest.ProcessDefinitionRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.StatisticsResultDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionQueryDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionStatisticsResultDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionSuspensionStateDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ProcessDefinitionRestService.PATH)
public class ProcessDefinitionRestController extends AbstractRestProcessEngineAware
		implements ProcessDefinitionRestService {

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<ProcessDefinitionDto> getProcessDefinitions(HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		ProcessDefinitionQueryDto queryDto = new ProcessDefinitionQueryDto(this.objectMapper,
				request.getParameterMap());
		List<ProcessDefinitionDto> definitions = new ArrayList<ProcessDefinitionDto>();

		ProcessDefinitionQuery query = queryDto.toQuery(this.processEngine);

		List<ProcessDefinition> matchingDefinitions = null;

		if (firstResult != null || maxResults != null) {
			matchingDefinitions = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingDefinitions = query.list();
		}

		for (ProcessDefinition definition : matchingDefinitions) {
			ProcessDefinitionDto def = ProcessDefinitionDto.fromProcessDefinition(definition);
			definitions.add(def);
		}
		return definitions;
	}

	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getProcessDefinitionsCount(HttpServletRequest request) {
		ProcessDefinitionQueryDto queryDto = new ProcessDefinitionQueryDto(this.objectMapper,
				request.getParameterMap());

		ProcessDefinitionQuery query = queryDto.toQuery(this.processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);
		return result;
	}

	@Override
	@GetMapping(path="/statistics", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<StatisticsResultDto> getStatistics(
			@RequestParam("failedJobs") Boolean includeFailedJobs, 
			@RequestParam("rootIncidents") Boolean includeRootIncidents,
			@RequestParam Boolean includeIncidents, 
			@RequestParam("incidentsForType") String includeIncidentsForType) {
		if (includeIncidents != null && includeIncidentsForType != null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
					"Only one of the query parameter includeIncidents or includeIncidentsForType can be set.");
		}

		if (includeIncidents != null && includeRootIncidents != null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
					"Only one of the query parameter includeIncidents or includeRootIncidents can be set.");
		}

		if (includeRootIncidents != null && includeIncidentsForType != null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
					"Only one of the query parameter includeRootIncidents or includeIncidentsForType can be set.");
		}

		ManagementService mgmtService = this.processEngine.getManagementService();
		ProcessDefinitionStatisticsQuery query = mgmtService.createProcessDefinitionStatisticsQuery();

		if (includeFailedJobs != null && includeFailedJobs) {
			query.includeFailedJobs();
		}

		if (includeIncidents != null && includeIncidents) {
			query.includeIncidents();
		} else if (includeIncidentsForType != null) {
			query.includeIncidentsForType(includeIncidentsForType);
		} else if (includeRootIncidents != null && includeRootIncidents) {
			query.includeRootIncidents();
		}

		List<ProcessDefinitionStatistics> queryResults = query.list();

		List<StatisticsResultDto> results = new ArrayList<StatisticsResultDto>();
		for (ProcessDefinitionStatistics queryResult : queryResults) {
			StatisticsResultDto dto = ProcessDefinitionStatisticsResultDto.fromProcessDefinitionStatistics(queryResult);
			results.add(dto);
		}

		return results;
	}

	@PutMapping(path="/suspended", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateSuspensionState(@RequestBody ProcessDefinitionSuspensionStateDto dto) {
		if (dto.getProcessDefinitionId() != null) {
			String message = "Only processDefinitionKey can be set to update the suspension state.";
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, message);
		}

		try {
			dto.updateSuspensionState(this.processEngine);

		} catch (IllegalArgumentException e) {
			String message = String.format("Could not update the suspension state of Process Definitions due to: %s",
					e.getMessage());
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, message);
		}
	}

	@Override
	@DeleteMapping("/key/{key}/delete")
	public void deleteProcessDefinitionsByKey(
			@PathVariable("key") String processDefinitionKey, 
			@RequestParam("processEngine") boolean cascade, 
			@RequestParam("skipCustomListeners") boolean skipCustomListeners,
			@RequestParam("skipIoMappings") boolean skipIoMappings) {
		RepositoryService repositoryService = processEngine.getRepositoryService();

		DeleteProcessDefinitionsBuilder builder = repositoryService.deleteProcessDefinitions()
				.byKey(processDefinitionKey);

		deleteProcessDefinitions(builder, cascade, skipCustomListeners, skipIoMappings);
	}

	@Override
	@DeleteMapping("/key/{key}/tenant-id/{tenantId}/delete")
	public void deleteProcessDefinitionsByKeyAndTenantId(
			@PathVariable("key") String processDefinitionKey, 
			@RequestParam("processEngine") boolean cascade, 
			@RequestParam("skipCustomListeners") boolean skipCustomListeners,
			@RequestParam("skipIoMappings") boolean skipIoMappings, 
			@PathVariable("tenantId") String tenantId) {
		RepositoryService repositoryService = processEngine.getRepositoryService();

		DeleteProcessDefinitionsBuilder builder = repositoryService.deleteProcessDefinitions()
				.byKey(processDefinitionKey).withTenantId(tenantId);

		deleteProcessDefinitions(builder, cascade, skipCustomListeners, skipIoMappings);
	}

	protected void deleteProcessDefinitions(DeleteProcessDefinitionsBuilder builder, boolean cascade,
			boolean skipCustomListeners, boolean skipIoMappings) {
		if (skipCustomListeners) {
			builder = builder.skipCustomListeners();
		}

		if (cascade) {
			builder = builder.cascade();
		}

		if (skipIoMappings) {
			builder = builder.skipIoMappings();
		}

		try {
			builder.delete();
		} catch (NotFoundException e) { // rewrite status code from bad request (400) to not found (404)
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	private List<ProcessDefinition> executePaginatedQuery(ProcessDefinitionQuery query, Integer firstResult,
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
