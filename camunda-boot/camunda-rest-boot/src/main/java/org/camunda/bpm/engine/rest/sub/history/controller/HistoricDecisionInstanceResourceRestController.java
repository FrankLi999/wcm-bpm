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

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.history.HistoricDecisionInstance;
import org.camunda.bpm.engine.history.HistoricDecisionInstanceQuery;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.history.HistoricDecisionInstanceDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.history.HistoricDecisionInstanceRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.rest.sub.history.HistoricDecisionInstanceResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(HistoryRestService.PATH + HistoricDecisionInstanceRestService.PATH + "/{decisionInstanceId}")
public class HistoricDecisionInstanceResourceRestController extends AbstractRestProcessEngineAware implements HistoricDecisionInstanceResource {
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public HistoricDecisionInstanceDto getHistoricDecisionInstance(
			@PathVariable("decisionInstanceId") String decisionInstanceId, 
			@RequestParam("includeInputs") Boolean includeInputs, 
			@RequestParam("includeOutputs") Boolean includeOutputs,
			@RequestParam("disableBinaryFetching") Boolean disableBinaryFetching, 
			@RequestParam("disableCustomObjectDeserialization") Boolean disableCustomObjectDeserialization) {
		HistoryService historyService = this.processEngine.getHistoryService();

		HistoricDecisionInstanceQuery query = historyService.createHistoricDecisionInstanceQuery()
				.decisionInstanceId(decisionInstanceId);
		if (includeInputs != null && includeInputs) {
			query.includeInputs();
		}
		if (includeOutputs != null && includeOutputs) {
			query.includeOutputs();
		}
		if (disableBinaryFetching != null && disableBinaryFetching) {
			query.disableBinaryFetching();
		}
		if (disableCustomObjectDeserialization != null && disableCustomObjectDeserialization) {
			query.disableCustomObjectDeserialization();
		}

		HistoricDecisionInstance instance = query.singleResult();

		if (instance == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Historic decision instance with id '" + decisionInstanceId + "' does not exist");
		}

		return HistoricDecisionInstanceDto.fromHistoricDecisionInstance(instance);
	}
}
