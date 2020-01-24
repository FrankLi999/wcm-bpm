package com.bpwizard.spring.boot.commons.reactive.demo;

import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.ADMIN_ID;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.CLIENT;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.TOKENS;
import static com.bpwizard.spring.boot.commons.reactive.demo.controllers.MyController.BASE_URI;

import org.junit.Test;

import com.bpwizard.spring.boot.commons.util.SecurityUtils;

public class BasicTests extends AbstractTests {

	@Test
	public void testPing() throws Exception {
		
		CLIENT.get()
			.uri(BASE_URI + "/ping")
			.exchange()
			.expectStatus()
				.isNoContent();
	}

	@Test
	public void testGetContextLoggedIn() throws Exception {
		
		testUtils.contextResponse(TOKENS.get(ADMIN_ID))
				.expectHeader().exists(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME)
				.expectBody()
					.jsonPath("$.context.reCaptchaSiteKey").exists()
					.jsonPath("$.user.id").isEqualTo(ADMIN_ID.toString())
					.jsonPath("$.user.roles[0]").isEqualTo("ADMIN")
					.jsonPath("$.user.password").doesNotExist();
	}
	
	@Test
	public void testGetContextWithoutLoggedIn() throws Exception {
		
		CLIENT.get()
		.uri(BASE_URI + "/context")
			.exchange()
			.expectStatus().isOk()
			.expectHeader().doesNotExist(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME)
			.expectBody()
				.jsonPath("$.context.reCaptchaSiteKey").exists()
				.jsonPath("$.user").doesNotExist();
	}	

}
