package org.camunda.bpm.runtime.rest.controller;

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
import org.camunda.bpm.runtime.plugin.AbstractRuntimePluginRestController;
import org.camunda.bpm.runtime.rest.dto.ProcessDefinitionDto;
import org.camunda.bpm.runtime.rest.dto.query.ProcessDefinitionQueryDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "processDefinitionRuntime")
@RequestMapping(ProcessDefinitionRestController.PATH)
public class ProcessDefinitionRestController extends AbstractRuntimePluginRestController {
	public static final String PATH = "/camunda/api/runtime/process-definition";

	@GetMapping(path = "/{id}/called-process-definitions", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProcessDefinitionDto> getCalledProcessDefinitions(HttpServletRequest request,
			@PathVariable("id") String id) {
		ProcessDefinitionQueryDto queryParameter = new ProcessDefinitionQueryDto(request.getParameterMap());
		return queryCalledProcessDefinitions(id, queryParameter);
	}

	@PostMapping(path = "/{id}/called-process-definitions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProcessDefinitionDto> queryCalledProcessDefinitions(@PathVariable("id") String id,
			final @RequestBody ProcessDefinitionQueryDto queryParameter) {
		return getCommandExecutor().executeCommand(new Command<List<ProcessDefinitionDto>>() {
			public List<ProcessDefinitionDto> execute(CommandContext commandContext) {
				queryParameter.setParentProcessDefinitionId(id);
				injectEngineConfig(queryParameter);
				configureExecutionQuery(queryParameter);
				queryParameter.disableMaxResultsLimit();
				return getQueryService().executeQuery("selectCalledProcessDefinitions", queryParameter);
			}
		});
	}

	private void injectEngineConfig(ProcessDefinitionQueryDto parameter) {

		ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl) getProcessEngine())
				.getProcessEngineConfiguration();
		if (processEngineConfiguration.getHistoryLevel().equals(HistoryLevel.HISTORY_LEVEL_NONE)) {
			parameter.setHistoryEnabled(false);
		}

		parameter.initQueryVariableValues(processEngineConfiguration.getVariableSerializers(), processEngineConfiguration.getDatabaseType());
	}

	protected void configureExecutionQuery(ProcessDefinitionQueryDto query) {
		configureAuthorizationCheck(query);
		configureTenantCheck(query);
		addPermissionCheck(query, PROCESS_INSTANCE, "EXEC2.PROC_INST_ID_", READ);
		addPermissionCheck(query, PROCESS_DEFINITION, "PROCDEF.KEY_", READ_INSTANCE);
	}
}
