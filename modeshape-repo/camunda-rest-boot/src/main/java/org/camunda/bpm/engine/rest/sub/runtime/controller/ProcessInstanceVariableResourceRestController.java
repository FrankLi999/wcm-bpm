package org.camunda.bpm.engine.rest.sub.runtime.controller;

import java.io.IOException;
import java.util.Map;

import org.camunda.bpm.engine.rest.ProcessInstanceRestService;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.PatchVariablesDto;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.sub.VariableResource;
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

@RestController
@RequestMapping(ProcessInstanceRestService.PATH + "/{processInstanceId}/variables")
public class ProcessInstanceVariableResourceRestController extends AbstractRestProcessEngineAware {
	@Autowired
	protected ExecutionVariablesResource executionVariablesResource;

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, VariableValueDto> getVariables(
			@PathVariable("processInstanceId") String processInstanceId,
			@RequestParam(name=VariableResource.DESERIALIZE_VALUES_QUERY_PARAM, defaultValue="true") boolean deserializeValues) {
		return executionVariablesResource.getVariables(processInstanceId, deserializeValues);
	}

	@GetMapping(path="/{varId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public VariableValueDto getVariable(
			@PathVariable("processInstanceId") String processInstanceId,
		    @PathVariable("varId") String variableName,
		    @RequestParam(name=VariableResource.DESERIALIZE_VALUE_QUERY_PARAM, defaultValue="true") boolean deserializeValue) {
		return executionVariablesResource.getVariable(processInstanceId, variableName, deserializeValue);
	}

	@GetMapping(path="/{varId}/data")
	public ResponseEntity<?> getVariableBinary(
			@PathVariable("processInstanceId") String processInstanceId,
			@PathVariable("varId") String variableName) {
		return executionVariablesResource.getVariableBinary(processInstanceId, variableName);
	}

	@PutMapping(path="/{varId}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void putVariable(
			@PathVariable("processInstanceId") String processInstanceId,
			@PathVariable("varId") String variableName, 
			@RequestBody VariableValueDto variable) {
		executionVariablesResource.putVariable(processInstanceId, variableName, variable);
	}

	@PostMapping(path="/{varId}/data", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public void setBinaryVariable(
			@PathVariable("processInstanceId") String processInstanceId,
			@PathVariable("varId") String variableName, 
			@RequestParam(name="type", defaultValue="") String objectType, 
			@RequestParam(name="valueType", defaultValue="") String valueType,
			@RequestParam("dataFile") MultipartFile dataFile) throws IOException {
		executionVariablesResource.setBinaryVariable(processInstanceId, variableName, objectType, valueType, dataFile);
	}

	@DeleteMapping(path="/{varId}")
	public void deleteVariable(
			@PathVariable("processInstanceId") String processInstanceId,
			@PathVariable("varId") String variableName) {
		executionVariablesResource.deleteVariable(processInstanceId, variableName);
	}

	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void modifyVariables(
			@PathVariable("processInstanceId") String processInstanceId,
			@RequestBody PatchVariablesDto patch) {
		executionVariablesResource.modifyVariables(processInstanceId, patch);
	}
}
