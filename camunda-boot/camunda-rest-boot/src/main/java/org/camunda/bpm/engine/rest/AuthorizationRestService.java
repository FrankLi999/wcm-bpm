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
package org.camunda.bpm.engine.rest;

import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.authorization.AuthorizationCheckResultDto;
import org.camunda.bpm.engine.rest.dto.authorization.AuthorizationCreateDto;
import org.camunda.bpm.engine.rest.dto.authorization.AuthorizationDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Daniel Meyer
 *
 */
public interface AuthorizationRestService {

	public static final String PATH = "/camunda/api/engine/authorization";

	AuthorizationCheckResultDto isUserAuthorized(String permissionName,
			String resourceName, Integer resourceType,
			String resourceId, String userId);

	List<AuthorizationDto> queryAuthorizations(HttpServletRequest request, Integer firstResult,
			Integer maxResults);

	CountResultDto getAuthorizationCount(HttpServletRequest request);

	AuthorizationDto createAuthorization(HttpServletRequest request, AuthorizationCreateDto dto);

//	@OPTIONS
//	@Produces(MediaType.APPLICATION_JSON)
	ResourceOptionsDto availableOperations();
	
	AuthorizationDto getAuthorization(String resourceId);

	void deleteAuthorization(String resourceId);

	void updateAuthorization(String resourceId, AuthorizationDto dto);

	ResourceOptionsDto availableOperations(String resourceId);

}
