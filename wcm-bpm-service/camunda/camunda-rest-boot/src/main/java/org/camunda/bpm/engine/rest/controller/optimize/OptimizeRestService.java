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
package org.camunda.bpm.engine.rest.controller.optimize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricDecisionInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricVariableUpdate;
import org.camunda.bpm.engine.history.UserOperationLogEntry;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.persistence.entity.optimize.OptimizeHistoricIdentityLinkLogEntity;
import org.camunda.bpm.engine.rest.dto.converter.DateConverter;
import org.camunda.bpm.engine.rest.dto.history.HistoricActivityInstanceDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricDecisionInstanceDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricTaskInstanceDto;
import org.camunda.bpm.engine.rest.dto.history.UserOperationLogEntryDto;
import org.camunda.bpm.engine.rest.dto.history.optimize.OptimizeHistoricIdentityLinkLogDto;
import org.camunda.bpm.engine.rest.dto.history.optimize.OptimizeHistoricVariableUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(OptimizeRestService.PATH)
public class OptimizeRestService { //extends AbstractRestProcessEngineAware {
	public final static String PATH = "/camunda/api/engine/impl/optimize";
	public static final String RESOURCE_PATH = "/optimize";

	private DateConverter dateConverter;
	@Autowired
	ProcessEngine engine;
	
	@PostConstruct
	public void afterPropertySet() {
		dateConverter = new DateConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		dateConverter.setObjectMapper(objectMapper);
	}

	@GetMapping(path="/activity-instance/completed")
	public List<HistoricActivityInstanceDto> getCompletedHistoricActivityInstances(
			@RequestParam("finishedAfter") String finishedAfterAsString,
			@RequestParam("finishedAt") String finishedAtAsString, 
			@RequestParam("maxResults") int maxResults) {

		Date finishedAfter = dateConverter.convertQueryParameterToType(finishedAfterAsString);
		Date finishedAt = dateConverter.convertQueryParameterToType(finishedAtAsString);
		maxResults = ensureValidMaxResults(maxResults);

		ProcessEngineConfigurationImpl config = (ProcessEngineConfigurationImpl) this.engine
				.getProcessEngineConfiguration();

		List<HistoricActivityInstance> historicActivityInstances = config.getOptimizeService()
				.getCompletedHistoricActivityInstances(finishedAfter, finishedAt, maxResults);

		List<HistoricActivityInstanceDto> result = new ArrayList<>();
		for (HistoricActivityInstance instance : historicActivityInstances) {
			HistoricActivityInstanceDto dto = HistoricActivityInstanceDto.fromHistoricActivityInstance(instance);
			result.add(dto);
		}
		return result;
	}

	@GetMapping("/activity-instance/running")
	public List<HistoricActivityInstanceDto> getRunningHistoricActivityInstances(
			@RequestParam("startedAfter") String startedAfterAsString, 
			@RequestParam("startedAt") String startedAtAsString,
			@RequestParam("maxResults") int maxResults) {

		Date startedAfter = dateConverter.convertQueryParameterToType(startedAfterAsString);
		Date startedAt = dateConverter.convertQueryParameterToType(startedAtAsString);
		maxResults = ensureValidMaxResults(maxResults);

		ProcessEngineConfigurationImpl config = (ProcessEngineConfigurationImpl) this.engine
				.getProcessEngineConfiguration();

		List<HistoricActivityInstance> historicActivityInstances = config.getOptimizeService()
				.getRunningHistoricActivityInstances(startedAfter, startedAt, maxResults);

		List<HistoricActivityInstanceDto> result = new ArrayList<>();
		for (HistoricActivityInstance instance : historicActivityInstances) {
			HistoricActivityInstanceDto dto = HistoricActivityInstanceDto.fromHistoricActivityInstance(instance);
			result.add(dto);
		}
		return result;
	}

	@GetMapping("/task-instance/completed")
	public List<HistoricTaskInstanceDto> getCompletedHistoricTaskInstances(
			@RequestParam("finishedAfter") String finishedAfterAsString,
			@RequestParam("finishedAt") String finishedAtAsString, 
			@RequestParam("maxResults") int maxResults) {

		Date finishedAfter = dateConverter.convertQueryParameterToType(finishedAfterAsString);
		Date finishedAt = dateConverter.convertQueryParameterToType(finishedAtAsString);
		maxResults = ensureValidMaxResults(maxResults);

		ProcessEngineConfigurationImpl config = (ProcessEngineConfigurationImpl) this.engine
				.getProcessEngineConfiguration();

		List<HistoricTaskInstance> historicTaskInstances = config.getOptimizeService()
				.getCompletedHistoricTaskInstances(finishedAfter, finishedAt, maxResults);

		List<HistoricTaskInstanceDto> result = new ArrayList<>();
		for (HistoricTaskInstance instance : historicTaskInstances) {
			HistoricTaskInstanceDto dto = HistoricTaskInstanceDto.fromHistoricTaskInstance(instance);
			result.add(dto);
		}
		return result;
	}

