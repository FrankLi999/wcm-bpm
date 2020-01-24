package org.camunda.bpm.engine.rest.sub.repository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProcessDefinitionResource {
	ProcessDefinitionDto getProcessDefinition(String processDefinitionId);
	
	ResponseEntity<?> deleteProcessDefinition(
			String processDefinitionId, 
			boolean cascade, 
			boolean skipCustomListeners, 
			boolean skipIoMappings);
	
	ProcessInstanceDto startProcessInstance(
			String processDefinitionId,
			HttpServletRequest request, 
			StartProcessInstanceDto parameters);

	ProcessInstanceDto submitForm(
			String processDefinitionId,
			HttpServletRequest request, 
			StartProcessInstanceDto parameters);

	List<StatisticsResultDto> getActivityStatistics(
			String processDefinitionId,
			Boolean includeFailedJobs, 
			Boolean includeIncidents,
			String includeIncidentsForType);

	ProcessDefinitionDiagramDto getProcessDefinitionBpmn20Xml(String processDefinitionId);

	ResponseEntity<?> getProcessDefinitionDiagram(String processDefinitionId);

	FormDto getStartForm(String processDefinitionId);

	ResponseEntity<Resource> getRenderedForm(String processDefinitionId);

	void updateSuspensionState(
			String processDefinitionId,
			ProcessDefinitionSuspensionStateDto dto);

	void updateHistoryTimeToLive(String processDefinitionId,
			HistoryTimeToLiveDto historyTimeToLiveDto);
	
	Map<String, VariableValueDto> getFormVariables(
			String processDefinitionId,
			String variableNames, 
			boolean deserializeValues);

	void restartProcessInstance(
			String processDefinitionId,
			RestartProcessInstanceDto restartProcessInstanceDto);
	
	BatchDto restartProcessInstanceAsync(
			String processDefinitionId,
			RestartProcessInstanceDto restartProcessInstanceDto);

	ResponseEntity<Resource> getDeployedStartForm(String processDefinitionId);
}
