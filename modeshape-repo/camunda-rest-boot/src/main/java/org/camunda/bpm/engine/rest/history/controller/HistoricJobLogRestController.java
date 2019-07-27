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

import org.camunda.bpm.engine.history.HistoricJobLog;
import org.camunda.bpm.engine.history.HistoricJobLogQuery;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricJobLogDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricJobLogQueryDto;
import org.camunda.bpm.engine.rest.history.HistoricJobLogRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Roman Smirnov
 *
 */
@RestController
@RequestMapping(HistoryRestService.PATH + HistoricJobLogRestService.PATH)
public class HistoricJobLogRestController extends AbstractRestProcessEngineAware implements HistoricJobLogRestService {

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricJobLogDto> getHistoricJobLogs(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		HistoricJobLogQueryDto queryDto = new HistoricJobLogQueryDto(objectMapper, request.getParameterMap());
		return queryHistoricJobLogs(queryDto, firstResult, maxResults);
	}

	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricJobLogDto> queryHistoricJobLogs(
			@RequestBody HistoricJobLogQueryDto queryDto, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		queryDto.setObjectMapper(objectMapper);
		HistoricJobLogQuery query = queryDto.toQuery(processEngine);

		List<HistoricJobLog> matchingHistoricJobLogs;
		if (firstResult != null || maxResults != null) {
			matchingHistoricJobLogs = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingHistoricJobLogs = query.list();
		}

		List<HistoricJobLogDto> results = new ArrayList<HistoricJobLogDto>();
		for (HistoricJobLog historicJobLog : matchingHistoricJobLogs) {
			HistoricJobLogDto result = HistoricJobLogDto.fromHistoricJobLog(historicJobLog);
			results.add(result);
		}

		return results;
	}

	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getHistoricJobLogsCount(HttpServletRequest request) {
		HistoricJobLogQueryDto queryDto = new HistoricJobLogQueryDto(objectMapper, request.getParameterMap());
		return queryHistoricJobLogsCount(queryDto);
	}

	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryHistoricJobLogsCount(@RequestBody HistoricJobLogQueryDto queryDto) {
		queryDto.setObjectMapper(objectMapper);
		HistoricJobLogQuery query = queryDto.toQuery(processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}

	protected List<HistoricJobLog> executePaginatedQuery(HistoricJobLogQuery query, Integer firstResult,
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