	@GetMapping("/task-instance/running")
	public List<HistoricTaskInstanceDto> getRunningHistoricTaskInstances(
			@RequestParam("startedAfter") String startedAfterAsString, 
			@RequestParam("startedAt") String startedAtAsString,
			@RequestParam("maxResults") int maxResults) {

		Date startedAfter = dateConverter.convertQueryParameterToType(startedAfterAsString);
		Date startedAt = dateConverter.convertQueryParameterToType(startedAtAsString);
		maxResults = ensureValidMaxResults(maxResults);

		ProcessEngineConfigurationImpl config = (ProcessEngineConfigurationImpl) this.engine
				.getProcessEngineConfiguration();

		List<HistoricTaskInstance> historicTaskInstances = config.getOptimizeService()
				.getRunningHistoricTaskInstances(startedAfter, startedAt, maxResults);

		List<HistoricTaskInstanceDto> result = new ArrayList<>();
		for (HistoricTaskInstance instance : historicTaskInstances) {
			HistoricTaskInstanceDto dto = HistoricTaskInstanceDto.fromHistoricTaskInstance(instance);
			result.add(dto);
		}
		return result;
	}

	@GetMapping("/user-operation")
	public List<UserOperationLogEntryDto> getHistoricUserOperationLogs(
			@RequestParam("occurredAfter") String occurredAfterAsString,
			@RequestParam("occurredAt") String occurredAtAsString, 
			@RequestParam("maxResults") int maxResults) {

		Date occurredAfter = dateConverter.convertQueryParameterToType(occurredAfterAsString);
		Date occurredAt = dateConverter.convertQueryParameterToType(occurredAtAsString);
		maxResults = ensureValidMaxResults(maxResults);

		ProcessEngineConfigurationImpl config = (ProcessEngineConfigurationImpl) this.engine
				.getProcessEngineConfiguration();

		List<UserOperationLogEntry> operationLogEntries = config.getOptimizeService()
				.getHistoricUserOperationLogs(occurredAfter, occurredAt, maxResults);

		List<UserOperationLogEntryDto> result = new ArrayList<>();
		for (UserOperationLogEntry logEntry : operationLogEntries) {
			UserOperationLogEntryDto dto = UserOperationLogEntryDto.map(logEntry);
			result.add(dto);
		}
		return result;
	}

	@GetMapping("/identity-link-log")
	public List<OptimizeHistoricIdentityLinkLogDto> getHistoricIdentityLinkLogs(
			@RequestParam("occurredAfter") String occurredAfterAsString,
			@RequestParam("occurredAt") String occurredAtAsString, 
			@RequestParam("maxResults") int maxResults) {

		Date occurredAfter = dateConverter.convertQueryParameterToType(occurredAfterAsString);
		Date occurredAt = dateConverter.convertQueryParameterToType(occurredAtAsString);
		maxResults = ensureValidMaxResults(maxResults);

		ProcessEngineConfigurationImpl config = (ProcessEngineConfigurationImpl) this.engine
				.getProcessEngineConfiguration();

		List<OptimizeHistoricIdentityLinkLogEntity> operationLogEntries = config.getOptimizeService()
				.getHistoricIdentityLinkLogs(occurredAfter, occurredAt, maxResults);

		List<OptimizeHistoricIdentityLinkLogDto> result = new ArrayList<>();
		for (OptimizeHistoricIdentityLinkLogEntity logEntry : operationLogEntries) {
			OptimizeHistoricIdentityLinkLogDto dto = OptimizeHistoricIdentityLinkLogDto
					.fromHistoricIdentityLink(logEntry);
			result.add(dto);
		}
		return result;
	}

