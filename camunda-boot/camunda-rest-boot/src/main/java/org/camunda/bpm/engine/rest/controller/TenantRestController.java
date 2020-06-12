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

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.Tenant;
import org.camunda.bpm.engine.identity.TenantQuery;
import org.camunda.bpm.engine.rest.TenantRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.identity.TenantDto;
import org.camunda.bpm.engine.rest.dto.identity.TenantQueryDto;
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
import org.springframework.web.util.UriComponentsBuilder;;

@RestController(value="tenantApi")
@RequestMapping(TenantRestService.PATH)
public class TenantRestController extends AbstractAuthorizedRestResource implements TenantRestService {

//	public TenantRestController(String engineName, final ObjectMapper objectMapper) {
//		super(engineName, TENANT, ANY, objectMapper);
//	}

//	public TenantResource getTenant(String id) {
//		id = PathUtil.decodePathParam(id);
//		return new TenantRestController(getProcessEngine().getName(), id, relativeRootResourcePath, getObjectMapper());
//	}

	@GetMapping(path="/", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TenantDto> queryTenants(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		TenantQueryDto queryDto = new TenantQueryDto(getObjectMapper(), request.getParameterMap());

		TenantQuery query = queryDto.toQuery(getProcessEngine());

		List<Tenant> tenants;
		if (firstResult != null || maxResults != null) {
			tenants = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			tenants = query.list();
		}

		return TenantDto.fromTenantList(tenants);
	}

	@GetMapping(path="/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getTenantCount(HttpServletRequest request) {
		TenantQueryDto queryDto = new TenantQueryDto(getObjectMapper(), request.getParameterMap());

		TenantQuery query = queryDto.toQuery(getProcessEngine());
		long count = query.count();

		return new CountResultDto(count);
	}

	@PostMapping(path="/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createTenant(@RequestBody TenantDto dto) {

		if (getIdentityService().isReadOnly()) {
			throw new InvalidRequestException(HttpStatus.FORBIDDEN, "Identity service implementation is read-only.");
		}

		Tenant newTenant = getIdentityService().newTenant(dto.getId());
		dto.update(newTenant);

		getIdentityService().saveTenant(newTenant);
	}

	@RequestMapping(path="/availableOperations", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResourceOptionsDto availableOperations(HttpServletRequest request) {

		UriComponentsBuilder baseUriBuilder = UriComponentsBuilder.fromPath(this.rootResourcePath)
				.path(TenantRestService.PATH);

		ResourceOptionsDto resourceOptionsDto = new ResourceOptionsDto();

		// GET /
		URI baseUri = baseUriBuilder.build().toUri();
		resourceOptionsDto.addReflexiveLink(baseUri, HttpMethod.GET.name(), "list");

		// GET /count
		URI countUri = baseUriBuilder.cloneBuilder().path("/count").build().toUri();
		resourceOptionsDto.addReflexiveLink(countUri, HttpMethod.GET.name(), "count");

		// POST /create
		if (!getIdentityService().isReadOnly() && isAuthorized(Permissions.CREATE, getResource(), Authorization.ANY)) {
			URI createUri = baseUriBuilder.cloneBuilder().path("/create").build().toUri();
			resourceOptionsDto.addReflexiveLink(createUri, HttpMethod.POST.name(), "create");
		}

		return resourceOptionsDto;
	}

	protected List<Tenant> executePaginatedQuery(TenantQuery query, Integer firstResult, Integer maxResults) {
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
		return Resources.TENANT;
	}
}
