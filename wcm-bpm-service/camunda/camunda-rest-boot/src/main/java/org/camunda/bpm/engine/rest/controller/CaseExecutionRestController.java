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

import org.camunda.bpm.engine.rest.CaseExecutionRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.runtime.CaseExecutionDto;
import org.camunda.bpm.engine.rest.dto.runtime.CaseExecutionQueryDto;
import org.camunda.bpm.engine.runtime.CaseExecution;
import org.camunda.bpm.engine.runtime.CaseExecutionQuery;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CaseExecutionRestService.PATH)
public class CaseExecutionRestController extends AbstractRestProcessEngineAware implements CaseExecutionRestService {

//	public CaseExecutionResource getCaseExecution(String caseExecutionId) {
//		return new CaseExecutionRestController(getProcessEngine(), caseExecutionId, getObjectMapper());
//	}

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<CaseExecutionDto> getCaseExecutions(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		CaseExecutionQueryDto queryDto = new CaseExecutionQueryDto(this.objectMapper, request.getParameterMap());
		return queryCaseExecutions(queryDto, firstResult, maxResults);
	}

	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<CaseExecutionDto> queryCaseExecutions(
			@RequestBody CaseExecutionQueryDto queryDto, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("firstResult") Integer maxResults) {	

		queryDto.setObjectMapper(this.objectMapper);
		CaseExecutionQuery query = queryDto.toQuery(this.processEngine);

		List<CaseExecution> matchingExecutions;
		if (firstResult != null || maxResults != null) {
			matchingExecutions = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingExecutions = query.list();
		}

		List<CaseExecutionDto> executionResults = new ArrayList<CaseExecutionDto>();
		for (CaseExecution execution : matchingExecutions) {
			CaseExecutionDto resultExecution = CaseExecutionDto.fromCaseExecution(execution);
			executionResults.add(resultExecution);
		}
		return executionResults;
	}

	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getCaseExecutionsCount(HttpServletRequest request) {
		CaseExecutionQueryDto queryDto = new CaseExecutionQueryDto(this.objectMapper, request.getParameterMap());
		return queryCaseExecutionsCount(queryDto);
	}

	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryCaseExecutionsCount(CaseExecutionQueryDto queryDto) {
		queryDto.setObjectMapper(this.objectMapper);
		CaseExecutionQuery query = queryDto.toQuery(this.processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}
	
	private List<CaseExecution> executePaginatedQuery(CaseExecutionQuery query, Integer firstResult,
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
