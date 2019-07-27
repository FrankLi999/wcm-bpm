package org.camunda.bpm.engine.rest.sub.task;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

public interface TaskReportResource {

	ResponseEntity<?> getTaskCountByCandidateGroupReport(HttpServletRequest request);
}
