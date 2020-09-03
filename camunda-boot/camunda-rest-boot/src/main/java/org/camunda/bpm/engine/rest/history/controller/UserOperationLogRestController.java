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

import org.camunda.bpm.engine.history.UserOperationLogQuery;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.UserOperationLogEntryDto;
import org.camunda.bpm.engine.rest.dto.history.UserOperationLogQueryDto;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.rest.history.UserOperationLogRestService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Danny Gräf
 */
@RestController(value="userOperationLogApi")
@RequestMapping(HistoryRestService.PATH + UserOperationLogRestService.PATH)
public class UserOperationLogRestController extends AbstractRestProcessEngineAware implements UserOperationLogRestService {

	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryUserOperationCount(HttpServletRequest request) {
		UserOperationLogQueryDto queryDto = new UserOperationLogQueryDto(this.getObjectMapper(), request.getParameterMap());
		UserOperationLogQuery query = queryDto.toQuery(processEngine);
		return new CountResultDto(query.count());
	}

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<UserOperationLogEntryDto> queryUserOperationEntries(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		UserOperationLogQueryDto queryDto = new UserOperationLogQueryDto(this.getObjectMapper(), request.getParameterMap());
		UserOperationLogQuery query = queryDto.toQuery(processEngine);

		if (firstResult == null && maxResults == null) {
			return UserOperationLogEntryDto.map(query.list());
		} else {
			if (firstResult == null) {
				firstResult = 0;
			}
			if (maxResults == null) {
				maxResults = Integer.MAX_VALUE;
			}
			return UserOperationLogEntryDto.map(query.listPage(firstResult, maxResults));
		}
	}
}
