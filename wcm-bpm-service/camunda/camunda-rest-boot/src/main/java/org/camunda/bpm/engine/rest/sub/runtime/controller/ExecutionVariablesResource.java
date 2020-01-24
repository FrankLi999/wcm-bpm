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

import java.util.List;

import org.camunda.bpm.engine.impl.RuntimeServiceImpl;
import org.camunda.bpm.engine.rest.sub.VariableResource;
import org.camunda.bpm.engine.rest.sub.impl.AbstractVariablesService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.stereotype.Component;

@Component
public class ExecutionVariablesResource extends AbstractVariablesService implements VariableResource {

	private String resourceTypeName = "process instance";

//	public ExecutionVariablesResource(ProcessEngine engine, String resourceId, boolean isProcessInstance,
//			ObjectMapper objectMapper) {
//		super(engine, resourceId, objectMapper);
//		if (isProcessInstance) {
//			resourceTypeName = "process instance";
//		} else {
//			resourceTypeName = "execution";
//		}
//	}

	protected String getResourceTypeName() {
		return resourceTypeName;
	}

	protected void updateVariableEntities(String resourceId, VariableMap modifications, List<String> deletions) {
		RuntimeServiceImpl runtimeService = (RuntimeServiceImpl) engine.getRuntimeService();
		runtimeService.updateVariables(resourceId, modifications, deletions);
	}

	protected void removeVariableEntity(String resourceId, String variableKey) {
		engine.getRuntimeService().removeVariable(resourceId, variableKey);
	}

	protected VariableMap getVariableEntities(String resourceId, boolean deserializeValues) {
		return engine.getRuntimeService().getVariablesTyped(resourceId, deserializeValues);
	}

	protected TypedValue getVariableEntity(String resourceId, String variableKey, boolean deserializeValue) {
		return engine.getRuntimeService().getVariableTyped(resourceId, variableKey, deserializeValue);
	}

	protected void setVariableEntity(String resourceId, String variableKey, TypedValue variableValue) {
		engine.getRuntimeService().setVariable(resourceId, variableKey, variableValue);
	}

}
