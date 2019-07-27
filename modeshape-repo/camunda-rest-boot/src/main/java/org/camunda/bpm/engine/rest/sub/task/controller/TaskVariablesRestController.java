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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.TaskServiceImpl;
import org.camunda.bpm.engine.rest.TaskRestService;
import org.camunda.bpm.engine.rest.dto.PatchVariablesDto;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.sub.VariableResource;
import org.camunda.bpm.engine.rest.sub.impl.AbstractVariablesService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Daniel Meyer
 *
 */
@RestController
@RequestMapping(TaskRestService.PATH + "/{taskId}/variables")
public class TaskVariablesRestController extends AbstractVariablesService {
	@Autowired 
	ProcessEngine engine;
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, VariableValueDto> getVariables(
			@PathVariable("taskId") String taskId,
			@RequestParam(name=VariableResource.DESERIALIZE_VALUES_QUERY_PARAM, defaultValue= "true") boolean deserializeValues) {
		return super.getVariables(taskId, deserializeValues);
	}

	@GetMapping(path="/{varId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public VariableValueDto getVariable(
			@PathVariable("taskId") String taskId,
			@PathVariable("varId") String variableName,
			@RequestParam(name=VariableResource.DESERIALIZE_VALUE_QUERY_PARAM, defaultValue="true") boolean deserializeValue) {
		return super.getVariable(taskId, variableName, deserializeValue);
	}

	@GetMapping(path="/{varId}/data", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getVariableBinary(@PathVariable("taskId") String taskId,
			@PathVariable("varId") String variableName) {
		return super.getVariableBinary(taskId, variableName);
	}

	@PutMapping(path="/{varId}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void putVariable(@PathVariable("taskId") String taskId,
			@PathVariable("varId") String variableName, @RequestBody VariableValueDto variable) {
		super.putVariable(taskId, variableName, variable);
	}

	@PostMapping(path="/{varId}/data", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public void setBinaryVariable(@PathVariable("taskId") String taskId,
			@PathVariable("varId") String variableName, 
			@RequestParam(name="type", defaultValue="") String objectType, 
			@RequestParam(name="valueType", defaultValue="") String valueType,
			@RequestParam("dataFile") MultipartFile dataFile) throws IOException {
		super.setBinaryVariable(taskId, variableName, objectType, valueType, dataFile);
	}

	@DeleteMapping(path="/{varId}")
	public void deleteVariable(@PathVariable("taskId") String taskId,
			@PathVariable("varId") String variableName) {
		super.deleteVariable(taskId, variableName);
	}

	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void modifyVariables(@PathVariable("taskId") String taskId, @RequestBody PatchVariablesDto patch) {
		super.modifyVariables(taskId, patch);
	}
	
	protected String getResourceTypeName() {
		return "task";
	}

	protected TypedValue getVariableEntity(String resourceId, String variableKey, boolean deserializeValue) {
		return engine.getTaskService().getVariableTyped(resourceId, variableKey, deserializeValue);
	}

	protected VariableMap getVariableEntities(String resourceId, boolean deserializeValues) {
		return engine.getTaskService().getVariablesTyped(resourceId, deserializeValues);
	}

	protected void removeVariableEntity(String resourceId, String variableKey) {
		engine.getTaskService().removeVariable(resourceId, variableKey);
	}

	protected void updateVariableEntities(String resourceId, VariableMap modifications, List<String> deletions) {
		TaskServiceImpl taskService = (TaskServiceImpl) engine.getTaskService();
		taskService.updateVariables(resourceId, modifications, deletions);
	}

	protected void setVariableEntity(String resourceId, String variableKey, TypedValue variableValue) {
		engine.getTaskService().setVariable(resourceId, variableKey, variableValue);
	}

}
