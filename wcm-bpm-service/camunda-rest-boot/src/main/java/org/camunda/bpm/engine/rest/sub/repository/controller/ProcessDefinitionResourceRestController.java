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
package org.camunda.bpm.engine.rest.sub.repository.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.camunda.bpm.engine.rest.sub.VariableResource;
import org.camunda.bpm.engine.rest.sub.repository.ProcessDefinitionResource;
import org.camunda.bpm.engine.rest.sub.repository.service.ProcessDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
@RequestMapping(ProcessDefinitionRestService.PATH + "/{processDefinitionId}")
public class ProcessDefinitionResourceRestController implements ProcessDefinitionResource {

	@Autowired
	protected ProcessDefinitionService processDefinitionService;

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public ProcessDefinitionDto getProcessDefinition(@PathVariable("processDefinitionId") String processDefinitionId) {
		return this.processDefinitionService.getProcessDefinition(processDefinitionId);
	}
	
	@DeleteMapping(path="/")
	public ResponseEntity<?> deleteProcessDefinition(
			@PathVariable("processDefinitionId") String processDefinitionId, 
			@RequestParam("cascade") boolean cascade, 
			@RequestParam("skipCustomListeners") boolean skipCustomListeners, 
			@RequestParam("skipIoMappings") boolean skipIoMappings) {
		return this.processDefinitionService.deleteProcessDefinition(processDefinitionId, cascade, skipCustomListeners, skipIoMappings);
	}

	@PostMapping(path="/start", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ProcessInstanceDto startProcessInstance(
			@PathVariable("processDefinitionId") String processDefinitionId,
			HttpServletRequest request, 
			@RequestBody StartProcessInstanceDto parameters) {
		return this.processDefinitionService.startProcessInstance(processDefinitionId, request, parameters);
	}

	@PostMapping(path="/submit-form", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ProcessInstanceDto submitForm(
			@PathVariable("processDefinitionId") String processDefinitionId,
			HttpServletRequest request, 
			@RequestBody StartProcessInstanceDto parameters) {
		return this.processDefinitionService.submitForm(processDefinitionId, request, parameters);
	}

	@GetMapping(path="/statistics", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<StatisticsResultDto> getActivityStatistics(
			@PathVariable("processDefinitionId") String processDefinitionId,
			@RequestParam("failedJobs") Boolean includeFailedJobs, 
			@RequestParam("incidents") Boolean includeIncidents,
			@RequestParam("incidentsForType") String includeIncidentsForType) {
		return this.processDefinitionService.getActivityStatistics(processDefinitionId, includeFailedJobs, includeIncidents, includeIncidentsForType);
	}

	@GetMapping(path="/xml", produces=MediaType.APPLICATION_JSON_VALUE)
	public ProcessDefinitionDiagramDto getProcessDefinitionBpmn20Xml(@PathVariable("processDefinitionId") String processDefinitionId) {
		return this.processDefinitionService.getProcessDefinitionBpmn20Xml(processDefinitionId);
	}

	@GetMapping(path="/diagram", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProcessDefinitionDiagram(@PathVariable("processDefinitionId") String processDefinitionId) {
		return this.processDefinitionService.getProcessDefinitionDiagram(processDefinitionId);
	}

	@GetMapping(path="/startForm", produces=MediaType.APPLICATION_JSON_VALUE)
	public FormDto getStartForm(@PathVariable("processDefinitionId") String processDefinitionId) {
		return this.processDefinitionService.getStartForm(processDefinitionId);
	}

	@GetMapping(path="/rendered-form", produces=MediaType.APPLICATION_XHTML_XML_VALUE)
	public ResponseEntity<Resource> getRenderedForm(@PathVariable("processDefinitionId") String processDefinitionId) {
		return this.processDefinitionService.getRenderedForm(processDefinitionId);
	}

	@PutMapping(path="/suspended", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateSuspensionState(
			@PathVariable("processDefinitionId") String processDefinitionId,
			@RequestBody ProcessDefinitionSuspensionStateDto dto) {
		this.processDefinitionService.updateSuspensionState(processDefinitionId, dto);
	}

	@PutMapping(path="/history-time-to-live", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateHistoryTimeToLive(@PathVariable("processDefinitionId") String processDefinitionId,
			@RequestBody HistoryTimeToLiveDto historyTimeToLiveDto) {
		this.processDefinitionService.updateHistoryTimeToLive(processDefinitionId, historyTimeToLiveDto);
	}

	@GetMapping(path="/form-variables", produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, VariableValueDto> getFormVariables(
			@PathVariable("processDefinitionId") String processDefinitionId,
			@RequestParam("variableNames") String variableNames, 
			@RequestParam(name=VariableResource.DESERIALIZE_VALUES_QUERY_PARAM, defaultValue="true") boolean deserializeValues) {

		return this.processDefinitionService.getFormVariables(processDefinitionId, variableNames, deserializeValues);
	}

	@PostMapping(path="restart", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void restartProcessInstance(
			@PathVariable("processDefinitionId") String processDefinitionId,
			@RequestBody RestartProcessInstanceDto restartProcessInstanceDto) {
		this.processDefinitionService.restartProcessInstance(processDefinitionId, restartProcessInstanceDto);
	}

	
	@PostMapping(path="/restart-async", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto restartProcessInstanceAsync(
			@PathVariable("processDefinitionId") String processDefinitionId,
			@RequestBody RestartProcessInstanceDto restartProcessInstanceDto) {
		return this.processDefinitionService.restartProcessInstanceAsync(processDefinitionId, restartProcessInstanceDto);
	}

	@GetMapping(path="/deployed-start-form", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource> getDeployedStartForm(
			@PathVariable("processDefinitionId") String processDefinitionId) {
		return this.processDefinitionService.getDeployedStartForm(processDefinitionId);
	}
}
