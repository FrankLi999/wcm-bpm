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
package org.camunda.bpm.engine.rest.history.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.CleanableHistoricCaseInstanceReport;
import org.camunda.bpm.engine.history.CleanableHistoricCaseInstanceReportResult;
import org.camunda.bpm.engine.history.HistoricCaseActivityStatistics;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.CleanableHistoricCaseInstanceReportDto;
import org.camunda.bpm.engine.rest.dto.history.CleanableHistoricCaseInstanceReportResultDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricCaseActivityStatisticsDto;
import org.camunda.bpm.engine.rest.history.HistoricCaseDefinitionRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Roman Smirnov
 *
 */

@RestController
@RequestMapping(HistoryRestService.PATH + HistoricCaseDefinitionRestService.PATH)
public class HistoricCaseDefinitionRestController extends AbstractRestProcessEngineAware implements HistoricCaseDefinitionRestService {

	@GetMapping(path="/{id}/statistics", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricCaseActivityStatisticsDto> getHistoricCaseActivityStatistics(
			@PathVariable("id") String caseDefinitionId) {
		HistoryService historyService = processEngine.getHistoryService();

		List<HistoricCaseActivityStatistics> statistics = historyService
				.createHistoricCaseActivityStatisticsQuery(caseDefinitionId).list();

		List<HistoricCaseActivityStatisticsDto> result = new ArrayList<HistoricCaseActivityStatisticsDto>();
		for (HistoricCaseActivityStatistics currentStatistics : statistics) {
			result.add(HistoricCaseActivityStatisticsDto.fromHistoricCaseActivityStatistics(currentStatistics));
		}

		return result;
	}

	@Override
	@GetMapping(path="/cleanable-case-instance-report", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<CleanableHistoricCaseInstanceReportResultDto> getCleanableHistoricCaseInstanceReport(
			HttpServletRequest request,
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		CleanableHistoricCaseInstanceReportDto queryDto = new CleanableHistoricCaseInstanceReportDto(objectMapper,
				request.getParameterMap());
		CleanableHistoricCaseInstanceReport query = queryDto.toQuery(processEngine);

		List<CleanableHistoricCaseInstanceReportResult> reportResult;
		if (firstResult != null || maxResults != null) {
			reportResult = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			reportResult = query.list();
		}

		return CleanableHistoricCaseInstanceReportResultDto.convert(reportResult);
	}

	@Override
	@GetMapping(path="/cleanable-case-instance-report/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getCleanableHistoricCaseInstanceReportCount(HttpServletRequest request) {
		CleanableHistoricCaseInstanceReportDto queryDto = new CleanableHistoricCaseInstanceReportDto(objectMapper,
				request.getParameterMap());
		queryDto.setObjectMapper(objectMapper);
		CleanableHistoricCaseInstanceReport query = queryDto.toQuery(processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}
	
	private List<CleanableHistoricCaseInstanceReportResult> executePaginatedQuery(
			CleanableHistoricCaseInstanceReport query, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}
}
