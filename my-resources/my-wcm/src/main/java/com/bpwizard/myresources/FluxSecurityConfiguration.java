package com.bpwizard.myresources;

import static org.springframework.security.core.userdetails.User.withUsername;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
// mport org.springframework.core.annotation.Order;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity 
public class FluxSecurityConfiguration { // extends WebSecurityConfigurerAdapter {

	@Bean
	public MapReactiveUserDetailsService userDetailsRepository() {
        UserDetails tom = withUsername("tom").password("password").roles("USER").build();
        UserDetails harry = withUsername("harry").password("password").roles("USER", "ADMIN").build();
        return new MapReactiveUserDetailsService(tom, harry);
    }
	
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.csrf().disable().authorizeExchange().pathMatchers("/**").permitAll();
//			.authorizeExchange()
//				.anyExchange().authenticated()
//				.and()
//			.httpBasic().and()
//			.formLogin();
		return http.build();
	}
}