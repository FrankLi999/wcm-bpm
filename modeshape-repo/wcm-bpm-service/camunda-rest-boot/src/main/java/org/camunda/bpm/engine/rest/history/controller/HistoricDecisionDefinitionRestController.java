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
package org.camunda.bpm.engine.rest.history.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.history.CleanableHistoricDecisionInstanceReport;
import org.camunda.bpm.engine.history.CleanableHistoricDecisionInstanceReportResult;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.CleanableHistoricDecisionInstanceReportDto;
import org.camunda.bpm.engine.rest.dto.history.CleanableHistoricDecisionInstanceReportResultDto;
import org.camunda.bpm.engine.rest.history.HistoricDecisionDefinitionRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(HistoryRestService.PATH + HistoricDecisionDefinitionRestService.PATH)
public class HistoricDecisionDefinitionRestController extends AbstractRestProcessEngineAware implements HistoricDecisionDefinitionRestService {

	@Override
	@GetMapping(path="/cleanable-decision-instance-report", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<CleanableHistoricDecisionInstanceReportResultDto> getCleanableHistoricDecisionInstanceReport(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		CleanableHistoricDecisionInstanceReportDto queryDto = new CleanableHistoricDecisionInstanceReportDto(
				objectMapper, request.getParameterMap());
		CleanableHistoricDecisionInstanceReport query = queryDto.toQuery(processEngine);

		List<CleanableHistoricDecisionInstanceReportResult> reportResult;
		if (firstResult != null || maxResults != null) {
			reportResult = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			reportResult = query.list();
		}

		return CleanableHistoricDecisionInstanceReportResultDto.convert(reportResult);
	}

	@Override
	@GetMapping(path="/cleanable-decision-instance-report/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getCleanableHistoricDecisionInstanceReportCount(
			HttpServletRequest request) {
		CleanableHistoricDecisionInstanceReportDto queryDto = new CleanableHistoricDecisionInstanceReportDto(
				objectMapper, request.getParameterMap());
		queryDto.setObjectMapper(objectMapper);
		CleanableHistoricDecisionInstanceReport query = queryDto.toQuery(processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}
	
	private List<CleanableHistoricDecisionInstanceReportResult> executePaginatedQuery(
			CleanableHistoricDecisionInstanceReport query, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}
}
