package com.bpwizard.spring.boot.commons.reactive.demo;

import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.ADMIN_ID;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.BLOCKED_ADMIN_ID;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.CLIENT;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.TOKENS;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.UNVERIFIED_ADMIN_ID;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.UNVERIFIED_USER_EMAIL;
import static com.bpwizard.spring.boot.commons.reactive.demo.MyTestUtils.UNVERIFIED_USER_ID;
import static com.bpwizard.spring.boot.commons.reactive.demo.controllers.MyController.BASE_URI;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;

import com.bpwizard.spring.boot.commons.reactive.demo.domain.User;
import com.bpwizard.spring.boot.commons.reactive.demo.dto.TestUserDto;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.bpwizard.spring.boot.commons.util.UserUtils;

public class UpdateUserTests extends AbstractTests {

	private static final String UPDATED_NAME = "Edited name";
	
	@Value("classpath:/update-user/patch-update-user.json")
    private Resource userPatch;
	
	@Value("classpath:/update-user/patch-admin-role.json")
    private Resource userPatchAdminRole;
    
	@Value("classpath:/update-user/patch-null-name.json")
    private Resource userPatchNullName;

	@Value("classpath:/update-user/patch-long-name.json")
	private Resource userPatchLongName;

	/**
	 * A non-admin user should be able to update his own name,
	 * but changes in roles should be skipped.
	 * The name of security principal object should also
	 * change in the process.
	 * @throws Exception 
	 */
	@Test
    public void testUpdateSelf() throws Exception {
		
		updateUser(UNVERIFIED_USER_ID, UNVERIFIED_USER_ID, userPatch)
		.expectStatus().isOk()
			.expectHeader().exists(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME)
			.expectBody(TestUserDto.class)
			.consumeWith(result -> {
				
				TestUserDto userDto = result.getResponseBody();
				
				Assertions.assertEquals(UNVERIFIED_USER_ID, userDto.getId());
				Assertions.assertEquals(UNVERIFIED_USER_EMAIL, userDto.getUsername());
				Assertions.assertEquals(UPDATED_NAME, userDto.getTag().getName());
				Assertions.assertEquals(1, userDto.getRoles().size());
				Assertions.assertTrue(userDto.getRoles().contains(UserUtils.Role.UNVERIFIED));
			});
		
		User user = mongoTemplate.findById(UNVERIFIED_USER_ID, User.class);
		
		// Ensure that data changed properly
		Assertions.assertEquals(UNVERIFIED_USER_EMAIL, user.getEmail());
		Assertions.assertEquals(1, user.getRoleNames().size());
		Assertions.assertTrue(user.getRoleNames().contains(UserUtils.Role.UNVERIFIED));
		Assertions.assertEquals(1L, user.getVersion().longValue());
		
		// Version mismatch
		updateUser(UNVERIFIED_USER_ID, UNVERIFIED_USER_ID, userPatch)
		.expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }
	
	
	/**
	 * A good ADMIN should be able to update another user's name and roles.
	 * The name of security principal object should NOT change in the process,
	 * and the verification code should get set/unset on addition/deletion of
	 * the UNVERIFIED role. 
	 * @throws Exception 
	 */
	@Test
    public void testGoodAdminCanUpdateOther() throws Exception {
		
		updateUser(UNVERIFIED_USER_ID, ADMIN_ID, userPatch)
		.expectStatus().isOk()
			.expectHeader().exists(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME)
			.expectBody(TestUserDto.class)
			.consumeWith(result -> {
				
				TestUserDto userDto = result.getResponseBody();
				
				Assertions.assertEquals(UNVERIFIED_USER_ID, userDto.getId());
				Assertions.assertEquals(UNVERIFIED_USER_EMAIL, userDto.getUsername());
				Assertions.assertEquals(UPDATED_NAME, userDto.getTag().getName());
				Assertions.assertEquals(1, userDto.getRoles().size());
				Assertions.assertTrue(userDto.getRoles().contains(UserUtils.Role.ADMIN));
			});
		
		User user = mongoTemplate.findById(UNVERIFIED_USER_ID, User.class);
		
		// Ensure that data changed properly
		Assertions.assertEquals(UNVERIFIED_USER_EMAIL, user.getEmail());
		Assertions.assertEquals(1, user.getRoleNames().size());
		Assertions.assertTrue(user.getRoleNames().contains(UserUtils.Role.ADMIN));
    }

	/**
	 * Providing an unknown id should return 404.
	 */
	@Test
    public void testUpdateUnknownId() throws Exception {
    	
		updateUser(ObjectId.get(), ADMIN_ID, userPatch)
		.expectStatus().isNotFound();
    }
	
	/**
	 * A non-admin trying to update the name and roles of another user should throw exception
	 * @throws Exception 
	 */
	@Test
    public void testUpdateAnotherUser() throws Exception {
    	
		updateUser(ADMIN_ID, UNVERIFIED_USER_ID, userPatch)
		.expectStatus().isForbidden();
    }

	/**
	 * A bad ADMIN trying to update the name and roles of another user should throw exception
	 * @throws Exception 
	 */
	@Test
    public void testBadAdminUpdateAnotherUser() throws Exception {
		
		updateUser(UNVERIFIED_USER_ID, UNVERIFIED_ADMIN_ID, userPatch)
		.expectStatus().isForbidden();

		updateUser(UNVERIFIED_USER_ID, BLOCKED_ADMIN_ID, userPatch)
		.expectStatus().isForbidden();
	}
	
	/**
	 * A good ADMIN should not be able to change his own roles
	 * @throws Exception 
	 */
	@Test
    public void goodAdminCanNotUpdateSelfRoles() throws Exception {
    	
		updateUser(ADMIN_ID, ADMIN_ID, userPatchAdminRole)
		.expectStatus().isOk()
			.expectHeader().exists(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME)
			.expectBody(TestUserDto.class)
			.consumeWith(result -> {
				
				TestUserDto userDto = result.getResponseBody();
				
				Assertions.assertEquals(UPDATED_NAME, userDto.getTag().getName());
				Assertions.assertEquals(1, userDto.getRoles().size());
				Assertions.assertTrue(userDto.getRoles().contains(UserUtils.Role.ADMIN));
			});
		
		User user = mongoTemplate.findById(ADMIN_ID, User.class);
		
		Assertions.assertEquals(1, user.getRoleNames().size());
    }

	/**
	 * Invalid name
	 * @throws Exception 
	 */
	@Test
    public void testUpdateUserInvalidNewName() throws Exception {
    	
		// Null name
		updateUser(UNVERIFIED_USER_ID, ADMIN_ID, userPatchNullName)
		.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

		// Too long name
		updateUser(UNVERIFIED_USER_ID, UNVERIFIED_USER_ID, userPatchLongName)
		.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

	private ResponseSpec updateUser(ObjectId userId, ObjectId loggedInId, Resource patch) {
		
		return CLIENT.patch().uri(BASE_URI + "/users/{id}", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, TOKENS.get(loggedInId))
				.body(BodyInserters.fromResource(patch))
			.exchange();

	}
}
