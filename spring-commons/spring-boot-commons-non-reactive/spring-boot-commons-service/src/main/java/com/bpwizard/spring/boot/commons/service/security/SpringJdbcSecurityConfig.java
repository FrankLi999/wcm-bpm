package com.bpwizard.spring.boot.commons.service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.web.security.SpringWebSecurityConfig;

/**
 * Security configuration class. Extend it in the
 * application, and make a configuration class. Override
 * protected methods, if you need any customization.
 */
public class SpringJdbcSecurityConfig extends SpringWebSecurityConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringJdbcSecurityConfig.class);

	private SpringProperties properties;
	private SpringUserDetailsService<?, ?> userDetailsService;
	private SpringAuthenticationSuccessHandler authenticationSuccessHandler;
	private SpringOidcUserService oidcUserService;
	private SpringOAuth2UserService<?, ?> oauth2UserService;
	private OAuth2AuthenticationSuccessHandler<?> oauth2AuthenticationSuccessHandler;
	private OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
	// private PasswordEncoder passwordEncoder;
	
	@Autowired
	public void createSpringSecurityConfig(SpringProperties properties, SpringUserDetailsService<?, ?> userDetailsService,
			SpringAuthenticationSuccessHandler authenticationSuccessHandler,
			SpringOidcUserService oidcUserService,
			SpringOAuth2UserService<?, ?> oauth2UserService,
			OAuth2AuthenticationSuccessHandler<?> oauth2AuthenticationSuccessHandler,
			// PasswordEncoder passwordEncoder
			OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler) {
		this.properties = properties;
		this.userDetailsService = userDetailsService;
		this.authenticationSuccessHandler = authenticationSuccessHandler;
		this.oidcUserService = oidcUserService;
		this.oauth2UserService = oauth2UserService;
		this.oauth2AuthenticationSuccessHandler = oauth2AuthenticationSuccessHandler;
		this.oauth2AuthenticationFailureHandler = oauth2AuthenticationFailureHandler;
		//this.passwordEncoder = passwordEncoder;
		logger.info("Created");
	}

	/**
	 * Security configuration, calling protected methods
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		super.configure(http);
		login(http); // authentication
		exceptionHandling(http); // exception handling
		oauth2Client(http);
	}
	
	/**
	 * Configuring authentication.
	 */
	protected void login(HttpSecurity http) throws Exception {
		
		http
		.formLogin() // form login
			.loginPage(loginPage())
			
			/******************************************
			 * Setting a successUrl would redirect the user there. Instead,
			 * let's send 200 and the userDto along with an Authorization token.
			 *****************************************/
			.successHandler(authenticationSuccessHandler)
			
			/*******************************************
			 * Setting the failureUrl will redirect the user to
			 * that url if login fails. Instead, we need to send
			 * 401. So, let's set failureHandler instead.
			 *******************************************/
        	.failureHandler(new SimpleUrlAuthenticationFailureHandler());
	}

	
	/**
	 * Override this to change login URL
	 * 
	 * @return
	 */
	protected String loginPage() {
		
		return properties.getLoginUrl(); //"/api/core/login";
	}

	
	protected void oauth2Client(HttpSecurity http) throws Exception {
		
		http.oauth2Login()
			    .authorizationEndpoint()
			        // .baseUri("/oauth2/authorize")
				    .authorizationRequestRepository(new HttpCookieOAuth2AuthorizationRequestRepository(properties))
			        .and()
			    // .redirectionEndpoint()
                    // .baseUri("/oauth2/callback/*")
                    // .and()
                // .userInfoEndpoint()
    				// .oidcUserService(oidcUserService)
    				// .userService(oauth2UserService)
    				// .and()
			    .successHandler(oauth2AuthenticationSuccessHandler)
			    .failureHandler(oauth2AuthenticationFailureHandler)
				.userInfoEndpoint()
					 .oidcUserService(oidcUserService)
					 .userService(oauth2UserService);
	
	}	

	
	/**
	 * Configuring token authentication filter
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void tokenAuthentication(HttpSecurity http) throws Exception {
		
		http.addFilterBefore(new SpringJdbcTokenAuthenticationFilter(jwsTokenService, userDetailsService),
				UsernamePasswordAuthenticationFilter.class);
	}
}
