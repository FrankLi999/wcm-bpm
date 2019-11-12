package com.bpwizard.spring.boot.commons.reactive.demo;

import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.ADMIN_EMAIL;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.CLIENT;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.UNVERIFIED_USER_ID;
import static com.bpwizard.spring.boot.commons.reactive.demo.controllers.MyController.BASE_URI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.bpwizard.spring.boot.commons.reactive.demo.domain.User;
import com.bpwizard.spring.boot.commons.reactive.demo.dto.TestErrorResponse;
import com.bpwizard.spring.boot.commons.reactive.demo.dto.TestSpringFieldError;
import com.bpwizard.spring.boot.commons.reactive.demo.dto.TestUser;
import com.bpwizard.spring.boot.commons.reactive.demo.dto.TestUserDto;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;

import reactor.core.publisher.Mono;

public class SignupTests extends AbstractTests {

	@Test
	public void testSignup() throws Exception {
		
		signup("user.foo@example.com", "user123", "User Foo")
		.expectStatus().isCreated()
		.expectHeader().exists(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME)
		.expectBody(TestUserDto.class)
		.consumeWith(result -> {			
			TestUserDto userDto = result.getResponseBody();
			assertNotNull(userDto.getId());
			assertNull(userDto.getPassword());
			assertEquals("user.foo@example.com", userDto.getUsername());
			assertEquals(1, userDto.getRoles().size());
			assertTrue(userDto.getRoles().contains("UNVERIFIED"));
			assertEquals("User Foo", userDto.getTag().getName());
			assertTrue(userDto.isUnverified());
			assertFalse(userDto.isBlocked());
			assertFalse(userDto.isAdmin());
			assertFalse(userDto.isGoodUser());
			assertFalse(userDto.isGoodAdmin());
		});
				
		verify(mailSender).send(any());

		// Ensure that password got encrypted
		assertNotEquals("user123",
			mongoTemplate.findOne(query(where("email").is("user.foo@example.com")),
					User.class).getPassword());
	}

	@Test
	public void testSignupWithInvalidData() throws Exception {
		
		signup("abc", "user1", null)
			.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
			.expectBody(TestErrorResponse.class)
			.consumeWith(errorResponseResult -> {				
				assertErrors(errorResponseResult,
						"userMono.email",
						"userMono.email",
						"userMono.password",
						"userMono.name");
				Collection<TestSpringFieldError> errors = errorResponseResult.getResponseBody().getErrors();
				assertTrue(errors.stream()
						.map(TestSpringFieldError::getCode).collect(Collectors.toSet())
						.containsAll(Arrays.asList(
								"NotBlank",
								"Size",
								"Email")));
				
				assertTrue(errors.stream()
						.map(TestSpringFieldError::getMessage).collect(Collectors.toSet())
						.containsAll(Arrays.asList(
								"Not a well formed email address",
								"Name required",
								"Email must be between 4 and 250 characters",
								"Password must be between 6 and 50 characters")));
			});
		
		verify(mailSender, never()).send(any());
	}
	
	@Test
	public void testSignupDuplicateEmail() throws Exception {
		
		signup(ADMIN_EMAIL, "user123", "User Foo")
			.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

		verify(mailSender, never()).send(any());
	}

	private ResponseSpec signup(String email, String password, String name) {
		
		TestUser user = new TestUser(email, password, name);
		
		return CLIENT.post().uri(BASE_URI + "/users", UNVERIFIED_USER_ID)
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(user), TestUser.class)
		.exchange();
	}

}
