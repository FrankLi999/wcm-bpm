package com.bpwizard.spring.boot.commons.reactive.service;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebExchange;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.domain.ChangePasswordForm;
import com.bpwizard.spring.boot.commons.domain.ResetPasswordForm;
import com.bpwizard.spring.boot.commons.reactive.service.domain.AbstractMongoUser;
import com.bpwizard.spring.boot.commons.reactive.service.forms.EmailForm;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.security.UserDto;

import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

/**
 * The Spring Commons Mongo API. See the
 * <a href="https://github.com/bpwizard/spring-commons#documentation-and-resources">
 * API documentation</a> for details.
 */
public class SpringReactiveController
	<U extends AbstractMongoUser<ID>, ID extends Serializable> {

	private static final Logger logger = LoggerFactory.getLogger(SpringReactiveController.class);

    protected long jwtExpirationMillis;
	protected SpringReactiveService<U, ID> springReactiveService;	
	
	@Autowired
	public void createSpringController(
			SpringProperties properties,
			SpringReactiveService<U, ID> springReactiveService) {
		
		this.jwtExpirationMillis = properties.getJwt().getExpirationMillis();
		this.springReactiveService = springReactiveService;		

		logger.info("Created");
	}

	
	/**
	 * After a successful login, returns the current user with an authorization header.
	 */
	@PostMapping("/login")
	public Mono<UserDto> login(ServerWebExchange exchange) {
		
		logger.debug("Returning current user ... ");
		
		return ReactiveSecurityContextHolder.getContext()
				.map(SecurityContext::getAuthentication)
				.map(Authentication::getPrincipal)
				.cast(SpringPrincipal.class)
				.doOnNext(SpringPrincipal::eraseCredentials)
				.map(SpringPrincipal::currentUser)
				.zipWith(exchange.getFormData())
				.doOnNext(tuple -> {					
					long expirationMillis = springReactiveService.getExpirationMillis(tuple.getT2());
					springReactiveService.addAuthHeader(exchange.getResponse(), tuple.getT1(), expirationMillis);
				})
				.map(Tuple2::getT1);
	}

	
	/**
	 * A simple function for pinging this server.
	 */
	@GetMapping("/ping")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> ping() {
		
		logger.debug("Received a ping");
		return Mono.empty();
	}

	
	/**
	 * Returns context properties needed at the client side,
	 * current-user data and an Authorization token as a response header.
	 */
	@GetMapping("/context")
	public Mono<Map<String, Object>> getContext(
			@RequestParam Optional<Long> expirationMillis,
			ServerHttpResponse response) {

		logger.debug("Getting context ");
		
		return springReactiveService
			.getContext(expirationMillis, response)
			.doOnNext(context -> {
				logger.debug("Returning context " + context);
			});
	}

	
	/**
	 * Signs up a user, and
	 * returns current-user data and an Authorization token as a response header.
	 */
	@PostMapping("/users")
	@ResponseStatus(HttpStatus.CREATED)
	protected Mono<UserDto> signup(Mono<U> user, ServerHttpResponse response) {
		
		logger.debug("Signing up: " + user);
		
		return userWithToken(springReactiveService.signup(user), response);
	}

	
	/**
	 * Resends verification mail
	 */
	@PostMapping("/users/{id}/resend-verification-mail")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> resendVerificationMail(@PathVariable("id") ID userId) {
		
		logger.debug("Resending verification mail for user " + userId);
		
		return springReactiveService.resendVerificationMail(userId);
	}	


	/**
	 * Verifies current-user
	 */
	@PostMapping("/users/{id}/verification")
	public Mono<UserDto> verifyUser(
			@PathVariable ID id,
			ServerWebExchange exchange) {
		
		logger.debug("Verifying user ...");		
		return userWithToken(springReactiveService.verifyUser(id, exchange.getFormData()),
				exchange.getResponse());
	}

	
	/**
	 * The forgot Password feature
	 */
	@PostMapping("/forgot-password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> forgotPassword(ServerWebExchange exchange) {
		
		logger.debug("Received forgot password request ... " );				
		return springReactiveService.forgotPassword(exchange.getFormData());
	}

	
	/**
	 * Resets password after it's forgotten
	 */
	@PostMapping("/reset-password")
	public Mono<UserDto> resetPassword(
			@RequestBody @Valid Mono<ResetPasswordForm> form,
		    ServerHttpResponse response) {
		
		logger.debug("Resetting password ... ");				
		return userWithToken(springReactiveService.resetPassword(form), response);
	}

	
	/**
	 * Fetches a user by email
	 */
	@PostMapping("/users/fetch-by-email")
	public Mono<U> fetchUserByEmail(ServerWebExchange exchange) {
		
		logger.debug("Fetching user by email ... ");						
		return springReactiveService.fetchUserByEmail(exchange.getFormData());
	}

	
	/**
	 * Fetches a user by ID
	 */	
	@GetMapping("/users/{id}")
	public Mono<U> fetchUserById(@PathVariable ID id) {
		
		logger.debug("Fetching user: " + id);				
		return springReactiveService.fetchUserById(id);
	}


	/**
	 * Updates a user
	 */
	@PatchMapping(value = "/users/{id}")
	public Mono<UserDto> updateUser(
			@PathVariable ID id,
			@RequestBody @NotBlank Mono<String> patch,
			ServerHttpResponse response) {
		
		logger.debug("Updating user ... ");
		return userWithToken(springReactiveService.updateUser(id, patch), response);
	}

	
	/**
	 * Changes password
	 */
	@PostMapping("/users/{id}/password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> changePassword(@PathVariable ID id,
			@RequestBody @Valid Mono<ChangePasswordForm> changePasswordForm,
			ServerHttpResponse response) {
		
		logger.debug("Changing password ... ");
		return userWithToken(springReactiveService.changePassword(id, changePasswordForm), response).then();
	}


	/**
	 * Requests for changing email
	 */
	@PostMapping("/users/{id}/email-change-request")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> requestEmailChange(@PathVariable ID id,
			@RequestBody @Valid	Mono<EmailForm> emailForm) {
		
		logger.debug("Requesting email change ... ");				
		return springReactiveService.requestEmailChange(id, emailForm);
	}	


	/**
	 * Changes the email
	 */
	@PostMapping("/users/{userId}/email")
	public Mono<UserDto> changeEmail(
			@PathVariable ID userId,
			ServerWebExchange exchange) {
		
		logger.debug("Changing email of user ...");
		return userWithToken(springReactiveService.changeEmail(
				userId,
				exchange.getFormData()),
			exchange.getResponse());
	}

	
	/**
	 * Fetch a new token - for session sliding, switch user etc.
	 */
	@PostMapping("/fetch-new-auth-token")
	public Mono<Map<String, String>> fetchNewToken(ServerWebExchange exchange) {
		
		logger.debug("Fetching a new token ... ");
		
		return springReactiveService.fetchNewToken(exchange);
		
		//return LecUtils.mapOf("token", springService.fetchNewToken(expirationMillis, username));
	}
	
	
	/**
	 * Fetch a self-sufficient token with embedded UserDto - for interservice communications
	 */
	@GetMapping("/fetch-full-token")
	public Mono<Map<String, String>> fetchFullToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
		
		logger.debug("Fetching a micro token");
		return springReactiveService.fetchFullToken(authHeader);
	}	

	
	/**
	 * returns the current user and a new authorization token in the response
	 */
	protected Mono<UserDto> userWithToken(Mono<UserDto> userDto,
			ServerHttpResponse response) {
		
		return springReactiveService.userWithToken(userDto, response, jwtExpirationMillis);
	}
}
