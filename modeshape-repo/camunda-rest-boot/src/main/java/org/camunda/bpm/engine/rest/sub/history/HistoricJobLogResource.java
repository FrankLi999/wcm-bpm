package org.camunda.bpm.engine.rest.sub.history;

import org.camunda.bpm.engine.rest.dto.history.HistoricJobLogDto;

public interface HistoricJobLogResource {
	HistoricJobLogDto getHistoricJobLog(String id);
	String getStacktrace(String id);
}
