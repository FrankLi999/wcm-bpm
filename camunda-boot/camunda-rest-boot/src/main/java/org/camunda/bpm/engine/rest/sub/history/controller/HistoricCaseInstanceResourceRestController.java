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
import org.camunda.bpm.engine.history.HistoricCaseInstance;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.history.HistoricCaseInstanceDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.history.HistoricCaseInstanceRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.rest.sub.history.HistoricCaseInstanceResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(HistoryRestService.PATH + HistoricCaseInstanceRestService.PATH + "/{caseInstanceId}")
public class HistoricCaseInstanceResourceRestController extends AbstractRestProcessEngineAware implements HistoricCaseInstanceResource {
	public final static String PATH = "/camunda/api/engine/sub/history/case-instance";

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public HistoricCaseInstanceDto getHistoricCaseInstance(@PathVariable("caseInstanceId") String caseInstanceId) {
		HistoryService historyService = this.processEngine.getHistoryService();
		HistoricCaseInstance instance = historyService.createHistoricCaseInstanceQuery().caseInstanceId(caseInstanceId)
				.singleResult();

		if (instance == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Historic case instance with id '" + caseInstanceId + "' does not exist");
		}

		return HistoricCaseInstanceDto.fromHistoricCaseInstance(instance);
	}

}
