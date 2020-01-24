package com.bpwizard.spring.boot.commons.reactive.demo;

import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.ADMIN_ID;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.ADMIN_PASSWORD;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.CLIENT;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.TOKENS;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.UNVERIFIED_ADMIN_ID;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.UNVERIFIED_USER_EMAIL;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.UNVERIFIED_USER_ID;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.USER_ID;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.USER_PASSWORD;
import static com.bpwizard.spring.boot.commons.reactive.demo.controllers.MyController.BASE_URI;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.bpwizard.spring.boot.commons.domain.ChangePasswordForm;
import com.bpwizard.spring.boot.commons.reactive.demo.dto.TestErrorResponse;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;

import reactor.core.publisher.Mono;

public class ChangePasswordTests extends AbstractTests {

	private static final String NEW_PASSWORD = "a-new-password";
	
	private ChangePasswordForm changePasswordForm(String oldPassword) {
		
		ChangePasswordForm form = new ChangePasswordForm();
		form.setOldPassword(oldPassword);
		form.setPassword(NEW_PASSWORD);
		form.setRetypePassword(NEW_PASSWORD);
		
		return form;		
	}
	
	/**
	 * A non-admin user should be able to change his password.
	 */
	@Test
	public void testChangePassword() throws Exception {
		
		CLIENT.post().uri(BASE_URI + "/users/{id}/password", UNVERIFIED_USER_ID)
				.header(HttpHeaders.AUTHORIZATION, TOKENS.get(UNVERIFIED_USER_ID))
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(changePasswordForm(USER_PASSWORD)), ChangePasswordForm.class)
			.exchange()
				.expectStatus().isNoContent()
				.expectHeader().exists(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME);
		
		// Ensure able to login with new password
		testUtils.login(UNVERIFIED_USER_EMAIL, NEW_PASSWORD);
	}

	
	/**
	 * An good admin user should be able to change the password of another user.
	 */
	@Test
	public void testAdminChangePasswordAnotherUser() throws Exception {
		
		CLIENT.post().uri(BASE_URI + "/users/{id}/password", UNVERIFIED_USER_ID)
			.header(HttpHeaders.AUTHORIZATION, TOKENS.get(ADMIN_ID))
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(changePasswordForm(ADMIN_PASSWORD)), ChangePasswordForm.class)
		.exchange()
			.expectStatus().isNoContent()
			.expectHeader().exists(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME);
		
		// Ensure able to login with new password
		testUtils.login(UNVERIFIED_USER_EMAIL, NEW_PASSWORD);
	}

	/**
	 * Providing an unknown id should return 404.
	 */
	@Test
	public void testChangePasswordUnknownId() throws Exception {
		
		CLIENT.post().uri(BASE_URI + "/users/{id}/password", ObjectId.get())
			.header(HttpHeaders.AUTHORIZATION, TOKENS.get(ADMIN_ID))
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(changePasswordForm(ADMIN_PASSWORD)), ChangePasswordForm.class)
		.exchange()
			.expectStatus().isNotFound()
			.expectHeader().doesNotExist(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME);
	}

	/**
	 * A non-admin user should not be able to change others' password.
	 */
	@Test
	public void testChangePasswordAnotherUser() throws Exception {
		
		CLIENT.post().uri(BASE_URI + "/users/{id}/password", UNVERIFIED_USER_ID)
			.header(HttpHeaders.AUTHORIZATION, TOKENS.get(USER_ID))
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(changePasswordForm(USER_PASSWORD)), ChangePasswordForm.class)
		.exchange()
			.expectStatus().isForbidden()
			.expectHeader().doesNotExist(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME);
		
		// Ensure password didn't change
		testUtils.login(UNVERIFIED_USER_EMAIL, USER_PASSWORD);
	}


	/**
	 * A  bad admin user should not be able to change others' password.
	 */
	@Test
	public void testBadAdminChangePasswordAnotherUser() throws Exception {
		
		CLIENT.post().uri(BASE_URI + "/users/{id}/password", UNVERIFIED_USER_ID)
			.header(HttpHeaders.AUTHORIZATION, TOKENS.get(UNVERIFIED_ADMIN_ID))
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(changePasswordForm(ADMIN_PASSWORD)), ChangePasswordForm.class)
		.exchange()
			.expectStatus().isForbidden()
			.expectHeader().doesNotExist(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME);
		
		// Ensure password didn't change
		testUtils.login(UNVERIFIED_USER_EMAIL, USER_PASSWORD);
	}

	
	@Test
	public void testChangePasswordInvalidData() throws Exception {
		
		//@formatter:off
		CLIENT.post().uri(BASE_URI + "/users/{id}/password", UNVERIFIED_USER_ID)
			.header(HttpHeaders.AUTHORIZATION, TOKENS.get(ADMIN_ID))
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(new ChangePasswordForm()), ChangePasswordForm.class)
		.exchange()
			.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
			.expectHeader().doesNotExist(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME)
			.expectBody(TestErrorResponse.class)
			.consumeWith(errorResponseResult -> {				
				assertErrors(errorResponseResult,
						"changePasswordFormMono.oldPassword",
						"changePasswordFormMono.retypePassword",
						"changePasswordFormMono.password");
			});
		//@formatter:on
		
		// Ensure password didn't change
		testUtils.login(UNVERIFIED_USER_EMAIL, USER_PASSWORD);

		// All fields too short
		ChangePasswordForm form = new ChangePasswordForm();
		form.setOldPassword("short");
		form.setPassword("short");
		form.setRetypePassword("short");

		//@formatter:off
		CLIENT.post().uri(BASE_URI + "/users/{id}/password", UNVERIFIED_USER_ID)
			.header(HttpHeaders.AUTHORIZATION, TOKENS.get(ADMIN_ID))
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(form), ChangePasswordForm.class)
		.exchange()
			.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
			.expectHeader().doesNotExist(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME)
			.expectBody(TestErrorResponse.class)
			.consumeWith(errorResponseResult -> {				
				assertErrors(errorResponseResult,
						"changePasswordFormMono.oldPassword",
						"changePasswordFormMono.retypePassword",
						"changePasswordFormMono.password");
			});
		//@formatter:on

		// Ensure password didn't change
		testUtils.login(UNVERIFIED_USER_EMAIL, USER_PASSWORD);

		// different retype-password
		form = changePasswordForm(USER_PASSWORD);
		form.setRetypePassword("different-retype-password");

		//@formatter:off
		CLIENT.post().uri(BASE_URI + "/users/{id}/password", UNVERIFIED_USER_ID)
			.header(HttpHeaders.AUTHORIZATION, TOKENS.get(ADMIN_ID))
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(form), ChangePasswordForm.class)
		.exchange()
			.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
			.expectHeader().doesNotExist(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME)
			.expectBody(TestErrorResponse.class)
			.consumeWith(errorResponseResult -> {				
				assertErrors(errorResponseResult,
						"changePasswordFormMono.retypePassword",
						"changePasswordFormMono.password");
			});
		//@formatter:on
		
		// Ensure password didn't change
		testUtils.login(UNVERIFIED_USER_EMAIL, USER_PASSWORD);
	}
}
