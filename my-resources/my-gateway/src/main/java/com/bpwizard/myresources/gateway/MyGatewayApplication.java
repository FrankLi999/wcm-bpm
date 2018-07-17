package com.bpwizard.myresources.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@SpringBootApplication
@EnableDiscoveryClient
public class MyGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(
				MyGatewayApplication.class);
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