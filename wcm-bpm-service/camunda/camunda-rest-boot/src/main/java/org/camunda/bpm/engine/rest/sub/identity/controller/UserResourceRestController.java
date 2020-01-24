/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.rest.sub.identity.controller;

import static org.camunda.bpm.engine.authorization.Permissions.DELETE;
import static org.camunda.bpm.engine.authorization.Permissions.UPDATE;

import java.net.URI;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.rest.UserRestService;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.identity.UserCredentialsDto;
import org.camunda.bpm.engine.rest.dto.identity.UserProfileDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.sub.identity.UserResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Daniel Meyer
 *
 */
@RestController
@RequestMapping(UserRestService.PATH + "/{userId}")
public class UserResourceRestController extends AbstractIdentityRestService implements UserResource {

	@GetMapping(path="/profile", produces=MediaType.APPLICATION_JSON_VALUE)
	public UserProfileDto getUserProfile(@PathVariable("userId") String userId) {

		User dbUser = findUserObject(userId);
		if (dbUser == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "User with id " + userId + " does not exist");
		}

		UserProfileDto user = UserProfileDto.fromUser(dbUser);

		return user;
	}

	@RequestMapping(path = "/", method = RequestMethod.OPTIONS, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourceOptionsDto availableOperations(@PathVariable("userId") String userId) {
		ResourceOptionsDto dto = new ResourceOptionsDto();

		// add links if operations are authorized
		UriComponentsBuilder baseUriBuilder = UriComponentsBuilder.fromPath(rootResourcePath).path(UserRestService.PATH)
				.path(userId);
		URI baseUri = baseUriBuilder.build().toUri();
		URI profileUri = baseUriBuilder.path("/profile").build().toUri();

		dto.addReflexiveLink(profileUri, HttpMethod.GET.name(), "self");

		if (!identityService.isReadOnly() && this.isAuthorized(DELETE, Resources.USER, userId)) {
			dto.addReflexiveLink(baseUri, HttpMethod.DELETE.name(), "delete");
		}
		if (!identityService.isReadOnly() && this.isAuthorized(UPDATE, Resources.USER, userId)) {
			dto.addReflexiveLink(profileUri, HttpMethod.PUT.name(), "update");
		}

		return dto;
	}

	@DeleteMapping(path="/")
	public void deleteUser(@PathVariable("userId") String userId) {
		this.ensureNotReadOnly();
		identityService.deleteUser(userId);
	}

	@PostMapping(path="/unlock")
	public void unlockUser(@PathVariable("userId") String userId) {
		this.ensureNotReadOnly();
		identityService.unlockUser(userId);
	}

	@PutMapping(path="/credentials", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateCredentials(@PathVariable("userId") String userId, @RequestBody UserCredentialsDto account) {
		this.ensureNotReadOnly();

		Authentication currentAuthentication = identityService.getCurrentAuthentication();
		if (currentAuthentication != null && currentAuthentication.getUserId() != null) {
			if (!identityService.checkPassword(currentAuthentication.getUserId(),
					account.getAuthenticatedUserPassword())) {
				throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
						"The given authenticated user password is not valid.");
			}
		}

		User dbUser = findUserObject(userId);
		if (dbUser == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "User with id " + userId + " does not exist");
		}

		dbUser.setPassword(account.getPassword());

		identityService.saveUser(dbUser);
	}

	@PutMapping(path="/profile", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateProfile(@PathVariable("userId") String userId, @RequestBody UserProfileDto profile) {
		this.ensureNotReadOnly();

		User dbUser = findUserObject(userId);
		if (dbUser == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "User with id " + userId + " does not exist");
		}

		profile.update(dbUser);

		identityService.saveUser(dbUser);
	}

	protected User findUserObject(String userId) {
		try {
			return identityService.createUserQuery().userId(userId).singleResult();
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Exception while performing user query: " + e.getMessage());
		}
	}

	@Override
	protected Resource getResource() {
		return Resources.USER;
	}

}
