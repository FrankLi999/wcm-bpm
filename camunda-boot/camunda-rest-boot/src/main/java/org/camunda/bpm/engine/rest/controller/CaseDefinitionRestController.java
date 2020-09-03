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

import org.camunda.bpm.engine.repository.CaseDefinition;
import org.camunda.bpm.engine.repository.CaseDefinitionQuery;
import org.camunda.bpm.engine.rest.CaseDefinitionRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.repository.CaseDefinitionDto;
import org.camunda.bpm.engine.rest.dto.repository.CaseDefinitionQueryDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Roman Smirnov
 *
 */
@RestController(value="caseDefinitionApi")
@RequestMapping(CaseDefinitionRestService.PATH)
public class CaseDefinitionRestController extends AbstractRestProcessEngineAware implements CaseDefinitionRestService {

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<CaseDefinitionDto> getCaseDefinitions(HttpServletRequest request, @RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		CaseDefinitionQueryDto queryDto = new CaseDefinitionQueryDto(this.getObjectMapper(), request.getParameterMap());
		List<CaseDefinitionDto> definitions = new ArrayList<CaseDefinitionDto>();

		CaseDefinitionQuery query = queryDto.toQuery(this.processEngine);

		List<CaseDefinition> matchingDefinitions = null;

		if (firstResult != null || maxResults != null) {
			matchingDefinitions = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingDefinitions = query.list();
		}

		for (CaseDefinition definition : matchingDefinitions) {
			CaseDefinitionDto def = CaseDefinitionDto.fromCaseDefinition(definition);
			definitions.add(def);
		}
		return definitions;
	}

	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getCaseDefinitionsCount(HttpServletRequest request) {
		CaseDefinitionQueryDto queryDto = new CaseDefinitionQueryDto(this.getObjectMapper(), request.getParameterMap());

		CaseDefinitionQuery query = queryDto.toQuery(this.processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);
		return result;
	}

	private List<CaseDefinition> executePaginatedQuery(CaseDefinitionQuery query, Integer firstResult,
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
