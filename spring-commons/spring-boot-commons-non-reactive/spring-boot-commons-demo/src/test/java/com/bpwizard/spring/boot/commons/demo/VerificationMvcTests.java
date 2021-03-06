package com.bpwizard.spring.boot.commons.demo;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.bpwizard.spring.boot.commons.security.JSONWebEncryptionService;
import com.bpwizard.spring.boot.commons.service.domain.User;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;

public class VerificationMvcTests extends AbstractMvcTests {
	
	private String verificationCode;
	
	@Autowired
	private JSONWebEncryptionService jweTokenService;
	
	@BeforeEach
	public void setUp() {
		
		verificationCode = jweTokenService.createToken(JSONWebEncryptionService.VERIFY_AUDIENCE,
				Long.toString(UNVERIFIED_USER_ID), 60000L,
				SecurityUtils.mapOf("email", UNVERIFIED_USER_EMAIL));
	}
	
	@Test
	public void testEmailVerification() throws Exception {
		
		mvc.perform(post("/api/core/users/{userId}/verification", UNVERIFIED_USER_ID)
                .param("code", verificationCode)
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(200))
				.andExpect(header().string(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME, containsString(".")))
				.andExpect(jsonPath("$.id").value(UNVERIFIED_USER_ID))
				.andExpect(jsonPath("$.roles").value(hasSize(0)))
				.andExpect(jsonPath("$.unverified").value(false))
				.andExpect(jsonPath("$.goodUser").value(true));
		
		// Already verified
		mvc.perform(post("/api/core/users/{userId}/verification", UNVERIFIED_USER_ID)
                .param("code", verificationCode)
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(422));
	}
	
	@Test
	public void testEmailVerificationNonExistingUser() throws Exception {
		
		mvc.perform(post("/api/core/users/99/verification")
                .param("code", verificationCode)
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(404));
	}
	
	@Test
	public void testEmailVerificationWrongToken() throws Exception {
		
		// null token
		mvc.perform(post("/api/core/users/{userId}/verification", UNVERIFIED_USER_ID)
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(400));

		// blank token
		mvc.perform(post("/api/core/users/{userId}/verification", UNVERIFIED_USER_ID)
                .param("code", "")
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(401));

		// Wrong audience
		String token = jweTokenService.createToken("wrong-audience",
				Long.toString(UNVERIFIED_USER_ID), 60000L,
				SecurityUtils.mapOf("email", UNVERIFIED_USER_EMAIL));
		mvc.perform(post("/api/core/users/{userId}/verification", UNVERIFIED_USER_ID)
                .param("code", token)
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(401));
		
		// Wrong email
		token = jweTokenService.createToken(JSONWebEncryptionService.VERIFY_AUDIENCE,
				Long.toString(UNVERIFIED_USER_ID), 60000L,
				SecurityUtils.mapOf("email", "wrong.email@example.com"));
		mvc.perform(post("/api/core/users/{userId}/verification", UNVERIFIED_USER_ID)
                .param("code", token)
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(403));

		// expired token
		token = jweTokenService.createToken(JSONWebEncryptionService.VERIFY_AUDIENCE,
				Long.toString(UNVERIFIED_USER_ID), 1L,
				SecurityUtils.mapOf("email", UNVERIFIED_USER_EMAIL));	
		// Thread.sleep(1001L);
		mvc.perform(post("/api/core/users/{userId}/verification", UNVERIFIED_USER_ID)
                .param("code", token)
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(401));
	}
	
	@Test
	public void testEmailVerificationAfterCredentialsUpdate() throws Exception {
		
		// Credentials updated after the verification token is issued
		Thread.sleep(1L);
		User user = userService.findById(UNVERIFIED_USER_ID).get();
		user.setCredentialsUpdatedMillis(System.currentTimeMillis());
		userService.save(user, null, null);
		
		mvc.perform(post("/api/core/users/{userId}/verification", UNVERIFIED_USER_ID)
                .param("code", verificationCode)
                .header("contentType",  MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is(401));
	}
}
