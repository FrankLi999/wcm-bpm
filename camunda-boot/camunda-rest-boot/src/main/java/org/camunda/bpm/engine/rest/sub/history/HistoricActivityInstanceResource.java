package org.camunda.bpm.engine.rest.sub.history;

import org.camunda.bpm.engine.rest.dto.history.HistoricActivityInstanceDto;

public interface HistoricActivityInstanceResource {
	HistoricActivityInstanceDto getHistoricActivityInstance(String activityInstanceId);
}
