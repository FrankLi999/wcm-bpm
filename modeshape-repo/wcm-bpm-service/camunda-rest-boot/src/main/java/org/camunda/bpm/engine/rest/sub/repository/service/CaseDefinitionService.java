package org.camunda.bpm.engine.rest.sub.repository.service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.exception.NotAllowedException;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.exception.NotValidException;
import org.camunda.bpm.engine.impl.util.IoUtil;
import org.camunda.bpm.engine.repository.CaseDefinition;
import org.camunda.bpm.engine.rest.CaseInstanceRestService;
import org.camunda.bpm.engine.rest.dto.HistoryTimeToLiveDto;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.repository.CaseDefinitionDiagramDto;
import org.camunda.bpm.engine.rest.dto.repository.CaseDefinitionDto;
import org.camunda.bpm.engine.rest.dto.runtime.CaseInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.CreateCaseInstanceDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.repository.CaseDefinitionResource;
import org.camunda.bpm.engine.rest.sub.repository.controller.ProcessDefinitionResourceRestController;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CaseDefinitionService implements CaseDefinitionResource {
	// public final static String PATH = "/camunda/api/engine/sub/repository/case-definition";
	
	@Autowired
	protected ProcessEngine engine;
	
	protected String rootResourcePath = "http://localhost:8080";
	protected ObjectMapper objectMapper = new ObjectMapper();

	public CaseDefinitionDto getCaseDefinition(String caseDefinitionId) {
		RepositoryService repositoryService = engine.getRepositoryService();

		CaseDefinition definition = null;

		try {
			definition = repositoryService.getCaseDefinition(caseDefinitionId);

		} catch (NotFoundException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e, e.getMessage());

		} catch (NotValidException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, e.getMessage());

		} catch (ProcessEngineException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e);

		}

		return CaseDefinitionDto.fromCaseDefinition(definition);
	}

	public CaseDefinitionDiagramDto getCaseDefinitionCmmnXml(String caseDefinitionId) {
		InputStream caseModelInputStream = null;
		try {
			caseModelInputStream = engine.getRepositoryService().getCaseModel(caseDefinitionId);

			byte[] caseModel = IoUtil.readInputStream(caseModelInputStream, "caseModelCmmnXml");
			return CaseDefinitionDiagramDto.create(caseDefinitionId, new String(caseModel, "UTF-8"));

		} catch (NotFoundException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e, e.getMessage());

		} catch (NotValidException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, e.getMessage());

		} catch (ProcessEngineException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e);

		} catch (UnsupportedEncodingException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e);

		} finally {
			IoUtil.closeSilently(caseModelInputStream);
		}
	}

	public CaseInstanceDto createCaseInstance(String caseDefinitionId, HttpServletRequest Request, CreateCaseInstanceDto parameters) {
		CaseService caseService = engine.getCaseService();

		CaseInstance instance = null;
		try {

			String businessKey = parameters.getBusinessKey();
			VariableMap variables = VariableValueDto.toMap(parameters.getVariables(), engine, objectMapper);

			instance = caseService.withCaseDefinition(caseDefinitionId).businessKey(businessKey).setVariables(variables)
					.create();

		} catch (RestException e) {
			String errorMessage = String.format("Cannot instantiate case definition %s: %s", caseDefinitionId,
					e.getMessage());
			throw new InvalidRequestException(e.getStatus(), e, errorMessage);

		} catch (NotFoundException e) {
			String errorMessage = String.format("Cannot instantiate case definition %s: %s", caseDefinitionId,
					e.getMessage());
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e, errorMessage);

		} catch (NotValidException e) {
			String errorMessage = String.format("Cannot instantiate case definition %s: %s", caseDefinitionId,
					e.getMessage());
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, errorMessage);

		} catch (NotAllowedException e) {
			String errorMessage = String.format("Cannot instantiate case definition %s: %s", caseDefinitionId,
					e.getMessage());
			throw new InvalidRequestException(HttpStatus.FORBIDDEN, e, errorMessage);

		} catch (ProcessEngineException e) {
			String errorMessage = String.format("Cannot instantiate case definition %s: %s", caseDefinitionId,
					e.getMessage());
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, errorMessage);

		}

		CaseInstanceDto result = CaseInstanceDto.fromCaseInstance(instance);

		URI uri = UriComponentsBuilder.fromPath(rootResourcePath).path(CaseInstanceRestService.PATH)
				.path(instance.getId()).build().toUri();

		result.addReflexiveLink(uri, HttpMethod.GET.name(), "self");

		return result;
	}

	public ResponseEntity<?> getCaseDefinitionDiagram(String caseDefinitionId) {
		CaseDefinition definition = engine.getRepositoryService().getCaseDefinition(caseDefinitionId);
		InputStream caseDiagram = engine.getRepositoryService().getCaseDiagram(caseDefinitionId);
		if (caseDiagram == null) {
			return ResponseEntity.noContent().build();
		} else {
			String fileName = definition.getDiagramResourceName();
			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + fileName)
					.contentType(MediaType.parseMediaType(ProcessDefinitionService.getMediaTypeForFileSuffix(fileName))).body(new InputStreamResource(caseDiagram));
		}
	}

	public void updateHistoryTimeToLive(String caseDefinitionId, HistoryTimeToLiveDto historyTimeToLiveDto) {
		engine.getRepositoryService().updateCaseDefinitionHistoryTimeToLive(caseDefinitionId,
				historyTimeToLiveDto.getHistoryTimeToLive());
	}
	
	public CaseDefinition getCaseDefinitionByCaseDefinitionKey(String caseDefinitionKey) {
		return this.engine.getRepositoryService().createCaseDefinitionQuery()
				.caseDefinitionKey(caseDefinitionKey).withoutTenantId().latestVersion().singleResult();
	}
	
	public CaseDefinition getCaseDefinitionByKeyAndTenant(String caseDefinitionKey, String tenantId) {
		return this.engine.getRepositoryService().createCaseDefinitionQuery()
			.caseDefinitionKey(caseDefinitionKey).tenantIdIn(tenantId).latestVersion().singleResult();
	}
}