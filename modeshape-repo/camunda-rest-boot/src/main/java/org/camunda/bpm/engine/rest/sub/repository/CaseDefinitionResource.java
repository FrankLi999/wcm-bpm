package org.camunda.bpm.engine.rest.sub.repository;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.rest.dto.HistoryTimeToLiveDto;
import org.camunda.bpm.engine.rest.dto.repository.CaseDefinitionDiagramDto;
import org.camunda.bpm.engine.rest.dto.repository.CaseDefinitionDto;
import org.camunda.bpm.engine.rest.dto.runtime.CaseInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.CreateCaseInstanceDto;
import org.springframework.http.ResponseEntity;

public interface CaseDefinitionResource {
	CaseDefinitionDto getCaseDefinition(String caseDefinitionId);
	CaseDefinitionDiagramDto getCaseDefinitionCmmnXml(String caseDefinitionId);
	CaseInstanceDto createCaseInstance(String caseDefinitionId, HttpServletRequest Request, CreateCaseInstanceDto parameters);
	ResponseEntity<?> getCaseDefinitionDiagram(String caseDefinitionId);
	void updateHistoryTimeToLive(String caseDefinitionId, HistoryTimeToLiveDto historyTimeToLiveDto);
}
