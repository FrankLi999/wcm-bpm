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

import org.camunda.bpm.engine.rest.DecisionRequirementsDefinitionRestService;
import org.camunda.bpm.engine.rest.dto.repository.DecisionRequirementsDefinitionDto;
import org.camunda.bpm.engine.rest.dto.repository.DecisionRequirementsDefinitionXmlDto;
import org.camunda.bpm.engine.rest.sub.repository.DecisionRequirementsDefinitionResource;
import org.camunda.bpm.engine.rest.sub.repository.service.DecisionRequirementsDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Deivarayan Azhagappan
 *
 */
@RestController
@RequestMapping(DecisionRequirementsDefinitionRestService.PATH + "/{drdId}")
public class DecisionRequirementsDefinitionResourceRestController implements DecisionRequirementsDefinitionResource {
	
	@Autowired
	protected DecisionRequirementsDefinitionService decisionRequirementsDefinitionService;

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public DecisionRequirementsDefinitionDto getDecisionRequirementsDefinition(@PathVariable("drdId") String drdId) {
		return decisionRequirementsDefinitionService.getDecisionRequirementsDefinition(drdId);
	}

	@GetMapping(path="/xml", produces=MediaType.APPLICATION_JSON_VALUE)
	public DecisionRequirementsDefinitionXmlDto getDecisionRequirementsDefinitionDmnXml(@PathVariable("drdId") String drdId) {
		return decisionRequirementsDefinitionService.getDecisionRequirementsDefinitionDmnXml(drdId);
	}

	@GetMapping(path="/diagram")
	public ResponseEntity<?> getDecisionRequirementsDefinitionDiagram(@PathVariable("drdId") String drdId) {
		return decisionRequirementsDefinitionService.getDecisionRequirementsDefinitionDiagram(drdId);
	}
}
