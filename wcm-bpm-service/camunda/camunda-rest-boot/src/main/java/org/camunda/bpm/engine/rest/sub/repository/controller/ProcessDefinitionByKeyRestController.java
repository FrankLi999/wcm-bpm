package org.camunda.bpm.engine.rest.sub.repository.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.rest.ProcessDefinitionRestService;
import org.camunda.bpm.engine.rest.dto.HistoryTimeToLiveDto;
import org.camunda.bpm.engine.rest.dto.StatisticsResultDto;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDiagramDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionSuspensionStateDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.RestartProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.StartProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.task.FormDto;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.VariableResource;
import org.camunda.bpm.engine.rest.sub.repository.service.ProcessDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ProcessDefinitionRestService.PATH + "/key/{key}")
public class ProcessDefinitionByKeyRestController {

	@Autowired
	protected ProcessDefinitionService processDefinitionService;

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public ProcessDefinitionDto getProcessDefinition(@PathVariable("key") String processDefinitionKey) {
		return this.processDefinitionService.getProcessDefinition(this.getProcessDefinitionId(processDefinitionKey));
	}
	
	@DeleteMapping(path="/")
	public ResponseEntity<?> deleteProcessDefinition(
			@PathVariable("key") String processDefinitionKey, 
			@RequestParam("cascade") boolean cascade, 
			@RequestParam("skipCustomListeners") boolean skipCustomListeners, 
			@RequestParam("skipIoMappings") boolean skipIoMappings) {
		return this.processDefinitionService.deleteProcessDefinition(this.getProcessDefinitionId(processDefinitionKey), cascade, skipCustomListeners, skipIoMappings);
	}

	@PostMapping(path="/start", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ProcessInstanceDto startProcessInstance(
			@PathVariable("key") String processDefinitionKey,
			HttpServletRequest request, 
			@RequestBody StartProcessInstanceDto parameters) {
		return this.processDefinitionService.startProcessInstance(this.getProcessDefinitionId(processDefinitionKey), request, parameters);
	}

	@PostMapping(path="/submit-form", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ProcessInstanceDto submitForm(
			@PathVariable("key") String processDefinitionKey,
			HttpServletRequest request, 
			@RequestBody StartProcessInstanceDto parameters) {
		return this.processDefinitionService.submitForm(this.getProcessDefinitionId(processDefinitionKey), request, parameters);
	}

	@GetMapping(path="/statistics", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<StatisticsResultDto> getActivityStatistics(
			@PathVariable("key") String processDefinitionKey,
			@RequestParam("failedJobs") Boolean includeFailedJobs, 
			@RequestParam("incidents") Boolean includeIncidents,
			@RequestParam("incidentsForType") String includeIncidentsForType) {
		return this.processDefinitionService.getActivityStatistics(this.getProcessDefinitionId(processDefinitionKey), includeFailedJobs, includeIncidents, includeIncidentsForType);
	}

	@GetMapping(path="/xml", produces=MediaType.APPLICATION_JSON_VALUE)
	public ProcessDefinitionDiagramDto getProcessDefinitionBpmn20Xml(
			@PathVariable("key") String processDefinitionKey) {
		return this.processDefinitionService.getProcessDefinitionBpmn20Xml(this.getProcessDefinitionId(processDefinitionKey));
	}

	@GetMapping(path="/diagram", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProcessDefinitionDiagram(
			@PathVariable("key") String processDefinitionKey) {
		return this.processDefinitionService.getProcessDefinitionDiagram(this.getProcessDefinitionId(processDefinitionKey));
	}

	@GetMapping(path="/startForm", produces=MediaType.APPLICATION_JSON_VALUE)
	public FormDto getStartForm(@PathVariable("key") String processDefinitionKey) {
		return this.processDefinitionService.getStartForm(this.getProcessDefinitionId(processDefinitionKey));
	}

	@GetMapping(path="/rendered-form", produces=MediaType.APPLICATION_XHTML_XML_VALUE)
	public ResponseEntity<Resource> getRenderedForm(@PathVariable("key") String processDefinitionKey) {
		return this.processDefinitionService.getRenderedForm(this.getProcessDefinitionId(processDefinitionKey));
	}

	@PutMapping(path="/suspended", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateSuspensionState(
			@PathVariable("key") String processDefinitionKey,
			@RequestBody ProcessDefinitionSuspensionStateDto dto) {
		this.processDefinitionService.updateSuspensionState(this.getProcessDefinitionId(processDefinitionKey), dto);
	}

	@PutMapping(path="/history-time-to-live", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateHistoryTimeToLive(@PathVariable("key") String processDefinitionKey,
			@RequestBody HistoryTimeToLiveDto historyTimeToLiveDto) {
		this.processDefinitionService.updateHistoryTimeToLive(this.getProcessDefinitionId(processDefinitionKey), historyTimeToLiveDto);
	}

	@GetMapping(path="/form-variables", produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, VariableValueDto> getFormVariables(
			@PathVariable("key") String processDefinitionKey,
			@RequestParam("variableNames") String variableNames, 
			@RequestParam(name=VariableResource.DESERIALIZE_VALUES_QUERY_PARAM, defaultValue="true") boolean deserializeValues) {

		return this.processDefinitionService.getFormVariables(this.getProcessDefinitionId(processDefinitionKey), variableNames, deserializeValues);
	}

	@PostMapping(path="restart", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void restartProcessInstance(
			@PathVariable("key") String processDefinitionKey,
			@RequestBody RestartProcessInstanceDto restartProcessInstanceDto) {
		this.processDefinitionService.restartProcessInstance(this.getProcessDefinitionId(processDefinitionKey), restartProcessInstanceDto);
	}

	
	@PostMapping(path="/restart-async", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto restartProcessInstanceAsync(
			@PathVariable("key") String processDefinitionKey,
			@RequestBody RestartProcessInstanceDto restartProcessInstanceDto) {
		return this.processDefinitionService.restartProcessInstanceAsync(this.getProcessDefinitionId(processDefinitionKey), restartProcessInstanceDto);
	}

	@GetMapping(path="/deployed-start-form", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource> getDeployedStartForm(@PathVariable("key") String processDefinitionKey) {
		return this.processDefinitionService.getDeployedStartForm(this.getProcessDefinitionId(processDefinitionKey));
	}
	
	protected String getProcessDefinitionId(String processDefinitionKey) {
		ProcessDefinition processDefinition = this.processDefinitionService.getProcessDefinitionByKey(processDefinitionKey);
		if (processDefinition == null) {
			String errorMessage = String.format("No matching process definition with key: %s and no tenant-id",
					processDefinitionKey);
			throw new RestException(HttpStatus.NOT_FOUND, errorMessage);

		} 
		return processDefinition.getId();
	}
}
