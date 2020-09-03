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

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.history.ReportResult;
import org.camunda.bpm.engine.history.SetRemovalTimeSelectModeForHistoricProcessInstancesBuilder;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.dto.converter.ReportResultToCsvConverter;
import org.camunda.bpm.engine.rest.dto.history.DeleteHistoricProcessInstancesDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricProcessInstanceQueryDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricProcessInstanceReportDto;
import org.camunda.bpm.engine.rest.dto.history.ReportResultDto;
import org.camunda.bpm.engine.rest.dto.history.batch.removaltime.SetRemovalTimeToHistoricProcessInstancesDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.history.HistoricProcessInstanceRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.rest.util.VariantUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="historicProcessInstanceApi")
@RequestMapping(HistoryRestService.PATH + HistoricProcessInstanceRestService.PATH)
public class HistoricProcessInstanceRestController extends AbstractRestProcessEngineAware implements HistoricProcessInstanceRestService {

	public static final MediaType APPLICATION_CSV_TYPE = new MediaType("application", "csv");
	public static final MediaType TEXT_CSV_TYPE = new MediaType("text", "csv");

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricProcessInstanceDto> getHistoricProcessInstances(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		HistoricProcessInstanceQueryDto queryHistoriProcessInstanceDto = new HistoricProcessInstanceQueryDto(
				this.getObjectMapper(), request.getParameterMap());
		return queryHistoricProcessInstances(queryHistoriProcessInstanceDto, firstResult, maxResults);
	}

	@Override
	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricProcessInstanceDto> queryHistoricProcessInstances(
			@RequestBody HistoricProcessInstanceQueryDto queryDto,
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		
		queryDto.setObjectMapper(this.getObjectMapper());
		HistoricProcessInstanceQuery query = queryDto.toQuery(processEngine);

		List<HistoricProcessInstance> matchingHistoricProcessInstances;
		if (firstResult != null || maxResults != null) {
			matchingHistoricProcessInstances = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingHistoricProcessInstances = query.list();
		}

		List<HistoricProcessInstanceDto> historicProcessInstanceDtoResults = new ArrayList<HistoricProcessInstanceDto>();
		for (HistoricProcessInstance historicProcessInstance : matchingHistoricProcessInstances) {
			HistoricProcessInstanceDto resultHistoricProcessInstanceDto = HistoricProcessInstanceDto
					.fromHistoricProcessInstance(historicProcessInstance);
			historicProcessInstanceDtoResults.add(resultHistoricProcessInstanceDto);
		}
		return historicProcessInstanceDtoResults;
	}

	

	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getHistoricProcessInstancesCount(HttpServletRequest request) {
		HistoricProcessInstanceQueryDto queryDto = new HistoricProcessInstanceQueryDto(this.getObjectMapper(),
				request.getParameterMap());
		return queryHistoricProcessInstancesCount(queryDto);
	}

	@Override
	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryHistoricProcessInstancesCount(@RequestBody HistoricProcessInstanceQueryDto queryDto) {
		queryDto.setObjectMapper(this.getObjectMapper());
		HistoricProcessInstanceQuery query = queryDto.toQuery(processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}

	

	@Override
	@GetMapping(path="/report", produces= {MediaType.APPLICATION_JSON_VALUE, "text/csv", "application/csv"})
	public ResponseEntity<?> getHistoricProcessInstancesReport(
			HttpServletRequest request) {

		if (VariantUtils.accept(request, MediaType.APPLICATION_JSON)) {
			List<ReportResultDto> result = getReportResultAsJson(request);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result);
		} else if (VariantUtils.accept(request, APPLICATION_CSV_TYPE) || VariantUtils.accept(request, TEXT_CSV_TYPE)) {
			String csv = getReportResultAsCsv(request);
			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=process-instance-report.csv")
					.contentType(VariantUtils.accept(request, APPLICATION_CSV_TYPE) ? APPLICATION_CSV_TYPE : TEXT_CSV_TYPE)
					.body(csv);
		}

		throw new InvalidRequestException(HttpStatus.NOT_ACCEPTABLE, "No acceptable content-type found");
	}

