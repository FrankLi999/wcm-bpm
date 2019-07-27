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

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.rest.CaseDefinitionRestService;
import org.camunda.bpm.engine.rest.dto.HistoryTimeToLiveDto;
import org.camunda.bpm.engine.rest.dto.repository.CaseDefinitionDiagramDto;
import org.camunda.bpm.engine.rest.dto.repository.CaseDefinitionDto;
import org.camunda.bpm.engine.rest.dto.runtime.CaseInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.CreateCaseInstanceDto;
import org.camunda.bpm.engine.rest.sub.repository.CaseDefinitionResource;
import org.camunda.bpm.engine.rest.sub.repository.service.CaseDefinitionService;
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

/**
 *
 * @author Roman Smirnov
 *
 */
@RestController
@RequestMapping(CaseDefinitionRestService.PATH + "/{caseDefinitionId}")
public class CaseDefinitionRestController {
	
	@Autowired
	protected CaseDefinitionService caseDefinitionService;

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public CaseDefinitionDto getCaseDefinition(@PathVariable("caseDefinitionId") String caseDefinitionId) {
		return caseDefinitionService.getCaseDefinition(caseDefinitionId);
	}

	@GetMapping(path="/xml", produces=MediaType.APPLICATION_JSON_VALUE)
	public CaseDefinitionDiagramDto getCaseDefinitionCmmnXml(@PathVariable("caseDefinitionId") String caseDefinitionId) {
		return caseDefinitionService.getCaseDefinitionCmmnXml(caseDefinitionId);
	}

	@PostMapping(path="/create", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CaseInstanceDto createCaseInstance(@PathVariable("caseDefinitionId") String caseDefinitionId, HttpServletRequest Request, @RequestBody CreateCaseInstanceDto parameters) {
		return caseDefinitionService.createCaseInstance(caseDefinitionId, Request, parameters);
	}

	@GetMapping(path="/diagram", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCaseDefinitionDiagram(@PathVariable("caseDefinitionId") String caseDefinitionId) {
		return caseDefinitionService.getCaseDefinitionDiagram(caseDefinitionId);
	}

	@PutMapping(path="/history-time-to-live", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateHistoryTimeToLive(@PathVariable("caseDefinitionId") String caseDefinitionId, @RequestBody HistoryTimeToLiveDto historyTimeToLiveDto) {
		caseDefinitionService.updateHistoryTimeToLive(caseDefinitionId, historyTimeToLiveDto);
	}
}
