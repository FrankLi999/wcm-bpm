package com.bpwizard.wcm.repo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.bpwizard.spring.boot.commons.SpringProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bpwizard.spring.boot.commons.service.domain.User;
import com.bpwizard.spring.boot.commons.service.security.SpringJdbcSecurityConfig;
import com.bpwizard.spring.boot.commons.service.security.SpringUserDetailsService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class AppSecurityConfig extends SpringJdbcSecurityConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(AppSecurityConfig.class);
    
    @Autowired
    private SpringProperties springProperties;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private SpringUserDetailsService<User<Long>, Long> userDetailsService;
	
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
		http.formLogin().disable();
        	//.httpBasic().disable();
	}
	
	@Override
	protected void authorizeRequests(HttpSecurity http) throws Exception {
        logger.debug("config authorizeRequests");
        String permitAllMaters[] = springProperties.getAppSecurity().getPermitAll();
        http.authorizeRequests()
            .antMatchers(permitAllMaters)
            .permitAll()            
            .anyRequest()
                .authenticated();
	}
}