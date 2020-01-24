package org.camunda.bpm.engine.rest.sub.runtime.controller;

import java.io.IOException;
import java.util.Map;

import org.camunda.bpm.engine.rest.CaseExecutionRestService;
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
@RequestMapping(CaseExecutionRestService.PATH + "/{caseExecutionId}/variables")
public class CaseExecutionVariableRestController {

	@Autowired
	CaseExecutionVariablesService caseExecutionVariablesService;
	
	@GetMapping(path="/variables", produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, VariableValueDto> getVariables(
			@PathVariable("caseExecutionId") String caseExecutionId,
			@RequestParam(name=VariableResource.DESERIALIZE_VALUES_QUERY_PARAM, defaultValue= "true") boolean deserializeValues) {
		return this.caseExecutionVariablesService.getVariables(caseExecutionId, deserializeValues);
	}

	@GetMapping(path="/variables/{varId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public VariableValueDto getVariable(
			@PathVariable("caseExecutionId") String caseExecutionId,
			@PathVariable("varId") String variableName,
			@RequestParam(name=VariableResource.DESERIALIZE_VALUE_QUERY_PARAM, defaultValue="true") boolean deserializeValue) {
		return this.caseExecutionVariablesService.getVariable(caseExecutionId, variableName, deserializeValue);
	}

	@GetMapping(path="/variables/{varId}/data", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getVariableBinary(@PathVariable("caseExecutionId") String caseExecutionId,
			@PathVariable("varId") String variableName) {
		return this.caseExecutionVariablesService.getVariableBinary(caseExecutionId, variableName);
	}

	@PutMapping(path="/variables/{varId}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void putVariable(@PathVariable("caseExecutionId") String caseExecutionId,
			@PathVariable("varId") String variableName, 
			@RequestBody VariableValueDto variable) {
		this.caseExecutionVariablesService.putVariable(caseExecutionId, variableName, variable);
	}

	@PostMapping(path="/variables/{varId}/data", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public void setBinaryVariable(@PathVariable("caseExecutionId") String caseExecutionId,
			@PathVariable("varId") String variableName,  
			@RequestParam(name="type", defaultValue="") String objectType, 
			@RequestParam(name="valueType", defaultValue="") String valueType,
			@RequestParam("dataFile") MultipartFile dataFile) throws IOException {
		this.caseExecutionVariablesService.setBinaryVariable(caseExecutionId, variableName, objectType, valueType, dataFile);
	}

	@DeleteMapping(path="/variables/{varId}")
	public void deleteVariable(@PathVariable("caseExecutionId") String caseExecutionId,
			@PathVariable("varId") String variableName) {
		this.caseExecutionVariablesService.deleteVariable(caseExecutionId, variableName);
	}

	@PostMapping(path="/variables", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void modifyVariables(@PathVariable("caseExecutionId") String caseExecutionId,
			@RequestBody PatchVariablesDto patch) {
		this.caseExecutionVariablesService.modifyVariables(caseExecutionId, patch);
	}

}
