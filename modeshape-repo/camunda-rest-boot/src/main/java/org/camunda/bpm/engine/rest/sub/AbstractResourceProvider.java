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
package org.camunda.bpm.engine.rest.sub;

import org.camunda.bpm.engine.query.Query;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.sub.impl.VariableResponseProvider;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Base class to unify the getResource(boolean deserialized) and
 * getResourceBinary() methods for several subclasses. (formerly getVariable()
 * and getBinaryVariable())
 *
 * @author Ronny Bräunlich
 *
 */
public abstract class AbstractResourceProvider<T extends Query<?, U>, U, DTO> extends AbstractRestProcessEngineAware {

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public DTO getResource(
			@PathVariable("id") String id,
			@RequestParam(name=VariableResource.DESERIALIZE_VALUE_QUERY_PARAM, defaultValue="true") boolean deserializeObjectValue) {
		U variableInstance = baseQueryForVariable(id, deserializeObjectValue).singleResult();
		if (variableInstance != null) {
			return transformToDto(variableInstance);
		} else {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					getResourceNameForErrorMessage() + " with Id '" + id + "' does not exist.");
		}
	}

	@GetMapping(path="/data", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getResourceBinary(@PathVariable("id") String id) {
		U queryResult = baseQueryForBinaryVariable(id).singleResult();
		if (queryResult != null) {
			TypedValue variableInstance = transformQueryResultIntoTypedValue(id, queryResult);
			return new VariableResponseProvider().getResponseForTypedVariable(variableInstance, id);
		} else {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					getResourceNameForErrorMessage() + " with Id '" + id + "' does not exist.");
		}
	}

	/**
	 * Create the query we need for fetching the desired result. Setting properties
	 * in the query like disableCustomObjectDeserialization() or
	 * disableBinaryFetching() should be done in this method.
	 */
	protected abstract Query<T, U> baseQueryForBinaryVariable(String id);

	/**
	 * TODO change comment Create the query we need for fetching the desired result.
	 * Setting properties in the query like disableCustomObjectDeserialization() or
	 * disableBinaryFetching() should be done in this method.
	 *
	 * @param deserializeObjectValue
	 */
	protected abstract Query<T, U> baseQueryForVariable(String id, boolean deserializeObjectValue);

	protected abstract TypedValue transformQueryResultIntoTypedValue(String id, U queryResult);

	protected abstract DTO transformToDto(U queryResult);

	protected abstract String getResourceNameForErrorMessage();

}
