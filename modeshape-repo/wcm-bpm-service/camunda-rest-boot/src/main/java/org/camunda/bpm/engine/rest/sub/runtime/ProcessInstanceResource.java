package org.camunda.bpm.engine.rest.sub.runtime;

import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.dto.runtime.ActivityInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceSuspensionStateDto;
import org.camunda.bpm.engine.rest.dto.runtime.modification.ProcessInstanceModificationDto;

public interface ProcessInstanceResource {
	ProcessInstanceDto getProcessInstance(String processInstanceId);
    void deleteProcessInstance(String processInstanceId,
			boolean skipCustomListeners,
			boolean skipIoMappings,
			boolean skipSubprocesses,
			boolean failIfNotExists);
	
	ActivityInstanceDto getActivityInstanceTree(String processInstanceId);
	void updateSuspensionState(String processInstanceId, ProcessInstanceSuspensionStateDto dto);
	void modifyProcessInstance(String processInstanceId, ProcessInstanceModificationDto dto);
	BatchDto modifyProcessInstanceAsync(String processInstanceId, ProcessInstanceModificationDto dto);
}
