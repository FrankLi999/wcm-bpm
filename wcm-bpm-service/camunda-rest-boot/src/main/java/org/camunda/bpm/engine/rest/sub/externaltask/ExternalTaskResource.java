package org.camunda.bpm.engine.rest.sub.externaltask;

import org.camunda.bpm.engine.rest.dto.externaltask.CompleteExternalTaskDto;
import org.camunda.bpm.engine.rest.dto.externaltask.ExtendLockOnExternalTaskDto;
import org.camunda.bpm.engine.rest.dto.externaltask.ExternalTaskBpmnError;
import org.camunda.bpm.engine.rest.dto.externaltask.ExternalTaskDto;
import org.camunda.bpm.engine.rest.dto.externaltask.ExternalTaskFailureDto;
import org.camunda.bpm.engine.rest.dto.runtime.PriorityDto;
import org.camunda.bpm.engine.rest.dto.runtime.RetriesDto;

public interface ExternalTaskResource {
	ExternalTaskDto getExternalTask(String externalTaskId);
	String getErrorDetails(String externalTaskId);	
	void setRetries(String externalTaskId, RetriesDto dto);
    void setPriority(String externalTaskId, PriorityDto dto);
    void complete(String externalTaskId, CompleteExternalTaskDto dto);
    void handleFailure(String externalTaskId, ExternalTaskFailureDto dto);
    void handleBpmnError(String externalTaskId, ExternalTaskBpmnError dto);
	void unlock(String externalTaskId);
	void extendLock(String externalTaskId, ExtendLockOnExternalTaskDto extendLockDto);
}
