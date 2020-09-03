package org.camunda.bpm.runtime.rest.controller;

import static org.camunda.bpm.engine.authorization.Permissions.READ;
import static org.camunda.bpm.engine.authorization.Permissions.READ_INSTANCE;
import static org.camunda.bpm.engine.authorization.Resources.PROCESS_DEFINITION;
import static org.camunda.bpm.engine.authorization.Resources.PROCESS_INSTANCE;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.runtime.plugin.AbstractRuntimePluginRestController;
import org.camunda.bpm.runtime.rest.dto.IncidentDto;
import org.camunda.bpm.runtime.rest.dto.query.IncidentQueryDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "incidentRuntime")
@RequestMapping(IncidentRestController.PATH)
public class IncidentRestController extends AbstractRuntimePluginRestController {
	public static final String PATH = "/camunda/api/runtime/incident";

	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IncidentDto> getIncidents(HttpServletRequest request, @RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		IncidentQueryDto queryParameter = new IncidentQueryDto(request.getParameterMap());
		return queryIncidents(queryParameter, firstResult, maxResults);
	}

	@PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IncidentDto> queryIncidents(final @RequestBody IncidentQueryDto queryParameter,
			@RequestParam("firstResult") Integer firstResult, @RequestParam("maxResults") Integer maxResults) {

		paginateQueryParameters(queryParameter, firstResult, maxResults);
		configureExecutionQuery(queryParameter);
		List<IncidentDto> matchingIncidents = getQueryService()
				.executeQuery("selectIncidentWithCauseAndRootCauseIncidents", queryParameter);
		return matchingIncidents;
	}

	@GetMapping(path = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getIncidentsCount(HttpServletRequest request) {
		IncidentQueryDto queryParameter = new IncidentQueryDto(request.getParameterMap());
		return queryIncidentsCount(queryParameter);
	}

	@PostMapping(path = "/count", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryIncidentsCount(@RequestBody IncidentQueryDto queryParameter) {
		CountResultDto result = new CountResultDto();
		configureExecutionQuery(queryParameter);
		long count = getQueryService().executeQueryRowCount("selectIncidentWithCauseAndRootCauseIncidentsCount",
				queryParameter);
		result.setCount(count);

		return result;
	}

	protected void configureExecutionQuery(IncidentQueryDto query) {
		configureAuthorizationCheck(query);
		configureTenantCheck(query);
		addPermissionCheck(query, PROCESS_INSTANCE, "RES.PROC_INST_ID_", READ);
		addPermissionCheck(query, PROCESS_DEFINITION, "PROCDEF.KEY_", READ_INSTANCE);
	}

	private void paginateQueryParameters(IncidentQueryDto queryParameter, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		queryParameter.setFirstResult(firstResult);
		queryParameter.setMaxResults(maxResults);
	}

}