	@Override
	@PostMapping(path="/delete", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto deleteAsync(DeleteHistoricProcessInstancesDto dto) {
		HistoryService historyService = processEngine.getHistoryService();

		HistoricProcessInstanceQuery historicProcessInstanceQuery = null;
		if (dto.getHistoricProcessInstanceQuery() != null) {
			historicProcessInstanceQuery = dto.getHistoricProcessInstanceQuery().toQuery(processEngine);
		}

		try {
			Batch batch;
			batch = historyService.deleteHistoricProcessInstancesAsync(dto.getHistoricProcessInstanceIds(),
					historicProcessInstanceQuery, dto.getDeleteReason());
			return BatchDto.fromBatch(batch);

		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@Override
	@PostMapping(path="/set-removal-time", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto setRemovalTimeAsync(@RequestBody SetRemovalTimeToHistoricProcessInstancesDto dto) {
		HistoryService historyService = processEngine.getHistoryService();

		HistoricProcessInstanceQuery historicProcessInstanceQuery = null;

		if (dto.getHistoricProcessInstanceQuery() != null) {
			historicProcessInstanceQuery = dto.getHistoricProcessInstanceQuery().toQuery(processEngine);

		}

		SetRemovalTimeSelectModeForHistoricProcessInstancesBuilder builder = historyService
				.setRemovalTimeToHistoricProcessInstances();

		if (dto.isCalculatedRemovalTime()) {
			builder.calculatedRemovalTime();

		}

		Date removalTime = dto.getAbsoluteRemovalTime();
		if (dto.getAbsoluteRemovalTime() != null) {
			builder.absoluteRemovalTime(removalTime);

		}

		if (dto.isClearedRemovalTime()) {
			builder.clearedRemovalTime();

		}

		builder.byIds(dto.getHistoricProcessInstanceIds());
		builder.byQuery(historicProcessInstanceQuery);

		if (dto.isHierarchical()) {
			builder.hierarchical();

		}

		Batch batch = builder.executeAsync();
		return BatchDto.fromBatch(batch);
	}

	@Override
	@DeleteMapping(path="/{id}/variable-instances")
	public ResponseEntity<?> deleteHistoricVariableInstancesByProcessInstanceId(@PathVariable("id") String processInstanceId) {
		try {
			processEngine.getHistoryService().deleteHistoricVariableInstancesByProcessInstanceId(processInstanceId);
		} catch (NotFoundException nfe) { // rewrite status code from bad request (400) to not found (404)
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, nfe.getMessage());
		}
		// return no content (204) since resource is deleted
		return ResponseEntity.noContent().build();
	}

	private List<HistoricProcessInstance> executePaginatedQuery(HistoricProcessInstanceQuery query, Integer firstResult,
			Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}
	
	@SuppressWarnings("unchecked")
	protected List<ReportResult> queryHistoricProcessInstanceReport(HttpServletRequest request) {
		HistoricProcessInstanceReportDto reportDto = new HistoricProcessInstanceReportDto(this.getObjectMapper(),
				request.getParameterMap());
		return (List<ReportResult>) reportDto.executeReport(processEngine);
	}
	
	protected List<ReportResultDto> getReportResultAsJson(HttpServletRequest request) {
		List<ReportResult> reports = queryHistoricProcessInstanceReport(request);
		List<ReportResultDto> result = new ArrayList<ReportResultDto>();
		for (ReportResult report : reports) {
			result.add(ReportResultDto.fromReportResult(report));
		}
		return result;
	}

	protected String getReportResultAsCsv(HttpServletRequest request) {
		List<ReportResult> reports = queryHistoricProcessInstanceReport(request);
		Map<String, String[]> queryParameters = request.getParameterMap();
		String reportType = queryParameters.get("reportType")[0];
		return ReportResultToCsvConverter.convertReportResult(reports, reportType);
	}
}
