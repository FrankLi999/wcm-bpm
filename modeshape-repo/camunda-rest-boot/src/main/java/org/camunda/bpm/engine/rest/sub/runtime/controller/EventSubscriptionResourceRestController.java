package org.camunda.bpm.engine.rest.sub.runtime.controller;

import org.camunda.bpm.engine.AuthorizationException;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.rest.ExecutionRestService;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.runtime.EventSubscriptionDto;
import org.camunda.bpm.engine.rest.dto.runtime.ExecutionTriggerDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.runtime.EventSubscriptionResource;
import org.camunda.bpm.engine.runtime.EventSubscription;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ExecutionRestService.PATH + "/{executionId}/messageSubscriptions/{messageName}")
public class EventSubscriptionResourceRestController extends AbstractRestProcessEngineAware implements EventSubscriptionResource {
	protected static final String MESSAGE_EVENT_TYPE = "message";
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public EventSubscriptionDto getEventSubscription(
			@PathVariable("executionId") String executionId, 
			@PathVariable("messageName") String messageName) {
		RuntimeService runtimeService = this.processEngine.getRuntimeService();
		EventSubscription eventSubscription = runtimeService.createEventSubscriptionQuery().executionId(executionId)
				.eventName(messageName).eventType(MESSAGE_EVENT_TYPE).singleResult();

		if (eventSubscription == null) {
			String errorMessage = String.format("Message event subscription for execution %s named %s does not exist",
					executionId, messageName);
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, errorMessage);
		}

		return EventSubscriptionDto.fromEventSubscription(eventSubscription);
	}

	@PostMapping(path="/trigger", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void triggerEvent(
			@PathVariable("executionId") String executionId, 
			@PathVariable("messageName") String messageName,
			@RequestBody ExecutionTriggerDto triggerDto) {
		RuntimeService runtimeService = this.processEngine.getRuntimeService();

		try {
			VariableMap variables = VariableValueDto.toMap(triggerDto.getVariables(), this.processEngine, objectMapper);
			runtimeService.messageEventReceived(messageName, executionId, variables);

		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, String.format(
					"Cannot trigger message %s for execution %s: %s", messageName, executionId, e.getMessage()));

		} catch (RestException e) {
			String errorMessage = String.format("Cannot trigger message %s for execution %s: %s", messageName,
					executionId, e.getMessage());
			throw new InvalidRequestException(e.getStatus(), e, errorMessage);

		}

	}
}
