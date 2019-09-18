package com.bpwizard.spring.boot.commons.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

import com.bpwizard.spring.boot.commons.service.security.SpringJpaSecurityConfig;

@Component
public class MySecurityConfig extends SpringJpaSecurityConfig {
	
	private static final Logger log = LogManager.getLogger(MySecurityConfig.class);
	
	public MySecurityConfig() {
		log.info("Created");
	}

	@Override
	protected void authorizeRequests(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.mvcMatchers("/admin/**").hasRole("GOOD_ADMIN");
		super.authorizeRequests(http);
	}
}
