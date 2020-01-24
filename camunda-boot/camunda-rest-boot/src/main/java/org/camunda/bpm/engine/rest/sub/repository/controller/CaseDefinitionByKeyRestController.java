package org.camunda.bpm.engine.rest.sub.repository.controller;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.repository.CaseDefinition;
import org.camunda.bpm.engine.rest.CaseDefinitionRestService;
import org.camunda.bpm.engine.rest.dto.HistoryTimeToLiveDto;
import org.camunda.bpm.engine.rest.dto.repository.CaseDefinitionDiagramDto;
import org.camunda.bpm.engine.rest.dto.repository.CaseDefinitionDto;
import org.camunda.bpm.engine.rest.dto.runtime.CaseInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.CreateCaseInstanceDto;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.repository.service.CaseDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Roman Smirnov
 *
 */
@RestController
@RequestMapping(CaseDefinitionRestService.PATH + "/key/{key}")
public class CaseDefinitionByKeyRestController {
	
	@Autowired
	protected CaseDefinitionService caseDefinitionService;
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public CaseDefinitionDto getCaseDefinition(@PathVariable("key") String caseDefinitionKey) {
		return caseDefinitionService.getCaseDefinition(this.getCaseDefinitionId(caseDefinitionKey));
	}

	@GetMapping(path="/xml", produces=MediaType.APPLICATION_JSON_VALUE)
	public CaseDefinitionDiagramDto getCaseDefinitionCmmnXml(@PathVariable("key") String caseDefinitionKey) {
		return caseDefinitionService.getCaseDefinitionCmmnXml(this.getCaseDefinitionId(caseDefinitionKey));
	}

	@PostMapping(path="/create", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CaseInstanceDto createCaseInstance(@PathVariable("key") String caseDefinitionKey, HttpServletRequest Request, @RequestBody CreateCaseInstanceDto parameters) {
		return caseDefinitionService.createCaseInstance(this.getCaseDefinitionId(caseDefinitionKey), Request, parameters);
	}

	@GetMapping(path="/diagram", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCaseDefinitionDiagram(@PathVariable("key") String caseDefinitionKey) {
		return caseDefinitionService.getCaseDefinitionDiagram(this.getCaseDefinitionId(caseDefinitionKey));
	}

	@PutMapping(path="/history-time-to-live", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateHistoryTimeToLive(@PathVariable("key") String caseDefinitionKey, @RequestBody HistoryTimeToLiveDto historyTimeToLiveDto) {
		caseDefinitionService.updateHistoryTimeToLive(this.getCaseDefinitionId(caseDefinitionKey), historyTimeToLiveDto);
	}
	
	protected String getCaseDefinitionId(String caseDefinitionKey) {
		CaseDefinition caseDefinition = caseDefinitionService.getCaseDefinitionByCaseDefinitionKey(caseDefinitionKey);
		if (caseDefinition == null) {
			String errorMessage = String.format("No matching case definition with key: %s and no tenant-id",
					caseDefinitionKey);
			throw new RestException(HttpStatus.NOT_FOUND, errorMessage);

		} else {
			return caseDefinition.getId();
		}
	}
}
