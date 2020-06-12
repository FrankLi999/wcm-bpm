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

import org.camunda.bpm.engine.history.HistoricExternalTaskLog;
import org.camunda.bpm.engine.history.HistoricExternalTaskLogQuery;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricExternalTaskLogDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricExternalTaskLogQueryDto;
import org.camunda.bpm.engine.rest.history.HistoricExternalTaskLogRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="historicExternalTaskLogApi")
@RequestMapping(HistoryRestService.PATH + HistoricExternalTaskLogRestService.PATH)
public class HistoricExternalTaskLogRestController extends AbstractRestProcessEngineAware implements HistoricExternalTaskLogRestService {

//	@Override
//	public HistoricExternalTaskLogResource getHistoricExternalTaskLog(String historicExternalTaskLogId) {
//		return new HistoricExternalTaskLogRestController(historicExternalTaskLogId, processEngine);
//	}

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricExternalTaskLogDto> getHistoricExternalTaskLogs(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		HistoricExternalTaskLogQueryDto queryDto = new HistoricExternalTaskLogQueryDto(objectMapper,
				request.getParameterMap());
		return queryHistoricExternalTaskLogs(queryDto, firstResult, maxResults);
	}

	@Override
	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricExternalTaskLogDto> queryHistoricExternalTaskLogs(
			@RequestBody HistoricExternalTaskLogQueryDto queryDto,
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		queryDto.setObjectMapper(objectMapper);
		HistoricExternalTaskLogQuery query = queryDto.toQuery(processEngine);

		List<HistoricExternalTaskLog> matchingHistoricExternalTaskLogs;
		if (firstResult != null || maxResults != null) {
			matchingHistoricExternalTaskLogs = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingHistoricExternalTaskLogs = query.list();
		}

		List<HistoricExternalTaskLogDto> results = new ArrayList<HistoricExternalTaskLogDto>();
		for (HistoricExternalTaskLog historicExternalTaskLog : matchingHistoricExternalTaskLogs) {
			HistoricExternalTaskLogDto result = HistoricExternalTaskLogDto
					.fromHistoricExternalTaskLog(historicExternalTaskLog);
			results.add(result);
		}

		return results;
	}

	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getHistoricExternalTaskLogsCount(HttpServletRequest request) {
		HistoricExternalTaskLogQueryDto queryDto = new HistoricExternalTaskLogQueryDto(objectMapper,
				request.getParameterMap());
		return queryHistoricExternalTaskLogsCount(queryDto);
	}

	@Override
	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryHistoricExternalTaskLogsCount(
			@RequestBody HistoricExternalTaskLogQueryDto queryDto) {
		queryDto.setObjectMapper(objectMapper);
		HistoricExternalTaskLogQuery query = queryDto.toQuery(processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}

	protected List<HistoricExternalTaskLog> executePaginatedQuery(HistoricExternalTaskLogQuery query,
			Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}
}
