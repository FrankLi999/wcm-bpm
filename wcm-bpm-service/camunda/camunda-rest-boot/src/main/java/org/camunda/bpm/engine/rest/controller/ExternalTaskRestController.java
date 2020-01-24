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

import javax.net.ssl.SSLEngineResult.Status;
import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.camunda.bpm.engine.externaltask.ExternalTaskQuery;
import org.camunda.bpm.engine.externaltask.ExternalTaskQueryBuilder;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.externaltask.UpdateExternalTaskRetriesBuilder;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.rest.ExternalTaskRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.dto.externaltask.ExternalTaskDto;
import org.camunda.bpm.engine.rest.dto.externaltask.ExternalTaskQueryDto;
import org.camunda.bpm.engine.rest.dto.externaltask.FetchExternalTasksDto;
import org.camunda.bpm.engine.rest.dto.externaltask.LockedExternalTaskDto;
import org.camunda.bpm.engine.rest.dto.externaltask.SetRetriesForExternalTasksDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricProcessInstanceQueryDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceQueryDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Thorben Lindhauer
 *
 */
@RestController
@RequestMapping(ExternalTaskRestService.PATH)
public class ExternalTaskRestController extends AbstractRestProcessEngineAware implements ExternalTaskRestService {

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<ExternalTaskDto> getExternalTasks(HttpServletRequest request, @RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		ExternalTaskQueryDto queryDto = new ExternalTaskQueryDto(this.objectMapper, request.getParameterMap());
		return queryExternalTasks(queryDto, firstResult, maxResults);
	}

	@Override
	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<ExternalTaskDto> queryExternalTasks(@RequestBody ExternalTaskQueryDto queryDto, @RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {

		queryDto.setObjectMapper(this.objectMapper);
		ExternalTaskQuery query = queryDto.toQuery(this.processEngine);

		List<ExternalTask> matchingTasks;
		if (firstResult != null || maxResults != null) {
			matchingTasks = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingTasks = query.list();
		}

		List<ExternalTaskDto> taskResults = new ArrayList<ExternalTaskDto>();
		for (ExternalTask task : matchingTasks) {
			ExternalTaskDto resultInstance = ExternalTaskDto.fromExternalTask(task);
			taskResults.add(resultInstance);
		}
		return taskResults;
	}

	protected List<ExternalTask> executePaginatedQuery(ExternalTaskQuery query, Integer firstResult,
			Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}

	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getExternalTasksCount(HttpServletRequest request) {
		ExternalTaskQueryDto queryDto = new ExternalTaskQueryDto(this.objectMapper, request.getParameterMap());
		return queryExternalTasksCount(queryDto);
	}

	@Override
	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryExternalTasksCount(@RequestBody ExternalTaskQueryDto queryDto) {
		queryDto.setObjectMapper(this.objectMapper);
		ExternalTaskQuery query = queryDto.toQuery(this.processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}

	@Override
	@PostMapping(path="/fetchAndLock", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<LockedExternalTaskDto> fetchAndLock(@RequestBody FetchExternalTasksDto fetchingDto) {
		ExternalTaskQueryBuilder fetchBuilder = fetchingDto.buildQuery(processEngine);
		List<LockedExternalTask> externalTasks = fetchBuilder.execute();
		return LockedExternalTaskDto.fromLockedExternalTasks(externalTasks);
	}

//	@Override
//	public ExternalTaskResource getExternalTask(String externalTaskId) {
//		return new ExternalTaskRestController(getProcessEngine(), externalTaskId, getObjectMapper());
//	}

	@Override
	@PostMapping(path="/retries-async", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto setRetriesAsync(@RequestBody SetRetriesForExternalTasksDto retriesDto) {

		UpdateExternalTaskRetriesBuilder builder = updateRetries(retriesDto);
		Integer retries = retriesDto.getRetries();

		if (retries == null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, "The number of retries cannot be null.");
		}

		try {
			Batch batch = builder.setAsync(retries);
			return BatchDto.fromBatch(batch);
		} catch (NotFoundException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

	@Override
	@PutMapping(path="/retries", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void setRetries(@RequestBody SetRetriesForExternalTasksDto retriesDto) {

		UpdateExternalTaskRetriesBuilder builder = updateRetries(retriesDto);
		Integer retries = retriesDto.getRetries();

		if (retries == null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, "The number of retries cannot be null.");
		}

		try {
			builder.set(retries);
		} catch (NotFoundException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	protected UpdateExternalTaskRetriesBuilder updateRetries(SetRetriesForExternalTasksDto retriesDto) {

		ExternalTaskService externalTaskService = this.processEngine.getExternalTaskService();

		List<String> externalTaskIds = retriesDto.getExternalTaskIds();
		List<String> processInstanceIds = retriesDto.getProcessInstanceIds();

		ExternalTaskQuery externalTaskQuery = null;
		ProcessInstanceQuery processInstanceQuery = null;
		HistoricProcessInstanceQuery historicProcessInstanceQuery = null;

		ExternalTaskQueryDto externalTaskQueryDto = retriesDto.getExternalTaskQuery();
		if (externalTaskQueryDto != null) {
			externalTaskQuery = externalTaskQueryDto.toQuery(this.processEngine);
		}

		ProcessInstanceQueryDto processInstanceQueryDto = retriesDto.getProcessInstanceQuery();
		if (processInstanceQueryDto != null) {
			processInstanceQuery = processInstanceQueryDto.toQuery(this.processEngine);
		}

		HistoricProcessInstanceQueryDto historicProcessInstanceQueryDto = retriesDto.getHistoricProcessInstanceQuery();
		if (historicProcessInstanceQueryDto != null) {
			historicProcessInstanceQuery = historicProcessInstanceQueryDto.toQuery(this.processEngine);
		}

		return externalTaskService.updateRetries().externalTaskIds(externalTaskIds)
				.processInstanceIds(processInstanceIds).externalTaskQuery(externalTaskQuery)
				.processInstanceQuery(processInstanceQuery).historicProcessInstanceQuery(historicProcessInstanceQuery);
	}

}
