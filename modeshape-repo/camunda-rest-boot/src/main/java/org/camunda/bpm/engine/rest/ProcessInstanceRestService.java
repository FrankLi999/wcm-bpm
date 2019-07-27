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
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceQueryDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceSuspensionStateDto;
import org.camunda.bpm.engine.rest.dto.runtime.SetJobRetriesByProcessDto;
import org.camunda.bpm.engine.rest.dto.runtime.batch.DeleteProcessInstancesDto;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;

public interface ProcessInstanceRestService {

	public static final String PATH = "/camunda/api/engine/process-instance";
//
//	@Path("/{id}")
//	ProcessInstanceResource getProcessInstance(@PathParam("id") String processInstanceId);

	/**
	 * Exposes the {@link ProcessInstanceQuery} interface as a REST service.
	 *
	 * @param uriInfo
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	List<ProcessInstanceDto> getProcessInstances(HttpServletRequest request, Integer firstResult, Integer maxResults);

	/**
	 * Expects the same parameters as
	 * {@link ProcessInstanceRestService#getProcessInstances(UriInfo, Integer, Integer)}
	 * (as a JSON message body) and allows for any number of variable checks.
	 *
	 * @param query
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	List<ProcessInstanceDto> queryProcessInstances(ProcessInstanceQueryDto query, Integer firstResult, Integer maxResults);
	CountResultDto getProcessInstancesCount(HttpServletRequest request);
	CountResultDto queryProcessInstancesCount(ProcessInstanceQueryDto query);
	void updateSuspensionState(ProcessInstanceSuspensionStateDto dto);
	BatchDto updateSuspensionStateAsync(ProcessInstanceSuspensionStateDto dto);
	BatchDto deleteAsync(DeleteProcessInstancesDto dto);
	BatchDto deleteAsyncHistoricQueryBased(DeleteProcessInstancesDto dto);
	BatchDto setRetriesByProcess(SetJobRetriesByProcessDto setJobRetriesDto);
	BatchDto setRetriesByProcessHistoricQueryBased(SetJobRetriesByProcessDto setJobRetriesDto);
}
