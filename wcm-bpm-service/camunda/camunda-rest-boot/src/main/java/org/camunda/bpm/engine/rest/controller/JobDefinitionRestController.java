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

import org.camunda.bpm.engine.management.JobDefinition;
import org.camunda.bpm.engine.management.JobDefinitionQuery;
import org.camunda.bpm.engine.rest.JobDefinitionRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.management.JobDefinitionDto;
import org.camunda.bpm.engine.rest.dto.management.JobDefinitionQueryDto;
import org.camunda.bpm.engine.rest.dto.management.JobDefinitionSuspensionStateDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author roman.smirnov
 */
@RestController
@RequestMapping(JobDefinitionRestService.PATH)
public class JobDefinitionRestController extends AbstractRestProcessEngineAware implements JobDefinitionRestService {

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<JobDefinitionDto> getJobDefinitions(HttpServletRequest request, @RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		JobDefinitionQueryDto queryDto = new JobDefinitionQueryDto(this.objectMapper, request.getParameterMap());
		return queryJobDefinitions(queryDto, firstResult, maxResults);

	}

	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getJobDefinitionsCount(HttpServletRequest request) {
		JobDefinitionQueryDto queryDto = new JobDefinitionQueryDto(this.objectMapper, request.getParameterMap());
		return queryJobDefinitionsCount(queryDto);
	}

	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<JobDefinitionDto> queryJobDefinitions(
			@RequestBody JobDefinitionQueryDto queryDto,
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		queryDto.setObjectMapper(this.objectMapper);
		JobDefinitionQuery query = queryDto.toQuery(this.processEngine);

		List<JobDefinition> matchingJobDefinitions;
		if (firstResult != null || maxResults != null) {
			matchingJobDefinitions = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingJobDefinitions = query.list();
		}

		List<JobDefinitionDto> jobDefinitionResults = new ArrayList<JobDefinitionDto>();
		for (JobDefinition jobDefinition : matchingJobDefinitions) {
			JobDefinitionDto result = JobDefinitionDto.fromJobDefinition(jobDefinition);
			jobDefinitionResults.add(result);
		}

		return jobDefinitionResults;
	}

	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryJobDefinitionsCount(@RequestBody JobDefinitionQueryDto queryDto) {
		queryDto.setObjectMapper(this.objectMapper);
		JobDefinitionQuery query = queryDto.toQuery(this.processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}

	@PutMapping(path="/suspended", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateSuspensionState(@RequestBody JobDefinitionSuspensionStateDto dto) {
		if (dto.getJobDefinitionId() != null) {
			String message = "Either processDefinitionId or processDefinitionKey can be set to update the suspension state.";
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, message);
		}

		try {
			dto.updateSuspensionState(this.processEngine);

		} catch (IllegalArgumentException e) {
			String message = String.format("Could not update the suspension state of Job Definitions due to: %s",
					e.getMessage());
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, message);
		}
	}

	private List<JobDefinition> executePaginatedQuery(JobDefinitionQuery query, Integer firstResult,
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
