package org.camunda.bpm.engine.rest.sub.repository.service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.exception.NotValidException;
import org.camunda.bpm.engine.impl.util.IoUtil;
import org.camunda.bpm.engine.repository.DecisionRequirementsDefinition;
import org.camunda.bpm.engine.rest.dto.repository.DecisionRequirementsDefinitionDto;
import org.camunda.bpm.engine.rest.dto.repository.DecisionRequirementsDefinitionXmlDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.repository.DecisionRequirementsDefinitionResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class DecisionRequirementsDefinitionService implements DecisionRequirementsDefinitionResource {
	@Autowired
	protected ProcessEngine engine;

	public DecisionRequirementsDefinitionDto getDecisionRequirementsDefinition(String drdId) {
		RepositoryService repositoryService = engine.getRepositoryService();

		DecisionRequirementsDefinition definition = null;

		try {
			definition = repositoryService.getDecisionRequirementsDefinition(drdId);

		} catch (NotFoundException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e, e.getMessage());

		} catch (NotValidException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, e.getMessage());

		} catch (ProcessEngineException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e);

		}

		return DecisionRequirementsDefinitionDto.fromDecisionRequirementsDefinition(definition);
	}

	public DecisionRequirementsDefinitionXmlDto getDecisionRequirementsDefinitionDmnXml(String drdId) {
		InputStream decisionRequirementsModelInputStream = null;
		try {
			decisionRequirementsModelInputStream = engine.getRepositoryService()
					.getDecisionRequirementsModel(drdId);

			byte[] decisionRequirementsModel = IoUtil.readInputStream(decisionRequirementsModelInputStream,
					"decisionRequirementsModelDmnXml");
			return DecisionRequirementsDefinitionXmlDto.create(drdId,
					new String(decisionRequirementsModel, "UTF-8"));

		} catch (NotFoundException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e, e.getMessage());

		} catch (NotValidException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, e.getMessage());

		} catch (ProcessEngineException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e);

		} catch (UnsupportedEncodingException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e);

		} finally {
			IoUtil.closeSilently(decisionRequirementsModelInputStream);
		}
	}

	public ResponseEntity<?> getDecisionRequirementsDefinitionDiagram(String drdId) {
		DecisionRequirementsDefinition definition = engine.getRepositoryService()
				.getDecisionRequirementsDefinition(drdId);
		InputStream decisionRequirementsDiagram = engine.getRepositoryService()
				.getDecisionRequirementsDiagram(drdId);
		if (decisionRequirementsDiagram == null) {
			return ResponseEntity.noContent().build();
		} else {
			String fileName = definition.getDiagramResourceName();
			return ResponseEntity.ok()
					.header("Content-Disposition", "attachment; filename=" + fileName)
					.contentType(MediaType.parseMediaType(ProcessDefinitionService.getMediaTypeForFileSuffix(fileName)))
					.body(new InputStreamResource(decisionRequirementsDiagram));
		}
	}
	
	public DecisionRequirementsDefinition getDecisionRequirementsDefinitionByKey(
			String decisionRequirementsDefinitionKey) {
		return this.engine.getRepositoryService()
			.createDecisionRequirementsDefinitionQuery()
			.decisionRequirementsDefinitionKey(decisionRequirementsDefinitionKey).withoutTenantId().latestVersion()
			.singleResult();
	}
	
	public DecisionRequirementsDefinition getDecisionRequirementsDefinitionByKeyAndTenant(
			String decisionRequirementsDefinitionKey,
			String tenantId) {
		return this.engine.getRepositoryService()
				.createDecisionRequirementsDefinitionQuery()
				.decisionRequirementsDefinitionKey(decisionRequirementsDefinitionKey).tenantIdIn(tenantId)
				.latestVersion().singleResult();
	}
}
