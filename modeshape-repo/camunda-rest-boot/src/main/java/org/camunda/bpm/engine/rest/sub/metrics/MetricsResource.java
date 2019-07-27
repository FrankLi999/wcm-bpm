package org.camunda.bpm.engine.rest.sub.metrics;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.rest.dto.metrics.MetricsResultDto;

public interface MetricsResource {
	MetricsResultDto sum(String metricsName, HttpServletRequest request);
}
