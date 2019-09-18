package org.camunda.bpm.engine.rest.sub.runtime.controller;

import java.io.IOException;
import java.util.Map;

import org.camunda.bpm.engine.rest.CaseInstanceRestService;
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
@RequestMapping(CaseInstanceRestService.PATH + "/{caseInstanceId}/variables")
public class CaseInstanceVariableRestController {

	@Autowired
	CaseExecutionVariablesService caseExecutionVariablesService;
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, VariableValueDto> getVariables(
			@PathVariable("caseInstanceId") String caseInstanceId,
			@RequestParam(name=VariableResource.DESERIALIZE_VALUES_QUERY_PARAM, defaultValue= "true") boolean deserializeValues) {
		return this.caseExecutionVariablesService.getVariables(caseInstanceId, deserializeValues);
	}

	@GetMapping(path="/{varId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public VariableValueDto getVariable(
			@PathVariable("caseInstanceId") String caseInstanceId,
			@PathVariable("varId") String variableName,
			@RequestParam(name=VariableResource.DESERIALIZE_VALUE_QUERY_PARAM, defaultValue="true") boolean deserializeValue) {
		return this.caseExecutionVariablesService.getVariable(caseInstanceId, variableName, deserializeValue);
	}

	@GetMapping(path="/{varId}/data", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getVariableBinary(@PathVariable("caseInstanceId") String caseInstanceId,
			@PathVariable("varId") String variableName) {
		return this.caseExecutionVariablesService.getVariableBinary(caseInstanceId, variableName);
	}

	@PutMapping(path="/{varId}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void putVariable(@PathVariable("caseInstanceId") String caseInstanceId,
			@PathVariable("varId") String variableName, 
			@RequestBody VariableValueDto variable) {
		this.caseExecutionVariablesService.putVariable(caseInstanceId, variableName, variable);
	}

	@PostMapping(path="/{varId}/data", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public void setBinaryVariable(@PathVariable("caseInstanceId") String caseInstanceId,
			@PathVariable("varId") String variableName,  
			@RequestParam(name="type", defaultValue="") String objectType, 
			@RequestParam(name="valueType", defaultValue="") String valueType,
			@RequestParam("dataFile") MultipartFile dataFile) throws IOException {
		this.caseExecutionVariablesService.setBinaryVariable(caseInstanceId, variableName, objectType, valueType, dataFile);
	}

	@DeleteMapping(path="/{varId}")
	public void deleteVariable(@PathVariable("caseInstanceId") String caseInstanceId,
			@PathVariable("varId") String variableName) {
		this.caseExecutionVariablesService.deleteVariable(caseInstanceId, variableName);
	}

	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void modifyVariables(@PathVariable("caseInstanceId") String caseInstanceId,
			@RequestBody PatchVariablesDto patch) {
		this.caseExecutionVariablesService.modifyVariables(caseInstanceId, patch);
	}
}
