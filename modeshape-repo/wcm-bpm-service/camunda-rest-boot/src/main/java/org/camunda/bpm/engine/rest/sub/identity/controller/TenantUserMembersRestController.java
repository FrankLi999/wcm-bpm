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

import java.net.URI;

import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.rest.TenantRestService;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.sub.identity.TenantUserMembersResource;
import org.camunda.bpm.engine.rest.util.PathUtil;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(TenantRestService.PATH + "/{tenantId}/user-members")
public class TenantUserMembersRestController extends AbstractIdentityRestService
		implements TenantUserMembersResource {
	
	@PutMapping("/{userId}")
	public void createMembership(String tenantId, String userId) {
		ensureNotReadOnly();

		userId = PathUtil.decodePathParam(userId);
		identityService.createTenantUserMembership(tenantId, userId);
	}

	@DeleteMapping("/{userId}")
	public void deleteMembership(String tenantId, String userId) {
		ensureNotReadOnly();

		userId = PathUtil.decodePathParam(userId);
		identityService.deleteTenantUserMembership(tenantId, userId);
	}

	@RequestMapping(path = "/", method = RequestMethod.OPTIONS, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourceOptionsDto availableOperations(String tenantId) {
		ResourceOptionsDto dto = new ResourceOptionsDto();

		URI uri = UriComponentsBuilder.fromPath(this.rootResourcePath).path(TenantRestService.PATH)
				.path(tenantId).path("/user-members").build().toUri();

		dto.addReflexiveLink(uri, HttpMethod.GET.name(), "self");

		if (!identityService.isReadOnly() && isAuthorized(Permissions.DELETE, Resources.TENANT_MEMBERSHIP, tenantId)) {
			dto.addReflexiveLink(uri, HttpMethod.DELETE.name(), "delete");
		}
		if (!identityService.isReadOnly() && isAuthorized(Permissions.CREATE, Resources.TENANT_MEMBERSHIP, tenantId)) {
			dto.addReflexiveLink(uri, HttpMethod.PUT.name(), "create");
		}

		return dto;
	}

	@Override
	protected Resource getResource() {
		return Resources.TENANT_MEMBERSHIP;
	}
}
