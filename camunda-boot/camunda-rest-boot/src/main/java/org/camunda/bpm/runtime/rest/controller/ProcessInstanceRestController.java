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
import org.camunda.bpm.engine.rest.ObjectMapperProvider;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.runtime.plugin.AbstractRuntimePluginRestController;
import org.camunda.bpm.runtime.rest.dto.CalledProcessInstanceDto;
import org.camunda.bpm.runtime.rest.dto.ProcessInstanceDto;
import org.camunda.bpm.runtime.rest.dto.query.CalledProcessInstanceQueryDto;
import org.camunda.bpm.runtime.rest.dto.query.ProcessInstanceQueryDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "processInstanceRuntime")
@RequestMapping(ProcessInstanceRestController.PATH)
public class ProcessInstanceRestController extends AbstractRuntimePluginRestController {
	public static final String PATH = "/camunda/api/runtime/process-instance";

	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProcessInstanceDto> getProcessInstances(HttpServletRequest request,
			@RequestParam("firstResult") Integer firstResult, @RequestParam("maxResults") Integer maxResults) {
		ProcessInstanceQueryDto queryParameter = new ProcessInstanceQueryDto(request.getParameterMap());
		return queryProcessInstances(queryParameter, firstResult, maxResults);
	}

	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProcessInstanceDto> queryProcessInstances(final @RequestBody ProcessInstanceQueryDto queryParameter,
			final @RequestParam("firstResult") Integer firstResult,
			final @RequestParam("maxResults") Integer maxResults) {

		return getCommandExecutor().executeCommand(new Command<List<ProcessInstanceDto>>() {
			public List<ProcessInstanceDto> execute(CommandContext commandContext) {
				injectObjectMapper(queryParameter);
				injectEngineConfig(queryParameter);
				paginate(queryParameter, firstResult, maxResults);
				configureExecutionQuery(queryParameter);
				return getQueryService().executeQuery("selectRunningProcessInstancesIncludingIncidents",
						queryParameter);
			}
		});

	}

	@GetMapping(path = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getProcessInstancesCount(HttpServletRequest request) {
		ProcessInstanceQueryDto queryParameter = new ProcessInstanceQueryDto(request.getParameterMap());
		return queryProcessInstancesCount(queryParameter);
	}

	@PostMapping(path = "/count", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryProcessInstancesCount(final ProcessInstanceQueryDto queryParameter) {

		return getCommandExecutor().executeCommand(new Command<CountResultDto>() {
			public CountResultDto execute(CommandContext commandContext) {
				injectEngineConfig(queryParameter);
				configureExecutionQuery(queryParameter);
				long result = getQueryService().executeQueryRowCount("selectRunningProcessInstancesCount",
						queryParameter);
				return new CountResultDto(result);
			}
		});

	}

	@GetMapping(path = "/{id}/called-process-instances", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CalledProcessInstanceDto> getCalledProcessInstances(HttpServletRequest request,
			@PathVariable("id") String id) {

		CalledProcessInstanceQueryDto queryParameter = new CalledProcessInstanceQueryDto(request.getParameterMap());
		return queryCalledProcessInstances(id, queryParameter);
	}

	@PostMapping(path = "/{id}/called-process-instances", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CalledProcessInstanceDto> queryCalledProcessInstances(@PathVariable("id") String id,
			CalledProcessInstanceQueryDto queryParameter) {
		queryParameter.setParentProcessInstanceId(id);
		configureExecutionQuery(queryParameter);
		queryParameter.disableMaxResultsLimit();
		return getQueryService().executeQuery("selectCalledProcessInstances", queryParameter);
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

		ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl) getProcessEngine())
				.getProcessEngineConfiguration();
		if (processEngineConfiguration.getHistoryLevel().equals(HistoryLevel.HISTORY_LEVEL_NONE)) {
			parameter.setHistoryEnabled(false);
		}

		parameter.initQueryVariableValues(processEngineConfiguration.getVariableSerializers(), processEngineConfiguration.getDatabaseType());
	}

	protected void configureExecutionQuery(ProcessInstanceQueryDto query) {
		configureAuthorizationCheck(query);
		configureTenantCheck(query);
		addPermissionCheck(query, PROCESS_INSTANCE, "RES.PROC_INST_ID_", READ);
		addPermissionCheck(query, PROCESS_DEFINITION, "P.KEY_", READ_INSTANCE);
	}

	protected void injectObjectMapper(ProcessInstanceQueryDto queryParameter) {
		queryParameter.setObjectMapper(ObjectMapperProvider.getObjectMapper());
	}

}
