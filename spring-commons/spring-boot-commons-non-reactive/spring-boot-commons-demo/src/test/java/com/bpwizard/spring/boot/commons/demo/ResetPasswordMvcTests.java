package com.bpwizard.spring.boot.commons.demo;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.bpwizard.spring.boot.commons.domain.ResetPasswordForm;
import com.bpwizard.spring.boot.commons.security.JSONWebEncryptionService;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ResetPasswordMvcTests extends AbstractMvcTests {
	
	private String forgotPasswordCode;
	
	@Autowired
	private JSONWebEncryptionService jweTokenService;
	
	@BeforeEach
	public void setUp() {
		
		forgotPasswordCode = jweTokenService.createToken(
				JSONWebEncryptionService.FORGOT_PASSWORD_AUDIENCE,
				ADMIN_EMAIL, 60000L);
	}

	@Test
	public void testResetPassword() throws Exception {
		
		final String NEW_PASSWORD = "newPassword!";
		
		//Thread.sleep(1001L);
		
		mvc.perform(post("/api/core/reset-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(form(forgotPasswordCode, NEW_PASSWORD)))
		        .andExpect(status().is(200))
				.andExpect(header().string(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME, containsString(".")))
				.andExpect(jsonPath("$.id").value(ADMIN_ID));
		
		// New password should work
		login(ADMIN_EMAIL, NEW_PASSWORD);
		
	    // Repeating shouldn't work
		mvc.perform(post("/api/core/reset-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(form(forgotPasswordCode, NEW_PASSWORD)))
		        .andExpect(status().is(401));
	}
	
	@Test
	public void testResetPasswordInvalidData() throws Exception {
		
		// Wrong code
		mvc.perform(post("/api/core/reset-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(form("wrong-code", "abc99!")))
		        .andExpect(status().is(401));

		// Blank password
		mvc.perform(post("/api/core/reset-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(form(forgotPasswordCode, "")))
		        .andExpect(status().is(422));

		// Invalid password
		mvc.perform(post("/api/core/reset-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(form(forgotPasswordCode, "abc")))
		        .andExpect(status().is(422));
	}
	
	private String form(String code, String newPassword) throws JsonProcessingException {
		
		ResetPasswordForm form = new ResetPasswordForm();
		form.setCode(code);
		form.setNewPassword(newPassword);
		
		return SecurityUtils.toJson(form);
	}
}
