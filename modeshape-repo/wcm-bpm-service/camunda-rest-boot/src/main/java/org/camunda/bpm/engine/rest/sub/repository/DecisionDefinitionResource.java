package org.camunda.bpm.engine.rest.sub.repository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.rest.dto.HistoryTimeToLiveDto;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.dmn.EvaluateDecisionDto;
import org.camunda.bpm.engine.rest.dto.repository.DecisionDefinitionDiagramDto;
import org.camunda.bpm.engine.rest.dto.repository.DecisionDefinitionDto;
import org.springframework.http.ResponseEntity;

public interface DecisionDefinitionResource {
	DecisionDefinitionDto getDecisionDefinition(String decisionDefinitionId);

	DecisionDefinitionDiagramDto getDecisionDefinitionDmnXml(String decisionDefinitionId);

	ResponseEntity<?> getDecisionDefinitionDiagram(String decisionDefinitionId);

	List<Map<String, VariableValueDto>> evaluateDecision(String decisionDefinitionId, HttpServletRequest request, EvaluateDecisionDto parameters);

	void updateHistoryTimeToLive(String decisionDefinitionId, HistoryTimeToLiveDto historyTimeToLiveDto);
}
