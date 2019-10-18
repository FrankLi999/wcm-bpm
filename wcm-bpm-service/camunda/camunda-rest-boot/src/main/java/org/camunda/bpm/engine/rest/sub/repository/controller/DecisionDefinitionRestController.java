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
package org.camunda.bpm.engine.rest.sub.repository.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.rest.DecisionDefinitionRestService;
import org.camunda.bpm.engine.rest.dto.HistoryTimeToLiveDto;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.dmn.EvaluateDecisionDto;
import org.camunda.bpm.engine.rest.dto.repository.DecisionDefinitionDiagramDto;
import org.camunda.bpm.engine.rest.dto.repository.DecisionDefinitionDto;
import org.camunda.bpm.engine.rest.sub.repository.DecisionDefinitionResource;
import org.camunda.bpm.engine.rest.sub.repository.service.DecisionDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DecisionDefinitionRestService.PATH + "/{decisionDefinitionId}")
public class DecisionDefinitionRestController implements DecisionDefinitionResource {
	
	
	@Autowired
	DecisionDefinitionService decisionDefinitionService;
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public DecisionDefinitionDto getDecisionDefinition(@PathVariable("decisionDefinitionId") String decisionDefinitionId) {
		return this.decisionDefinitionService.getDecisionDefinition(decisionDefinitionId);
	}

	@GetMapping(path="/xml", produces=MediaType.APPLICATION_JSON_VALUE)
	public DecisionDefinitionDiagramDto getDecisionDefinitionDmnXml(@PathVariable("decisionDefinitionId") String decisionDefinitionId) {
		return this.decisionDefinitionService.getDecisionDefinitionDmnXml(decisionDefinitionId);
	}

	@GetMapping(path="/diagram", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDecisionDefinitionDiagram(@PathVariable("decisionDefinitionId") String decisionDefinitionId) {
		return this.decisionDefinitionService.getDecisionDefinitionDiagram(decisionDefinitionId);
	}

	@PostMapping(path="/evaluate", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, VariableValueDto>> evaluateDecision(@PathVariable("decisionDefinitionId") String decisionDefinitionId, HttpServletRequest request, @RequestBody EvaluateDecisionDto parameters) {
		return this.decisionDefinitionService.evaluateDecision(decisionDefinitionId, request, parameters);
	}

	@PutMapping(path="/history-time-to-live", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateHistoryTimeToLive(@PathVariable("decisionDefinitionId") String decisionDefinitionId, @RequestBody HistoryTimeToLiveDto historyTimeToLiveDto) {
		this.decisionDefinitionService.updateHistoryTimeToLive(decisionDefinitionId, historyTimeToLiveDto);
	}
}
