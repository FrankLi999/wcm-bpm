package com.bpwizard.spring.boot.commons.reactive.service.security;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.reactive.service.util.ReactiveServiceUtils;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.bpwizard.spring.boot.commons.webflux.util.ReactiveUtils;

import reactor.core.publisher.Mono;

public class ReactiveCookieServerOAuth2AuthorizedClientRepository implements ServerOAuth2AuthorizedClientRepository {

	private static final Log log = LogFactory.getLog(ReactiveCookieServerOAuth2AuthorizedClientRepository.class);

	private long cookieExpirySecs;
	
	public ReactiveCookieServerOAuth2AuthorizedClientRepository(SpringProperties properties) {

		cookieExpirySecs = properties.getJwt().getShortLivedMillis() / 1000;
	}

	@Override
	public Mono<OAuth2AuthorizedClient> loadAuthorizedClient(String clientRegistrationId,
			Authentication principal, ServerWebExchange exchange) {
		
		log.debug("Loading authorized client for clientRegistrationId " + clientRegistrationId
				+ ", principal " + principal + ", and exchange " + exchange);
		
		return ReactiveUtils.fetchCookie(exchange, SecurityUtils.AUTHORIZATION_REQUEST_COOKIE_NAME)
				.map(this::deserialize)
				.orElse(Mono.empty());
	}

	@Override
	public Mono<Void> saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal,
			ServerWebExchange exchange) {
		
		log.debug("Saving authorized client " + authorizedClient
				+ " for principal " + principal + ", and exchange " + exchange);

		ServerHttpResponse response = exchange.getResponse();
		
		Assert.notNull(exchange, "exchange cannot be null");
		if (authorizedClient == null) {
			
			ReactiveServiceUtils.deleteCookies(exchange, SecurityUtils.AUTHORIZATION_REQUEST_COOKIE_NAME, SecurityUtils.BPW_REDIRECT_URI_COOKIE_PARAM_NAME);
			return Mono.empty();
		}
		
		ResponseCookie cookie = ResponseCookie
				.from(SecurityUtils.AUTHORIZATION_REQUEST_COOKIE_NAME, SecurityUtils.serialize(authorizedClient))
				.path("/")
				.httpOnly(true)
				.maxAge(cookieExpirySecs)
				.build();

		response.addCookie(cookie);
		
		String lemonRedirectUri = exchange.getRequest()
				.getQueryParams().getFirst(SecurityUtils.BPW_REDIRECT_URI_COOKIE_PARAM_NAME);
		
		if (StringUtils.isNotBlank(lemonRedirectUri)) {
			
			cookie = ResponseCookie
					.from(SecurityUtils.BPW_REDIRECT_URI_COOKIE_PARAM_NAME, lemonRedirectUri)
					.path("/")
					.httpOnly(true)
					.maxAge(cookieExpirySecs)
					.build();

			response.addCookie(cookie);
		}
		
		return Mono.empty();
	}

	@Override
	public Mono<Void> removeAuthorizedClient(String clientRegistrationId, Authentication principal,
			ServerWebExchange exchange) {
		
		log.debug("Deleting authorized client for clientRegistrationId " + clientRegistrationId
				+ ", principal " + principal + ", and exchange " + exchange);

		ReactiveServiceUtils.deleteCookies(exchange, SecurityUtils.AUTHORIZATION_REQUEST_COOKIE_NAME);
		return Mono.empty();
	}
	
	private Mono<OAuth2AuthorizedClient> deserialize(HttpCookie cookie) {		
		return Mono.just(SecurityUtils.deserialize(cookie.getValue()));
	}

}