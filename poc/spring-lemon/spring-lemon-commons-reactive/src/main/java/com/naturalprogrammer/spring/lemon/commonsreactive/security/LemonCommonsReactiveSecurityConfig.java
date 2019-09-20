package com.naturalprogrammer.spring.lemon.commonsreactive.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.naturalprogrammer.spring.lemon.commons.security.BlueTokenService;
import com.naturalprogrammer.spring.lemon.commons.security.LemonPrincipal;
import com.naturalprogrammer.spring.lemon.commons.security.UserDto;
import com.naturalprogrammer.spring.lemon.commons.util.LecUtils;
import com.naturalprogrammer.spring.lemon.exceptions.util.LexUtils;
import com.nimbusds.jwt.JWTClaimsSet;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class LemonCommonsReactiveSecurityConfig {

	private static final Log log = LogFactory.getLog(LemonCommonsReactiveSecurityConfig.class);
	
	protected BlueTokenService blueTokenService;

	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		
		log.info("Configuring SecurityWebFilterChain ...");
		
		formLogin(http); // Configure form login
		authorizeExchange(http); // configure authorization
		oauth2Login(http); // configure OAuth2 login

		return http
			.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
			.exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler())
				.authenticationEntryPoint(authenticationEntryPoint())
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
		filter.setAuthenticationFailureHandler(authenticationFailureHandler());
		
		return filter;
	}
	
	protected ReactiveAuthenticationManager tokenAuthenticationManager() {
		
		return authentication -> {
			
			log.debug("Authenticating with token ...");

			String token = (String) authentication.getCredentials();
			
			JWTClaimsSet claims = blueTokenService.parseToken(token, BlueTokenService.AUTH_AUDIENCE);
			
			UserDto userDto = LecUtils.getUserDto(claims);
			
			Mono<UserDto> userDtoMono = userDto == null ?
					fetchUserDto(claims) : Mono.just(userDto);
			
			return userDtoMono.map(LemonPrincipal::new)
					.doOnNext(LemonPrincipal::eraseCredentials)
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
				LexUtils.getMessage("com.naturalprogrammer.spring.userClaimAbsent")));
	}

	protected ServerAuthenticationConverter tokenAuthenticationConverter() {
		
		return serverWebExchange -> {
			
			String authorization = serverWebExchange.getRequest()
				.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
			
			if(authorization == null || !authorization.startsWith(LecUtils.TOKEN_PREFIX))
				return Mono.empty();

			return Mono.just(new UsernamePasswordAuthenticationToken(null, authorization.substring(LecUtils.TOKEN_PREFIX_LENGTH)));		
		};
	}
	
	protected ServerAuthenticationFailureHandler authenticationFailureHandler() {
		
		return (webFilterExchange, exception) -> Mono.error(exception);		
	}
	
	protected ServerAccessDeniedHandler accessDeniedHandler() {
		
		return (exchange, exception) -> Mono.error(exception);
	}
	
	protected ServerAuthenticationEntryPoint authenticationEntryPoint() {
		
		return (exchange, exception) -> Mono.error(exception);
	}
	
}