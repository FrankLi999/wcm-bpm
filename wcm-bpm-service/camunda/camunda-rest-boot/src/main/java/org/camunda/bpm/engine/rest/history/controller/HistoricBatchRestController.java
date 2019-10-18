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

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.batch.history.HistoricBatch;
import org.camunda.bpm.engine.batch.history.HistoricBatchQuery;
import org.camunda.bpm.engine.history.CleanableHistoricBatchReport;
import org.camunda.bpm.engine.history.CleanableHistoricBatchReportResult;
import org.camunda.bpm.engine.history.SetRemovalTimeSelectModeForHistoricBatchesBuilder;
import org.camunda.bpm.engine.query.Query;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.dto.history.batch.CleanableHistoricBatchReportDto;
import org.camunda.bpm.engine.rest.dto.history.batch.CleanableHistoricBatchReportResultDto;
import org.camunda.bpm.engine.rest.dto.history.batch.HistoricBatchDto;
import org.camunda.bpm.engine.rest.dto.history.batch.HistoricBatchQueryDto;
import org.camunda.bpm.engine.rest.dto.history.batch.removaltime.SetRemovalTimeToHistoricBatchesDto;
import org.camunda.bpm.engine.rest.history.HistoricBatchRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(HistoryRestService.PATH + HistoricBatchRestService.PATH)
public class HistoricBatchRestController extends AbstractRestProcessEngineAware implements HistoricBatchRestService {

	@SuppressWarnings("unchecked")
	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricBatchDto> getHistoricBatches(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		HistoricBatchQueryDto queryDto = new HistoricBatchQueryDto(objectMapper, request.getParameterMap());
		HistoricBatchQuery query = queryDto.toQuery(processEngine);

		List<HistoricBatch> matchingBatches;
		if (firstResult != null || maxResults != null) {
			matchingBatches = (List<HistoricBatch>) executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingBatches = query.list();
		}

		List<HistoricBatchDto> batchResults = new ArrayList<HistoricBatchDto>();
		for (HistoricBatch matchingBatch : matchingBatches) {
			batchResults.add(HistoricBatchDto.fromBatch(matchingBatch));
		}
		return batchResults;
	}

	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getHistoricBatchesCount(HttpServletRequest request) {
		HistoricBatchQueryDto queryDto = new HistoricBatchQueryDto(objectMapper, request.getParameterMap());
		HistoricBatchQuery query = queryDto.toQuery(processEngine);

		long count = query.count();
		return new CountResultDto(count);
	}

	@SuppressWarnings("unchecked")
	@Override
	@GetMapping(path="/cleanable-batch-report", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<CleanableHistoricBatchReportResultDto> getCleanableHistoricBatchesReport(
			HttpServletRequest request,
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		CleanableHistoricBatchReportDto queryDto = new CleanableHistoricBatchReportDto(objectMapper,
				request.getParameterMap());
		CleanableHistoricBatchReport query = queryDto.toQuery(processEngine);

		List<CleanableHistoricBatchReportResult> reportResult;
		if (firstResult != null || maxResults != null) {
			reportResult = (List<CleanableHistoricBatchReportResult>) executePaginatedQuery(query, firstResult,
					maxResults);
		} else {
			reportResult = query.list();
		}

		return CleanableHistoricBatchReportResultDto.convert(reportResult);
	}

	@Override
	@GetMapping(path="/cleanable-batch-report/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getCleanableHistoricBatchesReportCount(HttpServletRequest request) {
		CleanableHistoricBatchReportDto queryDto = new CleanableHistoricBatchReportDto(objectMapper,
				request.getParameterMap());
		queryDto.setObjectMapper(objectMapper);
		CleanableHistoricBatchReport query = queryDto.toQuery(processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}

	@PostMapping(path="/set-removal-time", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto setRemovalTimeAsync(@RequestBody SetRemovalTimeToHistoricBatchesDto dto) {
		HistoryService historyService = processEngine.getHistoryService();

		HistoricBatchQuery historicBatchQuery = null;

		if (dto.getHistoricBatchQuery() != null) {
			historicBatchQuery = dto.getHistoricBatchQuery().toQuery(processEngine);

		}

		SetRemovalTimeSelectModeForHistoricBatchesBuilder builder = historyService.setRemovalTimeToHistoricBatches();

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

		builder.byIds(dto.getHistoricBatchIds());
		builder.byQuery(historicBatchQuery);

		Batch batch = builder.executeAsync();
		return BatchDto.fromBatch(batch);
	}
	
	@SuppressWarnings("rawtypes")
	protected List<?> executePaginatedQuery(Query query, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}

		return query.listPage(firstResult, maxResults);
	}
}
