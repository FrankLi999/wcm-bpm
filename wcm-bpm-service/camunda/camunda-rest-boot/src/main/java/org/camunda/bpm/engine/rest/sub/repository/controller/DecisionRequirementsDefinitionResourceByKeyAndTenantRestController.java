package org.camunda.bpm.engine.rest.sub.repository.controller;

import org.camunda.bpm.engine.repository.DecisionRequirementsDefinition;
import org.camunda.bpm.engine.rest.DecisionRequirementsDefinitionRestService;
import org.camunda.bpm.engine.rest.dto.repository.DecisionRequirementsDefinitionDto;
import org.camunda.bpm.engine.rest.dto.repository.DecisionRequirementsDefinitionXmlDto;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.repository.service.DecisionRequirementsDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DecisionRequirementsDefinitionRestService.PATH + "/key/{key}/tenant-id/{tenantId}")
public class DecisionRequirementsDefinitionResourceByKeyAndTenantRestController {
	
	@Autowired
	protected DecisionRequirementsDefinitionService decisionRequirementsDefinitionService;

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public DecisionRequirementsDefinitionDto getDecisionRequirementsDefinition(@PathVariable("key") String key, @PathVariable("tenantId") String tenantId) {
		return decisionRequirementsDefinitionService.getDecisionRequirementsDefinition(this.getDrdId(key, tenantId));
	}

	@GetMapping(path="/xml", produces=MediaType.APPLICATION_JSON_VALUE)
	public DecisionRequirementsDefinitionXmlDto getDecisionRequirementsDefinitionDmnXml(@PathVariable("key") String key, @PathVariable("tenantId") String tenantId) {
		return decisionRequirementsDefinitionService.getDecisionRequirementsDefinitionDmnXml(this.getDrdId(key, tenantId));
	}

	@GetMapping(path="/diagram")
	public ResponseEntity<?> getDecisionRequirementsDefinitionDiagram(@PathVariable("key") String key, @PathVariable("tenantId") String tenantId) {
		return decisionRequirementsDefinitionService.getDecisionRequirementsDefinitionDiagram(this.getDrdId(key, tenantId));
	}
	
	protected String getDrdId(String decisionRequirementsDefinitionKey, String tenantId) {
		DecisionRequirementsDefinition decisionRequirementsDefinition = decisionRequirementsDefinitionService.getDecisionRequirementsDefinitionByKeyAndTenant(decisionRequirementsDefinitionKey, tenantId);
		if (decisionRequirementsDefinition == null) {
			String errorMessage = String.format(
					"No matching decision requirements definition with key: %s and no tenant-id",
					decisionRequirementsDefinitionKey);
			throw new RestException(HttpStatus.NOT_FOUND, errorMessage);

		}
		return decisionRequirementsDefinition.getId();
	}
}
