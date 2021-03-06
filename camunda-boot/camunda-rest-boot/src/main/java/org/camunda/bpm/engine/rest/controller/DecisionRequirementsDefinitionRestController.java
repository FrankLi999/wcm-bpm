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

import org.camunda.bpm.engine.repository.DecisionRequirementsDefinition;
import org.camunda.bpm.engine.repository.DecisionRequirementsDefinitionQuery;
import org.camunda.bpm.engine.rest.DecisionRequirementsDefinitionRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.repository.DecisionRequirementsDefinitionDto;
import org.camunda.bpm.engine.rest.dto.repository.DecisionRequirementsDefinitionQueryDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="decisionRequirementsDefinitionApi")
@RequestMapping(DecisionRequirementsDefinitionRestService.PATH)
public class DecisionRequirementsDefinitionRestController extends AbstractRestProcessEngineAware
		implements DecisionRequirementsDefinitionRestService {

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<DecisionRequirementsDefinitionDto> getDecisionRequirementsDefinitions(HttpServletRequest request,
			@RequestParam("firstResult") Integer firstResult, @RequestParam("maxResults") Integer maxResults) {
		DecisionRequirementsDefinitionQueryDto queryDto = new DecisionRequirementsDefinitionQueryDto(this.getObjectMapper(),
				request.getParameterMap());
		List<DecisionRequirementsDefinitionDto> dtos = new ArrayList<DecisionRequirementsDefinitionDto>();

		DecisionRequirementsDefinitionQuery query = queryDto.toQuery(this.processEngine);

		List<DecisionRequirementsDefinition> matchingDefinitions = null;

		if (firstResult != null || maxResults != null) {
			matchingDefinitions = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingDefinitions = query.list();
		}

		for (DecisionRequirementsDefinition definition : matchingDefinitions) {
			DecisionRequirementsDefinitionDto dto = DecisionRequirementsDefinitionDto
					.fromDecisionRequirementsDefinition(definition);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getDecisionRequirementsDefinitionsCount(HttpServletRequest request) {
		DecisionRequirementsDefinitionQueryDto queryDto = new DecisionRequirementsDefinitionQueryDto(this.getObjectMapper(),
				request.getParameterMap());

		DecisionRequirementsDefinitionQuery query = queryDto.toQuery(this.processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);
		return result;
	}

	private List<DecisionRequirementsDefinition> executePaginatedQuery(DecisionRequirementsDefinitionQuery query,
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
