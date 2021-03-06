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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.identity.GroupDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Daniel Meyer
 *
 */
public interface GroupRestService {

	public static final String PATH = "/camunda/api/engine/group";

//	@Path("/{id}")
//	GroupResource getGroup(@PathParam("id") String id);

	List<GroupDto> queryGroups(HttpServletRequest request, Integer firstResult, Integer maxResults);

	CountResultDto getGroupCount(HttpServletRequest request);

	@PostMapping(path="/create", consumes=MediaType.APPLICATION_JSON_VALUE)
	void createGroup(@RequestBody GroupDto GroupDto);

	ResourceOptionsDto availableOperations(HttpServletRequest request);
}
