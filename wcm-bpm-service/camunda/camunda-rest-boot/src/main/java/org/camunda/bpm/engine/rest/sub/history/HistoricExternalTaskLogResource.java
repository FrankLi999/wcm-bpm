package org.camunda.bpm.engine.rest.sub.history;

import org.camunda.bpm.engine.rest.dto.history.HistoricExternalTaskLogDto;

public interface HistoricExternalTaskLogResource {
	HistoricExternalTaskLogDto getHistoricExternalTaskLog(String id);
	String getErrorDetails(String id);
}
