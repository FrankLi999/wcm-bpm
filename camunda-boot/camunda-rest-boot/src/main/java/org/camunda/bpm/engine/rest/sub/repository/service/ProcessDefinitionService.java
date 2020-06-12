package org.camunda.bpm.engine.rest.sub.repository.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.AuthorizationException;
import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.exception.NullValueException;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidationException;
import org.camunda.bpm.engine.impl.util.IoUtil;
import org.camunda.bpm.engine.management.ActivityStatistics;
import org.camunda.bpm.engine.management.ActivityStatisticsQuery;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.rest.ProcessInstanceRestService;
import org.camunda.bpm.engine.rest.dto.HistoryTimeToLiveDto;
import org.camunda.bpm.engine.rest.dto.StatisticsResultDto;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.dto.converter.StringListConverter;
import org.camunda.bpm.engine.rest.dto.repository.ActivityStatisticsResultDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDiagramDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionSuspensionStateDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceWithVariablesDto;
import org.camunda.bpm.engine.rest.dto.runtime.RestartProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.StartProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.modification.ProcessInstanceModificationInstructionDto;
import org.camunda.bpm.engine.rest.dto.task.FormDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.repository.ProcessDefinitionResource;
import org.camunda.bpm.engine.rest.util.ApplicationContextPathUtil;
import org.camunda.bpm.engine.rest.util.EncodingUtil;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.camunda.bpm.engine.runtime.RestartProcessInstanceBuilder;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProcessDefinitionService implements ProcessDefinitionResource {

	@Autowired
	protected ProcessEngine engine;
	protected String processDefinitionId;
	protected String rootResourcePath = "http://localhost:8080";
	
	protected ObjectMapper objectMapper = new ObjectMapper();

	public ProcessDefinitionDto getProcessDefinition(String processDefinitionId) {
		RepositoryService repoService = engine.getRepositoryService();

		ProcessDefinition definition;
		try {
			definition = repoService.getProcessDefinition(processDefinitionId);
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e,
					"No matching definition with id " + processDefinitionId);
		}

		ProcessDefinitionDto result = ProcessDefinitionDto.fromProcessDefinition(definition);

		return result;
	}
	
	public ResponseEntity<?> deleteProcessDefinition(
			String processDefinitionId, 
			boolean cascade, 
			boolean skipCustomListeners, 
			boolean skipIoMappings) {
		RepositoryService repositoryService = engine.getRepositoryService();

		try {
			repositoryService.deleteProcessDefinition(processDefinitionId, cascade, skipCustomListeners,
					skipIoMappings);
		} catch (NotFoundException nfe) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, nfe, nfe.getMessage());
		}
		return ResponseEntity.ok().build();
	}

	public ProcessInstanceDto startProcessInstance(
			String processDefinitionId,
			HttpServletRequest request, 
			StartProcessInstanceDto parameters) {
		ProcessInstanceWithVariables instance = null;
		try {
			instance = startProcessInstanceAtActivities(parameters);
		} catch (AuthorizationException e) {
			throw e;

		} catch (ProcessEngineException e) {
			String errorMessage = String.format("Cannot instantiate process definition %s: %s", processDefinitionId,
					e.getMessage());
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, errorMessage);

		} catch (RestException e) {
			String errorMessage = String.format("Cannot instantiate process definition %s: %s", processDefinitionId,
					e.getMessage());
			throw new InvalidRequestException(e.getStatus(), e, errorMessage);

		}

		ProcessInstanceDto result;
		if (parameters.isWithVariablesInReturn()) {
			result = ProcessInstanceWithVariablesDto.fromProcessInstance(instance);
		} else {
			result = ProcessInstanceDto.fromProcessInstance(instance);
		}

		URI uri = UriComponentsBuilder.fromPath(rootResourcePath).path(ProcessInstanceRestService.PATH)
				.path(instance.getId()).build().toUri();

		result.addReflexiveLink(uri, HttpMethod.GET.name(), "self");

		return result;
	}

	public ProcessInstanceDto submitForm(
			String processDefinitionId,
			HttpServletRequest request, 
			StartProcessInstanceDto parameters) {
		FormService formService = engine.getFormService();

		ProcessInstance instance = null;
		try {
			Map<String, Object> variables = VariableValueDto.toMap(parameters.getVariables(), engine, objectMapper);
			String businessKey = parameters.getBusinessKey();
			if (businessKey != null) {
				instance = formService.submitStartForm(processDefinitionId, businessKey, variables);
			} else {
				instance = formService.submitStartForm(processDefinitionId, variables);
			}

		} catch (AuthorizationException e) {
			throw e;

		} catch (FormFieldValidationException e) {
			String errorMessage = String.format("Cannot instantiate process definition %s: %s", processDefinitionId,
					e.getMessage());
			throw new RestException(HttpStatus.BAD_REQUEST, e, errorMessage);

		} catch (ProcessEngineException e) {
			String errorMessage = String.format("Cannot instantiate process definition %s: %s", processDefinitionId,
					e.getMessage());
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, errorMessage);

		} catch (RestException e) {
			String errorMessage = String.format("Cannot instantiate process definition %s: %s", processDefinitionId,
					e.getMessage());
			throw new InvalidRequestException(e.getStatus(), e, errorMessage);

		}

		ProcessInstanceDto result = ProcessInstanceDto.fromProcessInstance(instance);

		URI uri = UriComponentsBuilder.fromPath(rootResourcePath).path(ProcessInstanceRestService.PATH)
				.path(instance.getId()).build().toUri();

		result.addReflexiveLink(uri, HttpMethod.GET.name(), "self");

		return result;
	}

	public List<StatisticsResultDto> getActivityStatistics(
			String processDefinitionId,
			Boolean includeFailedJobs, 
			Boolean includeIncidents,
			String includeIncidentsForType) {
		if (includeIncidents != null && includeIncidentsForType != null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
					"Only one of the query parameter includeIncidents or includeIncidentsForType can be set.");
		}

		ManagementService mgmtService = engine.getManagementService();
		ActivityStatisticsQuery query = mgmtService.createActivityStatisticsQuery(processDefinitionId);

		if (includeFailedJobs != null && includeFailedJobs) {
			query.includeFailedJobs();
		}

		if (includeIncidents != null && includeIncidents) {
			query.includeIncidents();
		} else if (includeIncidentsForType != null) {
			query.includeIncidentsForType(includeIncidentsForType);
		}

		List<ActivityStatistics> queryResults = query.list();

		List<StatisticsResultDto> results = new ArrayList<>();
		for (ActivityStatistics queryResult : queryResults) {
			StatisticsResultDto dto = ActivityStatisticsResultDto.fromActivityStatistics(queryResult);
			results.add(dto);
		}

		return results;
	}

	public ProcessDefinitionDiagramDto getProcessDefinitionBpmn20Xml(String processDefinitionId) {
		InputStream processModelIn = null;
		try {
			processModelIn = engine.getRepositoryService().getProcessModel(processDefinitionId);
			byte[] processModel = IoUtil.readInputStream(processModelIn, "processModelBpmn20Xml");
			return ProcessDefinitionDiagramDto.create(processDefinitionId, new String(processModel, "UTF-8"));
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e,
					"No matching definition with id " + processDefinitionId);
		} catch (UnsupportedEncodingException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e);
		} finally {
			IoUtil.closeSilently(processModelIn);
		}
	}

	public ResponseEntity<?> getProcessDefinitionDiagram(String processDefinitionId) {
		ProcessDefinition definition = engine.getRepositoryService().getProcessDefinition(processDefinitionId);
		InputStream processDiagram = engine.getRepositoryService().getProcessDiagram(processDefinitionId);
		if (processDiagram == null) {
			return ResponseEntity.noContent().build();
		} else {
			String fileName = definition.getDiagramResourceName();
			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + fileName)
					.contentType(MediaType.parseMediaType(getMediaTypeForFileSuffix(fileName))).body(new InputStreamResource(processDiagram));
		}
	}

	public FormDto getStartForm(String processDefinitionId) {
		final FormService formService = engine.getFormService();

		final StartFormData formData;
		try {
			formData = formService.getStartFormData(processDefinitionId);
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e,
					"Cannot get start form data for process definition " + processDefinitionId);
		}
		FormDto dto = FormDto.fromFormData(formData);
		if (dto.getKey() == null || dto.getKey().isEmpty()) {
			if (formData != null && formData.getFormFields() != null && !formData.getFormFields().isEmpty()) {
				dto.setKey("embedded:engine://engine/:engine/process-definition/" + processDefinitionId
						+ "/rendered-form");
			}
		}
		dto.setContextPath(
				ApplicationContextPathUtil.getApplicationPathByProcessDefinitionId(engine, processDefinitionId));

		return dto;
	}

	public ResponseEntity<Resource> getRenderedForm(String processDefinitionId) {
		FormService formService = engine.getFormService();

		Object startForm = formService.getRenderedStartForm(processDefinitionId);
		if (startForm != null) {
			String content = startForm.toString();
			InputStream stream = new ByteArrayInputStream(content.getBytes(EncodingUtil.DEFAULT_ENCODING));
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_XHTML_XML).body(new InputStreamResource(stream));
		}

		throw new InvalidRequestException(HttpStatus.NOT_FOUND,
				"No matching rendered start form for process definition with the id " + processDefinitionId
						+ " found.");
	}

	public void updateSuspensionState(
			String processDefinitionId,
			ProcessDefinitionSuspensionStateDto dto) {
		try {
			dto.setProcessDefinitionId(processDefinitionId);
			dto.updateSuspensionState(engine);

		} catch (IllegalArgumentException e) {
			String message = String.format(
					"The suspension state of Process Definition with id %s could not be updated due to: %s",
					processDefinitionId, e.getMessage());
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, message);
		}
	}

	public void updateHistoryTimeToLive(String processDefinitionId,
			HistoryTimeToLiveDto historyTimeToLiveDto) {
		engine.getRepositoryService().updateProcessDefinitionHistoryTimeToLive(processDefinitionId,
				historyTimeToLiveDto.getHistoryTimeToLive());
	}

	public Map<String, VariableValueDto> getFormVariables(
			String processDefinitionId,
			String variableNames, 
			boolean deserializeValues) {

		final FormService formService = engine.getFormService();
		List<String> formVariables = null;

		if (variableNames != null) {
			StringListConverter stringListConverter = new StringListConverter();
			formVariables = stringListConverter.convertQueryParameterToType(variableNames);
		}

		VariableMap startFormVariables = formService.getStartFormVariables(processDefinitionId, formVariables,
				deserializeValues);

		return VariableValueDto.fromMap(startFormVariables);
	}

	public void restartProcessInstance(
			String processDefinitionId,
			RestartProcessInstanceDto restartProcessInstanceDto) {
		try {
			createRestartProcessInstanceBuilder(restartProcessInstanceDto).execute();
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	
	public BatchDto restartProcessInstanceAsync(String processDefinitionId, RestartProcessInstanceDto restartProcessInstanceDto) {
		Batch batch = null;
		try {
			batch = createRestartProcessInstanceBuilder(restartProcessInstanceDto).executeAsync();
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return BatchDto.fromBatch(batch);
	}

	public ResponseEntity<Resource> getDeployedStartForm(String processDefinitionId) {
		InputStream deployedStartForm = null;
		try {
			deployedStartForm = engine.getFormService().getDeployedStartForm(processDefinitionId);
		} catch (NotFoundException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (NullValueException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (AuthorizationException e) {
			throw new InvalidRequestException(HttpStatus.FORBIDDEN, e.getMessage());
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_XHTML_XML).body(new InputStreamResource(deployedStartForm));
	}
	
	/**
	 * Determines an IANA media type based on the file suffix. Hint: as of Java 7
	 * the method Files.probeContentType() provides an implementation based on file
	 * type detection.
	 *
	 * @param fileName
	 * @return content type, defaults to octet-stream
	 */
	public static String getMediaTypeForFileSuffix(String fileName) {
		String mediaType = "application/octet-stream"; // default
		if (fileName != null) {
			fileName = fileName.toLowerCase();
			if (fileName.endsWith(".png")) {
				mediaType = "image/png";
			} else if (fileName.endsWith(".svg")) {
				mediaType = "image/svg+xml";
			} else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
				mediaType = "image/jpeg";
			} else if (fileName.endsWith(".gif")) {
				mediaType = "image/gif";
			} else if (fileName.endsWith(".bmp")) {
				mediaType = "image/bmp";
			}
		}
		return mediaType;
	}
	
	protected ProcessInstanceWithVariables startProcessInstanceAtActivities(StartProcessInstanceDto dto) {
		Map<String, Object> processInstanceVariables = VariableValueDto.toMap(dto.getVariables(), engine, objectMapper);
		String businessKey = dto.getBusinessKey();
		String caseInstanceId = dto.getCaseInstanceId();

		ProcessInstantiationBuilder instantiationBuilder = engine.getRuntimeService()
				.createProcessInstanceById(processDefinitionId).businessKey(businessKey).caseInstanceId(caseInstanceId)
				.setVariables(processInstanceVariables);

		if (dto.getStartInstructions() != null && !dto.getStartInstructions().isEmpty()) {
			for (ProcessInstanceModificationInstructionDto instruction : dto.getStartInstructions()) {
				instruction.applyTo(instantiationBuilder, engine, objectMapper);
			}
		}

		return instantiationBuilder.executeWithVariablesInReturn(dto.isSkipCustomListeners(), dto.isSkipIoMappings());
	}
	
	private RestartProcessInstanceBuilder createRestartProcessInstanceBuilder(
			RestartProcessInstanceDto restartProcessInstanceDto) {
		RuntimeService runtimeService = engine.getRuntimeService();
		RestartProcessInstanceBuilder builder = runtimeService.restartProcessInstances(processDefinitionId);

		if (restartProcessInstanceDto.getProcessInstanceIds() != null) {
			builder.processInstanceIds(restartProcessInstanceDto.getProcessInstanceIds());
		}

		if (restartProcessInstanceDto.getHistoricProcessInstanceQuery() != null) {
			builder.historicProcessInstanceQuery(
					restartProcessInstanceDto.getHistoricProcessInstanceQuery().toQuery(engine));
		}

		if (restartProcessInstanceDto.isInitialVariables()) {
			builder.initialSetOfVariables();
		}

		if (restartProcessInstanceDto.isWithoutBusinessKey()) {
			builder.withoutBusinessKey();
		}

		if (restartProcessInstanceDto.isSkipCustomListeners()) {
			builder.skipCustomListeners();
		}

		if (restartProcessInstanceDto.isSkipIoMappings()) {
			builder.skipIoMappings();
		}
		restartProcessInstanceDto.applyTo(builder, engine, objectMapper);
		return builder;
	}
	

	public ProcessDefinition getProcessDefinitionByKeyAndTenantId(String processDefinitionKey,
			String tenantId) {
		return this.engine.getRepositoryService().createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey).tenantIdIn(tenantId).latestVersion().singleResult();
	}
	
	public ProcessDefinition getProcessDefinitionByKey(String processDefinitionKey) {
		return this.engine.getRepositoryService().createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey).withoutTenantId().latestVersion().singleResult();
	}
}
