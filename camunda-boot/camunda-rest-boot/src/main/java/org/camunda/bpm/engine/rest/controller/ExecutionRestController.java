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

import org.camunda.bpm.engine.rest.ExecutionRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.runtime.ExecutionDto;
import org.camunda.bpm.engine.rest.dto.runtime.ExecutionQueryDto;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ExecutionQuery;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="executionApi")
@RequestMapping(ExecutionRestService.PATH)
public class ExecutionRestController extends AbstractRestProcessEngineAware implements ExecutionRestService {

	@Override
	@GetMapping(path="/", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ExecutionDto> getExecutions(HttpServletRequest request, @RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		ExecutionQueryDto queryDto = new ExecutionQueryDto(this.getObjectMapper(), request.getParameterMap());
		return queryExecutions(queryDto, firstResult, maxResults);
	}

	@Override
	@PostMapping(path="/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ExecutionDto> queryExecutions(@RequestBody ExecutionQueryDto queryDto, @RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		queryDto.setObjectMapper(this.getObjectMapper());
		ExecutionQuery query = queryDto.toQuery(this.processEngine);

		List<Execution> matchingExecutions;
		if (firstResult != null || maxResults != null) {
			matchingExecutions = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingExecutions = query.list();
		}

		List<ExecutionDto> executionResults = new ArrayList<ExecutionDto>();
		for (Execution execution : matchingExecutions) {
			ExecutionDto resultExecution = ExecutionDto.fromExecution(execution);
			executionResults.add(resultExecution);
		}
		return executionResults;
	}

	private List<Execution> executePaginatedQuery(ExecutionQuery query, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}

	@Override
	@GetMapping(path="/count",produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getExecutionsCount(HttpServletRequest request) {
		ExecutionQueryDto queryDto = new ExecutionQueryDto(this.getObjectMapper(), request.getParameterMap());
		return queryExecutionsCount(queryDto);
	}

	@Override
	public @PostMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
		CountResultDto queryExecutionsCount(@RequestBody ExecutionQueryDto queryDto) {
		
		queryDto.setObjectMapper(this.getObjectMapper());
		ExecutionQuery query = queryDto.toQuery(this.processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}
}