	@GetMapping("/process-instance/completed")
	public List<HistoricProcessInstanceDto> getCompletedHistoricProcessInstances(
			@RequestParam("finishedAfter") String finishedAfterAsString,
			@RequestParam("finishedAt") String finishedAtAsString, 
			@RequestParam("maxResults") int maxResults) {
		Date finishedAfter = dateConverter.convertQueryParameterToType(finishedAfterAsString);
		Date finishedAt = dateConverter.convertQueryParameterToType(finishedAtAsString);
		maxResults = ensureValidMaxResults(maxResults);

		ProcessEngineConfigurationImpl config = (ProcessEngineConfigurationImpl) this.engine
				.getProcessEngineConfiguration();
		List<HistoricProcessInstance> historicProcessInstances = config.getOptimizeService()
				.getCompletedHistoricProcessInstances(finishedAfter, finishedAt, maxResults);

		List<HistoricProcessInstanceDto> result = new ArrayList<>();
		for (HistoricProcessInstance instance : historicProcessInstances) {
			HistoricProcessInstanceDto dto = HistoricProcessInstanceDto.fromHistoricProcessInstance(instance);
			result.add(dto);
		}
		return result;
	}

	@GetMapping("/process-instance/running")
	public List<HistoricProcessInstanceDto> getRunningHistoricProcessInstances(
			@RequestParam("startedAfter") String startedAfterAsString, 
			@RequestParam("startedAt") String startedAtAsString,
			@RequestParam("maxResults") int maxResults) {
		Date startedAfter = dateConverter.convertQueryParameterToType(startedAfterAsString);
		Date startedAt = dateConverter.convertQueryParameterToType(startedAtAsString);
		maxResults = ensureValidMaxResults(maxResults);

		ProcessEngineConfigurationImpl config = (ProcessEngineConfigurationImpl) this.engine
				.getProcessEngineConfiguration();
		List<HistoricProcessInstance> historicProcessInstances = config.getOptimizeService()
				.getRunningHistoricProcessInstances(startedAfter, startedAt, maxResults);

		List<HistoricProcessInstanceDto> result = new ArrayList<>();
		for (HistoricProcessInstance instance : historicProcessInstances) {
			HistoricProcessInstanceDto dto = HistoricProcessInstanceDto.fromHistoricProcessInstance(instance);
			result.add(dto);
		}
		return result;
	}

	@GetMapping("/variable-update")
	public List<OptimizeHistoricVariableUpdateDto> getHistoricVariableUpdates(
			@RequestParam("occurredAfter") String occurredAfterAsString,
			@RequestParam("occurredAt") String occurredAtAsString, 
			@RequestParam("maxResults") int maxResults) {
		Date occurredAfter = dateConverter.convertQueryParameterToType(occurredAfterAsString);
		Date occurredAt = dateConverter.convertQueryParameterToType(occurredAtAsString);
		maxResults = ensureValidMaxResults(maxResults);

		ProcessEngineConfigurationImpl config = (ProcessEngineConfigurationImpl) this.engine
				.getProcessEngineConfiguration();
		List<HistoricVariableUpdate> historicVariableUpdates = config.getOptimizeService()
				.getHistoricVariableUpdates(occurredAfter, occurredAt, maxResults);

		List<OptimizeHistoricVariableUpdateDto> result = new ArrayList<>();
		for (HistoricVariableUpdate instance : historicVariableUpdates) {
			OptimizeHistoricVariableUpdateDto dto = OptimizeHistoricVariableUpdateDto
					.fromHistoricVariableUpdate(instance);
			result.add(dto);
		}
		return result;
	}

	@GetMapping("/decision-instance")
	public List<HistoricDecisionInstanceDto> getHistoricDecisionInstances(
			@RequestParam("evaluatedAfter") String evaluatedAfterAsString,
			@RequestParam("evaluatedAt") String evaluatedAtAsString, 
			@RequestParam("maxResults") int maxResults) {
		Date evaluatedAfter = dateConverter.convertQueryParameterToType(evaluatedAfterAsString);
		Date evaluatedAt = dateConverter.convertQueryParameterToType(evaluatedAtAsString);
		maxResults = ensureValidMaxResults(maxResults);

		ProcessEngineConfigurationImpl config = (ProcessEngineConfigurationImpl) this.engine
				.getProcessEngineConfiguration();
		List<HistoricDecisionInstance> historicDecisionInstances = config.getOptimizeService()
				.getHistoricDecisionInstances(evaluatedAfter, evaluatedAt, maxResults);

		List<HistoricDecisionInstanceDto> resultList = new ArrayList<>();
		for (HistoricDecisionInstance historicDecisionInstance : historicDecisionInstances) {
			HistoricDecisionInstanceDto dto = HistoricDecisionInstanceDto
					.fromHistoricDecisionInstance(historicDecisionInstance);
			resultList.add(dto);
		}

		return resultList;
	}

	protected int ensureValidMaxResults(int givenMaxResults) {
		return givenMaxResults > 0 ? givenMaxResults : Integer.MAX_VALUE;
	}
}
