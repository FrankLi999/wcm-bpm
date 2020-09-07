package com.bpwizard.spring.boot.commons.reactive.service.security;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.reactive.service.domain.AbstractMongoUser;
import com.bpwizard.spring.boot.commons.reactive.service.util.ReactiveServiceUtils;
import com.bpwizard.spring.boot.commons.security.JSONWebSignatureService;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.bpwizard.spring.boot.commons.webflux.security.SpringCommonsReactiveSecurityConfig;
import com.nimbusds.jwt.JWTClaimsSet;

import reactor.core.publisher.Mono;

public class SpringReactiveSecurityConfig <U extends AbstractMongoUser<ID>, ID extends Serializable> extends SpringCommonsReactiveSecurityConfig {

	private static final Logger log = LogManager.getLogger(SpringReactiveSecurityConfig.class);
	
	protected SpringReactiveUserDetailsService<U, ID> userDetailsService;
	private SpringProperties properties;
	private ReactiveOAuth2AuthenticationSuccessHandler<U,ID> reactiveOAuth2AuthenticationSuccessHandler;
	
	public SpringReactiveSecurityConfig(JSONWebSignatureService jwsTokenService,
			SpringReactiveUserDetailsService<U, ID> userDetailsService,
			ReactiveOAuth2AuthenticationSuccessHandler<U,ID> reactiveOAuth2AuthenticationSuccessHandler,
			SpringProperties properties) {
		
		super(jwsTokenService);
		this.userDetailsService = userDetailsService;
		this.reactiveOAuth2AuthenticationSuccessHandler = reactiveOAuth2AuthenticationSuccessHandler;
		this.properties = properties;
		log.info("Created");
	}

	/**
	 * Configure form login
	 */
	@Override
	protected void formLogin(ServerHttpSecurity http) {
		
		http.formLogin()
		    .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
			.loginPage(loginPage()) // Should be "/login" by default, but not providing that overwrites our AuthenticationFailureHandler, because this is called later 
			.authenticationFailureHandler((exchange, exception) -> Mono.error(exception))
			.authenticationSuccessHandler(new WebFilterChainServerAuthenticationSuccessHandler());
	}

	/**
	 * Override this to change login URL
	 */
	protected String loginPage() {
		
		return "/api/core/login";
	}

	/**
	 * Configure OAuth2 login
	 */
	@Override
	protected void oauth2Login(ServerHttpSecurity http) {

		http.oauth2Login()
			.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
			.authorizedClientRepository(new ReactiveCookieServerOAuth2AuthorizedClientRepository(properties))
			.authenticationSuccessHandler(reactiveOAuth2AuthenticationSuccessHandler)
			.authenticationFailureHandler(this::onOauth2AuthenticationFailure);
			
	}
	
	@Override
	protected Mono<UserDto> fetchUserDto(JWTClaimsSet claims) {
		
		String username = claims.getSubject();
		
		return userDetailsService.findUserByUsername(username)
			.switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException(username))))
			.doOnNext(user -> {
		        log.debug("User found ...");
		        ReactiveServiceUtils.ensureCredentialsUpToDate(claims, user);
			}).map(AbstractMongoUser::toUserDto);
	}
	
	protected Mono<Void> onOauth2AuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
		
		ReactiveServiceUtils.deleteCookies(webFilterExchange.getExchange(),
				SecurityUtils.AUTHORIZATION_REQUEST_COOKIE_NAME,
				SecurityUtils.BPW_REDIRECT_URI_COOKIE_PARAM_NAME);
		
		return Mono.error(exception);
	}
}
