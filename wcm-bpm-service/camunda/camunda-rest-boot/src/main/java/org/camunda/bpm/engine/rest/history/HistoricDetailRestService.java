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

import org.camunda.bpm.engine.history.HistoricActivityInstanceQuery;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricDetailDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricDetailQueryDto;

/**
 *
 * @author Roman Smirnov
 *
 */
public interface HistoricDetailRestService {

	public static final String PATH = "/detail";

//	@Path("/{id}")
//	HistoricDetailResource historicDetail(@PathParam("id") String detailId);

	/**
	 * Exposes the {@link HistoricActivityInstanceQuery} interface as a REST
	 * service.
	 *
	 * @param uriInfo
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	List<HistoricDetailDto> getHistoricDetails(HttpServletRequest request, 
			Integer firstResult,
			Integer maxResults,
			boolean deserializeObjectValues);
	/**
	 * Exposes the {@link HistoricActivityInstanceQuery} interface as a REST service
	 * with additional query parameters (compared to the GET alternative).
	 *
	 * @param queryDto
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	List<HistoricDetailDto> queryHistoricDetails(HistoricDetailQueryDto queryDto,
			Integer firstResult, Integer maxResults, boolean deserializeObjectValues);
	CountResultDto getHistoricDetailsCount(HttpServletRequest request);
}
