package com.bpwizard.spring.boot.commons.reactive.demo;

import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.ADMIN_EMAIL;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.ADMIN_ID;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.CLIENT;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.TOKENS;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.UNVERIFIED_USER_EMAIL;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.UNVERIFIED_USER_ID;
import static com.bpwizard.spring.boot.commons.reactive.demo.controllers.MyController.BASE_URI;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import com.bpwizard.spring.boot.commons.reactive.demo.dto.TestToken;

public class FetchNewTokenTests extends AbstractTests {

	
	@Test
	public void testFetchNewToken() throws Exception {
		
		CLIENT.post().uri(BASE_URI + "/fetch-new-auth-token")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.header(HttpHeaders.AUTHORIZATION, TOKENS.get(UNVERIFIED_USER_ID))
			.exchange()
			.expectStatus().isOk()
			.expectBody(TestToken.class)
			.consumeWith(this::ensureTokenWorks);
	}

	
	@Test
	public void testFetchNewTokenExpiration() throws Exception {
		
		CLIENT.post().uri(BASE_URI + "/fetch-new-auth-token")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		.header(HttpHeaders.AUTHORIZATION, TOKENS.get(UNVERIFIED_USER_ID))
		.body(fromFormData("expirationMillis", "1000"))
		.exchange()
		.expectStatus().isOk()
		.expectBody(TestToken.class)
		.consumeWith(result -> {
			
			ensureTokenWorks(result);
			TestToken token = result.getResponseBody();
			
			try {
				Thread.sleep(1001L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			CLIENT.get()
				.uri(BASE_URI + "/context")
					.header(HttpHeaders.AUTHORIZATION, token.getToken())
					.exchange()
				.expectStatus().isUnauthorized();			
		});
	}
	
	@Test
	public void testFetchNewTokenByAdminForAnotherUser() throws Exception {
		
		CLIENT.post().uri(BASE_URI + "/fetch-new-auth-token")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		.header(HttpHeaders.AUTHORIZATION, TOKENS.get(ADMIN_ID))
		.body(fromFormData("username", UNVERIFIED_USER_EMAIL))
		.exchange()
		.expectStatus().isOk()
		.expectBody(TestToken.class)
		.consumeWith(this::ensureTokenWorks);
	}
	
	
	@Test
	public void testFetchNewTokenByNonAdminForAnotherUser() throws Exception {
		
		CLIENT.post().uri(BASE_URI + "/fetch-new-auth-token")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		.header(HttpHeaders.AUTHORIZATION, TOKENS.get(UNVERIFIED_USER_ID))
		.body(fromFormData("username", ADMIN_EMAIL))
		.exchange()
		.expectStatus().isForbidden()
		.expectBody().jsonPath("$.token").doesNotExist();
	}

	
	private void ensureTokenWorks(EntityExchangeResult<TestToken> result) {
		
		TestToken token = result.getResponseBody();
		Assertions.assertNotNull(token.getToken());

		testUtils.contextResponse(token.getToken())
			.expectBody()
			.jsonPath("$.user.id").isEqualTo(UNVERIFIED_USER_ID.toString());
	}
}
