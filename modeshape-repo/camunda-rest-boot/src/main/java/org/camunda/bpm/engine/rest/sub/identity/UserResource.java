package org.camunda.bpm.engine.rest.sub.identity;

import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.identity.UserCredentialsDto;
import org.camunda.bpm.engine.rest.dto.identity.UserProfileDto;

public interface UserResource {
	UserProfileDto getUserProfile(String userId);
	ResourceOptionsDto availableOperations(String userId);
	void deleteUser(String userId);
	void unlockUser(String userId);
	void updateCredentials(String userId, UserCredentialsDto account);
	void updateProfile(String userId, UserProfileDto profile);
}
