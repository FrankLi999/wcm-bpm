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
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.CleanableHistoricProcessInstanceReport;
import org.camunda.bpm.engine.history.CleanableHistoricProcessInstanceReportResult;
import org.camunda.bpm.engine.history.HistoricActivityStatistics;
import org.camunda.bpm.engine.history.HistoricActivityStatisticsQuery;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.converter.DateConverter;
import org.camunda.bpm.engine.rest.dto.history.CleanableHistoricProcessInstanceReportDto;
import org.camunda.bpm.engine.rest.dto.history.CleanableHistoricProcessInstanceReportResultDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricActivityStatisticsDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.history.HistoricProcessDefinitionRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="historicProcessDefinitionApi")
@RequestMapping(HistoryRestService.PATH + HistoricProcessDefinitionRestService.PATH)
public class HistoricProcessDefinitionRestController extends AbstractRestProcessEngineAware
		implements HistoricProcessDefinitionRestService {

	public static final String QUERY_PARAM_STARTED_AFTER = "startedAfter";
	public static final String QUERY_PARAM_STARTED_BEFORE = "startedBefore";
	public static final String QUERY_PARAM_FINISHED_AFTER = "finishedAfter";
	public static final String QUERY_PARAM_FINISHED_BEFORE = "finishedBefore";

	@Override
	@GetMapping(path="/{id}/statistics", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricActivityStatisticsDto> getHistoricActivityStatistics(
			HttpServletRequest request,
			@PathVariable("id") String processDefinitionId, 
			@RequestParam("canceled") Boolean includeCanceled, 
			@RequestParam("finished") Boolean includeFinished, 
			@RequestParam("completeScope") Boolean includeCompleteScope,
			@RequestParam("sortBy") String sortBy, 
			@RequestParam("sortOrder") String sortOrder) {
		HistoryService historyService = processEngine.getHistoryService();

		HistoricActivityStatisticsQuery query = historyService
				.createHistoricActivityStatisticsQuery(processDefinitionId);

		if (includeCanceled != null && includeCanceled) {
			query.includeCanceled();
		}

		if (includeFinished != null && includeFinished) {
			query.includeFinished();
		}

		if (includeCompleteScope != null && includeCompleteScope) {
			query.includeCompleteScope();
		}

		final Map<String, String[]> queryParameters = request.getParameterMap();

		DateConverter dateConverter = new DateConverter();
		dateConverter.setObjectMapper(this.getObjectMapper());

		if (queryParameters.get(QUERY_PARAM_STARTED_AFTER) != null) {
			Date startedAfter = dateConverter
					.convertQueryParameterToType(queryParameters.get(QUERY_PARAM_STARTED_AFTER)[0]);
			query.startedAfter(startedAfter);
		}

		if (queryParameters.get(QUERY_PARAM_STARTED_BEFORE) != null) {
			Date startedBefore = dateConverter
					.convertQueryParameterToType(queryParameters.get(QUERY_PARAM_STARTED_BEFORE)[0]);
			query.startedBefore(startedBefore);
		}

		if (queryParameters.get(QUERY_PARAM_FINISHED_AFTER) != null) {
			Date finishedAfter = dateConverter
					.convertQueryParameterToType(queryParameters.get(QUERY_PARAM_FINISHED_AFTER)[0]);
			query.finishedAfter(finishedAfter);
		}

		if (queryParameters.get(QUERY_PARAM_FINISHED_BEFORE) != null) {
			Date finishedBefore = dateConverter
					.convertQueryParameterToType(queryParameters.get(QUERY_PARAM_FINISHED_BEFORE)[0]);
			query.finishedBefore(finishedBefore);
		}

		setSortOptions(query, sortOrder, sortBy);

		List<HistoricActivityStatisticsDto> result = new ArrayList<HistoricActivityStatisticsDto>();

		List<HistoricActivityStatistics> statistics = query.list();

		for (HistoricActivityStatistics currentStatistics : statistics) {
			result.add(HistoricActivityStatisticsDto.fromHistoricActivityStatistics(currentStatistics));
		}

		return result;
	}

	@Override
	@GetMapping(path="/cleanable-process-instance-report", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<CleanableHistoricProcessInstanceReportResultDto> getCleanableHistoricProcessInstanceReport(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		CleanableHistoricProcessInstanceReportDto queryDto = new CleanableHistoricProcessInstanceReportDto(this.getObjectMapper(),
				request.getParameterMap());
		CleanableHistoricProcessInstanceReport query = queryDto.toQuery(processEngine);

		List<CleanableHistoricProcessInstanceReportResult> reportResult;
		if (firstResult != null || maxResults != null) {
			reportResult = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			reportResult = query.list();
		}

		return CleanableHistoricProcessInstanceReportResultDto.convert(reportResult);
	}

	@Override
	@GetMapping(path="/cleanable-process-instance-report/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getCleanableHistoricProcessInstanceReportCount(
			HttpServletRequest request) {
		CleanableHistoricProcessInstanceReportDto queryDto = new CleanableHistoricProcessInstanceReportDto(this.getObjectMapper(),
				request.getParameterMap());
		queryDto.setObjectMapper(this.getObjectMapper());
		CleanableHistoricProcessInstanceReport query = queryDto.toQuery(processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}
	
	private void setSortOptions(HistoricActivityStatisticsQuery query, String sortOrder, String sortBy) {
		boolean sortOptionsValid = (sortBy != null && sortOrder != null) || (sortBy == null && sortOrder == null);

		if (!sortOptionsValid) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
					"Only a single sorting parameter specified. sortBy and sortOrder required");
		}

		if (sortBy != null) {
			if (sortBy.equals("activityId")) {
				query.orderByActivityId();
			} else {
				throw new InvalidRequestException(HttpStatus.BAD_REQUEST, "sortBy parameter has invalid value: " + sortBy);
			}
		}

		if (sortOrder != null) {
			if (sortOrder.equals("asc")) {
				query.asc();
			} else if (sortOrder.equals("desc")) {
				query.desc();
			} else {
				throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
						"sortOrder parameter has invalid value: " + sortOrder);
			}
		}

	}

	private List<CleanableHistoricProcessInstanceReportResult> executePaginatedQuery(
			CleanableHistoricProcessInstanceReport query, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}
}
