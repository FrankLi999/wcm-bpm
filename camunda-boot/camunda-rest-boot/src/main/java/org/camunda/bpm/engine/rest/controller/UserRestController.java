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
package org.camunda.bpm.engine.rest.controller;

import static org.camunda.bpm.engine.authorization.Permissions.CREATE;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.identity.UserQuery;
import org.camunda.bpm.engine.rest.UserRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.identity.UserDto;
import org.camunda.bpm.engine.rest.dto.identity.UserProfileDto;
import org.camunda.bpm.engine.rest.dto.identity.UserQueryDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Daniel Meyer
 *
 */
@RestController(value="userApi")
@RequestMapping(UserRestService.PATH)
public class UserRestController extends AbstractAuthorizedRestResource implements UserRestService {

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<UserProfileDto> queryUsers(HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		UserQueryDto queryDto = new UserQueryDto(getObjectMapper(), request.getParameterMap());
		return queryUsers(queryDto, firstResult, maxResults);
	}

	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getUserCount(HttpServletRequest request) {
		UserQueryDto queryDto = new UserQueryDto(getObjectMapper(), request.getParameterMap());
		return getUserCount(queryDto);
	}

	@PostMapping(path="/create", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void createUser(@RequestBody UserDto userDto) {
		final IdentityService identityService = getIdentityService();

		if (identityService.isReadOnly()) {
			throw new InvalidRequestException(HttpStatus.FORBIDDEN, "Identity service implementation is read-only.");
		}

		UserProfileDto profile = userDto.getProfile();
		if (profile == null || profile.getId() == null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
					"request object must provide profile information with valid id.");
		}

		User newUser = identityService.newUser(profile.getId());
		profile.update(newUser);

		if (userDto.getCredentials() != null) {
			newUser.setPassword(userDto.getCredentials().getPassword());
		}

		identityService.saveUser(newUser);

	}

	// @RequestMapping(path="/", method=RequestMethod.OPTIONS, produces=MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path="/availableOperations", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourceOptionsDto availableOperations(HttpServletRequest request) {

		final IdentityService identityService = getIdentityService();

		UriComponentsBuilder baseUriBuilder = UriComponentsBuilder.fromPath(this.rootResourcePath)
				.path(UserRestService.PATH);

		ResourceOptionsDto resourceOptionsDto = new ResourceOptionsDto();

		// GET /
		URI baseUri = baseUriBuilder.build().toUri();
		resourceOptionsDto.addReflexiveLink(baseUri, HttpMethod.GET.name(), "list");

		// GET /count
		URI countUri = baseUriBuilder.cloneBuilder().path("/count").build().toUri();
		resourceOptionsDto.addReflexiveLink(countUri, HttpMethod.GET.name(), "count");

		// POST /create
		if (!identityService.isReadOnly() && isAuthorized(CREATE, this.getResource(), Authorization.ANY)) {
			URI createUri = baseUriBuilder.cloneBuilder().path("/create").build().toUri();
			resourceOptionsDto.addReflexiveLink(createUri, HttpMethod.POST.name(), "create");
		}

		return resourceOptionsDto;
	}

	// utility methods //////////////////////////////////////
	protected List<UserProfileDto> queryUsers(UserQueryDto queryDto, Integer firstResult, Integer maxResults) {

		queryDto.setObjectMapper(getObjectMapper());
		UserQuery query = queryDto.toQuery(getProcessEngine());

		List<User> resultList;
		if (firstResult != null || maxResults != null) {
			resultList = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			resultList = query.list();
		}

		return UserProfileDto.fromUserList(resultList);
	}

	protected CountResultDto getUserCount(UserQueryDto queryDto) {
		UserQuery query = queryDto.toQuery(getProcessEngine());
		long count = query.count();
		return new CountResultDto(count);
	}

	protected List<User> executePaginatedQuery(UserQuery query, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}

	protected IdentityService getIdentityService() {
		return getProcessEngine().getIdentityService();
	}

	@Override
	protected Resource getResource() {
		return Resources.USER;
	}

}
