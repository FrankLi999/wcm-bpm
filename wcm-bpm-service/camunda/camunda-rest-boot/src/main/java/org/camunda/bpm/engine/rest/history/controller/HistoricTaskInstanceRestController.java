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

import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.history.HistoricTaskInstanceReportResult;
import org.camunda.bpm.engine.history.ReportResult;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.AbstractReportDto;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricTaskInstanceDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricTaskInstanceQueryDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricTaskInstanceReportQueryDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricTaskInstanceReportResultDto;
import org.camunda.bpm.engine.rest.dto.history.ReportResultDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.history.HistoricTaskInstanceRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Roman Smirnov
 *HistoricTaskInstanceRestService
 */
@RestController
@RequestMapping(HistoryRestService.PATH + HistoricTaskInstanceRestService.PATH)
public class HistoricTaskInstanceRestController extends AbstractRestProcessEngineAware implements HistoricTaskInstanceRestService {

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricTaskInstanceDto> getHistoricTaskInstances(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		HistoricTaskInstanceQueryDto queryDto = new HistoricTaskInstanceQueryDto(objectMapper,
				request.getParameterMap());
		return queryHistoricTaskInstances(queryDto, firstResult, maxResults);
	}

	@Override
	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricTaskInstanceDto> queryHistoricTaskInstances(
			@RequestBody HistoricTaskInstanceQueryDto queryDto,
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		queryDto.setObjectMapper(objectMapper);
		HistoricTaskInstanceQuery query = queryDto.toQuery(processEngine);

		List<HistoricTaskInstance> match;
		if (firstResult != null || maxResults != null) {
			match = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			match = query.list();
		}

		List<HistoricTaskInstanceDto> result = new ArrayList<HistoricTaskInstanceDto>();
		for (HistoricTaskInstance taskInstance : match) {
			HistoricTaskInstanceDto taskInstanceDto = HistoricTaskInstanceDto.fromHistoricTaskInstance(taskInstance);
			result.add(taskInstanceDto);
		}
		return result;
	}

	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getHistoricTaskInstancesCount(HttpServletRequest request) {
		HistoricTaskInstanceQueryDto queryDto = new HistoricTaskInstanceQueryDto(objectMapper,
				request.getParameterMap());
		return queryHistoricTaskInstancesCount(queryDto);
	}

	@Override
	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryHistoricTaskInstancesCount(@RequestBody HistoricTaskInstanceQueryDto queryDto) {
		queryDto.setObjectMapper(objectMapper);
		HistoricTaskInstanceQuery query = queryDto.toQuery(processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}

	@Override
	@GetMapping(path="/report", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getHistoricTaskInstanceReport(HttpServletRequest request) {
		HistoricTaskInstanceReportQueryDto queryDto = new HistoricTaskInstanceReportQueryDto(objectMapper,
				request.getParameterMap());
		ResponseEntity<?> response;

		if (AbstractReportDto.REPORT_TYPE_DURATION.equals(queryDto.getReportType())) {
			List<? extends ReportResult> reportResults = queryDto.executeReport(processEngine);
			response = ResponseEntity.ok().body(generateDurationDto(reportResults));
		} else if (AbstractReportDto.REPORT_TYPE_COUNT.equals(queryDto.getReportType())) {
			List<HistoricTaskInstanceReportResult> reportResults = queryDto.executeCompletedReport(processEngine);
			response = ResponseEntity.ok().body(generateCountDto(reportResults));
		} else {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, "Parameter reportType is not set.");
		}

		return response;
	}

	protected List<HistoricTaskInstance> executePaginatedQuery(HistoricTaskInstanceQuery query, Integer firstResult,
			Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}
	
	protected List<HistoricTaskInstanceReportResultDto> generateCountDto(
			List<HistoricTaskInstanceReportResult> results) {
		List<HistoricTaskInstanceReportResultDto> dtoList = new ArrayList<HistoricTaskInstanceReportResultDto>();

		for (HistoricTaskInstanceReportResult result : results) {
			dtoList.add(HistoricTaskInstanceReportResultDto.fromHistoricTaskInstanceReportResult(result));
		}

		return dtoList;
	}

	protected List<ReportResultDto> generateDurationDto(List<? extends ReportResult> results) {
		List<ReportResultDto> dtoList = new ArrayList<ReportResultDto>();

		for (ReportResult result : results) {
			dtoList.add(ReportResultDto.fromReportResult(result));
		}

		return dtoList;
	}

}
