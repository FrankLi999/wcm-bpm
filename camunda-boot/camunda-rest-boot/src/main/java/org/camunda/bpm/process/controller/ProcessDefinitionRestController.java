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
import org.camunda.bpm.process.impl.plugin.base.dto.ProcessDefinitionDto;
import org.camunda.bpm.process.impl.plugin.base.dto.query.ProcessDefinitionQueryDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ProcessDefinitionRestController.PATH)
public class ProcessDefinitionRestController extends BaseProcessRestController {
	public static final String PATH = "/camunda/api/process/process-definition";

	public ProcessDefinitionRestController() {
	}

	@GetMapping(path = "/{id}/called-process-definitions", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProcessDefinitionDto> getCalledProcessDefinitions(@PathVariable("id") String id,
			HttpServletRequest request) {
		ProcessDefinitionQueryDto queryParameter = new ProcessDefinitionQueryDto(request.getParameterMap());
		return queryCalledProcessDefinitions(id, queryParameter);
	}

	@PostMapping(path = "/{id}/called-process-definitions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProcessDefinitionDto> queryCalledProcessDefinitions(@PathVariable("id") String id,
			@RequestBody final ProcessDefinitionQueryDto queryParameter) {
		return this.commandExecutor.executeCommand(new Command<List<ProcessDefinitionDto>>() {
			public List<ProcessDefinitionDto> execute(CommandContext commandContext) {
				queryParameter.setParentProcessDefinitionId(id);
				injectEngineConfig(queryParameter);
				configureExecutionQuery(queryParameter);
				return ProcessDefinitionRestController.this.queryService.executeQuery("selectCalledProcessDefinitions",
						queryParameter);
			}
		});
	}

	private void injectEngineConfig(ProcessDefinitionQueryDto parameter) {

		ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl) this.processEngine)
				.getProcessEngineConfiguration();
		if (processEngineConfiguration.getHistoryLevel().equals(HistoryLevel.HISTORY_LEVEL_NONE)) {
			parameter.setHistoryEnabled(false);
		}

		parameter.initQueryVariableValues(processEngineConfiguration.getVariableSerializers());
	}

	protected void configureExecutionQuery(ProcessDefinitionQueryDto query) {
		configureAuthorizationCheck(query);
		configureTenantCheck(query);
		addPermissionCheck(query, PROCESS_INSTANCE, "EXEC2.PROC_INST_ID_", READ);
		addPermissionCheck(query, PROCESS_DEFINITION, "PROCDEF.KEY_", READ_INSTANCE);
	}

}
