package org.camunda.bpm.engine.rest.sub.authorization;

import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.authorization.AuthorizationDto;

public interface AuthorizationResource {

	AuthorizationDto getAuthorization(String resourceId);

	void deleteAuthorization(String resourceId);

	void updateAuthorization(String resourceId, AuthorizationDto dto);

	ResourceOptionsDto availableOperations(String resourceId);
}
