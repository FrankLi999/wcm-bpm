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
package org.camunda.bpm.engine.rest.sub.batch.controller;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.rest.BatchRestService;
import org.camunda.bpm.engine.rest.dto.SuspensionStateDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.sub.batch.BatchResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="batchResourceRestControlerSub")
@RequestMapping(BatchRestService.PATH + "/{batchId}")
public class BatchResourceRestControler implements BatchResource {

	@Autowired
	protected ProcessEngine processEngine;

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto getBatch(@PathVariable("batchId") String batchId) {
		Batch batch = processEngine.getManagementService().createBatchQuery().batchId(batchId).singleResult();

		if (batch == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "Batch with id '" + batchId + "' does not exist");
		}

		return BatchDto.fromBatch(batch);
	}

	@PutMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateSuspensionState(@PathVariable("batchId") String batchId, @RequestBody SuspensionStateDto suspensionState) {
		if (suspensionState.getSuspended()) {
			suspendBatch(batchId);
		} else {
			activateBatch(batchId);
		}
	}

	@DeleteMapping(path="/")
	public void deleteBatch(@PathVariable("batchId") String batchId, @RequestParam("cascade") boolean cascade) {
		try {
			processEngine.getManagementService().deleteBatch(batchId, cascade);
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e,
					"Unable to delete batch with id '" + batchId + "'");
		}
	}
	
	protected void suspendBatch(String batchId) {
		try {
			processEngine.getManagementService().suspendBatchById(batchId);
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e,
					"Unable to suspend batch with id '" + batchId + "'");
		}
	}

	protected void activateBatch(String batchId) {
		try {
			processEngine.getManagementService().activateBatchById(batchId);
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e,
					"Unable to activate batch with id '" + batchId + "'");
		}
	}
}
