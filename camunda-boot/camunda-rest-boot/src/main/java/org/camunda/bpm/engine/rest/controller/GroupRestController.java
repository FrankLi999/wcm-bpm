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
import org.camunda.bpm.engine.authorization.Resources;//.GROUP;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.GroupQuery;
import org.camunda.bpm.engine.rest.GroupRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.identity.GroupDto;
import org.camunda.bpm.engine.rest.dto.identity.GroupQueryDto;
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
@RestController
@RequestMapping(GroupRestService.PATH)
public class GroupRestController extends AbstractAuthorizedRestResource implements GroupRestService {

//	public GroupRestController(String engineName, final ObjectMapper objectMapper) {
//		super(engineName, GROUP, ANY, objectMapper);
//	}

//	public GroupResource getGroup(String id) {
//		id = PathUtil.decodePathParam(id);
//		return new GroupRestController(getProcessEngine().getName(), id, relativeRootResourcePath, getObjectMapper());
//	}

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<GroupDto> queryGroups(HttpServletRequest request, @RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		GroupQueryDto queryDto = new GroupQueryDto(this.objectMapper, request.getParameterMap());
		return queryGroups(queryDto, firstResult, maxResults);
	}

	

	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getGroupCount(HttpServletRequest request) {
		GroupQueryDto queryDto = new GroupQueryDto(this.objectMapper, request.getParameterMap());
		return getGroupCount(queryDto);
	}

	@PostMapping(path="/create", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void createGroup(@RequestBody GroupDto groupDto) {
		final IdentityService identityService = getIdentityService();
		
		if (identityService.isReadOnly()) {
			throw new InvalidRequestException(HttpStatus.FORBIDDEN, "Identity service implementation is read-only.");
		}

		Group newGroup = identityService.newGroup(groupDto.getId());
		groupDto.update(newGroup);
		identityService.saveGroup(newGroup);

	}

	@RequestMapping(path="/", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourceOptionsDto availableOperations(HttpServletRequest request) {

		final IdentityService identityService = getIdentityService();

		UriComponentsBuilder baseUriBuilder = UriComponentsBuilder.fromPath(this.rootResourcePath)
				.path(GroupRestService.PATH);

		ResourceOptionsDto resourceOptionsDto = new ResourceOptionsDto();

		// GET /
		URI baseUri = baseUriBuilder.build().toUri();
		resourceOptionsDto.addReflexiveLink(baseUri, HttpMethod.GET.name(), "list");

		// GET /count
		URI countUri = baseUriBuilder.cloneBuilder().path("/count").build().toUri();
		resourceOptionsDto.addReflexiveLink(countUri, HttpMethod.GET.name(), "count");

		// POST /create
		if (!identityService.isReadOnly() && isAuthorized(CREATE, Resources.GROUP, Authorization.ANY)) {
			URI createUri = baseUriBuilder.cloneBuilder().path("/create").build().toUri();
			resourceOptionsDto.addReflexiveLink(createUri, HttpMethod.POST.name(), "create");
		}

		return resourceOptionsDto;
	}
	
	protected CountResultDto getGroupCount(GroupQueryDto queryDto) {
		GroupQuery query = queryDto.toQuery(this.processEngine);
		long count = query.count();
		return new CountResultDto(count);
	}

	protected List<GroupDto> queryGroups(GroupQueryDto queryDto, Integer firstResult, Integer maxResults) {

		queryDto.setObjectMapper(this.objectMapper);
		GroupQuery query = queryDto.toQuery(this.processEngine);

		List<Group> resultList;
		if (firstResult != null || maxResults != null) {
			resultList = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			resultList = query.list();
		}

		return GroupDto.fromGroupList(resultList);
	}

	// utility methods //////////////////////////////////////

	protected List<Group> executePaginatedQuery(GroupQuery query, Integer firstResult, Integer maxResults) {
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

	protected Resource getResource() {
		return Resources.GROUP;
	}
}
