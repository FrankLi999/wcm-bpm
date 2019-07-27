package org.camunda.bpm.engine.rest.sub.runtime;

import org.camunda.bpm.engine.rest.dto.runtime.JobDto;
import org.camunda.bpm.engine.rest.dto.runtime.JobDuedateDto;
import org.camunda.bpm.engine.rest.dto.runtime.JobSuspensionStateDto;
import org.camunda.bpm.engine.rest.dto.runtime.PriorityDto;
import org.camunda.bpm.engine.rest.dto.runtime.RetriesDto;

public interface JobResource {
	JobDto getJob(String jobId);
	String getStacktrace(String jobId);
	void setJobRetries(String jobId, RetriesDto dto);
	void executeJob(String jobId);
	void setJobDuedate(String jobId, JobDuedateDto dto);
	void recalculateDuedate(String jobId, boolean creationDateBased);
	void updateSuspensionState(String jobId, JobSuspensionStateDto dto);
	void setJobPriority(String jobId, PriorityDto dto);
	void deleteJob(String jobId);
}
