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
package org.camunda.bpm.engine.rest.sub.authorization.controller;

import static org.camunda.bpm.engine.authorization.Permissions.DELETE;
import static org.camunda.bpm.engine.authorization.Permissions.UPDATE;

import java.net.URI;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.rest.AuthorizationRestService;
import org.camunda.bpm.engine.rest.controller.AbstractAuthorizedRestResource;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.authorization.AuthorizationDto;
import org.camunda.bpm.engine.rest.sub.authorization.AuthorizationResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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
 *
 * @author Daniel Meyer
 *
 */

@RestController
@RequestMapping(AuthorizationRestService.PATH + "/{resourceId}")
public class AuthorizationRestController extends AbstractAuthorizedRestResource implements AuthorizationResource {
	@Autowired
	ProcessEngine processEngine;
	
	//protected String relativeRootResourcePath = "/camunda";

	public AuthorizationRestController() {}

	

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public AuthorizationDto getAuthorization(@PathVariable("resourceId") String resourceId) {
		return this.doGetAuthorization(resourceId);
	}

	@DeleteMapping(path="/")
	public void deleteAuthorization(@PathVariable("resourceId") String resourceId) {
		Authorization dbAuthorization = getDbAuthorization(resourceId);
		this.authorizationService.deleteAuthorization(dbAuthorization.getId());
	}

	@PutMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateAuthorization(@PathVariable("resourceId") String resourceId, @RequestBody AuthorizationDto dto) {
		// get db auth
		Authorization dbAuthorization = getDbAuthorization(resourceId);
		// copy values from dto
		AuthorizationDto.update(dto, dbAuthorization, processEngine.getProcessEngineConfiguration());
		// save
		this.authorizationService.saveAuthorization(dbAuthorization);
	}

	@RequestMapping(path = "/", method = RequestMethod.OPTIONS, produces=MediaType.APPLICATION_JSON_VALUE)
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
	protected org.camunda.bpm.engine.authorization.Resource getResource() {
		return Resources.AUTHORIZATION;
	}
	

}
