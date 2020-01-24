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
import org.camunda.bpm.engine.history.HistoricJobLog;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.history.HistoricJobLogDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.history.HistoricJobLogRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.rest.sub.history.HistoricJobLogResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Roman Smirnov
 *
 */
@RestController
@RequestMapping(HistoryRestService.PATH + HistoricJobLogRestService.PATH + "/{id}")
public class HistoricJobLogResourceRestController extends AbstractRestProcessEngineAware implements HistoricJobLogResource {

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public HistoricJobLogDto getHistoricJobLog(@PathVariable("id") String id) {
		HistoryService historyService = this.processEngine.getHistoryService();
		HistoricJobLog historicJobLog = historyService.createHistoricJobLogQuery().logId(id).singleResult();

		if (historicJobLog == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "Historic job log with id " + id + " does not exist");
		}

		return HistoricJobLogDto.fromHistoricJobLog(historicJobLog);
	}

	@GetMapping(path="/stacktrace", produces=MediaType.APPLICATION_JSON_VALUE)
	public String getStacktrace(@PathVariable("id") String id) {
		try {
			HistoryService historyService = this.processEngine.getHistoryService();
			String stacktrace = historyService.getHistoricJobLogExceptionStacktrace(id);
			return stacktrace;
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

}
