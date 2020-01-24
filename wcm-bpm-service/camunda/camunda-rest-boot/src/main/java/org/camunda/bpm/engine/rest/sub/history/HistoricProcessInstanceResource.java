package org.camunda.bpm.engine.rest.sub.history;

import org.camunda.bpm.engine.rest.dto.history.HistoricProcessInstanceDto;

public interface HistoricProcessInstanceResource {
	HistoricProcessInstanceDto getHistoricProcessInstance(String processInstanceId);
	void deleteHistoricProcessInstance(String processInstanceId, Boolean failIfNotExists);
}
