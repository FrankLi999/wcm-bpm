package com.bpwizard.spring.boot.commons.reactive.demo;

import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.CLIENT;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.UNVERIFIED_USER_EMAIL;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.UNVERIFIED_USER_ID;
import static com.bpwizard.spring.boot.commons.reactive.demo.controllers.MyController.BASE_URI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.bpwizard.spring.boot.commons.reactive.demo.dto.TestUserDto;
import com.bpwizard.spring.boot.commons.security.JSONWebEncryptionService;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;

public class VerificationTests extends AbstractTests {

	private String verificationCode;
	
	@Autowired
	private JSONWebEncryptionService jweTokenService;
	
	@Before
	public void setUp() {
		
		verificationCode = jweTokenService.createToken(JSONWebEncryptionService.VERIFY_AUDIENCE,
				UNVERIFIED_USER_ID.toString(), 60000L,
				SecurityUtils.mapOf("email", UNVERIFIED_USER_EMAIL));
	}
	
	@Test
	public void testEmailVerification() throws Exception {
		
		emailVerification(UNVERIFIED_USER_ID, verificationCode)
		.expectStatus().isOk()
		.expectHeader().exists(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME)
		.expectBody(TestUserDto.class)
		.consumeWith(result -> {
			
			TestUserDto userDto = result.getResponseBody();
			
			assertEquals(UNVERIFIED_USER_ID, userDto.getId());
			assertEquals(0, userDto.getRoles().size());
			assertTrue(userDto.isGoodUser());
			assertFalse(userDto.isUnverified());
		});		

		// Already verified
		emailVerification(UNVERIFIED_USER_ID, verificationCode)
		.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@Test
	public void testEmailVerificationNonExistingUser() throws Exception {
		
		emailVerification(ObjectId.get(), verificationCode)
		.expectStatus().isNotFound();
	}

	@Test
	public void testEmailVerificationWrongToken() throws Exception {
		
		// null token
		CLIENT.post().uri(BASE_URI + "/users/{id}/verification", UNVERIFIED_USER_ID)
	        .exchange()
	        .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

		// blank token
		emailVerification(UNVERIFIED_USER_ID, "")
		.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

		// Wrong audience
		String token = jweTokenService.createToken("wrong-audience",
				UNVERIFIED_USER_ID.toString(), 60000L,
				SecurityUtils.mapOf("email", UNVERIFIED_USER_EMAIL));
		emailVerification(UNVERIFIED_USER_ID, token)
		.expectStatus().isUnauthorized();
		
		// Wrong email
		token = jweTokenService.createToken(JSONWebEncryptionService.VERIFY_AUDIENCE,
				UNVERIFIED_USER_ID.toString(), 60000L,
				SecurityUtils.mapOf("email", "wrong.email@example.com"));
		emailVerification(UNVERIFIED_USER_ID, token)
		.expectStatus().isForbidden();

		// expired token
		token = jweTokenService.createToken(JSONWebEncryptionService.VERIFY_AUDIENCE,
				UNVERIFIED_USER_ID.toString(), 1L,
				SecurityUtils.mapOf("email", UNVERIFIED_USER_EMAIL));	
		emailVerification(UNVERIFIED_USER_ID, token)
		.expectStatus().isUnauthorized();
	}

	private ResponseSpec emailVerification(ObjectId userId, String code) {
		
		return CLIENT.post().uri(BASE_URI + "/users/{id}/verification", userId)
	        .body(fromFormData("code", code))
        .exchange();
	}
}
