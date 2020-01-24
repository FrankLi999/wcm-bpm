package com.bpwizard.wcm.modeshape.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

import com.bpwizard.wcm.modeshape.security.AuthoritiesConstants;
import com.bpwizard.wcm.modeshape.security.ReactiveUserDetailsServiceImpl;
import com.bpwizard.wcm.modeshape.security.TokenAuthenticationConverter;
import com.bpwizard.wcm.modeshape.security.UnauthorizedAuthenticationEntryPoint;
import com.bpwizard.wcm.modeshape.security.jwt.JWTHeadersExchangeMatcher;
import com.bpwizard.wcm.modeshape.security.jwt.JWTReactiveAuthenticationManager;
import com.bpwizard.wcm.modeshape.security.jwt.TokenProvider;

/**
 * @author duc-d
 */
@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
public class SecurityConfiguration {
	private static final Logger logger = LogManager.getLogger(SecurityConfiguration.class);
    private final ReactiveUserDetailsServiceImpl reactiveUserDetailsService;
    private final TokenProvider tokenProvider;

    private static final String[] AUTH_WHITELIST = {
            "/resources/**",
            "/webjars/**",
            "/auth/login/**",
            "/favicon.ico",
    };

    public SecurityConfiguration(ReactiveUserDetailsServiceImpl reactiveUserDetailsService,
                                 TokenProvider tokenProvider) {
        this.reactiveUserDetailsService = reactiveUserDetailsService;
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, UnauthorizedAuthenticationEntryPoint entryPoint) {

        http.httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .logout().disable();

        http
                .exceptionHandling()
                	.authenticationEntryPoint(entryPoint)
            .and()
                .authorizeExchange()
                	.matchers(EndpointRequest.to("health", "info"))
                	.permitAll()
            .and()
                .authorizeExchange()
                	.pathMatchers(HttpMethod.OPTIONS)
                	.permitAll()
            .and()
                .authorizeExchange()
                	.matchers(EndpointRequest.toAnyEndpoint())
                	.hasAuthority(AuthoritiesConstants.ADMIN)
            .and()
                .addFilterAt(webFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
                	.authorizeExchange()
                	.pathMatchers(AUTH_WHITELIST).permitAll()
                	.anyExchange().authenticated();

        return http.build();
    }

    @Bean
    public AuthenticationWebFilter webFilter() {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(repositoryReactiveAuthenticationManager());
        authenticationWebFilter.setAuthenticationConverter(new TokenAuthenticationConverter(tokenProvider));
        authenticationWebFilter.setRequiresAuthenticationMatcher(new JWTHeadersExchangeMatcher());
        authenticationWebFilter.setSecurityContextRepository(new WebSessionServerSecurityContextRepository());
        return authenticationWebFilter;
    }

    @Bean
    public JWTReactiveAuthenticationManager repositoryReactiveAuthenticationManager() {
        JWTReactiveAuthenticationManager repositoryReactiveAuthenticationManager = new JWTReactiveAuthenticationManager(reactiveUserDetailsService, passwordEncoder());
        return repositoryReactiveAuthenticationManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
	@ConditionalOnProperty(name="bpw.cors.allowed-origins")
	@ConditionalOnMissingBean(CorsConfigurationSource.class)
	public SpringCorsConfigurationSource corsConfigurationSource(SpringProperties properties) {
		
        logger.info("Configuring SpringCorsConfigurationSource");       
		return new SpringCorsConfigurationSource(properties);		
	}
}
