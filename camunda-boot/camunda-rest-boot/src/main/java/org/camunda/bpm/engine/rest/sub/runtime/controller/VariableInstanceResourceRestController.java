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
package org.camunda.bpm.engine.rest.sub.runtime.controller;

import org.camunda.bpm.engine.query.Query;
import org.camunda.bpm.engine.rest.VariableInstanceRestService;
import org.camunda.bpm.engine.rest.dto.runtime.VariableInstanceDto;
import org.camunda.bpm.engine.rest.sub.AbstractResourceProvider;
import org.camunda.bpm.engine.rest.sub.runtime.VariableInstanceResource;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.camunda.bpm.engine.runtime.VariableInstanceQuery;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daniel Meyer
 * @author Ronny Bräunlich
 *
 */
@RestController
@RequestMapping(VariableInstanceRestService.PATH + "/{id}")
public class VariableInstanceResourceRestController
		extends AbstractResourceProvider<VariableInstanceQuery, VariableInstance, VariableInstanceDto> 
        implements VariableInstanceResource {

	public final static String PATH = "/camunda/api/engine/sub/runtime/variable-instance";
	
	protected VariableInstanceQuery baseQuery(String variableId) {
		return this.processEngine.getRuntimeService().createVariableInstanceQuery().variableId(variableId);
	}

	@Override
	protected Query<VariableInstanceQuery, VariableInstance> baseQueryForBinaryVariable(String variableId) {
		return baseQuery(variableId).disableCustomObjectDeserialization();
	}

	@Override
	protected Query<VariableInstanceQuery, VariableInstance> baseQueryForVariable(String variableId, boolean deserializeObjectValue) {
		VariableInstanceQuery baseQuery = baseQuery(variableId);

		// do not fetch byte arrays
		baseQuery.disableBinaryFetching();

		if (!deserializeObjectValue) {
			baseQuery.disableCustomObjectDeserialization();
		}
		return baseQuery;
	}

	@Override
	protected TypedValue transformQueryResultIntoTypedValue(String variableId, VariableInstance queryResult) {
		return queryResult.getTypedValue();
	}

	@Override
	protected VariableInstanceDto transformToDto(VariableInstance queryResult) {
		return VariableInstanceDto.fromVariableInstance(queryResult);
	}

	@Override
	protected String getResourceNameForErrorMessage() {
		return "Variable instance";
	}

}
