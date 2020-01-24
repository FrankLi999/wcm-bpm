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

import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.StatisticsResultDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionSuspensionStateDto;
import org.springframework.http.MediaType;

public interface ProcessDefinitionRestService {

	public static final String APPLICATION_BPMN20_XML = "application/bpmn20+xml";
	public static final MediaType APPLICATION_BPMN20_XML_TYPE = new MediaType("application", "bpmn20+xml");

	public static final String PATH = "/camunda/api/engine/process-definition";

//	@Path("/{id}")
//	ProcessDefinitionResource getProcessDefinitionById(@PathParam("id") String processDefinitionId);
//
//	@Path("/key/{key}")
//	ProcessDefinitionResource getProcessDefinitionByKey(@PathParam("key") String processDefinitionKey);
//
//	@Path("/key/{key}/tenant-id/{tenantId}")
//	ProcessDefinitionResource getProcessDefinitionByKeyAndTenantId(@PathParam("key") String processDefinitionKey,
//			@PathParam("tenantId") String tenantId);

	/**
	 * Exposes the {@link ProcessDefinitionQuery} interface as a REST service.
	 *
	 * @param uriInfo
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	List<ProcessDefinitionDto> getProcessDefinitions(HttpServletRequest request, Integer firstResult, Integer maxResults);

	CountResultDto getProcessDefinitionsCount(HttpServletRequest request);

	List<StatisticsResultDto> getStatistics(
			Boolean includeFailedJobs,
			Boolean includeRootIncidents,
			Boolean includeIncidents,
			String includeIncidentsForType);

	void updateSuspensionState(ProcessDefinitionSuspensionStateDto dto);

	void deleteProcessDefinitionsByKey(
			String processDefinitionKey,
			boolean cascade, 
			boolean skipCustomListeners,
			boolean skipIoMappings);

	void deleteProcessDefinitionsByKeyAndTenantId(
			String processDefinitionKey,
			boolean cascade, 
			boolean skipCustomListeners,
			boolean skipIoMappings,
			String tenantId);
}
