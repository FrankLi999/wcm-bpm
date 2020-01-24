package org.camunda.bpm.engine.rest.sub.runtime.controller;

import java.io.IOException;
import java.util.Map;

import org.camunda.bpm.engine.rest.ExecutionRestService;
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
@RequestMapping(ExecutionRestService.PATH + "/{executionId}/localVariables")
public class ExecutionLocalVariableRestController {
	@Autowired
	protected LocalExecutionVariablesService localExecutionVariablesService;
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, VariableValueDto> getLocalVariables(
			@PathVariable("executionId") String executionId,
			@RequestParam(name=VariableResource.DESERIALIZE_VALUES_QUERY_PARAM, defaultValue= "true") boolean deserializeValues) {
		return this.localExecutionVariablesService.getVariables(executionId, deserializeValues);
	}

	@GetMapping(path="/{varId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public VariableValueDto getLocalVariable(
			@PathVariable("executionId") String executionId,
			@PathVariable("varId") String variableName,
			@RequestParam(name=VariableResource.DESERIALIZE_VALUE_QUERY_PARAM, defaultValue="true") boolean deserializeValue) {
		return this.localExecutionVariablesService.getVariable(executionId, variableName, deserializeValue);
	}

	@GetMapping(path="/{varId}/data", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getLocalVariableBinary(@PathVariable("executionId") String executionId,
			@PathVariable("varId") String variableName) {
		return this.localExecutionVariablesService.getVariableBinary(executionId, variableName);
	}

	@PutMapping(path="/{varId}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void putLocalVariable(@PathVariable("executionId") String executionId,
			@PathVariable("varId") String variableName, @RequestBody VariableValueDto variable) {
		this.localExecutionVariablesService.putVariable(executionId, variableName, variable);
	}

	@PostMapping(path="/{varId}/data", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public void setLocalBinaryVariable(@PathVariable("executionId") String executionId,
			@PathVariable("varId") String variableName,  
			@RequestParam(name="type", defaultValue="") String objectType, 
			@RequestParam(name="valueType", defaultValue="") String valueType,
			@RequestParam("dataFile") MultipartFile dataFile) throws IOException {
		this.localExecutionVariablesService.setBinaryVariable(executionId, variableName, objectType, valueType, dataFile);
	}

	@DeleteMapping(path="/{varId}")
	void deleteLocalVariable(@PathVariable("executionId") String executionId,
			@PathVariable("varId") String variableName) {
		this.localExecutionVariablesService.deleteVariable(executionId, variableName);
	}

	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void modifyLocalVariables(@PathVariable("executionId") String executionId, @RequestBody PatchVariablesDto patch) {
		this.localExecutionVariablesService.modifyVariables(executionId, patch);
	}
}
