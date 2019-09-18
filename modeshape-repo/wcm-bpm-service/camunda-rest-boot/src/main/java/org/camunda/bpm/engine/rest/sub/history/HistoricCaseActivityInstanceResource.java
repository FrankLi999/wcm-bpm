package org.camunda.bpm.engine.rest.sub.history;

import org.camunda.bpm.engine.rest.dto.history.HistoricCaseActivityInstanceDto;

public interface HistoricCaseActivityInstanceResource {
	HistoricCaseActivityInstanceDto getHistoricCaseActivityInstance(String caseActivityInstanceId);
}
