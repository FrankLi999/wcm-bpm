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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.batch.BatchQuery;
import org.camunda.bpm.engine.batch.BatchStatistics;
import org.camunda.bpm.engine.batch.BatchStatisticsQuery;
import org.camunda.bpm.engine.rest.BatchRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchQueryDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchStatisticsDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchStatisticsQueryDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(BatchRestService.PATH)
public class BatchRestController extends AbstractRestProcessEngineAware implements BatchRestService {

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<BatchDto> getBatches(HttpServletRequest request, @RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		BatchQueryDto queryDto = new BatchQueryDto(this.objectMapper, request.getParameterMap());
		BatchQuery query = queryDto.toQuery(this.processEngine);

		List<Batch> matchingBatches;
		if (firstResult != null || maxResults != null) {
			matchingBatches = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingBatches = query.list();
		}

		List<BatchDto> batchResults = new ArrayList<BatchDto>();
		for (Batch matchingBatch : matchingBatches) {
			batchResults.add(BatchDto.fromBatch(matchingBatch));
		}
		return batchResults;
	}

	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getBatchesCount(HttpServletRequest request) {
		BatchQueryDto queryDto = new BatchQueryDto(this.objectMapper, request.getParameterMap());
		BatchQuery query = queryDto.toQuery(this.processEngine);

		long count = query.count();
		return new CountResultDto(count);
	}

	@GetMapping(path="/statistics", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<BatchStatisticsDto> getStatistics(HttpServletRequest request, @RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		BatchStatisticsQueryDto queryDto = new BatchStatisticsQueryDto(this.objectMapper, request.getParameterMap());
		BatchStatisticsQuery query = queryDto.toQuery(this.processEngine);

		List<BatchStatistics> batchStatisticsList;
		if (firstResult != null || maxResults != null) {
			batchStatisticsList = executePaginatedStatisticsQuery(query, firstResult, maxResults);
		} else {
			batchStatisticsList = query.list();
		}

		List<BatchStatisticsDto> statisticsResults = new ArrayList<BatchStatisticsDto>();
		for (BatchStatistics batchStatistics : batchStatisticsList) {
			statisticsResults.add(BatchStatisticsDto.fromBatchStatistics(batchStatistics));
		}

		return statisticsResults;
	}
	@GetMapping(path="/statistics/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getStatisticsCount(HttpServletRequest request) {
		BatchStatisticsQueryDto queryDto = new BatchStatisticsQueryDto(this.objectMapper, request.getParameterMap());
		BatchStatisticsQuery query = queryDto.toQuery(this.processEngine);

		long count = query.count();
		return new CountResultDto(count);
	}

	protected List<Batch> executePaginatedQuery(BatchQuery query, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}

		return query.listPage(firstResult, maxResults);
	}

	protected List<BatchStatistics> executePaginatedStatisticsQuery(BatchStatisticsQuery query, Integer firstResult,
			Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}

		return query.listPage(firstResult, maxResults);
	}
}
