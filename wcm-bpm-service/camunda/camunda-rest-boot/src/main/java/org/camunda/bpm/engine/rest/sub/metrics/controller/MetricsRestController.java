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
package org.camunda.bpm.engine.rest.sub.metrics.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.management.MetricsQuery;
import org.camunda.bpm.engine.rest.MetricsRestService;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.converter.DateConverter;
import org.camunda.bpm.engine.rest.dto.metrics.MetricsResultDto;
import org.camunda.bpm.engine.rest.sub.metrics.MetricsResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Daniel Meyer
 *
 */
@RestController
@RequestMapping(MetricsRestService.PATH + "/{metricsName}")
public class MetricsRestController extends AbstractRestProcessEngineAware implements MetricsResource {

	//protected String metricsName;
	protected ProcessEngine processEngine;
	protected ObjectMapper objectMapper;

	public MetricsRestController() {}

	@GetMapping(path="/sum", produces=MediaType.APPLICATION_JSON_VALUE)
	public MetricsResultDto sum(@PathVariable("metricsName") String metricsName, HttpServletRequest request) {
		MetricsQuery query = processEngine.getManagementService().createMetricsQuery().name(metricsName);
		applyQueryParams(query, request);
		return new MetricsResultDto(query.sum());
	}

	protected void applyQueryParams(MetricsQuery query, HttpServletRequest request) {
		Map<String, String[]> queryParameters = request.getParameterMap();

		DateConverter dateConverter = new DateConverter();
		dateConverter.setObjectMapper(objectMapper);

		if ((queryParameters.get("startDate") != null) || (queryParameters.get("startDate")[0] != null)) {
			Date startDate = dateConverter.convertQueryParameterToType(queryParameters.get("startDate")[0]);
			query.startDate(startDate);
		}

	
		if ((queryParameters.get("endDate") != null) || (queryParameters.get("endDate")[0] != null)) {
			Date endDate = dateConverter.convertQueryParameterToType(queryParameters.get("endDate")[0]);
			query.endDate(endDate);
		}
	}

}
