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

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.history.HistoricDecisionInstanceStatistics;
import org.camunda.bpm.engine.history.HistoricDecisionInstanceStatisticsQuery;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.history.HistoricDecisionInstanceStatisticsDto;
import org.camunda.bpm.engine.rest.history.HistoricDecisionStatisticsRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Askar Akhmerov
 */
@RestController(value="historicDecisionStatisticsApi")
@RequestMapping(HistoryRestService.PATH + HistoricDecisionStatisticsRestService.PATH)
public class HistoricDecisionStatisticsRestController extends AbstractRestProcessEngineAware implements HistoricDecisionStatisticsRestService {

	@Override
	@GetMapping(path="/{id}/statistics", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<HistoricDecisionInstanceStatisticsDto> getDecisionStatistics(
			@PathVariable("id") String decisionRequirementsDefinitionId,
			@RequestParam("decisionInstanceId") String decisionInstanceId) {
		List<HistoricDecisionInstanceStatisticsDto> result = new ArrayList<HistoricDecisionInstanceStatisticsDto>();
		HistoricDecisionInstanceStatisticsQuery statisticsQuery = processEngine.getHistoryService()
				.createHistoricDecisionInstanceStatisticsQuery(decisionRequirementsDefinitionId);
		if (decisionInstanceId != null) {
			statisticsQuery.decisionInstanceId(decisionInstanceId);
		}
		for (HistoricDecisionInstanceStatistics stats : statisticsQuery.list()) {
			result.add(HistoricDecisionInstanceStatisticsDto.fromDecisionDefinitionStatistics(stats));
		}
		return result;
	}

}
