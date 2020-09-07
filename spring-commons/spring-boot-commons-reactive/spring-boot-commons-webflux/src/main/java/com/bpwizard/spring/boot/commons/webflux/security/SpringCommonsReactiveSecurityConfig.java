package com.bpwizard.spring.boot.commons.webflux.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;
import com.bpwizard.spring.boot.commons.security.JSONWebSignatureService;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.nimbusds.jwt.JWTClaimsSet;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class SpringCommonsReactiveSecurityConfig { //extends ReactiveSecurityAutoConfiguration {

	private static final Logger log = LogManager.getLogger(SpringCommonsReactiveSecurityConfig.class);
	
	protected JSONWebSignatureService jwsTokenService;

	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		
		log.info("Configuring SecurityWebFilterChain ...");
		
		formLogin(http); // Configure form login
		authorizeExchange(http); // configure authorization
		oauth2Login(http); // configure OAuth2 login

		return http
			.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
			.exceptionHandling()
				.accessDeniedHandler((exchange, exception) -> Mono.error(exception))
				.authenticationEntryPoint((exchange, exception) -> Mono.error(exception))
			.and()
				.cors()
			.and()
				.csrf().disable()
				.addFilterAt(tokenAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
			.logout().disable()
			.build();
	}

	/**
	 * Override this to configure oauth2 Login
	 */
	protected void oauth2Login(ServerHttpSecurity http) {

		// Bypass here. OAuth2 login is needed only in the auth service
	}

	/**
	 * Override this to configure authorization
	 */
	protected void authorizeExchange(ServerHttpSecurity http) {
		
		http.authorizeExchange()
			.anyExchange().permitAll();
	}


	/**
	 * Configures form login
	 */
	protected void formLogin(ServerHttpSecurity http) {
		
		// Bypass here. Form login is needed only in the auth service
	}


	protected AuthenticationWebFilter tokenAuthenticationFilter() {
		
		AuthenticationWebFilter filter = new AuthenticationWebFilter(tokenAuthenticationManager());		
		filter.setServerAuthenticationConverter(tokenAuthenticationConverter());
		// filter.setAuthenticationFailureHandler(authenticationFailureHandler());
		filter.setAuthenticationFailureHandler((exchange, exception) -> Mono.error(exception));
		return filter;
	}
	
	protected ReactiveAuthenticationManager tokenAuthenticationManager() {
		
		return authentication -> {
			
			log.debug("Authenticating with token ...");

			String token = (String) authentication.getCredentials();
			
			JWTClaimsSet claims = jwsTokenService.parseToken(token, JSONWebSignatureService.AUTH_AUDIENCE);
			
			UserDto userDto = SecurityUtils.getUserDto(claims);
			
			Mono<UserDto> userDtoMono = userDto == null ?
					fetchUserDto(claims) : Mono.just(userDto);
			
			return userDtoMono.map(SpringPrincipal::new)
					.doOnNext(SpringPrincipal::eraseCredentials)
					.map(principal -> new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities()));		
		};
	}
	
	/**
	 * Default behaviour is to throw error. To be overridden in auth service.
	 * 
	 * @param username
	 * @return
	 */
	protected Mono<UserDto> fetchUserDto(JWTClaimsSet claims) {
		return Mono.error(new AuthenticationCredentialsNotFoundException(
				SpringExceptionUtils.getMessage("com.bpwizard.spring.userClaimAbsent")));
	}

	protected ServerAuthenticationConverter tokenAuthenticationConverter() {
		
		return serverWebExchange -> {
			
			String authorization = serverWebExchange.getRequest()
				.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
			
			if(authorization == null || !authorization.startsWith(SecurityUtils.TOKEN_PREFIX))
				return Mono.empty();

			return Mono.just(new UsernamePasswordAuthenticationToken(null, authorization.substring(SecurityUtils.TOKEN_PREFIX_LENGTH)));		
		};
	}	
}
