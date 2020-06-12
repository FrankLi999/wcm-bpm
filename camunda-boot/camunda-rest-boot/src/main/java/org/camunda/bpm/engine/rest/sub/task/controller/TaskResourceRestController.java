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
package org.camunda.bpm.engine.rest.sub.task.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.AuthorizationException;
import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.exception.NotValidException;
import org.camunda.bpm.engine.exception.NullValueException;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidationException;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.rest.TaskRestService;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.converter.StringListConverter;
import org.camunda.bpm.engine.rest.dto.task.CompleteTaskDto;
import org.camunda.bpm.engine.rest.dto.task.FormDto;
import org.camunda.bpm.engine.rest.dto.task.IdentityLinkDto;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.camunda.bpm.engine.rest.dto.task.UserIdDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.hal.Hal;
import org.camunda.bpm.engine.rest.hal.task.HalTask;
import org.camunda.bpm.engine.rest.sub.VariableResource;
import org.camunda.bpm.engine.rest.sub.task.TaskResource;
import org.camunda.bpm.engine.rest.util.ApplicationContextPathUtil;
import org.camunda.bpm.engine.rest.util.EncodingUtil;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(TaskRestService.PATH + "/{taskId}")
public class TaskResourceRestController extends AbstractTaskResourceRestController implements TaskResource {
	
//	@Autowired
//	TaskVariablesRestController taskVariablesService;
//	
//	@Autowired
//	LocalTaskVariablesRestController localTaskVariablesService;
	
	protected ObjectMapper objectMapper = new ObjectMapper();
	protected String rootResourcePath = "/";
	public TaskResourceRestController() {}

