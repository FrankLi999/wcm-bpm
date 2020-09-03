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

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.history.HistoricDecisionInstance;
import org.camunda.bpm.engine.history.HistoricDecisionInstanceQuery;
import org.camunda.bpm.engine.history.SetRemovalTimeSelectModeForHistoricDecisionInstancesBuilder;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricDecisionInstanceDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricDecisionInstanceQueryDto;
import org.camunda.bpm.engine.rest.dto.history.batch.DeleteHistoricDecisionInstancesDto;
import org.camunda.bpm.engine.rest.dto.history.batch.removaltime.SetRemovalTimeToHistoricDecisionInstancesDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.history.HistoricDecisionInstanceRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController(value="historicDecisionInstanceApi")
@RequestMapping(HistoryRestService.PATH + HistoricDecisionInstanceRestService.PATH)
public class HistoricDecisionInstanceRestController extends AbstractRestProcessEngineAware implements HistoricDecisionInstanceRestService {

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricDecisionInstanceDto> getHistoricDecisionInstances(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		HistoricDecisionInstanceQueryDto queryHistoricDecisionInstanceDto = new HistoricDecisionInstanceQueryDto(
				this.getObjectMapper(), request.getParameterMap());
		return queryHistoricDecisionInstances(queryHistoricDecisionInstanceDto, firstResult, maxResults);
	}

	
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getHistoricDecisionInstancesCount(HttpServletRequest request) {
		HistoricDecisionInstanceQueryDto queryDto = new HistoricDecisionInstanceQueryDto(this.getObjectMapper(),
				request.getParameterMap());
		return queryHistoricDecisionInstancesCount(queryDto);
	}

	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryHistoricDecisionInstancesCount(@RequestBody HistoricDecisionInstanceQueryDto queryDto) {
		HistoricDecisionInstanceQuery query = queryDto.toQuery(processEngine);

		long count = query.count();

		return new CountResultDto(count);
	}

	@PostMapping(path="/delete", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto deleteAsync(@RequestBody DeleteHistoricDecisionInstancesDto dto) {
		HistoricDecisionInstanceQuery decisionInstanceQuery = null;
		if (dto.getHistoricDecisionInstanceQuery() != null) {
			decisionInstanceQuery = dto.getHistoricDecisionInstanceQuery().toQuery(processEngine);
		}

		try {
			List<String> historicDecisionInstanceIds = dto.getHistoricDecisionInstanceIds();
			String deleteReason = dto.getDeleteReason();
			Batch batch = processEngine.getHistoryService().deleteHistoricDecisionInstancesAsync(
					historicDecisionInstanceIds, decisionInstanceQuery, deleteReason);
			return BatchDto.fromBatch(batch);
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping(path="/set-removal-time", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto setRemovalTimeAsync(SetRemovalTimeToHistoricDecisionInstancesDto dto) {
		HistoryService historyService = processEngine.getHistoryService();

		HistoricDecisionInstanceQuery historicDecisionInstanceQuery = null;

		if (dto.getHistoricDecisionInstanceQuery() != null) {
			historicDecisionInstanceQuery = dto.getHistoricDecisionInstanceQuery().toQuery(processEngine);

		}

		SetRemovalTimeSelectModeForHistoricDecisionInstancesBuilder builder = historyService
				.setRemovalTimeToHistoricDecisionInstances();

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

		builder.byIds(dto.getHistoricDecisionInstanceIds());
		builder.byQuery(historicDecisionInstanceQuery);

		if (dto.isHierarchical()) {
			builder.hierarchical();

		}

		Batch batch = builder.executeAsync();
		return BatchDto.fromBatch(batch);
	}

	protected List<HistoricDecisionInstanceDto> queryHistoricDecisionInstances(HistoricDecisionInstanceQueryDto queryDto,
			Integer firstResult, Integer maxResults) {
		HistoricDecisionInstanceQuery query = queryDto.toQuery(processEngine);

		List<HistoricDecisionInstance> matchingHistoricDecisionInstances;
		if (firstResult != null || maxResults != null) {
			matchingHistoricDecisionInstances = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingHistoricDecisionInstances = query.list();
		}

		List<HistoricDecisionInstanceDto> historicDecisionInstanceDtoResults = new ArrayList<HistoricDecisionInstanceDto>();
		for (HistoricDecisionInstance historicDecisionInstance : matchingHistoricDecisionInstances) {
			HistoricDecisionInstanceDto resultHistoricDecisionInstanceDto = HistoricDecisionInstanceDto
					.fromHistoricDecisionInstance(historicDecisionInstance);
			historicDecisionInstanceDtoResults.add(resultHistoricDecisionInstanceDto);
		}
		return historicDecisionInstanceDtoResults;
	}

	private List<HistoricDecisionInstance> executePaginatedQuery(HistoricDecisionInstanceQuery query,
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
