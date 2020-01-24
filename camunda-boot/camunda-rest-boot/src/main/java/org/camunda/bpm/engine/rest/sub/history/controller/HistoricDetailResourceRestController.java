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

import org.camunda.bpm.engine.history.HistoricDetail;
import org.camunda.bpm.engine.history.HistoricDetailQuery;
import org.camunda.bpm.engine.history.HistoricVariableUpdate;
import org.camunda.bpm.engine.query.Query;
import org.camunda.bpm.engine.rest.dto.history.HistoricDetailDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.history.HistoricDetailRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.rest.sub.AbstractResourceProvider;
import org.camunda.bpm.engine.rest.sub.history.HistoricDetailResource;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daniel Meyer
 * @author Ronny Bräunlich
 *
 */
@RestController
@RequestMapping(HistoryRestService.PATH + HistoricDetailRestService.PATH + "/{id}")
public class HistoricDetailResourceRestController
		extends AbstractResourceProvider<HistoricDetailQuery, HistoricDetail, HistoricDetailDto> 
        implements HistoricDetailResource {

	protected HistoricDetailQuery baseQuery(String detailId) {
		return this.processEngine.getHistoryService().createHistoricDetailQuery().detailId(detailId);
	}

	protected Query<HistoricDetailQuery, HistoricDetail> baseQueryForBinaryVariable(String detailId) {
		return baseQuery(detailId).disableCustomObjectDeserialization();
	}

	protected Query<HistoricDetailQuery, HistoricDetail> baseQueryForVariable(String detailId, boolean deserializeObjectValue) {
		HistoricDetailQuery query = baseQuery(detailId).disableBinaryFetching();

		if (!deserializeObjectValue) {
			query.disableCustomObjectDeserialization();
		}
		return query;
	}

	protected TypedValue transformQueryResultIntoTypedValue(String detailId, HistoricDetail queryResult) {
		if (!(queryResult instanceof HistoricVariableUpdate)) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
					"Historic detail with Id '" + detailId + "' is not a variable update.");
		}
		HistoricVariableUpdate update = (HistoricVariableUpdate) queryResult;
		return update.getTypedValue();
	}
	
	@Override
	protected HistoricDetailDto transformToDto(HistoricDetail queryResult) {
		return HistoricDetailDto.fromHistoricDetail(queryResult);
	}

	@Override
	protected String getResourceNameForErrorMessage() {
		return "Historic detail";
	}
}
