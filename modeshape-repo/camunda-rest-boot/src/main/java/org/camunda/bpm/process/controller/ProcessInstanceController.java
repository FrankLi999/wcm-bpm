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
package org.camunda.bpm.process.controller;

import static org.camunda.bpm.engine.authorization.Permissions.READ;
import static org.camunda.bpm.engine.authorization.Permissions.READ_INSTANCE;
import static org.camunda.bpm.engine.authorization.Resources.PROCESS_DEFINITION;
import static org.camunda.bpm.engine.authorization.Resources.PROCESS_INSTANCE;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.impl.ProcessEngineImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.process.impl.plugin.base.dto.CalledProcessInstanceDto;
import org.camunda.bpm.process.impl.plugin.base.dto.ProcessInstanceDto;
import org.camunda.bpm.process.impl.plugin.base.dto.query.CalledProcessInstanceQueryDto;
import org.camunda.bpm.process.impl.plugin.base.dto.query.ProcessInstanceQueryDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ProcessInstanceController.PATH)
public class ProcessInstanceController extends BaseProcessRestController {

	public static final String PATH = "/camunda/api/process/process-instance";

	public ProcessInstanceController() {
	}

	@GetMapping(path = "/{id}/called-process-instances", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CalledProcessInstanceDto> getCalledProcessInstances(@PathVariable("id") String parentProcessInstanceId,
			HttpServletRequest request) {
		CalledProcessInstanceQueryDto queryParameter = new CalledProcessInstanceQueryDto(request.getParameterMap());
		return queryCalledProcessInstances(parentProcessInstanceId, queryParameter);
	}

	@PostMapping(path = "/{id}/called-process-instances", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CalledProcessInstanceDto> queryCalledProcessInstances(
			@PathVariable("id") String parentProcessInstanceId,
			@RequestBody CalledProcessInstanceQueryDto queryParameter) {
		queryParameter.setParentProcessInstanceId(parentProcessInstanceId);
		this.configureExecutionQuery(queryParameter);
		return this.queryService.executeQuery("selectCalledProcessInstances", queryParameter);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProcessInstanceDto> getProcessInstances(HttpServletRequest request,
			@RequestParam("firstResult") Integer firstResult, @RequestParam("maxResults") Integer maxResults) {
		ProcessInstanceQueryDto queryParameter = new ProcessInstanceQueryDto(request.getParameterMap());
		return queryProcessInstances(queryParameter, firstResult, maxResults);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProcessInstanceDto> queryProcessInstances(@RequestBody final ProcessInstanceQueryDto queryParameter,
			final @RequestParam("firstResult") Integer firstResult,
			final @RequestParam("maxResults") Integer maxResults) {

		return this.commandExecutor.executeCommand(new Command<List<ProcessInstanceDto>>() {
			public List<ProcessInstanceDto> execute(CommandContext commandContext) {
				injectObjectMapper(queryParameter);
				injectEngineConfig(queryParameter);
				paginate(queryParameter, firstResult, maxResults);
				configureExecutionQuery(queryParameter);
				return ProcessInstanceController.this.queryService
						.executeQuery("selectRunningProcessInstancesIncludingIncidents", queryParameter);
			}
		});
	}

	@GetMapping(path = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getProcessInstancesCount(HttpServletRequest request) {
		ProcessInstanceQueryDto queryParameter = new ProcessInstanceQueryDto(request.getParameterMap());
		return queryProcessInstancesCount(queryParameter);
	}

	@PostMapping(path = "/count", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryProcessInstancesCount(@RequestBody final ProcessInstanceQueryDto queryParameter) {

		return this.commandExecutor.executeCommand(new Command<CountResultDto>() {
			public CountResultDto execute(CommandContext commandContext) {
				injectEngineConfig(queryParameter);
				configureExecutionQuery(queryParameter);
				long result = ProcessInstanceController.this.queryService
						.executeQueryRowCount("selectRunningProcessInstancesCount", queryParameter);
				return new CountResultDto(result);
			}
		});

	}

	protected void configureExecutionQuery(CalledProcessInstanceQueryDto query) {
		configureAuthorizationCheck(query);
		configureTenantCheck(query);
		addPermissionCheck(query, PROCESS_INSTANCE, "EXEC1.PROC_INST_ID_", READ);
		addPermissionCheck(query, PROCESS_DEFINITION, "PROCDEF.KEY_", READ_INSTANCE);
	}

	private void paginate(ProcessInstanceQueryDto queryParameter, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		queryParameter.setFirstResult(firstResult);
		queryParameter.setMaxResults(maxResults);
	}

	private void injectEngineConfig(ProcessInstanceQueryDto parameter) {

		ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl) this.processEngine)
				.getProcessEngineConfiguration();
		if (processEngineConfiguration.getHistoryLevel().equals(HistoryLevel.HISTORY_LEVEL_NONE)) {
			parameter.setHistoryEnabled(false);
		}

		parameter.initQueryVariableValues(processEngineConfiguration.getVariableSerializers());
	}

	protected void configureExecutionQuery(ProcessInstanceQueryDto query) {
		configureAuthorizationCheck(query);
		configureTenantCheck(query);
		addPermissionCheck(query, PROCESS_INSTANCE, "RES.PROC_INST_ID_", READ);
		addPermissionCheck(query, PROCESS_DEFINITION, "P.KEY_", READ_INSTANCE);
	}

	protected void injectObjectMapper(ProcessInstanceQueryDto queryParameter) {
		queryParameter.setObjectMapper(objectMapper);
	}
}
