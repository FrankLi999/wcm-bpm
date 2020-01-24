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
package org.camunda.bpm.engine.rest.controller;

import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.rest.SignalRestService;
import org.camunda.bpm.engine.rest.dto.SignalDto;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.runtime.SignalEventReceivedBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tassilo Weidner
 */
@RestController
@RequestMapping(SignalRestService.PATH)
public class SignalRestController extends AbstractRestProcessEngineAware implements SignalRestService {

	@Override
	@PostMapping(path="/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void throwSignal(@RequestBody SignalDto dto) {
		String name = dto.getName();
		if (name == null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, "No signal name given");
		}

		SignalEventReceivedBuilder signalEvent = createSignalEventReceivedBuilder(dto);
		signalEvent.send();
	}

	protected SignalEventReceivedBuilder createSignalEventReceivedBuilder(SignalDto dto) {
		RuntimeService runtimeService = processEngine.getRuntimeService();
		String name = dto.getName();
		SignalEventReceivedBuilder signalEvent = runtimeService.createSignalEvent(name);

		String executionId = dto.getExecutionId();
		if (executionId != null) {
			signalEvent.executionId(executionId);
		}

		Map<String, VariableValueDto> variablesDto = dto.getVariables();
		if (variablesDto != null) {
			Map<String, Object> variables = VariableValueDto.toMap(variablesDto, processEngine, objectMapper);
			signalEvent.setVariables(variables);
		}

		String tenantId = dto.getTenantId();
		if (tenantId != null) {
			signalEvent.tenantId(tenantId);
		}

		boolean isWithoutTenantId = dto.isWithoutTenantId();
		if (isWithoutTenantId) {
			signalEvent.withoutTenantId();
		}

		return signalEvent;
	}
}
