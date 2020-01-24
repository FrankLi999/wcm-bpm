package org.camunda.bpm.engine.rest.sub.runtime;

import org.camunda.bpm.engine.rest.dto.runtime.CaseExecutionTriggerDto;
import org.camunda.bpm.engine.rest.dto.runtime.CaseInstanceDto;

public interface CaseInstanceResource {
	CaseInstanceDto getCaseInstance(String caseInstanceId);
	void complete(String caseInstanceId, CaseExecutionTriggerDto triggerDto);
	void close(String caseInstanceId, CaseExecutionTriggerDto triggerDto);
	void terminate(String caseInstanceId, CaseExecutionTriggerDto triggerDto);
}
