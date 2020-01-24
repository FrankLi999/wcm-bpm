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
import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.rest.ProcessInstanceRestService;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.dto.runtime.ActivityInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceSuspensionStateDto;
import org.camunda.bpm.engine.rest.dto.runtime.modification.ProcessInstanceModificationDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.sub.runtime.ProcessInstanceResource;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceModificationBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ProcessInstanceRestService.PATH + "/{processInstanceId}")
public class ProcessInstanceResourceRestController extends AbstractRestProcessEngineAware
		implements ProcessInstanceResource {


	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ProcessInstanceDto getProcessInstance(@PathVariable("processInstanceId") String processInstanceId) {
		RuntimeService runtimeService = this.processEngine.getRuntimeService();
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();

		if (instance == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Process instance with id " + processInstanceId + " does not exist");
		}

		ProcessInstanceDto result = ProcessInstanceDto.fromProcessInstance(instance);
		return result;
	}

	@DeleteMapping(path = "/")
	public void deleteProcessInstance(@PathVariable("processInstanceId") String processInstanceId,
			@RequestParam(name = "skipCustomListeners", defaultValue = "false") boolean skipCustomListeners,
			@RequestParam(name = "skipIoMappings", defaultValue = "false") boolean skipIoMappings,
			@RequestParam(name = "skipSubprocesses", defaultValue = "false") boolean skipSubprocesses,
			@RequestParam(name = "failIfNotExists", defaultValue = "true") boolean failIfNotExists) {
		RuntimeService runtimeService = this.processEngine.getRuntimeService();
		try {
			if (failIfNotExists) {
				runtimeService.deleteProcessInstance(processInstanceId, null, skipCustomListeners, true, skipIoMappings,
						skipSubprocesses);
			} else {
				runtimeService.deleteProcessInstanceIfExists(processInstanceId, null, skipCustomListeners, true,
						skipIoMappings, skipSubprocesses);
			}
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e,
					"Process instance with id " + processInstanceId + " does not exist");
		}

	}
	
	@GetMapping(path = "/{processInstanceId}/activity-instances", produces = MediaType.APPLICATION_JSON_VALUE)
	public ActivityInstanceDto getActivityInstanceTree(@PathVariable("processInstanceId") String processInstanceId) {
		RuntimeService runtimeService = this.processEngine.getRuntimeService();

		ActivityInstance activityInstance = null;

		try {
			activityInstance = runtimeService.getActivityInstance(processInstanceId);
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage());
		}

		if (activityInstance == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Process instance with id " + processInstanceId + " does not exist");
		}

		ActivityInstanceDto result = ActivityInstanceDto.fromActivityInstance(activityInstance);
		return result;
	}

	@PutMapping(path = "/{processInstanceId}/suspended", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateSuspensionState(@PathVariable("processInstanceId") String processInstanceId,
			@RequestBody ProcessInstanceSuspensionStateDto dto) {
		dto.setProcessInstanceId(processInstanceId);
		dto.updateSuspensionState(this.processEngine);
	}

	@PostMapping(path = "/{processInstanceId}/modification", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void modifyProcessInstance(@PathVariable("processInstanceId") String processInstanceId,
			@RequestBody ProcessInstanceModificationDto dto) {
		if (dto.getInstructions() != null && !dto.getInstructions().isEmpty()) {
			ProcessInstanceModificationBuilder modificationBuilder = this.processEngine.getRuntimeService()
					.createProcessInstanceModification(processInstanceId);

			dto.applyTo(modificationBuilder, this.processEngine, objectMapper);

			modificationBuilder.execute(dto.isSkipCustomListeners(), dto.isSkipIoMappings());
		}
	}

	@PostMapping(path = "/{processInstanceId}/modification-async", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BatchDto modifyProcessInstanceAsync(@PathVariable("processInstanceId") String processInstanceId,
			@RequestBody ProcessInstanceModificationDto dto) {
		Batch batch = null;
		if (dto.getInstructions() != null && !dto.getInstructions().isEmpty()) {
			ProcessInstanceModificationBuilder modificationBuilder = this.processEngine.getRuntimeService()
					.createProcessInstanceModification(processInstanceId);

			dto.applyTo(modificationBuilder, this.processEngine, objectMapper);

			try {
				batch = modificationBuilder.executeAsync(dto.isSkipCustomListeners(), dto.isSkipIoMappings());
			} catch (BadUserRequestException e) {
				throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
			return BatchDto.fromBatch(batch);
		}

		throw new InvalidRequestException(HttpStatus.BAD_REQUEST, "The provided instuctions are invalid.");
	}
}
