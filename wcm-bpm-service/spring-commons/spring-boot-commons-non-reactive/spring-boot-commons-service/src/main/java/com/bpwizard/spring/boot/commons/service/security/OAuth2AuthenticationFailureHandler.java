package com.bpwizard.spring.boot.commons.service.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.bpwizard.spring.boot.commons.util.SecurityUtils;
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
		
//		String targetUrl = WebUtils.fetchCookie(
//					request, 
//					WebUtils.BPW_REDIRECT_URI_COOKIE_PARAM_NAME)
//                .map(Cookie::getValue)
//                .orElse(("/"));
//
//        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
//                .queryParam("error", exception.getLocalizedMessage())
//                .build().toUriString();
//        
//		WebUtils.deleteCookies(request, response,
//			WebUtils.AUTHORIZATION_REQUEST_COOKIE_NAME,
//			WebUtils.BPW_REDIRECT_URI_COOKIE_PARAM_NAME);
//		
//		// super.onAuthenticationFailure(request, response, exception);
//		getRedirectStrategy().sendRedirect(request, response, targetUrl);
		
		WebUtils.deleteCookies(request, response,
				SecurityUtils.AUTHORIZATION_REQUEST_COOKIE_NAME,
				SecurityUtils.BPW_REDIRECT_URI_COOKIE_PARAM_NAME);
			
		super.onAuthenticationFailure(request, response, exception);
	}
}
