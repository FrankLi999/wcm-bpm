package com.bpwizard.spring.boot.commons.service.security;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.security.BlueTokenService;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.web.util.WebUtils;
import com.bpwizard.wcm.repo.exception.BadRequestException;

import lombok.AllArgsConstructor;

/**
 * Authentication success handler for redirecting the
 * OAuth2 signed in user to a URL with a short lived auth token
 * 
 * @author Sanjay Patel
 */
@AllArgsConstructor
public class OAuth2AuthenticationSuccessHandler<ID extends Serializable>
	extends SimpleUrlAuthenticationSuccessHandler {
	
	private static final Logger log = LogManager.getLogger(OAuth2AuthenticationSuccessHandler.class);

	private SpringProperties properties;
	private BlueTokenService blueTokenService;

	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, authentication);
        this.clearAuthenticationAttributes(request, response);
    }
	
	@Override
	protected String determineTargetUrl(HttpServletRequest request,
			HttpServletResponse response) {
		
		UserDto currentUser = WebUtils.currentUser();
		
		String shortLivedAuthToken = blueTokenService.createToken(
				BlueTokenService.AUTH_AUDIENCE,
				currentUser.getUsername(),
				(long) properties.getJwt().getShortLivedMillis());

		String targetUrl = WebUtils.fetchCookie(request,
				HttpCookieOAuth2AuthorizationRequestRepository.BPW_REDIRECT_URI_COOKIE_PARAM_NAME)
				.map(Cookie::getValue)
				.orElse(properties.getOauth2AuthenticationSuccessUrl());
		
		if(!isAuthorizedRedirectUri(targetUrl)) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }
		clearAuthenticationAttributes(request, response);
		
		// return targetUrl + shortLivedAuthToken;
		return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", shortLivedAuthToken)
                .build().toUriString();
	}
	
	protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
		WebUtils.deleteCookies(
			request,
			response, 
			HttpCookieOAuth2AuthorizationRequestRepository.AUTHORIZATION_REQUEST_COOKIE_NAME,
			HttpCookieOAuth2AuthorizationRequestRepository.BPW_REDIRECT_URI_COOKIE_PARAM_NAME);
    }
	
	private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return properties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
