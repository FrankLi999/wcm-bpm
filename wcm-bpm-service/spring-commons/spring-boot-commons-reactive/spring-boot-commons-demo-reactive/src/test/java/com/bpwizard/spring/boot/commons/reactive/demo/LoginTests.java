package com.bpwizard.spring.boot.commons.reactive.demo;

import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.ADMIN_EMAIL;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.ADMIN_ID;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.ADMIN_PASSWORD;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.CLIENT;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.TOKENS;
import static com.bpwizard.spring.boot.commons.reactive.demo.controllers.MyController.BASE_URI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

import org.junit.Test;
import org.springframework.http.HttpHeaders;

import com.bpwizard.spring.boot.commons.reactive.demo.domain.User;
import com.bpwizard.spring.boot.commons.reactive.demo.dto.TestUserDto;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;

public class LoginTests extends AbstractTests {

	@Test
	public void testLogin() {
		
		testUtils.loginResponse(ADMIN_EMAIL, ADMIN_PASSWORD)
			.expectStatus().isOk()
			.expectBody(TestUserDto.class)
			.consumeWith(result -> {
				
				TestUserDto user = result.getResponseBody();
				
				assertEquals(ADMIN_ID, user.getId());
				assertNull(user.getPassword());
				assertEquals("admin@example.com", user.getUsername());
				assertEquals(1, user.getRoles().size());
				assertTrue(user.getRoles().contains("ADMIN"));
				assertEquals("Admin 1", user.getTag().getName());
				assertFalse(user.isUnverified());
				assertFalse(user.isBlocked());
				assertTrue(user.isAdmin());
				assertTrue(user.isGoodUser());
				assertTrue(user.isGoodAdmin());				
			});
	}
	
	
	@Test
	public void testLoginTokenExpiry() throws Exception {
		
		String token = login(ADMIN_EMAIL, ADMIN_PASSWORD, 500L);
		Thread.sleep(501L);
		
		CLIENT.get()
			.uri(BASE_URI + "/ping")
			.header(HttpHeaders.AUTHORIZATION, token)
			.exchange()
			.expectStatus().isUnauthorized();
	}


	@Test
	public void testObsoleteToken() throws Exception {
		
		User user = mongoTemplate.findById(ADMIN_ID, User.class);
		user.setCredentialsUpdatedMillis(System.currentTimeMillis());
		mongoTemplate.save(user);
		
		CLIENT.get()
			.uri(BASE_URI + "/ping")
			.header(HttpHeaders.AUTHORIZATION, TOKENS.get(ADMIN_ID))
			.exchange()
			.expectStatus().isUnauthorized();
	}

	
	@Test
	public void testLoginWrongPassword() throws Exception {
		
		testUtils.loginResponse(ADMIN_EMAIL, "wrong-password")
			.expectStatus().isUnauthorized();
	}

	
	@Test
	public void testLoginBlankPassword() throws Exception {
		
		testUtils.loginResponse(ADMIN_EMAIL, "")
			.expectStatus().isUnauthorized();
	}

	
	@Test
	public void testTokenLogin() {
		
		CLIENT.get().uri(BASE_URI + "/context")
			.header(HttpHeaders.AUTHORIZATION, TOKENS.get(ADMIN_ID))
			.exchange()
				.expectStatus().isOk()
				.expectBody()
					.jsonPath("$.user.id").isEqualTo(ADMIN_ID.toString());
	}

	
	@Test
	public void testTokenLoginWrongToken() {
		
		CLIENT.get().uri(BASE_URI + "/context")
			.header(HttpHeaders.AUTHORIZATION, "Bearer a-wrong-token")
			.exchange()
				.expectStatus().isUnauthorized();
	}

	
	@Test
	public void testLogout() throws Exception {
		
		CLIENT.post().uri("/logout")
		.exchange()
			.expectStatus().isNotFound();
	}

	
	private String login(String username, String password, long expirationMillis) {
		
    	return CLIENT.post()
                .uri(BASE_URI + "/login")
                .body(fromFormData("username", username)
                		     .with("password", password)
                		     .with("expirationMillis", Long.toString(expirationMillis)))
                .exchange()
            	.returnResult(TestUserDto.class)
            	.getResponseHeaders()
            	.getFirst(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME);
	}
}
