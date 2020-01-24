package org.camunda.bpm.engine.rest.sub.repository.service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnDecisionResultEntries;
import org.camunda.bpm.dmn.engine.DmnEngineException;
import org.camunda.bpm.engine.AuthorizationException;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.exception.NotValidException;
import org.camunda.bpm.engine.impl.util.IoUtil;
import org.camunda.bpm.engine.repository.DecisionDefinition;
import org.camunda.bpm.engine.rest.dto.HistoryTimeToLiveDto;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.dmn.EvaluateDecisionDto;
import org.camunda.bpm.engine.rest.dto.repository.DecisionDefinitionDiagramDto;
import org.camunda.bpm.engine.rest.dto.repository.DecisionDefinitionDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.repository.DecisionDefinitionResource;
import org.camunda.bpm.engine.rest.sub.repository.controller.ProcessDefinitionResourceRestController;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DecisionDefinitionService implements DecisionDefinitionResource {
	@Autowired
	protected ProcessEngine engine;
	protected ObjectMapper objectMapper = new ObjectMapper();
	public DecisionDefinitionDto getDecisionDefinition(@PathVariable("decisionDefinitionId") String decisionDefinitionId) {
		RepositoryService repositoryService = engine.getRepositoryService();

		DecisionDefinition definition = null;

		try {
			definition = repositoryService.getDecisionDefinition(decisionDefinitionId);

		} catch (NotFoundException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e, e.getMessage());

		} catch (NotValidException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, e.getMessage());

		} catch (ProcessEngineException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e);

		}

		return DecisionDefinitionDto.fromDecisionDefinition(definition);
	}

	public DecisionDefinitionDiagramDto getDecisionDefinitionDmnXml(@PathVariable("decisionDefinitionId") String decisionDefinitionId) {
		InputStream decisionModelInputStream = null;
		try {
			decisionModelInputStream = engine.getRepositoryService().getDecisionModel(decisionDefinitionId);

			byte[] decisionModel = IoUtil.readInputStream(decisionModelInputStream, "decisionModelDmnXml");
			return DecisionDefinitionDiagramDto.create(decisionDefinitionId, new String(decisionModel, "UTF-8"));

		} catch (NotFoundException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e, e.getMessage());

		} catch (NotValidException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, e.getMessage());

		} catch (ProcessEngineException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e);

		} catch (UnsupportedEncodingException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e);

		} finally {
			IoUtil.closeSilently(decisionModelInputStream);
		}
	}

	public ResponseEntity<?> getDecisionDefinitionDiagram(@PathVariable("decisionDefinitionId") String decisionDefinitionId) {
		DecisionDefinition definition = engine.getRepositoryService().getDecisionDefinition(decisionDefinitionId);
		InputStream decisionDiagram = engine.getRepositoryService().getDecisionDiagram(decisionDefinitionId);
		if (decisionDiagram == null) {
			return ResponseEntity.noContent().build();
		} else {
			String fileName = definition.getDiagramResourceName();
			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + fileName)
					.contentType(MediaType.parseMediaType(ProcessDefinitionService.getMediaTypeForFileSuffix(fileName))).body(new InputStreamResource(decisionDiagram));
		}
	}

	public List<Map<String, VariableValueDto>> evaluateDecision(@PathVariable("decisionDefinitionId") String decisionDefinitionId, HttpServletRequest request, @RequestBody EvaluateDecisionDto parameters) {
		DecisionService decisionService = engine.getDecisionService();

		Map<String, Object> variables = VariableValueDto.toMap(parameters.getVariables(), engine, objectMapper);

		try {
			DmnDecisionResult decisionResult = decisionService.evaluateDecisionById(decisionDefinitionId)
					.variables(variables).evaluate();

			return createDecisionResultDto(decisionResult);

		} catch (AuthorizationException e) {
			throw e;
		} catch (NotFoundException e) {
			String errorMessage = String.format("Cannot evaluate decision %s: %s", decisionDefinitionId,
					e.getMessage());
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e, errorMessage);
		} catch (NotValidException e) {
			String errorMessage = String.format("Cannot evaluate decision %s: %s", decisionDefinitionId,
					e.getMessage());
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, errorMessage);
		} catch (ProcessEngineException e) {
			String errorMessage = String.format("Cannot evaluate decision %s: %s", decisionDefinitionId,
					e.getMessage());
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, errorMessage);
		} catch (DmnEngineException e) {
			String errorMessage = String.format("Cannot evaluate decision %s: %s", decisionDefinitionId,
					e.getMessage());
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, errorMessage);
		}
	}

	public void updateHistoryTimeToLive(@PathVariable("decisionDefinitionId") String decisionDefinitionId, @RequestBody HistoryTimeToLiveDto historyTimeToLiveDto) {
		engine.getRepositoryService().updateDecisionDefinitionHistoryTimeToLive(decisionDefinitionId,
				historyTimeToLiveDto.getHistoryTimeToLive());
	}
	
	public DecisionDefinition getDecisionDefinitionByKey(String decisionDefinitionKey) {
		
		return this.engine.getRepositoryService()
				.createDecisionDefinitionQuery().decisionDefinitionKey(decisionDefinitionKey).withoutTenantId()
				.latestVersion().singleResult();
	}
	
	public DecisionDefinition getDecisionDefinitionByKeyAndTenant(String decisionDefinitionKey, String tenantId) {
		
		return this.engine.getRepositoryService()
				.createDecisionDefinitionQuery().decisionDefinitionKey(decisionDefinitionKey).tenantIdIn(tenantId)
				.latestVersion().singleResult();
	}

	protected List<Map<String, VariableValueDto>> createDecisionResultDto(DmnDecisionResult decisionResult) {
		List<Map<String, VariableValueDto>> dto = new ArrayList<>();

		for (DmnDecisionResultEntries entries : decisionResult) {
			Map<String, VariableValueDto> resultEntriesDto = createResultEntriesDto(entries);
			dto.add(resultEntriesDto);
		}

		return dto;
	}
	

	protected Map<String, VariableValueDto> createResultEntriesDto(DmnDecisionResultEntries entries) {
		VariableMap variableMap = Variables.createVariables();

		for (String key : entries.keySet()) {
			TypedValue typedValue = entries.getEntryTyped(key);
			variableMap.putValueTyped(key, typedValue);
		}

		return VariableValueDto.fromMap(variableMap);
	}

}
