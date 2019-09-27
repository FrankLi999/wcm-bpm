package com.bpwizard.wcm.repo.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bpwizard.spring.boot.commons.service.AutoConfiguration;
import com.bpwizard.spring.boot.commons.service.security.SpringJpaSecurityConfig;
import com.bpwizard.spring.boot.commons.service.security.SpringUserDetailsService;
import com.bpwizard.wcm.repo.domain.User;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@AutoConfigureBefore({AutoConfiguration.class})
public class ModeshapeSecurityConfig extends SpringJpaSecurityConfig {
	
	private static final Logger logger = LogManager.getLogger(ModeshapeSecurityConfig.class);
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private SpringUserDetailsService userDetailsService;
	
	@Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(this.passwordEncoder);
    }
	
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Override
	/**
	 * Configuring authentication.
	 */
	protected void login(HttpSecurity http) throws Exception {
		http.formLogin().disable()
        	.httpBasic().disable();
	}
	
	@Override
	protected void authorizeRequests(HttpSecurity http) throws Exception {
		logger.debug("config authorizeRequests");
		http.authorizeRequests()
			.antMatchers("/",
                "/error",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js")
                .permitAll()
            .antMatchers("/auth/**", "/oauth2/**")
                .permitAll()
            .antMatchers("/modeshape/api/**", "/wcm/api/**", "/hello/**", "/camunda/api/**", "/webdav/**")
                .permitAll()
            .anyRequest()
            	//.permitAll();
                .authenticated();
		//super.authorizeRequests(http);
	}
}