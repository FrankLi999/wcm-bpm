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

import org.camunda.bpm.engine.repository.DecisionDefinition;
import org.camunda.bpm.engine.repository.DecisionDefinitionQuery;
import org.camunda.bpm.engine.rest.DecisionDefinitionRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.repository.DecisionDefinitionDto;
import org.camunda.bpm.engine.rest.dto.repository.DecisionDefinitionQueryDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="decisionDefinitionApi")
@RequestMapping(DecisionDefinitionRestService.PATH)
public class DecisionDefinitionRestController extends AbstractRestProcessEngineAware
		implements DecisionDefinitionRestService {


	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<DecisionDefinitionDto> getDecisionDefinitions(HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		DecisionDefinitionQueryDto queryDto = new DecisionDefinitionQueryDto(this.objectMapper,
				request.getParameterMap());
		List<DecisionDefinitionDto> definitions = new ArrayList<DecisionDefinitionDto>();

		DecisionDefinitionQuery query = queryDto.toQuery(this.processEngine);

		List<DecisionDefinition> matchingDefinitions = null;

		if (firstResult != null || maxResults != null) {
			matchingDefinitions = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingDefinitions = query.list();
		}

		for (DecisionDefinition definition : matchingDefinitions) {
			DecisionDefinitionDto def = DecisionDefinitionDto.fromDecisionDefinition(definition);
			definitions.add(def);
		}
		return definitions;
	}
	
	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getDecisionDefinitionsCount(HttpServletRequest request) {
		DecisionDefinitionQueryDto queryDto = new DecisionDefinitionQueryDto(this.objectMapper,
				request.getParameterMap());

		DecisionDefinitionQuery query = queryDto.toQuery(this.processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);
		return result;
	}
	
	private List<DecisionDefinition> executePaginatedQuery(DecisionDefinitionQuery query, Integer firstResult,
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
