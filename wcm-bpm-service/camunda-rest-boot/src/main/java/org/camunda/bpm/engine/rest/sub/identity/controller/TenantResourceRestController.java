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
import org.camunda.bpm.engine.identity.Tenant;
import org.camunda.bpm.engine.rest.TenantRestService;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.identity.TenantDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.sub.identity.TenantResource;
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
import org.springframework.web.util.UriComponentsBuilder;;

@RestController
@RequestMapping(TenantRestService.PATH + "/{tenantId}")
public class TenantResourceRestController extends AbstractIdentityRestService implements TenantResource {
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public TenantDto getTenant(@PathVariable("tenantId") String tenantId) {

		Tenant tenant = findTenantObject(tenantId);
		if (tenant == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "Tenant with id " + tenantId + " does not exist");
		}
		TenantDto dto = TenantDto.fromTenant(tenant);
		return dto;
	}

	@PutMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateTenant(@PathVariable("tenantId") String tenantId, @RequestBody TenantDto tenantDto) {
		this.ensureNotReadOnly();

		Tenant tenant = findTenantObject(tenantId);
		if (tenant == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "Tenant with id " + tenantId + " does not exist");
		}

		tenantDto.update(tenant);

		this.identityService.saveTenant(tenant);
	}

	@DeleteMapping(path="/")
	public void deleteTenant(@PathVariable("tenantId") String tenantId) {
		this.ensureNotReadOnly();

		this.identityService.deleteTenant(tenantId);
	}

	@RequestMapping(path = "/", method = RequestMethod.OPTIONS, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourceOptionsDto availableOperations(@PathVariable("tenantId") String tenantId) {
		ResourceOptionsDto dto = new ResourceOptionsDto();

		// add links if operations are authorized
		URI uri = UriComponentsBuilder.fromPath(rootResourcePath).path(TenantRestService.PATH).path(tenantId)
				.build().toUri();

		dto.addReflexiveLink(uri, HttpMethod.GET.name(), "self");

		if (!this.identityService.isReadOnly() && this.isAuthorized(DELETE, Resources.TENANT, tenantId)) {
			dto.addReflexiveLink(uri, HttpMethod.DELETE.name(), "delete");
		}
		if (!this.identityService.isReadOnly() && this.isAuthorized(UPDATE, Resources.TENANT, tenantId)) {
			dto.addReflexiveLink(uri, HttpMethod.PUT.name(), "update");
		}
		return dto;
	}

	protected Tenant findTenantObject(String tenantId) {
		try {
			return identityService.createTenantQuery().tenantId(tenantId).singleResult();

		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Exception while performing tenant query: " + e.getMessage());
		}
	}

	@Override
	protected Resource getResource() {
		return Resources.TENANT;
	}
}
