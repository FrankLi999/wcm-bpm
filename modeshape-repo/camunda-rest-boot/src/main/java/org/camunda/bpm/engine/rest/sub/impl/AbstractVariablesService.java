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
package org.camunda.bpm.engine.rest.sub.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.AuthorizationException;
import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.rest.dto.PatchVariablesDto;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.VariableResource;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public abstract class AbstractVariablesService implements VariableResource {

	protected static final String DEFAULT_BINARY_VALUE_TYPE = "Bytes";

	@Autowired
	protected ProcessEngine engine;
	// protected String resourceId;
	protected ObjectMapper objectMapper = new ObjectMapper();

	public AbstractVariablesService() {}

	@Override
	public Map<String, VariableValueDto> getVariables(String resourceId, boolean deserializeValues) {

		VariableMap variables = getVariableEntities(resourceId, deserializeValues);

		return VariableValueDto.fromMap(variables);
	}

	@Override
	public VariableValueDto getVariable(String resourceId, String variableName, boolean deserializeValue) {
		TypedValue value = getTypedValueForVariable(resourceId, variableName, deserializeValue);
		return VariableValueDto.fromTypedValue(value);

	}

	protected TypedValue getTypedValueForVariable(String resourceId, String variableName, boolean deserializeValue) {
		TypedValue value = null;
		try {
			value = getVariableEntity(resourceId, variableName, deserializeValue);
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			String errorMessage = String.format("Cannot get %s variable %s: %s", getResourceTypeName(), variableName,
					e.getMessage());
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, errorMessage);
		}

		if (value == null) {
			String errorMessage = String.format("%s variable with name %s does not exist", getResourceTypeName(),
					variableName);
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, errorMessage);
		}
		return value;
	}

	public ResponseEntity<?> getVariableBinary(String resourceId, String variableName) {
		TypedValue typedValue = getTypedValueForVariable(resourceId, variableName, false);
		return new VariableResponseProvider().getResponseForTypedVariable(typedValue, resourceId);
	}

	@Override
	public void putVariable(String resourceId, String variableName, VariableValueDto variable) {

		try {
			TypedValue typedValue = variable.toTypedValue(engine, objectMapper);
			setVariableEntity(resourceId, variableName, typedValue);

		} catch (RestException e) {
			throw new InvalidRequestException(e.getStatus(), e, String.format("Cannot put %s variable %s: %s",
					getResourceTypeName(), variableName, e.getMessage()));
		} catch (BadUserRequestException e) {
			throw new RestException(HttpStatus.BAD_REQUEST, e, String.format("Cannot put %s variable %s: %s",
					getResourceTypeName(), variableName, e.getMessage()));
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, String.format("Cannot put %s variable %s: %s",
					getResourceTypeName(), variableName, e.getMessage()));
		}
	}

	public void setBinaryVariable(String resourceId, String variableKey, String objectType, String valueType, MultipartFile dataFile) throws IOException {
		if (StringUtils.hasText(objectType)) {
			Object object = null;

			if (dataFile.getContentType() != null
					&& dataFile.getContentType().toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE)) {

				object = deserializeJsonObject(objectType, dataFile.getBytes());

			} else {
				throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
						"Unrecognized content type for serialized java type: " + dataFile.getContentType());
			}

			if (object != null) {
				setVariableEntity(resourceId, variableKey, Variables.objectValue(object).create());
			}
		} else {

			String valueTypeName = DEFAULT_BINARY_VALUE_TYPE;
			if (StringUtils.isEmpty(valueType)) {
				throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
							"Form part with name 'valueType' must have a text/plain value");
			}
			valueTypeName = valueType;
			VariableValueDto valueDto = VariableValueDto.fromMultipartFile(valueTypeName, dataFile);
			try {

				TypedValue typedValue = valueDto.toTypedValue(engine, objectMapper);
				setVariableEntity(resourceId, variableKey, typedValue);
			} catch (AuthorizationException e) {
				throw e;
			} catch (ProcessEngineException e) {
				String errorMessage = String.format("Cannot put %s variable %s: %s", getResourceTypeName(), variableKey,
						e.getMessage());
				throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, errorMessage);
			}
		}
	}

	protected Object deserializeJsonObject(String className, byte[] data) {
		try {
			JavaType type = TypeFactory.defaultInstance().constructFromCanonical(className);

			return objectMapper.readValue(new String(data, Charset.forName("UTF-8")), type);

		} catch (Exception e) {
			throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Could not deserialize JSON object: " + e.getMessage());

		}
	}

	@Override
	public void deleteVariable(String resourceId, String variableName) {
		try {
			removeVariableEntity(resourceId, variableName);
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			String errorMessage = String.format("Cannot delete %s variable %s: %s", getResourceTypeName(), variableName,
					e.getMessage());
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, errorMessage);
		}

	}

	@Override
	public void modifyVariables(String resourceId, PatchVariablesDto patch) {
		VariableMap variableModifications = null;
		try {
			variableModifications = VariableValueDto.toMap(patch.getModifications(), engine, objectMapper);

		} catch (RestException e) {
			String errorMessage = String.format("Cannot modify variables for %s: %s", getResourceTypeName(),
					e.getMessage());
			throw new InvalidRequestException(e.getStatus(), e, errorMessage);

		}

		List<String> variableDeletions = patch.getDeletions();

		try {
			updateVariableEntities(resourceId, variableModifications, variableDeletions);
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			String errorMessage = String.format("Cannot modify variables for %s %s: %s", getResourceTypeName(),
					resourceId, e.getMessage());
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, errorMessage);
		}

	}

	protected abstract VariableMap getVariableEntities(String resourceId, boolean deserializeValues);

	protected abstract void updateVariableEntities(String resourceId, VariableMap variables, List<String> deletions);

	protected abstract TypedValue getVariableEntity(String resourceId, String variableKey, boolean deserializeValue);

	protected abstract void setVariableEntity(String resourceId, String variableKey, TypedValue variableValue);

	protected abstract void removeVariableEntity(String resourceId, String variableKey);

	protected abstract String getResourceTypeName();

}
