package org.camunda.bpm.engine.rest.sub.repository;

import org.camunda.bpm.engine.rest.dto.repository.DecisionRequirementsDefinitionDto;
import org.camunda.bpm.engine.rest.dto.repository.DecisionRequirementsDefinitionXmlDto;
import org.springframework.http.ResponseEntity;

public interface DecisionRequirementsDefinitionResource {
	DecisionRequirementsDefinitionDto getDecisionRequirementsDefinition(String drdId);

	DecisionRequirementsDefinitionXmlDto getDecisionRequirementsDefinitionDmnXml(String drdId);

	ResponseEntity<?> getDecisionRequirementsDefinitionDiagram(String drdId);
}
