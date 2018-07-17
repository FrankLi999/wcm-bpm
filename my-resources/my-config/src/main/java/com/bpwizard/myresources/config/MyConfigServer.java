package com.bpwizard.myresources.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@SpringBootApplication
@EnableConfigServer
public class MyConfigServer {

	public static void main(String[] args) {
		SpringApplication.run(
				MyConfigServer.class, args);
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new InMemoryUserDetailsManager(
			User
				.withUsername("user")
				.password("password")
				.roles("USER").build());
	}
}
// end::code[]
