package org.camunda.bpm.runtime.rest.dto;

import java.util.List;

public class CalledProcessInstanceDto extends ProcessInstanceDto {

	protected String processDefinitionId;
	protected String processDefinitionKey;
	protected String processDefinitionName;
	protected List<IncidentStatisticsDto> incidents;

	protected String callActivityInstanceId;
	protected String callActivityId;

	public CalledProcessInstanceDto() {
	}

	public String getId() {
		return id;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public String getCallActivityInstanceId() {
		return callActivityInstanceId;
	}

	public String getCallActivityId() {
		return callActivityId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public List<IncidentStatisticsDto> getIncidents() {
		return incidents;
	}

	public void setIncidents(List<IncidentStatisticsDto> incidents) {
		this.incidents = incidents;
	}

}
