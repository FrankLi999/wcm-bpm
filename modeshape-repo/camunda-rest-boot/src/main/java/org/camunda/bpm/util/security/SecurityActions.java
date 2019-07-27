package org.camunda.bpm.util.security;

import java.io.IOException;

import javax.servlet.ServletException;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;

/**
 * @author Daniel Meyer
 *
 */

public class SecurityActions {

//	public static <T> T runWithAuthentications(ProcessEngine processEngine, SecurityAction<T> action,
//			Authentications authentications) throws IOException, ServletException {
//
//		List<Authen	tication> currentAuthentications = authentications.getAuthentications();
//		try {
//			for (Authentication authentication : currentAuthentications) {
//				authenticateProcessEngine(authentication);
//			}
//
//			return action.execute();
//
//		} finally {
//			for (Authentication authentication : currentAuthentications) {
//				clearAuthentication(processEngine, authentication);
//			}
//		}
//	}
//
//	private static void clearAuthentication(ProcessEngine processEngine, Authentication authentication) {
//		if (processEngine != null) {
//			processEngine.getIdentityService().clearAuthentication();
//		}
//	}
//
//	private static void authenticateProcessEngine(ProcessEngine processEngine, Authentication authentication) {
//
//		if (processEngine != null) {
//
//			String userId = authentication.getIdentityId();
//			List<String> groupIds = null;
//			List<String> tenantIds = null;
//
//			if (authentication instanceof UserAuthentication) {
//				UserAuthentication userAuthentication = (UserAuthentication) authentication;
//				groupIds = userAuthentication.getGroupIds();
//				tenantIds = userAuthentication.getTenantIds();
//			}
//
//			processEngine.getIdentityService().setAuthentication(userId, groupIds, tenantIds);
//		}
//	}

	public static <T> T runWithoutAuthentication(SecurityAction<T> action, ProcessEngine processEngine)
			throws IOException, ServletException {

		final IdentityService identityService = processEngine.getIdentityService();
		org.camunda.bpm.engine.impl.identity.Authentication currentAuth = identityService.getCurrentAuthentication();

		try {
			identityService.clearAuthentication();
			return action.execute();

		} finally {
			identityService.setAuthentication(currentAuth);

		}
	}

	public static interface SecurityAction<T> {
		public T execute() throws IOException, ServletException;
	}

}
