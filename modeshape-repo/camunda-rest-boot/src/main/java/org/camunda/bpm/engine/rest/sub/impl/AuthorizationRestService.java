package org.camunda.bpm.engine.rest.sub.impl;


import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.authorization.Permission;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationRestService extends AbstractRestProcessEngineAware {
	
	public boolean isAuthorized(Permission permission, Resource resource, String resourceId) {
		if (!this.processEngine.getProcessEngineConfiguration().isAuthorizationEnabled()) {
			// if authorization is disabled everyone is authorized
			return true;
		}

		final IdentityService identityService = this.processEngine.getIdentityService();
		final AuthorizationService authorizationService = this.processEngine.getAuthorizationService();

		Authentication authentication = identityService.getCurrentAuthentication();
		if (authentication == null) {
			return true;

		} else {
			return authorizationService.isUserAuthorized(authentication.getUserId(), authentication.getGroupIds(),
					permission, resource, resourceId);
		}
	}
}
