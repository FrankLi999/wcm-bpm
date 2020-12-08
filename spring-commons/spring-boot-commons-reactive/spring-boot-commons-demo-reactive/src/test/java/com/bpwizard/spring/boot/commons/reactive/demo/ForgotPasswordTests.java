package com.bpwizard.spring.boot.commons.reactive.demo;

import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.ADMIN_EMAIL;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.CLIENT;
import static com.bpwizard.spring.boot.commons.reactive.demo.controllers.MyController.BASE_URI;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ForgotPasswordTests extends AbstractTests {

	@Test
	public void testForgotPassword() {
		
		CLIENT.post().uri(BASE_URI + "/forgot-password")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		.body(fromFormData("email", ADMIN_EMAIL))
		.exchange()
		.expectStatus().isNoContent();

		verify(mailSender).send(any());
	}
	
	@Test
	public void testForgotPasswordInvalidEmail() throws Exception {
		
		// Unknown email
		CLIENT.post().uri(BASE_URI + "/forgot-password")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		.body(fromFormData("email", "unknown@example.com"))
		.exchange()
		.expectStatus().isNotFound();

		// Null email
		CLIENT.post().uri(BASE_URI + "/forgot-password")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		.exchange()
		.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

		// Blank email
		CLIENT.post().uri(BASE_URI + "/forgot-password")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		.body(fromFormData("email", ""))
		.exchange()
		.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
		
		// Wrong email format
		CLIENT.post().uri(BASE_URI + "/forgot-password")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		.body(fromFormData("email", "wrong-email-format"))
		.exchange()
		.expectStatus().isNotFound();
		
		verify(mailSender, never()).send(any());
	}
}
