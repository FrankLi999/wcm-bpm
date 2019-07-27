package org.camunda.bpm.engine.rest.sub.batch;

import org.camunda.bpm.engine.rest.dto.SuspensionStateDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;

public interface BatchResource {
	BatchDto getBatch(String batchId);
	void updateSuspensionState(String batchId, SuspensionStateDto suspensionState);
	void deleteBatch(String batchId, boolean cascade);
}
