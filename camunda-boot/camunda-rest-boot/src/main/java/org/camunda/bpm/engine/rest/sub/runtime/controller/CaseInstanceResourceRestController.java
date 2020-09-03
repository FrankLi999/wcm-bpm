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

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.exception.NotAllowedException;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.exception.NotValidException;
import org.camunda.bpm.engine.rest.CaseInstanceRestService;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.runtime.CaseExecutionTriggerDto;
import org.camunda.bpm.engine.rest.dto.runtime.CaseInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.TriggerVariableValueDto;
import org.camunda.bpm.engine.rest.dto.runtime.VariableNameDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.runtime.CaseInstanceResource;
import org.camunda.bpm.engine.runtime.CaseExecutionCommandBuilder;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Roman Smirnov
 *
 */
@RestController
@RequestMapping(CaseInstanceRestService.PATH + "/{caseInstanceId}")
public class CaseInstanceResourceRestController extends AbstractRestProcessEngineAware implements CaseInstanceResource {

	
	public CaseInstanceResourceRestController() {}
//	ProcessEngine engine, String caseInstanceId, ObjectMapper objectMapper) {
//		this.engine = engine;
//		this.caseInstanceId = caseInstanceId;
//		this.getObjectMapper() = objectMapper;
//	}

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public CaseInstanceDto getCaseInstance(@PathVariable("caseInstanceId") String caseInstanceId) {
		CaseService caseService = this.processEngine.getCaseService();

		CaseInstance instance = caseService.createCaseInstanceQuery().caseInstanceId(caseInstanceId).singleResult();

		if (instance == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Case instance with id " + caseInstanceId + " does not exist.");
		}

		CaseInstanceDto result = CaseInstanceDto.fromCaseInstance(instance);
		return result;
	}

	@PostMapping(path="/complete", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void complete(@PathVariable("caseInstanceId") String caseInstanceId, @RequestBody CaseExecutionTriggerDto triggerDto) {
		try {
			CaseService caseService = this.processEngine.getCaseService();
			CaseExecutionCommandBuilder commandBuilder = caseService.withCaseExecution(caseInstanceId);

			initializeCommand(caseInstanceId, commandBuilder, triggerDto, "complete");

			commandBuilder.complete();

		} catch (NotFoundException e) {
			throw createInvalidRequestException(caseInstanceId, "complete", HttpStatus.NOT_FOUND, e);

		} catch (NotValidException e) {
			throw createInvalidRequestException(caseInstanceId, "complete", HttpStatus.BAD_REQUEST, e);

		} catch (NotAllowedException e) {
			throw createInvalidRequestException(caseInstanceId, "complete", HttpStatus.FORBIDDEN, e);

		} catch (ProcessEngineException e) {
			throw createRestException(caseInstanceId, "complete", HttpStatus.INTERNAL_SERVER_ERROR, e);

		}
	}

	@PostMapping(path="/close", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void close(@PathVariable("caseInstanceId") String caseInstanceId, @RequestBody CaseExecutionTriggerDto triggerDto) {
		try {
			CaseService caseService = this.processEngine.getCaseService();
			CaseExecutionCommandBuilder commandBuilder = caseService.withCaseExecution(caseInstanceId);

			initializeCommand(caseInstanceId, commandBuilder, triggerDto, "close");

			commandBuilder.close();

		} catch (NotFoundException e) {
			throw createInvalidRequestException(caseInstanceId, "close", HttpStatus.NOT_FOUND, e);

		} catch (NotValidException e) {
			throw createInvalidRequestException(caseInstanceId, "close", HttpStatus.BAD_REQUEST, e);

		} catch (NotAllowedException e) {
			throw createInvalidRequestException(caseInstanceId, "close", HttpStatus.FORBIDDEN, e);

		} catch (ProcessEngineException e) {
			throw createRestException(caseInstanceId, "close", HttpStatus.INTERNAL_SERVER_ERROR, e);

		}
	}

	@PostMapping(path="/terminate", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void terminate(@PathVariable("caseInstanceId") String caseInstanceId, @RequestBody CaseExecutionTriggerDto triggerDto) {
		try {
			CaseService caseService = this.processEngine.getCaseService();
			CaseExecutionCommandBuilder commandBuilder = caseService.withCaseExecution(caseInstanceId);

			initializeCommand(caseInstanceId, commandBuilder, triggerDto, "terminate");

			commandBuilder.terminate();

		} catch (NotFoundException e) {
			throw createInvalidRequestException(caseInstanceId, "terminate", HttpStatus.NOT_FOUND, e);

		} catch (NotValidException e) {
			throw createInvalidRequestException(caseInstanceId, "terminate", HttpStatus.BAD_REQUEST, e);

		} catch (NotAllowedException e) {
			throw createInvalidRequestException(caseInstanceId, "terminate", HttpStatus.FORBIDDEN, e);

		} catch (ProcessEngineException e) {
			throw createRestException(caseInstanceId, "terminate", HttpStatus.INTERNAL_SERVER_ERROR, e);

		}
	}

	protected InvalidRequestException createInvalidRequestException(String caseInstanceId, String transition, HttpStatus status,
			ProcessEngineException cause) {
		String errorMessage = String.format("Cannot %s case instance %s: %s", transition, caseInstanceId,
				cause.getMessage());
		return new InvalidRequestException(status, cause, errorMessage);
	}

	protected RestException createRestException(String caseInstanceId, String transition, HttpStatus status, ProcessEngineException cause) {
		String errorMessage = String.format("Cannot %s case instance %s: %s", transition, caseInstanceId,
				cause.getMessage());
		return new RestException(status, cause, errorMessage);
	}

	protected void initializeCommand(String caseInstanceId, CaseExecutionCommandBuilder commandBuilder, CaseExecutionTriggerDto triggerDto,
			String transition) {
		Map<String, TriggerVariableValueDto> variables = triggerDto.getVariables();
		if (variables != null && !variables.isEmpty()) {
			initializeCommandWithVariables(caseInstanceId, commandBuilder, variables, transition);
		}

		List<VariableNameDto> deletions = triggerDto.getDeletions();
		if (deletions != null && !deletions.isEmpty()) {
			initializeCommandWithDeletions(commandBuilder, deletions, transition);
		}
	}

	protected void initializeCommandWithVariables(String caseInstanceId, CaseExecutionCommandBuilder commandBuilder,
			Map<String, TriggerVariableValueDto> variables, String transition) {
		for (String variableName : variables.keySet()) {
			try {
				TriggerVariableValueDto variableValue = variables.get(variableName);

				if (variableValue.isLocal()) {
					commandBuilder.setVariableLocal(variableName, variableValue.toTypedValue(this.processEngine, this.getObjectMapper()));

				} else {
					commandBuilder.setVariable(variableName, variableValue.toTypedValue(this.processEngine, this.getObjectMapper()));
				}

			} catch (RestException e) {
				String errorMessage = String.format("Cannot %s case instance %s due to invalid variable %s: %s",
						transition, caseInstanceId, variableName, e.getMessage());
				throw new RestException(e.getStatus(), e, errorMessage);

			}
		}
	}

	protected void initializeCommandWithDeletions(CaseExecutionCommandBuilder commandBuilder,
			List<VariableNameDto> deletions, String transition) {
		for (VariableNameDto variableName : deletions) {
			if (variableName.isLocal()) {
				commandBuilder.removeVariableLocal(variableName.getName());
			} else {
				commandBuilder.removeVariable(variableName.getName());
			}
		}
	}
}
