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

import org.camunda.bpm.engine.history.HistoricDetail;
import org.camunda.bpm.engine.history.HistoricDetailQuery;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricDetailDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricDetailQueryDto;
import org.camunda.bpm.engine.rest.history.HistoricDetailRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.rest.sub.VariableResource;
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
@RequestMapping(HistoryRestService.PATH + HistoricDetailRestService.PATH)
public class HistoricDetailRestController extends AbstractRestProcessEngineAware implements HistoricDetailRestService {

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricDetailDto> getHistoricDetails(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults,
			@RequestParam(name=VariableResource.DESERIALIZE_VALUES_QUERY_PARAM, defaultValue="true") boolean deserializeObjectValues) {
		HistoricDetailQueryDto queryDto = new HistoricDetailQueryDto(objectMapper, request.getParameterMap());
		HistoricDetailQuery query = queryDto.toQuery(processEngine);

		return executeHistoricDetailQuery(query, firstResult, maxResults, deserializeObjectValues);
	}

	@Override
	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricDetailDto> queryHistoricDetails(
			@RequestBody HistoricDetailQueryDto queryDto, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults,
			@RequestParam(name=VariableResource.DESERIALIZE_VALUES_QUERY_PARAM, defaultValue="true") boolean deserializeObjectValues) {
		HistoricDetailQuery query = queryDto.toQuery(processEngine);

		return executeHistoricDetailQuery(query, firstResult, maxResults, deserializeObjectValues);
	}

	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getHistoricDetailsCount(HttpServletRequest request) {
		HistoricDetailQueryDto queryDto = new HistoricDetailQueryDto(objectMapper, request.getParameterMap());
		HistoricDetailQuery query = queryDto.toQuery(processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}

	private List<HistoricDetailDto> executeHistoricDetailQuery(HistoricDetailQuery query, Integer firstResult,
			Integer maxResults, boolean deserializeObjectValues) {

		query.disableBinaryFetching();
		if (!deserializeObjectValues) {
			query.disableCustomObjectDeserialization();
		}

		List<HistoricDetail> queryResult;
		if (firstResult != null || maxResults != null) {
			queryResult = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			queryResult = query.list();
		}

		List<HistoricDetailDto> result = new ArrayList<HistoricDetailDto>();
		for (HistoricDetail historicDetail : queryResult) {
			HistoricDetailDto dto = HistoricDetailDto.fromHistoricDetail(historicDetail);
			result.add(dto);
		}

		return result;
	}

	private List<HistoricDetail> executePaginatedQuery(HistoricDetailQuery query, Integer firstResult,
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
