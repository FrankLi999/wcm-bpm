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
package org.camunda.bpm.engine.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.management.SchemaLogEntry;
import org.camunda.bpm.engine.management.SchemaLogQuery;
import org.camunda.bpm.engine.rest.SchemaLogRestService;
import org.camunda.bpm.engine.rest.dto.SchemaLogEntryDto;
import org.camunda.bpm.engine.rest.dto.SchemaLogQueryDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Miklas Boskamp
 *
 */
@RestController(value="schemaLogApi")
@RequestMapping(SchemaLogRestService.PATH)
public class SchemaLogRestController extends AbstractRestProcessEngineAware implements SchemaLogRestService {

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<SchemaLogEntryDto> getSchemaLog(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		return querySchemaLog(new SchemaLogQueryDto(getObjectMapper(), request.getParameterMap()), firstResult,
				maxResults);
	}

	@Override
	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<SchemaLogEntryDto> querySchemaLog(
			@RequestBody SchemaLogQueryDto dto, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("firstResult") Integer maxResults) {
		SchemaLogQuery query = dto.toQuery(processEngine);
		List<SchemaLogEntry> schemaLogEntries;
		if (firstResult != null || maxResults != null) {
			schemaLogEntries = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			schemaLogEntries = query.list();
		}
		return SchemaLogEntryDto.fromSchemaLogEntries(schemaLogEntries);
	}

	protected List<SchemaLogEntry> executePaginatedQuery(SchemaLogQuery query, Integer firstResult,
			Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}
}