package org.camunda.bpm.engine.rest.sub.runtime;

import org.camunda.bpm.engine.rest.dto.runtime.CaseExecutionDto;
import org.camunda.bpm.engine.rest.dto.runtime.CaseExecutionTriggerDto;

public interface CaseExecutionResource {
	CaseExecutionDto getCaseExecution(String caseExecutionId);
	void manualStart(String caseExecutionId, CaseExecutionTriggerDto triggerDto);
	void disable(String caseExecutionId, CaseExecutionTriggerDto triggerDto);
	void reenable(String caseExecutionId, CaseExecutionTriggerDto triggerDto);
	void complete(String caseExecutionId, CaseExecutionTriggerDto triggerDto);
	void terminate(String caseExecutionId, CaseExecutionTriggerDto triggerDto);
}
