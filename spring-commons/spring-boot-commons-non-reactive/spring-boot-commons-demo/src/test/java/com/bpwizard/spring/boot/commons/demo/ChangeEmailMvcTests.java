package com.bpwizard.spring.boot.commons.demo;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.bpwizard.spring.boot.commons.security.JSONWebEncryptionService;
import com.bpwizard.spring.boot.commons.service.domain.User;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;

public class ChangeEmailMvcTests extends AbstractMvcTests {
	
	private static final String NEW_EMAIL = "new.email@example.com";

	private String changeEmailCode;
	
	@Autowired
	private JSONWebEncryptionService jweTokenService;
	
	@BeforeEach
	public void setUp() {
		
		User<Long> user = userService.findById(UNVERIFIED_USER_ID).get();
		user.setNewEmail(NEW_EMAIL);
		userService.save(user, null, null);
		
		changeEmailCode = jweTokenService.createToken(
				JSONWebEncryptionService.CHANGE_EMAIL_AUDIENCE,
				Long.toString(UNVERIFIED_USER_ID), 60000L,
				SecurityUtils.mapOf("newEmail", NEW_EMAIL));
	}

	@Test
	public void testChangeEmail() throws Exception {
		
		mvc.perform(post("/api/core/users/{id}/email", UNVERIFIED_USER_ID)
                .param("code", changeEmailCode)
				.header(HttpHeaders.AUTHORIZATION, tokens.get(UNVERIFIED_USER_ID))
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
		        .andExpect(status().is(200))
				.andExpect(header().string(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME, containsString(".")))
				.andExpect(jsonPath("$.id").value(UNVERIFIED_USER_ID));
		
		User updatedUser = userService.findById(UNVERIFIED_USER_ID).get();
		Assertions.assertNull(updatedUser.getNewEmail());
		Assertions.assertEquals(NEW_EMAIL, updatedUser.getEmail());
		
		// Shouldn't be able to login with old token
		mvc.perform(post("/api/core/users/{id}/email", UNVERIFIED_USER_ID)
                .param("code", changeEmailCode)
				.header(HttpHeaders.AUTHORIZATION, tokens.get(UNVERIFIED_USER_ID))
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
		        .andExpect(status().is(401));
	}
	
    /**
     * Providing a wrong changeEmailCode shouldn't work.
     */
	@Test
	public void testChangeEmailWrongCode() throws Exception {
		
		// Blank token
		mvc.perform(post("/api/core/users/{id}/email", UNVERIFIED_USER_ID)
                .param("code", "")
				.header(HttpHeaders.AUTHORIZATION, tokens.get(UNVERIFIED_USER_ID))
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
		        .andExpect(status().is(422));

		// Wrong audience
		String code = jweTokenService.createToken(
				"", // blank audience
				Long.toString(UNVERIFIED_USER_ID), 60000L,
				SecurityUtils.mapOf("newEmail", NEW_EMAIL));
		
		mvc.perform(post("/api/core/users/{id}/email", UNVERIFIED_USER_ID)
                .param("code", code)
				.header(HttpHeaders.AUTHORIZATION, tokens.get(UNVERIFIED_USER_ID))
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
		        .andExpect(status().is(401));

		// Wrong userId subject
		code = jweTokenService.createToken(
				JSONWebEncryptionService.CHANGE_EMAIL_AUDIENCE,
				Long.toString(ADMIN_ID), 60000L,
				SecurityUtils.mapOf("newEmail", NEW_EMAIL));
		
		mvc.perform(post("/api/core/users/{id}/email", UNVERIFIED_USER_ID)
                .param("code", code)
				.header(HttpHeaders.AUTHORIZATION, tokens.get(UNVERIFIED_USER_ID))
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
		        .andExpect(status().is(403));
		
		// Wrong new email
		code = jweTokenService.createToken(
				JSONWebEncryptionService.CHANGE_EMAIL_AUDIENCE,
				Long.toString(UNVERIFIED_USER_ID), 60000L,
				SecurityUtils.mapOf("newEmail", "wrong.new.email@example.com"));
		
		mvc.perform(post("/api/core/users/{id}/email", UNVERIFIED_USER_ID)
                .param("code", code)
				.header(HttpHeaders.AUTHORIZATION, tokens.get(UNVERIFIED_USER_ID))
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
		        .andExpect(status().is(403));
	}
	
    /**
     * Providing an obsolete changeEmailCode shouldn't work.
     */
	@Test
	public void testChangeEmailObsoleteCode() throws Exception {

		// credentials updated after the request for email change was made
		Thread.sleep(1L);
		User user = userService.findById(UNVERIFIED_USER_ID).get();
		user.setCredentialsUpdatedMillis(System.currentTimeMillis());
		userService.save(user, null, null);
		
		// A new auth token is needed, because old one would be obsolete!
		String authToken = login(UNVERIFIED_USER_EMAIL, USER_PASSWORD);
		
		// now ready to test!
		mvc.perform(post("/api/core/users/{id}/email", UNVERIFIED_USER_ID)
                .param("code", changeEmailCode)
				.header(HttpHeaders.AUTHORIZATION, authToken)
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
		        .andExpect(status().is(401));	
	}
	
	/**
     * Trying without having requested first.
	 * @throws Exception 
     */
	@Test
	public void testChangeEmailWithoutAnyRequest() throws Exception {

		mvc.perform(post("/api/core/users/{id}/email", USER_ID)
                .param("code", changeEmailCode)
				.header(HttpHeaders.AUTHORIZATION, tokens.get(USER_ID))
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
		        .andExpect(status().is(422));
	}
	
    /**
     * Trying after some user registers the newEmail, leaving it non unique.
     * @throws Exception 
     */
	@Test
	public void testChangeEmailNonUniqueEmail() throws Exception {
		
		// Some other user changed to the same email
		User user = userService.findById(ADMIN_ID).get();
		user.setEmail(NEW_EMAIL);
		userService.save(user, null, null);
		
		mvc.perform(post("/api/core/users/{id}/email", UNVERIFIED_USER_ID)
                .param("code", changeEmailCode)
				.header(HttpHeaders.AUTHORIZATION, tokens.get(UNVERIFIED_USER_ID))
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
		        .andExpect(status().is(422));
	}
}
