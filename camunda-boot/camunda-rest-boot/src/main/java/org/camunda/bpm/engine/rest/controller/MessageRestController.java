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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.rest.MessageRestService;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.message.CorrelationMessageDto;
import org.camunda.bpm.engine.rest.dto.message.MessageCorrelationResultDto;
import org.camunda.bpm.engine.rest.dto.message.MessageCorrelationResultWithVariableDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.runtime.MessageCorrelationResultWithVariables;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController(value="messageApi")
@RequestMapping(MessageRestService.PATH)
public class MessageRestController extends AbstractRestProcessEngineAware implements MessageRestService {

	@Override
	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deliverMessage(@RequestBody CorrelationMessageDto messageDto) {
		if (messageDto.getMessageName() == null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, "No message name supplied");
		}
		if (messageDto.getTenantId() != null && messageDto.isWithoutTenantId()) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
					"Parameter 'tenantId' cannot be used together with parameter 'withoutTenantId'.");
		}
		boolean variablesInResultEnabled = messageDto.isVariablesInResultEnabled();
		if (!messageDto.isResultEnabled() && variablesInResultEnabled) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
					"Parameter 'variablesInResultEnabled' cannot be used without 'resultEnabled' set to true.");
		}

		List<MessageCorrelationResultDto> resultDtos = new ArrayList<>();
		try {
			MessageCorrelationBuilder correlation = createMessageCorrelationBuilder(messageDto);
			if (!variablesInResultEnabled) {
				resultDtos.addAll(correlate(messageDto, correlation));
			} else {
				resultDtos.addAll(correlateWithVariablesEnabled(messageDto, correlation));
			}
		} catch (RestException e) {
			String errorMessage = String.format("Cannot deliver message: %s", e.getMessage());
			throw new InvalidRequestException(e.getStatus(), e, errorMessage);

		} catch (MismatchingMessageCorrelationException e) {
			throw new RestException(HttpStatus.BAD_REQUEST, e);
		}
		return createResponse(resultDtos, messageDto);
	}

	protected List<MessageCorrelationResultDto> correlate(CorrelationMessageDto messageDto,
			MessageCorrelationBuilder correlation) {
		List<MessageCorrelationResultDto> resultDtos = new ArrayList<>();
		if (!messageDto.isAll()) {
			MessageCorrelationResult result = correlation.correlateWithResult();
			resultDtos.add(MessageCorrelationResultDto.fromMessageCorrelationResult(result));
		} else {
			List<MessageCorrelationResult> results = correlation.correlateAllWithResult();
			for (MessageCorrelationResult result : results) {
				resultDtos.add(MessageCorrelationResultDto.fromMessageCorrelationResult(result));
			}
		}
		return resultDtos;
	}

	protected List<MessageCorrelationResultWithVariableDto> correlateWithVariablesEnabled(
			CorrelationMessageDto messageDto, MessageCorrelationBuilder correlation) {
		List<MessageCorrelationResultWithVariableDto> resultDtos = new ArrayList<>();
		if (!messageDto.isAll()) {
			MessageCorrelationResultWithVariables result = correlation.correlateWithResultAndVariables(false);
			resultDtos.add(MessageCorrelationResultWithVariableDto.fromMessageCorrelationResultWithVariables(result));
		} else {
			List<MessageCorrelationResultWithVariables> results = correlation.correlateAllWithResultAndVariables(false);
			for (MessageCorrelationResultWithVariables result : results) {
				resultDtos
						.add(MessageCorrelationResultWithVariableDto.fromMessageCorrelationResultWithVariables(result));
			}
		}
		return resultDtos;
	}

	protected ResponseEntity<?> createResponse(List<MessageCorrelationResultDto> resultDtos, CorrelationMessageDto messageDto) {
		ResponseEntity<?> response = null;
		if (messageDto.isResultEnabled()) {
			response = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(resultDtos);
		} else {
			response = ResponseEntity.noContent().build();
		}
		return response;
	}

	protected MessageCorrelationBuilder createMessageCorrelationBuilder(CorrelationMessageDto messageDto) {
		RuntimeService runtimeService = processEngine.getRuntimeService();

		ObjectMapper objectMapper = getObjectMapper();
		Map<String, Object> correlationKeys = VariableValueDto.toMap(messageDto.getCorrelationKeys(), processEngine,
				objectMapper);
		Map<String, Object> localCorrelationKeys = VariableValueDto.toMap(messageDto.getLocalCorrelationKeys(),
				processEngine, objectMapper);
		Map<String, Object> processVariables = VariableValueDto.toMap(messageDto.getProcessVariables(), processEngine,
				objectMapper);
		Map<String, Object> processVariablesLocal = VariableValueDto.toMap(messageDto.getProcessVariablesLocal(),
				processEngine, objectMapper);

		MessageCorrelationBuilder builder = runtimeService.createMessageCorrelation(messageDto.getMessageName());

		if (processVariables != null) {
			builder.setVariables(processVariables);
		}
		if (processVariablesLocal != null) {
			builder.setVariablesLocal(processVariablesLocal);
		}
		if (messageDto.getBusinessKey() != null) {
			builder.processInstanceBusinessKey(messageDto.getBusinessKey());
		}

		if (correlationKeys != null && !correlationKeys.isEmpty()) {
			for (Entry<String, Object> correlationKey : correlationKeys.entrySet()) {
				String name = correlationKey.getKey();
				Object value = correlationKey.getValue();
				builder.processInstanceVariableEquals(name, value);
			}
		}

		if (localCorrelationKeys != null && !localCorrelationKeys.isEmpty()) {
			for (Entry<String, Object> correlationKey : localCorrelationKeys.entrySet()) {
				String name = correlationKey.getKey();
				Object value = correlationKey.getValue();
				builder.localVariableEquals(name, value);
			}
		}

		if (messageDto.getTenantId() != null) {
			builder.tenantId(messageDto.getTenantId());

		} else if (messageDto.isWithoutTenantId()) {
			builder.withoutTenantId();
		}

		String processInstanceId = messageDto.getProcessInstanceId();
		if (processInstanceId != null) {
			builder.processInstanceId(processInstanceId);
		}

		return builder;
	}

}
