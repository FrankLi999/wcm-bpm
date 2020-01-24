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

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.exception.NullValueException;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.impl.util.EnsureUtil;
import org.camunda.bpm.engine.rest.ProcessInstanceRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricProcessInstanceQueryDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceQueryDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceSuspensionStateDto;
import org.camunda.bpm.engine.rest.dto.runtime.SetJobRetriesByProcessDto;
import org.camunda.bpm.engine.rest.dto.runtime.batch.DeleteProcessInstancesDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
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

@RestController
@RequestMapping(ProcessInstanceRestService.PATH)
public class ProcessInstanceRestController extends AbstractRestProcessEngineAware
		implements ProcessInstanceRestService {

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<ProcessInstanceDto> getProcessInstances(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		ProcessInstanceQueryDto queryDto = new ProcessInstanceQueryDto(getObjectMapper(), request.getParameterMap());
		return queryProcessInstances(queryDto, firstResult, maxResults);
	}

	@Override
	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<ProcessInstanceDto> queryProcessInstances(
			@RequestBody ProcessInstanceQueryDto queryDto, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		ProcessEngine engine = getProcessEngine();
		queryDto.setObjectMapper(getObjectMapper());
		ProcessInstanceQuery query = queryDto.toQuery(engine);

		List<ProcessInstance> matchingInstances;
		if (firstResult != null || maxResults != null) {
			matchingInstances = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingInstances = query.list();
		}

		List<ProcessInstanceDto> instanceResults = new ArrayList<ProcessInstanceDto>();
		for (ProcessInstance instance : matchingInstances) {
			ProcessInstanceDto resultInstance = ProcessInstanceDto.fromProcessInstance(instance);
			instanceResults.add(resultInstance);
		}
		return instanceResults;
	}

	

	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getProcessInstancesCount(HttpServletRequest request) {
		ProcessInstanceQueryDto queryDto = new ProcessInstanceQueryDto(getObjectMapper(), request.getParameterMap());
		return queryProcessInstancesCount(queryDto);
	}

	@Override
	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryProcessInstancesCount(@RequestBody ProcessInstanceQueryDto queryDto) {
		ProcessEngine engine = getProcessEngine();
		queryDto.setObjectMapper(getObjectMapper());
		ProcessInstanceQuery query = queryDto.toQuery(engine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}

	@Override
	@PutMapping(path="/suspended", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateSuspensionState(@RequestBody ProcessInstanceSuspensionStateDto dto) {
		if (dto.getProcessInstanceId() != null) {
			String message = "Either processDefinitionId or processDefinitionKey can be set to update the suspension state.";
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, message);
		}

		dto.updateSuspensionState(getProcessEngine());
	}

	@Override
	@PostMapping(path="/suspended-async", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto updateSuspensionStateAsync(@RequestBody ProcessInstanceSuspensionStateDto dto) {
		Batch batch = null;
		try {
			batch = dto.updateSuspensionStateAsync(getProcessEngine());
			return BatchDto.fromBatch(batch);

		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@Override
	@PostMapping(path="/delete", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto deleteAsync(@RequestBody DeleteProcessInstancesDto dto) {
		RuntimeService runtimeService = getProcessEngine().getRuntimeService();

		ProcessInstanceQuery processInstanceQuery = null;
		if (dto.getProcessInstanceQuery() != null) {
			processInstanceQuery = dto.getProcessInstanceQuery().toQuery(getProcessEngine());
		}

		Batch batch = null;

		try {
			batch = runtimeService.deleteProcessInstancesAsync(dto.getProcessInstanceIds(), processInstanceQuery,
					dto.getDeleteReason(), dto.isSkipCustomListeners(), dto.isSkipSubprocesses());
			return BatchDto.fromBatch(batch);
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@Override
	@PostMapping(path="/delete-historic-query-based", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto deleteAsyncHistoricQueryBased(@RequestBody DeleteProcessInstancesDto deleteProcessInstancesDto) {
		List<String> processInstanceIds = new ArrayList<String>();

		HistoricProcessInstanceQueryDto queryDto = deleteProcessInstancesDto.getHistoricProcessInstanceQuery();
		if (queryDto != null) {
			HistoricProcessInstanceQuery query = queryDto.toQuery(getProcessEngine());
			List<HistoricProcessInstance> historicProcessInstances = query.list();

			for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
				processInstanceIds.add(historicProcessInstance.getId());
			}
		}

		if (deleteProcessInstancesDto.getProcessInstanceIds() != null) {
			processInstanceIds.addAll(deleteProcessInstancesDto.getProcessInstanceIds());
		}

		try {
			RuntimeService runtimeService = getProcessEngine().getRuntimeService();
			Batch batch = runtimeService.deleteProcessInstancesAsync(processInstanceIds, null,
					deleteProcessInstancesDto.getDeleteReason(), deleteProcessInstancesDto.isSkipCustomListeners(),
					deleteProcessInstancesDto.isSkipSubprocesses());

			return BatchDto.fromBatch(batch);
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@Override
	@PostMapping(path="/job-retries", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto setRetriesByProcess(@RequestBody SetJobRetriesByProcessDto setJobRetriesDto) {
		try {
			EnsureUtil.ensureNotNull("setJobRetriesDto", setJobRetriesDto);
			EnsureUtil.ensureNotNull("retries", setJobRetriesDto.getRetries());
		} catch (NullValueException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		ProcessInstanceQuery processInstanceQuery = null;
		if (setJobRetriesDto.getProcessInstanceQuery() != null) {
			processInstanceQuery = setJobRetriesDto.getProcessInstanceQuery().toQuery(getProcessEngine());
		}

		try {
			Batch batch = getProcessEngine().getManagementService().setJobRetriesAsync(
					setJobRetriesDto.getProcessInstances(), processInstanceQuery,
					setJobRetriesDto.getRetries().intValue());
			return BatchDto.fromBatch(batch);
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@Override
	@PostMapping(path="/job-retries-historic-query-based", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto setRetriesByProcessHistoricQueryBased(@RequestBody SetJobRetriesByProcessDto setJobRetriesDto) {
		List<String> processInstanceIds = new ArrayList<String>();

		HistoricProcessInstanceQueryDto queryDto = setJobRetriesDto.getHistoricProcessInstanceQuery();
		if (queryDto != null) {
			HistoricProcessInstanceQuery query = queryDto.toQuery(getProcessEngine());
			List<HistoricProcessInstance> historicProcessInstances = query.list();

			for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
				processInstanceIds.add(historicProcessInstance.getId());
			}
		}

		if (setJobRetriesDto.getProcessInstances() != null) {
			processInstanceIds.addAll(setJobRetriesDto.getProcessInstances());
		}

		try {
			ManagementService managementService = getProcessEngine().getManagementService();
			Batch batch = managementService.setJobRetriesAsync(processInstanceIds, (ProcessInstanceQuery) null,
					setJobRetriesDto.getRetries());

			return BatchDto.fromBatch(batch);
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	private List<ProcessInstance> executePaginatedQuery(ProcessInstanceQuery query, Integer firstResult,
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
