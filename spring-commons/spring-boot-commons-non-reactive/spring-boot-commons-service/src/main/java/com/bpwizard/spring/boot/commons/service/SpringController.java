package com.bpwizard.spring.boot.commons.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.domain.ChangePasswordForm;
import com.bpwizard.spring.boot.commons.domain.ResetPasswordForm;
import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.service.domain.AbstractUser;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.bpwizard.spring.boot.commons.util.UserUtils;
import com.bpwizard.spring.boot.commons.web.util.WebUtils;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;

/**
 * The Spring Commons API. See the
 * <a href="https://github.com/bpwizard/spring-commons#documentation-and-resources">
 * API documentation</a> for details.
 * 
 * @author Sanjay Patel
 */
public abstract class SpringController
	<U extends AbstractUser<ID>, ID extends Serializable> {

	private static final Logger log = LogManager.getLogger(SpringController.class);

    // private long jwtExpirationMillis;
    
    @Autowired
	protected SpringService<U, ID> springService;
    
    @Autowired
    SpringProperties properties;
	
//    @Autowired
//	public void createSpringController(
//			SpringProperties properties,
//			SpringService<U, ID> springService) {
//		this.jwtExpirationMillis = properties.getJwt().getExpirationMillis();
//		this.springService = springService;
//		
//		log.info("Created");
//	}


	/**
	 * A simple function for pinging this server.
	 */
	@GetMapping("/ping")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ping() {
		
		log.debug("Received a ping");
	}
	
	
	/**
	 * Returns context properties needed at the client side,
	 * current-user data and an Authorization token as a response header.
	 */
	@GetMapping("/context")
	public Map<String, Object> getContext(
			@RequestParam Optional<Long> expirationMillis,
			HttpServletResponse response) {

		log.debug("Getting context ");
		Map<String, Object> context = springService.getContext(expirationMillis, response);
		log.debug("Returning context: " + context);

		return context;
	}
	
	/**
	 * Signs up a user, and
	 * returns current-user data and an Authorization token as a response header.
	 */
	@PostMapping("/users")
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto signup(@RequestBody @JsonView(UserUtils.SignupInput.class) U user,
			HttpServletResponse response) {
		
		log.debug("Signing up: " + user);
		springService.signup(user);
		log.debug("Signed up: " + user);

		return userWithToken(response);
	}
	
	
	/**
	 * Resends verification mail
	 */
	@PostMapping("/users/{id}/resend-verification-mail")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void resendVerificationMail(@PathVariable("id") U user) {
		
		log.debug("Resending verification mail for: " + user);
		springService.resendVerificationMail(user);
		log.debug("Resent verification mail for: " + user);
	}	


	/**
	 * Verifies current-user
	 */
	@PostMapping("/users/{id}/verification")
	public UserDto verifyUser(
			@PathVariable("id") ID id,
			@RequestParam("code") String code,
			HttpServletResponse response) {
		
		log.debug("Verifying user ...");		
		springService.verifyUser(id, code);
		
		return userWithToken(response);
	}
	

	/**
	 * The forgot Password feature
	 */
	@PostMapping("/forgot-password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void forgotPassword(@RequestParam("email") String email) {
		
		log.debug("Received forgot password request for: " + email);				
		springService.forgotPassword(email);
	}
	

	/**
	 * Resets password after it's forgotten
	 */
	@PostMapping("/reset-password")
	public UserDto resetPassword(
			@RequestBody ResetPasswordForm form,
			HttpServletResponse response) {
		
		log.debug("Resetting password ... ");				
		springService.resetPassword(form);
		
		return userWithToken(response);
	}


	/**
	 * Fetches a user by email
	 */
	@PostMapping("/users/fetch-by-email")
	public U fetchUserByEmail(@RequestParam("email") String email) {
		
		log.debug("Fetching user by email: " + email);						
		return springService.fetchUserByEmail(email);
	}

	
	/**
	 * Fetches a user by ID
	 */	
	@GetMapping("/users/{id}")
	public U fetchUserById(@PathVariable("id") U user) {
		
		log.debug("Fetching user: " + user);				
		return springService.processUser(user);
	}

	
	/**
	 * Updates a user
	 */
	@PatchMapping("/users/{id}")
	public UserDto updateUser(
			@PathVariable("id") U user,
			@RequestBody String patch,
			HttpServletResponse response)
			throws JsonProcessingException, IOException, JsonPatchException {
		
		log.debug("Updating user ... ");
		
		// ensure that the user exists
		SpringExceptionUtils.ensureFound(user);
		U updatedUser = SecurityUtils.applyPatch(user, patch); // create a patched form
		UserDto userDto = springService.updateUser(user, updatedUser);
		
		// Send a new token for logged in user in the response
		userWithToken(response);
		
		// Send updated user data in the response
		return userDto;
	}
	
	
	/**
	 * Changes password
	 */
	@PostMapping("/users/{id}/password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void changePassword(@PathVariable("id") String id,
			@RequestBody ChangePasswordForm changePasswordForm,
			HttpServletResponse response) {
		
		//TODO: what exception to throw
		U user = this.springService.findUserById(id).orElseThrow(RuntimeException::new);
		log.debug("Changing password ... ");				
		String username = springService.changePassword(user, changePasswordForm);
		
		springService.addAuthHeader(response, username, properties.getJwt().getExpirationMillis());
	}


	/**
	 * Requests for changing email
	 */
	@PostMapping("/users/{id}/email-change-request")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void requestEmailChange(@PathVariable("id") U user,
								   @RequestBody U updatedUser) {
		
		log.debug("Requesting email change ... ");				
		springService.requestEmailChange(user, updatedUser);
	}


	/**
	 * Changes the email
	 */
	@PostMapping("/users/{userId}/email")
	public UserDto changeEmail(
			@PathVariable("userId") ID userId,
			@RequestParam("code") String code,
			HttpServletResponse response) {
		
		log.debug("Changing email of user ...");		
		springService.changeEmail(userId, code);
		
		// return the currently logged in user with new email
		return userWithToken(response);		
	}


	/**
	 * Fetch a new token - for session sliding, switch user etc.
	 */
	@PostMapping("/fetch-new-auth-token")
	public Map<String, String> fetchNewToken(
			@RequestParam(name="expirationMillis", required=false) Optional<Long> expirationMillis,
			@RequestParam(name="username", required=false) Optional<String> username,
			HttpServletResponse response) {
		
		log.debug("Fetching a new token ... ");
		return SecurityUtils.mapOf("token", springService.fetchNewToken(expirationMillis, username));
	}


	/**
	 * Fetch a self-sufficient token with embedded UserDto - for interservice communications
	 */
	@GetMapping("/fetch-full-token")
	public Map<String, String> fetchFullToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
		
		log.debug("Fetching a micro token");
		return springService.fetchFullToken(authHeader);
	}	

	
	/**
	 * returns the current user and a new authorization token in the response
	 */
	protected UserDto userWithToken(HttpServletResponse response) {
		
		UserDto currentUser = WebUtils.currentUser();
		springService.addAuthHeader(response, currentUser.getUsername(), properties.getJwt().getExpirationMillis());
		return currentUser;
	}
}
