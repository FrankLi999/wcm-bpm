package org.camunda.bpm.engine.rest.sub.runtime;

import org.camunda.bpm.engine.rest.dto.CreateIncidentDto;
import org.camunda.bpm.engine.rest.dto.runtime.ExecutionDto;
import org.camunda.bpm.engine.rest.dto.runtime.ExecutionTriggerDto;
import org.camunda.bpm.engine.rest.dto.runtime.IncidentDto;

public interface ExecutionResource {
	ExecutionDto getExecution(String executionId);
	void signalExecution(String executionId, ExecutionTriggerDto triggerDto);
    IncidentDto createIncident(String executionId, CreateIncidentDto createIncidentDto);
}
