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
package org.camunda.bpm.engine.rest.history;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.dto.history.DeleteHistoricProcessInstancesDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricProcessInstanceQueryDto;
import org.camunda.bpm.engine.rest.dto.history.batch.removaltime.SetRemovalTimeToHistoricProcessInstancesDto;
import org.springframework.http.ResponseEntity;

public interface HistoricProcessInstanceRestService {

	public static final String PATH = "/process-instance";

//	@Path("/{id}")
//	HistoricProcessInstanceResource getHistoricProcessInstance(@PathParam("id") String processInstanceId);

	/**
	 * Exposes the {@link HistoricProcessInstanceQuery} interface as a REST service.
	 *
	 * @param uriInfo
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	List<HistoricProcessInstanceDto> getHistoricProcessInstances(
			HttpServletRequest request,
			Integer firstResult, 
			Integer maxResults);

	/**
	 * @param query
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	List<HistoricProcessInstanceDto> queryHistoricProcessInstances(
			HistoricProcessInstanceQueryDto query,
			Integer firstResult, 
			Integer maxResults);


	CountResultDto getHistoricProcessInstancesCount(HttpServletRequest request);

	CountResultDto queryHistoricProcessInstancesCount(HistoricProcessInstanceQueryDto query);

	ResponseEntity<?> getHistoricProcessInstancesReport(HttpServletRequest request);

	BatchDto deleteAsync(DeleteHistoricProcessInstancesDto dto);

	BatchDto setRemovalTimeAsync(SetRemovalTimeToHistoricProcessInstancesDto dto);

	ResponseEntity<?> deleteHistoricVariableInstancesByProcessInstanceId(String processInstanceId);
}
