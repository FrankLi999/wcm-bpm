package org.camunda.bpm.engine.rest.sub.repository;

import org.camunda.bpm.engine.rest.dto.runtime.IncidentDto;

public interface IncidentResource {
	IncidentDto getIncident(String incidentId);
	void resolveIncident(String incidentId);
}
