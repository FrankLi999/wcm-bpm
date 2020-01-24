/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.rest.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.management.MetricIntervalValue;
import org.camunda.bpm.engine.management.MetricsQuery;
import org.camunda.bpm.engine.rest.MetricsRestService;
import org.camunda.bpm.engine.rest.dto.converter.DateConverter;
import org.camunda.bpm.engine.rest.dto.converter.IntegerConverter;
import org.camunda.bpm.engine.rest.dto.converter.LongConverter;
import org.camunda.bpm.engine.rest.dto.metrics.MetricsIntervalResultDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daniel Meyer
 *
 */
@RestController
@RequestMapping(MetricsRestService.PATH)
public class MetricsRestController extends AbstractRestProcessEngineAware implements MetricsRestService {

	public static final String QUERY_PARAM_NAME = "name";
	public static final String QUERY_PARAM_REPORTER = "reporter";
	public static final String QUERY_PARAM_START_DATE = "startDate";
	public static final String QUERY_PARAM_END_DATE = "endDate";
	public static final String QUERY_PARAM_FIRST_RESULT = "firstResult";
	public static final String QUERY_PARAM_MAX_RESULTS = "maxResults";
	public static final String QUERY_PARAM_INTERVAL = "interval";
	public static final String QUERY_PARAM_AGG_BY_REPORTER = "aggregateByReporter";

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<MetricsIntervalResultDto> interval(HttpServletRequest request) {
		Map<String, String[]> queryParameters = request.getParameterMap();
		MetricsQuery query = processEngine.getManagementService().createMetricsQuery()
				.name(queryParameters.get(QUERY_PARAM_NAME)[0])
				.reporter(queryParameters.get(QUERY_PARAM_REPORTER)[0]);

		applyQueryParams(query, queryParameters);

		List<MetricIntervalValue> metrics;
		LongConverter longConverter = new LongConverter();
		longConverter.setObjectMapper(objectMapper);
		if (queryParameters.get(QUERY_PARAM_INTERVAL) != null) {
			long interval = longConverter.convertQueryParameterToType(queryParameters.get(QUERY_PARAM_INTERVAL)[0]);
			metrics = query.interval(interval);
		} else {
			metrics = query.interval();
		}

		return convertToDtos(metrics);
	}

	protected void applyQueryParams(MetricsQuery query, Map<String, String[]> queryParameters) {

		DateConverter dateConverter = new DateConverter();
		dateConverter.setObjectMapper(objectMapper);

		if (queryParameters.get(QUERY_PARAM_START_DATE) != null) {
			Date startDate = dateConverter
					.convertQueryParameterToType(queryParameters.get(QUERY_PARAM_START_DATE)[0]);
			query.startDate(startDate);
		}

		if (queryParameters.get(QUERY_PARAM_END_DATE) != null) {
			Date endDate = dateConverter.convertQueryParameterToType(queryParameters.get(QUERY_PARAM_END_DATE)[0]);
			query.endDate(endDate);
		}

		IntegerConverter intConverter = new IntegerConverter();
		intConverter.setObjectMapper(objectMapper);

		if (queryParameters.get(QUERY_PARAM_FIRST_RESULT) != null) {
			int firstResult = intConverter
					.convertQueryParameterToType(queryParameters.get(QUERY_PARAM_FIRST_RESULT)[0]);
			query.offset(firstResult);
		}

		if (queryParameters.get(QUERY_PARAM_MAX_RESULTS) != null) {
			int maxResults = intConverter
					.convertQueryParameterToType(queryParameters.get(QUERY_PARAM_MAX_RESULTS)[0]);
			query.limit(maxResults);
		}

		if (queryParameters.get(QUERY_PARAM_AGG_BY_REPORTER) != null) {
			query.aggregateByReporter();
		}
	}

	protected List<MetricsIntervalResultDto> convertToDtos(List<MetricIntervalValue> metrics) {
		List<MetricsIntervalResultDto> intervalMetrics = new ArrayList<>();
		for (MetricIntervalValue m : metrics) {
			intervalMetrics.add(new MetricsIntervalResultDto(m));
		}
		return intervalMetrics;
	}
}
