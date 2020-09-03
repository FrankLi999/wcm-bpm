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
package org.camunda.bpm.engine.rest.sub.externaltask.controller;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.camunda.bpm.engine.rest.ExternalTaskRestService;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.externaltask.CompleteExternalTaskDto;
import org.camunda.bpm.engine.rest.dto.externaltask.ExtendLockOnExternalTaskDto;
import org.camunda.bpm.engine.rest.dto.externaltask.ExternalTaskBpmnError;
import org.camunda.bpm.engine.rest.dto.externaltask.ExternalTaskDto;
import org.camunda.bpm.engine.rest.dto.externaltask.ExternalTaskFailureDto;
import org.camunda.bpm.engine.rest.dto.runtime.PriorityDto;
import org.camunda.bpm.engine.rest.dto.runtime.RetriesDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.externaltask.ExternalTaskResource;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Thorben Lindhauer
 *
 */
@RestController(value="externalTaskResourceRestControllerSub")
@RequestMapping(ExternalTaskRestService.PATH + "/{externalTaskId}")
public class ExternalTaskResourceRestController extends AbstractRestProcessEngineAware implements ExternalTaskResource {
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public ExternalTaskDto getExternalTask(@PathVariable("externalTaskId") String externalTaskId) {
		ExternalTask task = this.processEngine.getExternalTaskService().createExternalTaskQuery().externalTaskId(externalTaskId)
				.singleResult();

		if (task == null) {
			throw new RestException(HttpStatus.NOT_FOUND, "External task with id " + externalTaskId + " does not exist");
		}

		return ExternalTaskDto.fromExternalTask(task);
	}

	@GetMapping(path="/errorDetails", produces=MediaType.TEXT_PLAIN_VALUE)
	public String getErrorDetails(@PathVariable("externalTaskId") String externalTaskId) {
		ExternalTaskService externalTaskService = this.processEngine.getExternalTaskService();

		try {
			return externalTaskService.getExternalTaskErrorDetails(externalTaskId);
		} catch (NotFoundException e) {
			throw new RestException(HttpStatus.NOT_FOUND, e, "External task with id " + externalTaskId + " does not exist");
		}
	}

	@PutMapping(path="/retries", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void setRetries(@PathVariable("externalTaskId") String externalTaskId, @RequestBody RetriesDto dto) {
		ExternalTaskService externalTaskService = this.processEngine.getExternalTaskService();
		Integer retries = dto.getRetries();

		if (retries == null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, "The number of retries cannot be null.");
		}

		try {
			externalTaskService.setRetries(externalTaskId, retries);
		} catch (NotFoundException e) {
			throw new RestException(HttpStatus.NOT_FOUND, e, "External task with id " + externalTaskId + " does not exist");
		}
	}

	@PutMapping(path="/priority", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void setPriority(@PathVariable("externalTaskId") String externalTaskId, @RequestBody PriorityDto dto) {
		ExternalTaskService externalTaskService = this.processEngine.getExternalTaskService();

		try {
			externalTaskService.setPriority(externalTaskId, dto.getPriority());
		} catch (NotFoundException e) {
			throw new RestException(HttpStatus.NOT_FOUND, e, "External task with id " + externalTaskId + " does not exist");
		}
	}

	@PostMapping(path="/complete", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void complete(@PathVariable("externalTaskId") String externalTaskId, @RequestBody CompleteExternalTaskDto dto) {
		ExternalTaskService externalTaskService = this.processEngine.getExternalTaskService();

		VariableMap variables = VariableValueDto.toMap(dto.getVariables(), this.processEngine, this.getObjectMapper());
		VariableMap localVariables = VariableValueDto.toMap(dto.getLocalVariables(), this.processEngine, this.getObjectMapper());

		try {
			externalTaskService.complete(externalTaskId, dto.getWorkerId(), variables, localVariables);
		} catch (NotFoundException e) {
			throw new RestException(HttpStatus.NOT_FOUND, e, "External task with id " + externalTaskId + " does not exist");
		} catch (BadUserRequestException e) {
			throw new RestException(HttpStatus.BAD_REQUEST, e, e.getMessage());
		}

	}

	@PostMapping(path="/failure", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void handleFailure(@PathVariable("externalTaskId") String externalTaskId, @RequestBody ExternalTaskFailureDto dto) {
		ExternalTaskService externalTaskService = this.processEngine.getExternalTaskService();

		try {
			externalTaskService.handleFailure(externalTaskId, dto.getWorkerId(), dto.getErrorMessage(),
					dto.getErrorDetails(), dto.getRetries(), dto.getRetryTimeout());
		} catch (NotFoundException e) {
			throw new RestException(HttpStatus.NOT_FOUND, e, "External task with id " + externalTaskId + " does not exist");
		} catch (BadUserRequestException e) {
			throw new RestException(HttpStatus.BAD_REQUEST, e, e.getMessage());
		}
	}

	@PostMapping(path="/bpmnError", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void handleBpmnError(@PathVariable("externalTaskId") String externalTaskId, @RequestBody ExternalTaskBpmnError dto) {
		ExternalTaskService externalTaskService = this.processEngine.getExternalTaskService();

		try {
			externalTaskService.handleBpmnError(externalTaskId, dto.getWorkerId(), dto.getErrorCode(),
					dto.getErrorMessage(), VariableValueDto.toMap(dto.getVariables(), this.processEngine, this.getObjectMapper()));
		} catch (NotFoundException e) {
			throw new RestException(HttpStatus.NOT_FOUND, e, "External task with id " + externalTaskId + " does not exist");
		} catch (BadUserRequestException e) {
			throw new RestException(HttpStatus.BAD_REQUEST, e, e.getMessage());
		}
	}

	@PostMapping(path="/unlock")
	public void unlock(@PathVariable("externalTaskId") String externalTaskId) {
		ExternalTaskService externalTaskService = this.processEngine.getExternalTaskService();

		try {
			externalTaskService.unlock(externalTaskId);
		} catch (NotFoundException e) {
			throw new RestException(HttpStatus.NOT_FOUND, e, "External task with id " + externalTaskId + " does not exist");
		}
	}

	@PostMapping(path="/extendLock", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void extendLock(@PathVariable("externalTaskId") String externalTaskId, @RequestBody ExtendLockOnExternalTaskDto extendLockDto) {
		ExternalTaskService externalTaskService = this.processEngine.getExternalTaskService();

		try {
			externalTaskService.extendLock(externalTaskId, extendLockDto.getWorkerId(), extendLockDto.getNewDuration());
		} catch (NotFoundException e) {
			throw new RestException(HttpStatus.NOT_FOUND, e, "External task with id " + externalTaskId + " does not exist");
		} catch (BadUserRequestException e) {
			throw new RestException(HttpStatus.BAD_REQUEST, e, e.getMessage());
		}
	}
}
