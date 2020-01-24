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
package org.camunda.bpm.process.controller;

import static org.camunda.bpm.engine.authorization.Permissions.READ;
import static org.camunda.bpm.engine.authorization.Permissions.READ_INSTANCE;
import static org.camunda.bpm.engine.authorization.Resources.PROCESS_DEFINITION;
import static org.camunda.bpm.engine.authorization.Resources.PROCESS_INSTANCE;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.process.impl.plugin.base.dto.IncidentDto;
import org.camunda.bpm.process.impl.plugin.base.dto.query.IncidentQueryDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author roman.smirnov
 */
@RestController
@RequestMapping(IncidentRestController.PATH)
public class IncidentRestController extends BaseProcessRestController {

	public final static String PATH = "/camunda/api/process/incident";

	public IncidentRestController() {
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IncidentDto> getIncidents(HttpServletRequest request, @RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		IncidentQueryDto queryParameter = new IncidentQueryDto(request.getParameterMap());
		return queryIncidents(queryParameter, firstResult, maxResults);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IncidentDto> queryIncidents(@RequestBody IncidentQueryDto queryParameter,
			@RequestParam("firstResult") Integer firstResult, @RequestParam("maxResults") Integer maxResults) {

		paginateQueryParameters(queryParameter, firstResult, maxResults);
		configureExecutionQuery(queryParameter);
		List<IncidentDto> matchingIncidents = this.queryService
				.executeQuery("selectIncidentWithCauseAndRootCauseIncidents", queryParameter);
		return matchingIncidents;
	}

	private void paginateQueryParameters(IncidentQueryDto queryParameter, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		queryParameter.setFirstResult(firstResult);
		queryParameter.setMaxResults(maxResults);
	}

	@GetMapping(path = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getIncidentsCount(HttpServletRequest request) {
		IncidentQueryDto queryParameter = new IncidentQueryDto(request.getParameterMap());
		return queryIncidentsCount(queryParameter);
	}

	@PostMapping(path = "/count", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryIncidentsCount(@RequestBody IncidentQueryDto queryParameter) {
		CountResultDto result = new CountResultDto();
		configureExecutionQuery(queryParameter);
		long count = this.queryService.executeQueryRowCount("selectIncidentWithCauseAndRootCauseIncidentsCount",
				queryParameter);
		result.setCount(count);

		return result;
	}

	protected void configureExecutionQuery(IncidentQueryDto query) {
		configureAuthorizationCheck(query);
		configureTenantCheck(query);
		addPermissionCheck(query, PROCESS_INSTANCE, "RES.PROC_INST_ID_", READ);
		addPermissionCheck(query, PROCESS_DEFINITION, "PROCDEF.KEY_", READ_INSTANCE);
	}
}
