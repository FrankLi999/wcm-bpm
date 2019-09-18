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

import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.CleanableHistoricProcessInstanceReportResultDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricActivityStatisticsDto;

/**
 *
 * @author Roman Smirnov
 *
 */
public interface HistoricProcessDefinitionRestService {

	public static final String PATH = "/process-definition";

	public List<HistoricActivityStatisticsDto> getHistoricActivityStatistics(
			HttpServletRequest request,
			String processDefinitionId, 
			Boolean includeCanceled,
			Boolean includeFinished, 
			Boolean includeCompleteScope,
			String sortBy, 
			String sortOrder);
	public List<CleanableHistoricProcessInstanceReportResultDto> getCleanableHistoricProcessInstanceReport(
			HttpServletRequest request, 
			Integer firstResult,
			Integer maxResults);
	public CountResultDto getCleanableHistoricProcessInstanceReportCount(HttpServletRequest request);
}
