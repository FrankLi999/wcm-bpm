package com.bpwizard.myresources;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@EnableWebSecurity
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().antMatchers("/**").permitAll();
//		
////		http
////			.authorizeRequests()
////				.antMatchers("/css/**", "/index").permitAll()		
////				.antMatchers("/user/**").hasRole("USER")			
////				.and()
////			.formLogin()
////				.loginPage("/login").failureUrl("/login-error");	
//	}
//
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth
//			.inMemoryAuthentication()
//				.withUser("user").password("password").roles("USER");
//	}
}