package org.camunda.bpm.engine.rest.sub.identity.controller;

import static org.camunda.bpm.engine.authorization.Permissions.CREATE;
import static org.camunda.bpm.engine.authorization.Permissions.DELETE;

import java.net.URI;

import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.rest.GroupRestService;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.sub.identity.GroupResource;
import org.camunda.bpm.engine.rest.util.PathUtil;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(GroupRestService.PATH + "/{groupId}/members")
public class GroupMembersResourceRestController extends AbstractIdentityRestService implements GroupResource {
	@PutMapping("/{userId}")
	public void createGroupMember(@PathVariable("groupId") String groupId, @PathVariable("userId")String userId) {
		this.ensureNotReadOnly();

		userId = PathUtil.decodePathParam(userId);
		identityService.createMembership(userId, groupId);
	}

	@DeleteMapping("/{userId}")
	public void deleteGroupMember(@PathVariable("groupId") String groupId, @PathVariable("userId") String userId) {
		this.ensureNotReadOnly();

		userId = PathUtil.decodePathParam(userId);
		identityService.deleteMembership(userId, groupId);
	}

	@RequestMapping(value = "/", method = RequestMethod.OPTIONS, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourceOptionsDto availableMembersOperations(@PathVariable("groupId") String groupId) {

		ResourceOptionsDto dto = new ResourceOptionsDto();

		URI uri = UriComponentsBuilder.fromPath(this.rootResourcePath).path(GroupRestService.PATH)
				.path(groupId).path("/members").build().toUri();

		dto.addReflexiveLink(uri, HttpMethod.GET.name(), "self");

		if (!this.identityService.isReadOnly() && this.isAuthorized(DELETE, Resources.GROUP_MEMBERSHIP, groupId)) {
			dto.addReflexiveLink(uri, HttpMethod.DELETE.name(), "delete");
		}
		if (!this.identityService.isReadOnly() && this.isAuthorized(CREATE, Resources.GROUP_MEMBERSHIP, groupId)) {
			dto.addReflexiveLink(uri, HttpMethod.PUT.name(), "create");
		}

		return dto;

	}

	@Override
	protected Resource getResource() {
		return Resources.GROUP_MEMBERSHIP;
	}
}
