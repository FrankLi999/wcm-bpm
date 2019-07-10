package com.bpwizard.spring.boot.commons.service.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.Assert;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.bpwizard.spring.boot.commons.web.util.WebUtils;

/**
 * Cookie based repository for storing Authorization requests
 */
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
	
	public static final String AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
	public static final String BPW_REDIRECT_URI_COOKIE_PARAM_NAME = "redirect_uri";
    
	private int cookieExpirySecs;
	
	public HttpCookieOAuth2AuthorizationRequestRepository(SpringProperties properties) {

		cookieExpirySecs = properties.getJwt().getShortLivedMillis() / 1000;
	}

	/**
	 * Load authorization request from cookie
	 */
	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		
		Assert.notNull(request, "request cannot be null");
		
		return WebUtils.fetchCookie(request, AUTHORIZATION_REQUEST_COOKIE_NAME)
					.map(this::deserialize)
					.orElse(null);
	}

	/**
	 * Save authorization request in cookie
	 */
	@Override
	public void saveAuthorizationRequest(
			OAuth2AuthorizationRequest authorizationRequest, 
			HttpServletRequest request,
			HttpServletResponse response) {
		
		Assert.notNull(request, "request cannot be null");
		Assert.notNull(response, "response cannot be null");
		
		if (authorizationRequest == null) {			
			WebUtils.deleteCookies(request, response, AUTHORIZATION_REQUEST_COOKIE_NAME, BPW_REDIRECT_URI_COOKIE_PARAM_NAME);
			return;
		}
		
		Cookie cookie = new Cookie(AUTHORIZATION_REQUEST_COOKIE_NAME, SecurityUtils.serialize(authorizationRequest));
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(cookieExpirySecs);
		response.addCookie(cookie);
		
		String springRedirectUri = request.getParameter(BPW_REDIRECT_URI_COOKIE_PARAM_NAME);
		if (StringUtils.isNotBlank(springRedirectUri)) {
			cookie = new Cookie(BPW_REDIRECT_URI_COOKIE_PARAM_NAME, springRedirectUri);
			cookie.setPath("/");
			cookie.setHttpOnly(true);
			cookie.setMaxAge(cookieExpirySecs);
			response.addCookie(cookie);
		}		
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(
			HttpServletRequest request, 
			HttpServletResponse response) {		
		OAuth2AuthorizationRequest originalRequest = loadAuthorizationRequest(request);
		WebUtils.deleteCookies(request, response, AUTHORIZATION_REQUEST_COOKIE_NAME);
		return originalRequest;
	}
	
	private OAuth2AuthorizationRequest deserialize(Cookie cookie) {		
		return SecurityUtils.deserialize(cookie.getValue());
	}

	@Deprecated
	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		throw new UnsupportedOperationException("Spring Security shouldn't have called the deprecated removeAuthorizationRequest(request)");
	}
	
//	@Override
//    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
//        return this.loadAuthorizationRequest(request);
//    }
}