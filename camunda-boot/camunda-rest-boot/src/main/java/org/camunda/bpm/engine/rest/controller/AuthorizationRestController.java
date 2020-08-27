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

import static org.camunda.bpm.engine.authorization.Permissions.DELETE;
import static org.camunda.bpm.engine.authorization.Permissions.UPDATE;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.AuthorizationQuery;
import org.camunda.bpm.engine.authorization.Permission;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.rest.AuthorizationRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.authorization.AuthorizationCheckResultDto;
import org.camunda.bpm.engine.rest.dto.authorization.AuthorizationCreateDto;
import org.camunda.bpm.engine.rest.dto.authorization.AuthorizationDto;
import org.camunda.bpm.engine.rest.dto.authorization.AuthorizationQueryDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.util.ResourceUtil;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * @author Daniel Meyer
 *
 */
@RestController(value="authorizationApi")
@RequestMapping(AuthorizationRestService.PATH)
public class AuthorizationRestController extends AbstractAuthorizedRestResource implements AuthorizationRestService {

	protected Resource getResource() {
		return Resources.AUTHORIZATION;
	}
	
	@GetMapping(path="/check", produces= {MediaType.APPLICATION_JSON_VALUE})
	public AuthorizationCheckResultDto isUserAuthorized(
			@RequestParam("permissionName") String permissionName, 
			@RequestParam("resourceName") String resourceName,
			@RequestParam("resourceType") Integer resourceType, 
			@RequestParam("resourceId") String resourceId, 
			@RequestParam("userId") String userId) {

		// validate request:
		if (permissionName == null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, "Query parameter 'permissionName' cannot be null");

		} else if (resourceName == null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, "Query parameter 'resourceName' cannot be null");

		} else if (resourceType == null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, "Query parameter 'resourceType' cannot be null");

		}

		final Authentication currentAuthentication = processEngine.getIdentityService().getCurrentAuthentication();
		if (currentAuthentication == null) {
			throw new InvalidRequestException(HttpStatus.UNAUTHORIZED,
					"You must be authenticated in order to use this resource.");
		}

		final AuthorizationService authorizationService = processEngine.getAuthorizationService();

		ResourceUtil resource = new ResourceUtil(resourceName, resourceType);
		ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) this.processEngine
				.getProcessEngineConfiguration();
		Permission permission = processEngineConfiguration.getPermissionProvider().getPermissionForName(permissionName,
				resourceType);
		String currentUserId = currentAuthentication.getUserId();

		boolean isUserAuthorized = false;

		String userIdToCheck;
		List<String> groupIdsToCheck = new ArrayList<>();

		if (userId != null && !userId.equals(currentUserId)) {
			boolean isCurrentUserAuthorized = authorizationService.isUserAuthorized(currentUserId,
					currentAuthentication.getGroupIds(), Permissions.READ, Resources.AUTHORIZATION);
			if (!isCurrentUserAuthorized) {
				throw new InvalidRequestException(HttpStatus.FORBIDDEN,
						"You must have READ permission for Authorization resource.");
			}
			userIdToCheck = userId;
			groupIdsToCheck = getUserGroups(userId);
		} else {
			// userId == null || userId.equals(currentUserId)
			userIdToCheck = currentUserId;
			groupIdsToCheck = currentAuthentication.getGroupIds();
		}

		if (resourceId == null || Authorization.ANY.equals(resourceId)) {
			isUserAuthorized = authorizationService.isUserAuthorized(userIdToCheck, groupIdsToCheck, permission,
					resource);
		} else {
			isUserAuthorized = authorizationService.isUserAuthorized(userIdToCheck, groupIdsToCheck, permission,
					resource, resourceId);
		}

		return new AuthorizationCheckResultDto(isUserAuthorized, permissionName, resource, resourceId);
	}

	@GetMapping(path="/", produces= {MediaType.APPLICATION_JSON_VALUE})
	public List<AuthorizationDto> queryAuthorizations(HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		AuthorizationQueryDto queryDto = new AuthorizationQueryDto(this.objectMapper, request.getParameterMap());
		return queryAuthorizations(queryDto, firstResult, maxResults);
	}

	@GetMapping(path="/count", produces= {MediaType.APPLICATION_JSON_VALUE})
	public CountResultDto getAuthorizationCount(HttpServletRequest request) {
		AuthorizationQueryDto queryDto = new AuthorizationQueryDto(this.objectMapper, request.getParameterMap());
		return getAuthorizationCount(queryDto);
	}
	
	// @RequestMapping(value="/**", method = RequestMethod.OPTIONS, produces= {MediaType.APPLICATION_JSON_VALUE})
	@GetMapping(path="/availableOperations", produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResourceOptionsDto availableOperations() {

		UriComponentsBuilder baseUriBuilder = UriComponentsBuilder.fromPath(rootResourcePath)
				.path(AuthorizationRestService.PATH);

		ResourceOptionsDto resourceOptionsDto = new ResourceOptionsDto();

		// GET /
		URI baseUri = baseUriBuilder.build().toUri();
		resourceOptionsDto.addReflexiveLink(baseUri, HttpMethod.GET.name(), "list");

		// GET /count
		URI countUri = baseUriBuilder.cloneBuilder().path("/count").build().toUri();
		resourceOptionsDto.addReflexiveLink(countUri, HttpMethod.GET.name(), "count");

		// POST /create
		if (isAuthorized(Permissions.CREATE, Authorization.ANY)) {
			URI createUri = baseUriBuilder.cloneBuilder().path("/create").build().toUri();
			resourceOptionsDto.addReflexiveLink(createUri, HttpMethod.POST.name(), "create");
		}

		return resourceOptionsDto;
	}

	@PostMapping(path="/create", consumes= {MediaType.APPLICATION_JSON_VALUE}, produces= {MediaType.APPLICATION_JSON_VALUE})
	public AuthorizationDto createAuthorization(
			HttpServletRequest request, 
			@RequestBody AuthorizationCreateDto dto) {
		final AuthorizationService authorizationService = processEngine.getAuthorizationService();

		Authorization newAuthorization = authorizationService.createNewAuthorization(dto.getType());
		AuthorizationCreateDto.update(dto, newAuthorization, processEngine.getProcessEngineConfiguration());

		newAuthorization = authorizationService.saveAuthorization(newAuthorization);

		return this.doGetAuthorization(newAuthorization.getId());//.getAuthorization(context);
	}
	
	protected List<AuthorizationDto> queryAuthorizations(AuthorizationQueryDto queryDto, Integer firstResult,
			Integer maxResults) {

		queryDto.setObjectMapper(this.objectMapper);
		AuthorizationQuery query = queryDto.toQuery(this.processEngine);

		List<Authorization> resultList;
		if (firstResult != null || maxResults != null) {
			resultList = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			resultList = query.list();
		}

		return AuthorizationDto.fromAuthorizationList(resultList, processEngine.getProcessEngineConfiguration());
	}

	protected CountResultDto getAuthorizationCount(AuthorizationQueryDto queryDto) {
		AuthorizationQuery query = queryDto.toQuery(this.processEngine);
		long count = query.count();
		return new CountResultDto(count);
	}

	

	// utility methods //////////////////////////////////////

	protected List<Authorization> executePaginatedQuery(AuthorizationQuery query, Integer firstResult,
			Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}

	protected IdentityService getIdentityService() {
		return this.processEngine.getIdentityService();
	}

	protected List<String> getUserGroups(String userId) {
		List<String> groupIds = new ArrayList<>();
		List<Group> userGroups = getIdentityService().createGroupQuery().groupMember(userId).list();
		for (Group group : userGroups) {
			groupIds.add(group.getId());
		}
		return groupIds;
	}

	////////////////////////////////////
	@GetMapping(path="/{resourceId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public AuthorizationDto getAuthorization(@PathVariable("resourceId") String resourceId) {
		return this.doGetAuthorization(resourceId);
	}

	@DeleteMapping(path="/{resourceId}")
	public void deleteAuthorization(@PathVariable("resourceId") String resourceId) {
		Authorization dbAuthorization = getDbAuthorization(resourceId);
		this.authorizationService.deleteAuthorization(dbAuthorization.getId());
	}

	@PutMapping(path="/{resourceId}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateAuthorization(@PathVariable("resourceId") String resourceId, @RequestBody AuthorizationDto dto) {
		// get db auth
		Authorization dbAuthorization = getDbAuthorization(resourceId);
		// copy values from dto
		AuthorizationDto.update(dto, dbAuthorization, processEngine.getProcessEngineConfiguration());
		// save
		this.authorizationService.saveAuthorization(dbAuthorization);
	}

	// @RequestMapping(path = "/", method = RequestMethod.OPTIONS, produces=MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/{resourceId}/availableOperations", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourceOptionsDto availableOperations(@PathVariable("resourceId") String resourceId) {

		ResourceOptionsDto dto = new ResourceOptionsDto();

		URI uri = UriComponentsBuilder.fromPath(this.rootResourcePath).path(AuthorizationRestService.PATH)
				.path(resourceId).build().toUri();

		dto.addReflexiveLink(uri, HttpMethod.GET.name(), "self");

		if (this.isAuthorized(DELETE, Resources.AUTHORIZATION, resourceId)) {
			dto.addReflexiveLink(uri, HttpMethod.DELETE.name(), "delete");
		}
		if (this.isAuthorized(UPDATE, Resources.AUTHORIZATION, resourceId)) {
			dto.addReflexiveLink(uri, HttpMethod.PUT.name(), "update");
		}

		return dto;
	}

	// utils //////////////////////////////////////////////////
//	protected org.camunda.bpm.engine.authorization.Resource getResource() {
//		return Resources.AUTHORIZATION;
//	}
}
