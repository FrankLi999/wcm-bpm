package com.bpwizard.spring.boot.commons.service.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.bpwizard.spring.boot.commons.security.SpringPrincipal;

/**
 * Logs in or registers a user after OpenID Connect SignIn/Up
 */
public class SpringOidcUserService extends OidcUserService {
	
	private static final Logger log = LogManager.getLogger(SpringOidcUserService.class);

	private SpringOAuth2UserService<?, ?> oauth2UserService;

	public SpringOidcUserService(SpringOAuth2UserService<?, ?> oauth2UserService) {

		this.oauth2UserService = oauth2UserService;
		log.debug("Created");
	}

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		
		OidcUser oidcUser = super.loadUser(userRequest);
		SpringPrincipal principal = oauth2UserService.buildPrincipal(userRequest, oidcUser);
		
		principal.setClaims(oidcUser.getClaims());
		principal.setIdToken(oidcUser.getIdToken());
		principal.setUserInfo(oidcUser.getUserInfo());
		
		return principal;
	}
}
