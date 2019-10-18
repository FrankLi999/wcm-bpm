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

import java.io.IOException;
import java.util.Map;

import org.camunda.bpm.engine.rest.dto.PatchVariablesDto;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface VariableResource {

	public final static String DESERIALIZE_VALUE_QUERY_PARAM = "deserializeValue";
	public final static String DESERIALIZE_VALUES_QUERY_PARAM = DESERIALIZE_VALUE_QUERY_PARAM + "s";

	Map<String, VariableValueDto> getVariables(String resourceId, boolean deserializeValues);

	VariableValueDto getVariable(String resourceId, String variableName, boolean deserializeValue);

	public ResponseEntity<?> getVariableBinary(String resourceId, String variableName);

	void putVariable(String resourceId, String variableName, VariableValueDto variable);

	void setBinaryVariable(String resourceId, String variableName, String objectType, String valueType, MultipartFile dataFile) throws IOException;

	void deleteVariable(String resourceId, String variableName);

	void modifyVariables(String resourceId, PatchVariablesDto patch);
}
