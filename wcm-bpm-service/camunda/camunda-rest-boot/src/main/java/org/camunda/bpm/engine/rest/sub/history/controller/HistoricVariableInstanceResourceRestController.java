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

import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstanceQuery;
import org.camunda.bpm.engine.query.Query;
import org.camunda.bpm.engine.rest.dto.history.HistoricVariableInstanceDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.history.HistoricVariableInstanceRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.rest.sub.AbstractResourceProvider;
import org.camunda.bpm.engine.rest.sub.history.HistoricVariableInstanceResource;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daniel Meyer
 * @author Ronny Bräunlich
 *
 */
@RestController
@RequestMapping(HistoryRestService.PATH + HistoricVariableInstanceRestService.PATH+ "/{id}")
public class HistoricVariableInstanceResourceRestController extends
		AbstractResourceProvider<HistoricVariableInstanceQuery, HistoricVariableInstance, HistoricVariableInstanceDto> 
        implements HistoricVariableInstanceResource {

	protected HistoricVariableInstanceQuery baseQuery(String id) {
		return this.processEngine.getHistoryService().createHistoricVariableInstanceQuery().variableId(id);
	}

	protected Query<HistoricVariableInstanceQuery, HistoricVariableInstance> baseQueryForBinaryVariable(String id) {
		return baseQuery(id).disableCustomObjectDeserialization();
	}

	protected Query<HistoricVariableInstanceQuery, HistoricVariableInstance> baseQueryForVariable(
			String id, boolean deserializeObjectValue) {
		HistoricVariableInstanceQuery query = baseQuery(id).disableBinaryFetching();

		if (!deserializeObjectValue) {
			query.disableCustomObjectDeserialization();
		}
		return query;
	}

	protected TypedValue transformQueryResultIntoTypedValue(String id, HistoricVariableInstance queryResult) {
		return queryResult.getTypedValue();
	}

	protected HistoricVariableInstanceDto transformToDto(HistoricVariableInstance queryResult) {
		return HistoricVariableInstanceDto.fromHistoricVariableInstance(queryResult);
	}

	protected String getResourceNameForErrorMessage() {
		return "Historic variable instance";
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteVariableInstance(@PathVariable("id") String id) {
		try {
			this.processEngine.getHistoryService().deleteHistoricVariableInstance(id);
		} catch (NotFoundException nfe) { // rewrite status code from bad request (400) to not found (404)
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, nfe, nfe.getMessage());
		}
		// return no content (204) since resource is deleted
		return ResponseEntity.noContent().build();
	}

}
