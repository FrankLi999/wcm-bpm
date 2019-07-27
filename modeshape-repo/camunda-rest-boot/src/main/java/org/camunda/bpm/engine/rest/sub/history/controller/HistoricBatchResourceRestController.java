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

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.batch.history.HistoricBatch;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.history.batch.HistoricBatchDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.history.HistoricBatchRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.rest.sub.history.HistoricBatchResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(HistoryRestService.PATH + HistoricBatchRestService.PATH + "/{batchId}")
public class HistoricBatchResourceRestController extends AbstractRestProcessEngineAware implements HistoricBatchResource {
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public HistoricBatchDto getHistoricBatch(@PathVariable("batchId") String batchId) {
		HistoricBatch batch = processEngine.getHistoryService().createHistoricBatchQuery().batchId(batchId)
				.singleResult();

		if (batch == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Historic batch with id '" + batchId + "' does not exist");
		}

		return HistoricBatchDto.fromBatch(batch);
	}

	@DeleteMapping(path="/")
	public void deleteHistoricBatch(@PathVariable("batchId") String batchId) {
		try {
			processEngine.getHistoryService().deleteHistoricBatch(batchId);
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e,
					"Unable to delete historic batch with id '" + batchId + "'");
		}
	}
}
