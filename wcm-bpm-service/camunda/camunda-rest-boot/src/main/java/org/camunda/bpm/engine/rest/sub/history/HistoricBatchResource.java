package org.camunda.bpm.engine.rest.sub.history;

import org.camunda.bpm.engine.rest.dto.history.batch.HistoricBatchDto;

public interface HistoricBatchResource {
	HistoricBatchDto getHistoricBatch(String batchId);
    void deleteHistoricBatch(String batchId);
}
