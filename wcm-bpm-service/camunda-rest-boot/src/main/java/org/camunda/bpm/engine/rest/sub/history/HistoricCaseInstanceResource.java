package org.camunda.bpm.engine.rest.sub.history;

import org.camunda.bpm.engine.rest.dto.history.HistoricCaseInstanceDto;

public interface HistoricCaseInstanceResource {
	HistoricCaseInstanceDto getHistoricCaseInstance(String caseInstanceId);
}
