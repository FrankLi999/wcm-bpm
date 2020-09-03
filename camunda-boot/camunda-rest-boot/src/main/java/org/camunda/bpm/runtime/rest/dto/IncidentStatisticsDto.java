package org.camunda.bpm.runtime.rest.dto;

public class IncidentStatisticsDto {

	protected String incidentType;
	protected int incidentCount;

	public IncidentStatisticsDto() {
	}

	public String getIncidentType() {
		return incidentType;
	}

	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}

	public int getIncidentCount() {
		return incidentCount;
	}

	public void setIncidentCount(int incidentCount) {
		this.incidentCount = incidentCount;
	}

}