	@PostMapping(path="/claim", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void claim(@PathVariable("taskId") String taskId, @RequestBody UserIdDto dto) {
		TaskService taskService = engine.getTaskService();

		taskService.claim(taskId, dto.getUserId());
	}

	@PostMapping(path="/unclaim")
	public void unclaim(@PathVariable("taskId") String taskId) {
		engine.getTaskService().setAssignee(taskId, null);
	}

	@PostMapping(path="/complete", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> complete(@PathVariable("taskId") String taskId, @RequestBody CompleteTaskDto dto) {
		TaskService taskService = engine.getTaskService();

		try {
			VariableMap variables = VariableValueDto.toMap(dto.getVariables(), engine, objectMapper);
			if (dto.isWithVariablesInReturn()) {
				VariableMap taskVariables = taskService.completeWithVariablesInReturn(taskId, variables, false);

				Map<String, VariableValueDto> body = VariableValueDto.fromMap(taskVariables, true);

				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
			} else {
				taskService.complete(taskId, variables);
				return ResponseEntity.noContent().build();
			}

		} catch (RestException e) {
			String errorMessage = String.format("Cannot complete task %s: %s", taskId, e.getMessage());
			throw new InvalidRequestException(e.getStatus(), e, errorMessage);

		} catch (AuthorizationException e) {
			throw e;

		} catch (FormFieldValidationException e) {
			String errorMessage = String.format("Cannot complete task %s: %s", taskId, e.getMessage());
			throw new RestException(HttpStatus.BAD_REQUEST, e, errorMessage);

		} catch (ProcessEngineException e) {
			String errorMessage = String.format("Cannot complete task %s: %s", taskId, e.getMessage());
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, errorMessage);
		}
	}

	@PostMapping(path="/submit-form", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> submit(@PathVariable("taskId") String taskId, @RequestBody CompleteTaskDto dto) {
		FormService formService = engine.getFormService();

		try {
			VariableMap variables = VariableValueDto.toMap(dto.getVariables(), engine, objectMapper);
			if (dto.isWithVariablesInReturn()) {
				VariableMap taskVariables = formService.submitTaskFormWithVariablesInReturn(taskId, variables, false);

				Map<String, VariableValueDto> body = VariableValueDto.fromMap(taskVariables, true);
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
			} else {
				formService.submitTaskForm(taskId, variables);
				return ResponseEntity.noContent().build();
			}

		} catch (RestException e) {
			String errorMessage = String.format("Cannot submit task form %s: %s", taskId, e.getMessage());
			throw new InvalidRequestException(e.getStatus(), e, errorMessage);

		} catch (AuthorizationException e) {
			throw e;

		} catch (FormFieldValidationException e) {
			String errorMessage = String.format("Cannot submit task form %s: %s", taskId, e.getMessage());
			throw new RestException(HttpStatus.BAD_REQUEST, e, errorMessage);

		} catch (ProcessEngineException e) {
			String errorMessage = String.format("Cannot submit task form %s: %s", taskId, e.getMessage());
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, errorMessage);
		}

	}
	
	@PostMapping(path="/delegate", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public void delegate(@PathVariable("taskId") String taskId, @RequestBody UserIdDto delegatedUser) {
		engine.getTaskService().delegateTask(taskId, delegatedUser.getUserId());
	}
	
	@GetMapping(path="/", produces= {MediaType.APPLICATION_JSON_VALUE, Hal.APPLICATION_HAL_JSON})
	public ResponseEntity<?> getTask(@PathVariable("taskId") String taskId, HttpServletRequest request) {
		String contentType = request.getHeader("Accept");
		if (StringUtils.hasText(contentType)) {
			if (contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
				return getJsonTask(taskId);
			} else if (contentType.startsWith(Hal.APPLICATION_HAL_JSON)) {
				return getHalTask(taskId);
			}
		}
		throw new InvalidRequestException(HttpStatus.NOT_ACCEPTABLE, "No acceptable content-type found");
	}
	

	@GetMapping(path="/form", produces=MediaType.APPLICATION_JSON_VALUE)
	public FormDto getForm(@PathVariable("taskId") String taskId) {
		FormService formService = engine.getFormService();
		Task task = getTaskById(taskId);
		FormData formData;
		try {
			formData = formService.getTaskFormData(taskId);
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			throw new RestException(HttpStatus.BAD_REQUEST, e, "Cannot get form for task " + taskId);
		}

		FormDto dto = FormDto.fromFormData(formData);
		if (dto.getKey() == null || dto.getKey().isEmpty()) {
			if (formData != null && formData.getFormFields() != null && !formData.getFormFields().isEmpty()) {
				dto.setKey("embedded:engine://engine/:engine/task/" + taskId + "/rendered-form");
			}
		}

		// to get the application context path it is necessary to
		// execute it without authentication (tries to fetch the
		// process definition), because:
		// - user 'demo' has READ permission on a specific task resource
		// - user 'demo' does not have a READ permission on the corresponding
		// process definition
		// -> running the following lines with authorization would lead
		// to an AuthorizationException because the user 'demo' does not
		// have READ permission on the corresponding process definition
		IdentityService identityService = engine.getIdentityService();
		Authentication currentAuthentication = identityService.getCurrentAuthentication();
		try {
			identityService.clearAuthentication();
			String processDefinitionId = task.getProcessDefinitionId();
			String caseDefinitionId = task.getCaseDefinitionId();
			if (processDefinitionId != null) {
				dto.setContextPath(ApplicationContextPathUtil.getApplicationPathByProcessDefinitionId(engine,
						processDefinitionId));

			} else if (caseDefinitionId != null) {
				dto.setContextPath(
						ApplicationContextPathUtil.getApplicationPathByCaseDefinitionId(engine, caseDefinitionId));
			}
		} finally {
			identityService.setAuthentication(currentAuthentication);
		}

		return dto;
	}

	@GetMapping(path="/rendered-form", produces=MediaType.APPLICATION_XHTML_XML_VALUE)
	public ResponseEntity<Resource> getRenderedForm(String taskId) {
		FormService formService = engine.getFormService();

		Object renderedTaskForm = formService.getRenderedTaskForm(taskId);
		if (renderedTaskForm != null) {
			String content = renderedTaskForm.toString();
			ByteArrayResource resource = new ByteArrayResource(content.getBytes(EncodingUtil.DEFAULT_ENCODING));
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_XHTML_XML).body(resource);
		}
		throw new InvalidRequestException(HttpStatus.NOT_FOUND,
				"No matching rendered form for task with the id " + taskId + " found.");
	}


	@PostMapping(path="/resolve", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void resolve(@PathVariable("taskId") String taskId, @RequestBody CompleteTaskDto dto) {
		TaskService taskService = engine.getTaskService();

		try {
			VariableMap variables = VariableValueDto.toMap(dto.getVariables(), engine, objectMapper);
			taskService.resolveTask(taskId, variables);

		} catch (RestException e) {
			String errorMessage = String.format("Cannot resolve task %s: %s", taskId, e.getMessage());
			throw new InvalidRequestException(e.getStatus(), e, errorMessage);

		}
	}

	@PostMapping(path="/assignee", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void setAssignee(@PathVariable("taskId") String taskId, UserIdDto dto) {
		TaskService taskService = engine.getTaskService();
		taskService.setAssignee(taskId, dto.getUserId());
	}

	@GetMapping(path="/identity-links", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<IdentityLinkDto> getIdentityLinks(
			@PathVariable("taskId") String taskId, 
			@RequestParam("type") String type) {
		TaskService taskService = engine.getTaskService();
		List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);

		List<IdentityLinkDto> result = new ArrayList<>();
		for (IdentityLink link : identityLinks) {
			if (type == null || type.equals(link.getType())) {
				result.add(IdentityLinkDto.fromIdentityLink(link));
			}
		}

		return result;
	}

	@PostMapping(path="/identity-links", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void addIdentityLink(
			@PathVariable("taskId") String taskId, 
			@RequestBody IdentityLinkDto identityLink) {
		
		TaskService taskService = engine.getTaskService();
		identityLink.validate();
		if (identityLink.getUserId() != null) {
			taskService.addUserIdentityLink(taskId, identityLink.getUserId(), identityLink.getType());
		} else if (identityLink.getGroupId() != null) {
			taskService.addGroupIdentityLink(taskId, identityLink.getGroupId(), identityLink.getType());
		}

	}

	@PostMapping(path="/identity-links/delete", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void deleteIdentityLink(
			@PathVariable("taskId") String taskId,  
			@RequestBody IdentityLinkDto identityLink) {
		TaskService taskService = engine.getTaskService();

		identityLink.validate();

		if (identityLink.getUserId() != null) {
			taskService.deleteUserIdentityLink(taskId, identityLink.getUserId(), identityLink.getType());
		} else if (identityLink.getGroupId() != null) {
			taskService.deleteGroupIdentityLink(taskId, identityLink.getGroupId(), identityLink.getType());
		}

	}

	@GetMapping(path="/form-variables", produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, VariableValueDto> getFormVariables(
			@PathVariable("taskId") String taskId,  
			@RequestParam("variableNames") String variableNames, 
			@RequestParam(name=VariableResource.DESERIALIZE_VALUES_QUERY_PARAM, defaultValue="true") boolean deserializeValues) {

		final FormService formService = engine.getFormService();
		List<String> formVariables = null;

		if (variableNames != null) {
			StringListConverter stringListConverter = new StringListConverter();
			formVariables = stringListConverter.convertQueryParameterToType(variableNames);
		}

		VariableMap startFormVariables = formService.getTaskFormVariables(taskId, formVariables, deserializeValues);

		return VariableValueDto.fromMap(startFormVariables);
	}


	@PutMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateTask(@PathVariable("taskId") String taskId,  @RequestBody TaskDto taskDto) {
		TaskService taskService = engine.getTaskService();

		Task task = getTaskById(taskId);

		if (task == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "No matching task with id " + taskId);
		}

		taskDto.updateTask(task);
		taskService.saveTask(task);
	}

	@DeleteMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void deleteTask(@PathVariable("taskId") String taskId) {
		TaskService taskService = engine.getTaskService();

		try {
			taskService.deleteTask(taskId);
		} catch (NotValidException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, "Could not delete task: " + e.getMessage());
		}
	}

	@GetMapping(path="/deployed-form")
	public ResponseEntity<Resource> getDeployedForm(@PathVariable("taskId") String taskId) {
		InputStream deployedTaskForm = null;
		try {
			deployedTaskForm = engine.getFormService().getDeployedTaskForm(taskId);
		} catch (NotFoundException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (NullValueException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (AuthorizationException e) {
			throw new InvalidRequestException(HttpStatus.FORBIDDEN, e.getMessage());
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_XHTML_XML).body(new InputStreamResource(deployedTaskForm));
	}
	
	protected ResponseEntity<TaskDto> getJsonTask(String taskId) {
		Task task = getTaskById(taskId);
		if (task == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "No matching task with id " + taskId);
		}

		return ResponseEntity.ok().body(TaskDto.fromEntity(task));
	}

	protected ResponseEntity<HalTask> getHalTask(String taskId) {
		Task task = getTaskById(taskId);
		if (task == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "No matching task with id " + taskId);
		}

		return ResponseEntity.ok().body(HalTask.generate(task, engine));
	}

	/**
	 * Returns the task with the given id
	 *
	 * @param id
	 * @return
	 */
	protected Task getTaskById(@PathVariable("taskId") String taskId) {
		return engine.getTaskService().createTaskQuery().taskId(taskId).initializeFormKeys().singleResult();
	}
}
