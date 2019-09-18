package com.bpwizard.spring.boot.commons.reactive.service.security;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;

import com.bpwizard.spring.boot.commons.reactive.service.domain.AbstractMongoUser;
import com.bpwizard.spring.boot.commons.reactive.service.util.ReactiveServiceUtils;
import com.bpwizard.spring.boot.commons.security.BlueTokenService;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.webflux.security.SpringCommonsReactiveSecurityConfig;
import com.nimbusds.jwt.JWTClaimsSet;

import reactor.core.publisher.Mono;

public class SpringReactiveSecurityConfig <U extends AbstractMongoUser<ID>, ID extends Serializable> extends SpringCommonsReactiveSecurityConfig {

	private static final Logger log = LogManager.getLogger(SpringReactiveSecurityConfig.class);
	
	protected SpringReactiveUserDetailsService<U, ID> userDetailsService;

	public SpringReactiveSecurityConfig(BlueTokenService blueTokenService,
			SpringReactiveUserDetailsService<U, ID> userDetailsService) {
		
		super(blueTokenService);
		this.userDetailsService = userDetailsService;
		log.info("Created");
	}

	/**
	 * Configure form login
	 */
	@Override
	protected void formLogin(ServerHttpSecurity http) {
		
		http.formLogin()
			.loginPage(loginPage()) // Should be "/login" by default, but not providing that overwrites our AuthenticationFailureHandler, because this is called later 
			.authenticationFailureHandler(authenticationFailureHandler())
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

		http.oauth2Login(); // TODO: Configure properly
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
}
