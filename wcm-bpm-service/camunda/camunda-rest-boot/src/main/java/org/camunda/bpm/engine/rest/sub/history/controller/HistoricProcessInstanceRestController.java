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
package org.camunda.bpm.engine.rest.sub.history.controller;

import org.camunda.bpm.engine.AuthorizationException;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.history.HistoricProcessInstanceDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.history.HistoricProcessInstanceRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.rest.sub.history.HistoricProcessInstanceResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(HistoryRestService.PATH + HistoricProcessInstanceRestService.PATH + "/{processInstanceId}")
public class HistoricProcessInstanceRestController extends AbstractRestProcessEngineAware  implements HistoricProcessInstanceResource {
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public HistoricProcessInstanceDto getHistoricProcessInstance(@PathVariable("processInstanceId") String processInstanceId) {
		HistoryService historyService = this.processEngine.getHistoryService();
		HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		if (instance == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Historic process instance with id " + processInstanceId + " does not exist");
		}
		return HistoricProcessInstanceDto.fromHistoricProcessInstance(instance);
	}

	@DeleteMapping(path="/")
	public void deleteHistoricProcessInstance(
			@PathVariable("processInstanceId") String processInstanceId, 
			@RequestParam(name="failIfNotExists", defaultValue="true") Boolean failIfNotExists) {
		HistoryService historyService = this.processEngine.getHistoryService();
		try {
			if (failIfNotExists == null || failIfNotExists) {
				historyService.deleteHistoricProcessInstance(processInstanceId);
			} else {
				historyService.deleteHistoricProcessInstanceIfExists(processInstanceId);
			}
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e,
					"Historic process instance with id " + processInstanceId + " does not exist");
		}
	}
}
