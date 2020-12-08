package com.bpwizard.spring.boot.commons.reactive.service.security;


import java.io.Serializable;
import java.net.URI;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.exceptions.util.ExceptionUtils;
import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;
import com.bpwizard.spring.boot.commons.reactive.service.SpringReactiveService;
import com.bpwizard.spring.boot.commons.reactive.service.domain.AbstractMongoUser;
import com.bpwizard.spring.boot.commons.reactive.service.domain.AbstractMongoUserRepository;
import com.bpwizard.spring.boot.commons.reactive.service.util.ReactiveServiceUtils;
import com.bpwizard.spring.boot.commons.security.JSONWebSignatureService;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.bpwizard.spring.boot.commons.webflux.util.ReactiveUtils;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Authentication success handler for redirecting the
 * OAuth2 signed in user to a URL with a short lived auth token
 */
@AllArgsConstructor
public class ReactiveOAuth2AuthenticationSuccessHandler<U extends AbstractMongoUser<ID>, ID extends Serializable>
	implements ServerAuthenticationSuccessHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ReactiveOAuth2AuthenticationSuccessHandler.class);
	private static final ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

	private JSONWebSignatureService jwsTokenService;
	private AbstractMongoUserRepository<U, ID> userRepository;
	private SpringReactiveUserDetailsService<U, ?> userDetailsService;
	private SpringReactiveService<U, ?> springService;
	private PasswordEncoder passwordEncoder;
	private SpringProperties properties;
	
	@Override
	public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange,
		Authentication authentication) {
		
		ServerWebExchange exchange = webFilterExchange.getExchange();

		return ReactiveSecurityContextHolder.getContext()
				.map(SecurityContext::getAuthentication)
				.cast(OAuth2AuthenticationToken.class)
				.flatMap(token -> buildPrincipal(token.getPrincipal(), token.getAuthorizedClientRegistrationId()))
				.map(SpringPrincipal::currentUser)
				.map(this::getAuthToken)
				.map(authToken -> getTargetUrl(exchange, authToken))
				.map(URI::create)
				.flatMap(location -> redirectStrategy.sendRedirect(exchange, location));
	}
	
	/**
	 * Builds the security principal from the given userReqest.
	 * Registers the user if not already registered
	 */
	public Mono<SpringPrincipal> buildPrincipal(OAuth2User oath2User, String registrationId) {
		
		Map<String, Object> attributes = oath2User.getAttributes();
		String email = springService.getOAuth2Email(registrationId, attributes);
		SpringExceptionUtils.validate(email != null, "com.bpwizard.spring.oauth2EmailNeeded", registrationId).go();
		
		boolean emailVerified = springService.getOAuth2AccountVerified(registrationId, attributes);
		SpringExceptionUtils.validate(emailVerified, "com.bpwizard.spring.oauth2EmailNotVerified", registrationId).go();
		
		return userDetailsService.findUserByUsername(email)
				.switchIfEmpty(newUser(email, registrationId, attributes))
				.map(U::toUserDto)
				.map(userDto -> {
					
					SpringPrincipal principal = new SpringPrincipal(userDto);
					principal.setAttributes(attributes);
					principal.setName(oath2User.getName());
					
					return principal;
		});
	}
	
	private Mono<U> newUser(String email, String registrationId, Map<String, Object> attributes) {
		
		// register a new user
		U newUser = springService.newUser();
		newUser.setEmail(email);
		newUser.setPassword(passwordEncoder.encode(SecurityUtils.uid()));
		
		springService.fillAdditionalFields(registrationId, newUser, attributes);
		return userRepository.insert(newUser).doOnSuccess(user -> {
			try {
				
				springService.mailForgotPasswordLink(newUser);
				
			} catch (Throwable e) {
				
				// In case of exception, just log the error and keep silent			
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		});
	}

	private String getAuthToken(UserDto user) {
		
		String shortLivedAuthToken = jwsTokenService.createToken(
				JSONWebSignatureService.AUTH_AUDIENCE,
				user.getUsername(),
				(long) properties.getJwt().getShortLivedMillis());
		
		return shortLivedAuthToken;
	}
	
	private String getTargetUrl(ServerWebExchange exchange, String shortLivedAuthToken) {
		
		String targetUrl = ReactiveUtils.fetchCookie(exchange,
				SecurityUtils.BPW_REDIRECT_URI_COOKIE_PARAM_NAME)
				.map(HttpCookie::getValue)
				.orElse(properties.getOauth2AuthenticationSuccessUrl());
		
		ReactiveServiceUtils.deleteCookies(exchange,
				SecurityUtils.AUTHORIZATION_REQUEST_COOKIE_NAME,
				SecurityUtils.BPW_REDIRECT_URI_COOKIE_PARAM_NAME);
		
		return targetUrl + shortLivedAuthToken;
	}
}