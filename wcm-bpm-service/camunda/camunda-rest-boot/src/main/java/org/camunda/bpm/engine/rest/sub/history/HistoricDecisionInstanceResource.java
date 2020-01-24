package org.camunda.bpm.engine.rest.sub.history;

import org.camunda.bpm.engine.rest.dto.history.HistoricDecisionInstanceDto;

public interface HistoricDecisionInstanceResource {
	HistoricDecisionInstanceDto getHistoricDecisionInstance(
			String decisionInstanceId, 
			Boolean includeInputs, 
			Boolean includeOutputs,
			Boolean disableBinaryFetching, 
			Boolean disableCustomObjectDeserialization);
}
