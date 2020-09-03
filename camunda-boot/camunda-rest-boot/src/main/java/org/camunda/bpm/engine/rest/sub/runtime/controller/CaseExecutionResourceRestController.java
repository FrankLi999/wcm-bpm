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
import org.camunda.bpm.engine.rest.CaseExecutionRestService;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.runtime.CaseExecutionDto;
import org.camunda.bpm.engine.rest.dto.runtime.CaseExecutionTriggerDto;
import org.camunda.bpm.engine.rest.dto.runtime.TriggerVariableValueDto;
import org.camunda.bpm.engine.rest.dto.runtime.VariableNameDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.runtime.CaseExecutionResource;
import org.camunda.bpm.engine.runtime.CaseExecution;
import org.camunda.bpm.engine.runtime.CaseExecutionCommandBuilder;
import org.camunda.bpm.engine.variable.value.TypedValue;
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
@RequestMapping(CaseExecutionRestService.PATH + "/{caseExecutionId}")
public class CaseExecutionResourceRestController extends AbstractRestProcessEngineAware implements CaseExecutionResource {
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public CaseExecutionDto getCaseExecution(@PathVariable("caseExecutionId") String caseExecutionId) {
		CaseService caseService = this.processEngine.getCaseService();

		CaseExecution execution = caseService.createCaseExecutionQuery().caseExecutionId(caseExecutionId)
				.singleResult();

		if (execution == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Case execution with id " + caseExecutionId + " does not exist.");
		}

		CaseExecutionDto result = CaseExecutionDto.fromCaseExecution(execution);
		return result;
	}

