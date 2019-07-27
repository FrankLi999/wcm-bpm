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
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.rest.GroupRestService;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.identity.GroupDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.sub.identity.GroupResource;
import org.camunda.bpm.engine.rest.util.PathUtil;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(GroupRestService.PATH + "/{groupId}")
public class GroupResourceRestController extends AbstractIdentityRestService implements GroupResource {

	//public static final String MEMBER_RESOURCE_PATH = "/members";

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public GroupDto getGroup(@PathVariable("groupId") String groupId) {
		groupId = this.decodeGroupId(groupId);
		Group dbGroup = findGroupObject(groupId);
		if (dbGroup == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "Group with id " + groupId + " does not exist");
		}

		GroupDto group = GroupDto.fromGroup(dbGroup);

		return group;
	}

	@RequestMapping(path = "/", method = RequestMethod.OPTIONS, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourceOptionsDto availableOperations(@PathVariable("groupId") String groupId) {
		groupId = this.decodeGroupId(groupId);
		ResourceOptionsDto dto = new ResourceOptionsDto();

		// add links if operations are authorized
		URI uri = UriComponentsBuilder.fromPath(this.rootResourcePath).path(GroupRestService.PATH).path(groupId)
				.build().toUri();

		dto.addReflexiveLink(uri, HttpMethod.GET.name(), "self");
		if (!this.identityService.isReadOnly() && this.isAuthorized(DELETE, Resources.GROUP, groupId)) {
			dto.addReflexiveLink(uri, HttpMethod.DELETE.name(), "delete");
		}
		if (!this.identityService.isReadOnly() && this.isAuthorized(UPDATE, Resources.GROUP, groupId)) {
			dto.addReflexiveLink(uri, HttpMethod.PUT.name(), "update");
		}

		return dto;
	}

	@PutMapping(path = "/", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateGroup(String groupId, @RequestBody GroupDto group) {
		this.ensureNotReadOnly();

		Group dbGroup = findGroupObject(groupId);
		if (dbGroup == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "Group with id " + groupId + " does not exist");
		}

		group.update(dbGroup);

		this.identityService.saveGroup(dbGroup);
	}

	@DeleteMapping(path = "/")
	public void deleteGroup(String groupId) {
		this.ensureNotReadOnly();
		this.deleteGroup(groupId);
	}

//	public GroupMembersResource getGroupMembersResource() {
//		return new GroupMembersRestController(processEngine.getName(), resourceId, rootResourcePath, getObjectMapper());
//	}

	protected Group findGroupObject(String groupId) {
		try {
			return this.identityService.createGroupQuery().groupId(groupId).singleResult();
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Exception while performing group query: " + e.getMessage());
		}
	}

	protected String decodeGroupId(String groupId) {
		return PathUtil.decodePathParam(groupId);
	}

	@Override
	protected Resource getResource() {
		return Resources.GROUP;
	}
}
