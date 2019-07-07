package com.bpwizard.spring.boot.commons.service.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.bpwizard.spring.boot.commons.web.util.WebUtils;

/**
 * OAuth2 Authentication failure handler for removing oauth2 related cookies
 * 
 * @author Sanjay Patel
 */
public class OAuth2AuthenticationFailureHandler
	extends SimpleUrlAuthenticationFailureHandler {
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		
		String targetUrl = WebUtils.fetchCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.BPW_REDIRECT_URI_COOKIE_PARAM_NAME)
                .map(Cookie::getValue)
                .orElse(("/"));

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();
        
		WebUtils.deleteCookies(request, response,
			HttpCookieOAuth2AuthorizationRequestRepository.AUTHORIZATION_REQUEST_COOKIE_NAME,
			HttpCookieOAuth2AuthorizationRequestRepository.BPW_REDIRECT_URI_COOKIE_PARAM_NAME);
		
		// super.onAuthenticationFailure(request, response, exception);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
