package com.bpwizard.spring.boot.commons.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.bpwizard.spring.boot.commons.service.domain.User;
import com.bpwizard.spring.boot.commons.service.security.SpringJpaSecurityConfig;
import com.bpwizard.spring.boot.commons.service.security.SpringUserDetailsService;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@AutoConfigureBefore({AutoConfiguration.class})
public class MySecurityConfig extends SpringJpaSecurityConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(MySecurityConfig.class);
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private SpringUserDetailsService<User<Long>, Long> userDetailsService;
	
	public MySecurityConfig() {
		logger.info("Created");
	}

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
	protected void authorizeRequests(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.mvcMatchers("/admin/**").hasRole("GOOD_ADMIN");
		super.authorizeRequests(http);
	}
}
