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
package org.camunda.bpm.engine.rest.sub.runtime.controller;

import org.camunda.bpm.engine.AuthorizationException;
import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.rest.ExecutionRestService;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CreateIncidentDto;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.runtime.ExecutionDto;
import org.camunda.bpm.engine.rest.dto.runtime.ExecutionTriggerDto;
import org.camunda.bpm.engine.rest.dto.runtime.IncidentDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.runtime.ExecutionResource;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.Incident;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ExecutionRestService.PATH + "/{executionId}")
public class ExecutionResourceRestController extends AbstractRestProcessEngineAware implements ExecutionResource {

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public ExecutionDto getExecution(@PathVariable("executionId") String executionId) {
		RuntimeService runtimeService = this.processEngine.getRuntimeService();
		Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();

		if (execution == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "Execution with id " + executionId + " does not exist");
		}

		return ExecutionDto.fromExecution(execution);
	}

	@PostMapping(path="/signal", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public void signalExecution(@PathVariable("executionId") String executionId, 
			@RequestBody ExecutionTriggerDto triggerDto) {
		RuntimeService runtimeService = this.processEngine.getRuntimeService();
		try {
			VariableMap variables = VariableValueDto.toMap(triggerDto.getVariables(), this.processEngine, objectMapper);
			runtimeService.signal(executionId, variables);

		} catch (RestException e) {
			String errorMessage = String.format("Cannot signal execution %s: %s", executionId, e.getMessage());
			throw new InvalidRequestException(e.getStatus(), e, errorMessage);

		} catch (AuthorizationException e) {
			throw e;

		} catch (ProcessEngineException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e,
					"Cannot signal execution " + executionId + ": " + e.getMessage());

		}
	}	

	@PostMapping(path="/{executionId}/create-incident", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public IncidentDto createIncident(@PathVariable("executionId") String executionId, @RequestBody CreateIncidentDto createIncidentDto) {
		Incident newIncident = null;

		try {
			newIncident = this.processEngine.getRuntimeService().createIncident(createIncidentDto.getIncidentType(), executionId,
					createIncidentDto.getConfiguration(), createIncidentDto.getMessage());
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return IncidentDto.fromIncident(newIncident);
	}
}
