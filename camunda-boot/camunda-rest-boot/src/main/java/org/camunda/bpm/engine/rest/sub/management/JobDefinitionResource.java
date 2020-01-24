package org.camunda.bpm.engine.rest.sub.management;

import org.camunda.bpm.engine.rest.dto.management.JobDefinitionDto;
import org.camunda.bpm.engine.rest.dto.management.JobDefinitionSuspensionStateDto;
import org.camunda.bpm.engine.rest.dto.runtime.JobDefinitionPriorityDto;
import org.camunda.bpm.engine.rest.dto.runtime.RetriesDto;

public interface JobDefinitionResource {
	JobDefinitionDto getJobDefinition(String jobDefinitionId);

	void updateSuspensionState(String jobDefinitionId, JobDefinitionSuspensionStateDto dto);

	void setJobRetries(String jobDefinitionId, RetriesDto dto);

	void setJobPriority(String jobDefinitionId, JobDefinitionPriorityDto dto);
}