	@PostMapping(path="/manual-start", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void manualStart(@PathVariable("caseExecutionId") String caseExecutionId, @RequestBody CaseExecutionTriggerDto triggerDto) {
		try {
			CaseService caseService = this.processEngine.getCaseService();
			CaseExecutionCommandBuilder commandBuilder = caseService.withCaseExecution(caseExecutionId);

			initializeCommand(caseExecutionId, commandBuilder, triggerDto, "start manually");

			commandBuilder.manualStart();

		} catch (NotFoundException e) {
			throw createInvalidRequestException(caseExecutionId, "manualStart", HttpStatus.NOT_FOUND, e);

		} catch (NotValidException e) {
			throw createInvalidRequestException(caseExecutionId, "manualStart", HttpStatus.BAD_REQUEST, e);

		} catch (NotAllowedException e) {
			throw createInvalidRequestException(caseExecutionId, "manualStart", HttpStatus.FORBIDDEN, e);

		} catch (ProcessEngineException e) {
			throw createRestException(caseExecutionId, "manualStart", HttpStatus.INTERNAL_SERVER_ERROR, e);

		}

	}

	@PostMapping(path="/disable", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void disable(@PathVariable("caseExecutionId") String caseExecutionId, @RequestBody CaseExecutionTriggerDto triggerDto) {
		try {
			CaseService caseService = this.processEngine.getCaseService();
			CaseExecutionCommandBuilder commandBuilder = caseService.withCaseExecution(caseExecutionId);

			initializeCommand(caseExecutionId, commandBuilder, triggerDto, "disable");

			commandBuilder.disable();

		} catch (NotFoundException e) {
			throw createInvalidRequestException(caseExecutionId, "disable", HttpStatus.NOT_FOUND, e);

		} catch (NotValidException e) {
			throw createInvalidRequestException(caseExecutionId, "disable", HttpStatus.BAD_REQUEST, e);

		} catch (NotAllowedException e) {
			throw createInvalidRequestException(caseExecutionId, "disable", HttpStatus.FORBIDDEN, e);

		} catch (ProcessEngineException e) {
			throw createRestException(caseExecutionId, "disable", HttpStatus.INTERNAL_SERVER_ERROR, e);

		}

	}

	@PostMapping(path="/reenable", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void reenable(@PathVariable("caseExecutionId") String caseExecutionId, @RequestBody CaseExecutionTriggerDto triggerDto) {
		try {
			CaseService caseService = this.processEngine.getCaseService();
			CaseExecutionCommandBuilder commandBuilder = caseService.withCaseExecution(caseExecutionId);

			initializeCommand(caseExecutionId, commandBuilder, triggerDto, "reenable");

			commandBuilder.reenable();

		} catch (NotFoundException e) {
			throw createInvalidRequestException(caseExecutionId, "reenable", HttpStatus.NOT_FOUND, e);

		} catch (NotValidException e) {
			throw createInvalidRequestException(caseExecutionId, "reenable", HttpStatus.BAD_REQUEST, e);

		} catch (NotAllowedException e) {
			throw createInvalidRequestException(caseExecutionId, "reenable", HttpStatus.FORBIDDEN, e);

		} catch (ProcessEngineException e) {
			throw createRestException(caseExecutionId, "reenable", HttpStatus.INTERNAL_SERVER_ERROR, e);

		}
	}

	@PostMapping(path="/complete", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void complete(@PathVariable("caseExecutionId") String caseExecutionId, @RequestBody CaseExecutionTriggerDto triggerDto) {
		try {
			CaseService caseService = this.processEngine.getCaseService();
			CaseExecutionCommandBuilder commandBuilder = caseService.withCaseExecution(caseExecutionId);

			initializeCommand(caseExecutionId, commandBuilder, triggerDto, "complete");

			commandBuilder.complete();

		} catch (NotFoundException e) {
			throw createInvalidRequestException(caseExecutionId, "complete", HttpStatus.NOT_FOUND, e);

		} catch (NotValidException e) {
			throw createInvalidRequestException(caseExecutionId, "complete", HttpStatus.BAD_REQUEST, e);

		} catch (NotAllowedException e) {
			throw createInvalidRequestException(caseExecutionId, "complete", HttpStatus.FORBIDDEN, e);

		} catch (ProcessEngineException e) {
			throw createRestException(caseExecutionId, "complete", HttpStatus.INTERNAL_SERVER_ERROR, e);

		}
	}

	@PostMapping(path="/terminate", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void terminate(@PathVariable("caseExecutionId") String caseExecutionId, @RequestBody CaseExecutionTriggerDto triggerDto) {
		try {
			CaseService caseService = this.processEngine.getCaseService();
			CaseExecutionCommandBuilder commandBuilder = caseService.withCaseExecution(caseExecutionId);

			initializeCommand(caseExecutionId, commandBuilder, triggerDto, "terminate");

			commandBuilder.terminate();

		} catch (NotFoundException e) {
			throw createInvalidRequestException(caseExecutionId, "terminate", HttpStatus.NOT_FOUND, e);

		} catch (NotValidException e) {
			throw createInvalidRequestException(caseExecutionId, "terminate", HttpStatus.BAD_REQUEST, e);

		} catch (NotAllowedException e) {
			throw createInvalidRequestException(caseExecutionId, "terminate", HttpStatus.FORBIDDEN, e);

		} catch (ProcessEngineException e) {
			throw createRestException(caseExecutionId, "terminate", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
	}

	protected InvalidRequestException createInvalidRequestException(String caseExecutionId, String transition, HttpStatus status,
			ProcessEngineException cause) {
		String errorMessage = String.format("Cannot %s case execution %s: %s", transition, caseExecutionId,
				cause.getMessage());
		return new InvalidRequestException(status, cause, errorMessage);
	}

	protected RestException createRestException(String caseExecutionId, String transition, HttpStatus status, ProcessEngineException cause) {
		String errorMessage = String.format("Cannot %s case execution %s: %s", transition, caseExecutionId,
				cause.getMessage());
		return new RestException(status, cause, errorMessage);
	}

	protected void initializeCommand(String caseExecutionId, CaseExecutionCommandBuilder commandBuilder, CaseExecutionTriggerDto triggerDto,
			String transition) {
		Map<String, TriggerVariableValueDto> variables = triggerDto.getVariables();
		if (variables != null && !variables.isEmpty()) {
			initializeCommandWithVariables(caseExecutionId, commandBuilder, variables, transition);
		}

		List<VariableNameDto> deletions = triggerDto.getDeletions();
		if (deletions != null && !deletions.isEmpty()) {
			initializeCommandWithDeletions(commandBuilder, deletions, transition);
		}
	}

	protected void initializeCommandWithVariables(String caseExecutionId, CaseExecutionCommandBuilder commandBuilder,
			Map<String, TriggerVariableValueDto> variables, String transition) {
		for (String variableName : variables.keySet()) {
			try {
				TriggerVariableValueDto variableValue = variables.get(variableName);
				TypedValue typedValue = variableValue.toTypedValue(this.processEngine, this.getObjectMapper());

				if (variableValue.isLocal()) {
					commandBuilder.setVariableLocal(variableName, typedValue);

				} else {
					commandBuilder.setVariable(variableName, typedValue);
				}

			} catch (RestException e) {
				String errorMessage = String.format("Cannot %s case execution %s due to invalid variable %s: %s",
						transition, caseExecutionId, variableName, e.getMessage());
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
