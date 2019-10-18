package org.camunda.bpm.engine.rest.sub.runtime;

import org.camunda.bpm.engine.rest.dto.runtime.EventSubscriptionDto;
import org.camunda.bpm.engine.rest.dto.runtime.ExecutionTriggerDto;

public interface EventSubscriptionResource {
	EventSubscriptionDto getEventSubscription(
			String executionId, 
			String messageName);

	void triggerEvent(
			String executionId, 
			String messageName,
			ExecutionTriggerDto triggerDto);
}
