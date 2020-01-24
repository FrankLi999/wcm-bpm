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
package org.camunda.bpm.engine.rest.sub.task.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.TaskRestService;
import org.camunda.bpm.engine.rest.dto.converter.TaskReportResultToCsvConverter;
import org.camunda.bpm.engine.rest.dto.task.TaskCountByCandidateGroupResultDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.util.VariantUtils;
import org.camunda.bpm.engine.task.TaskCountByCandidateGroupResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TaskRestService.PATH + "/report")
public class TaskReportRestController {

	public static final MediaType APPLICATION_CSV = new MediaType("application", "csv");
	public static final MediaType TEXT_CSV = new MediaType("text", "csv");

	public static final String APPLICATION_CSV_VALUE = "application/csv";
	public static final String TEXT_CSV_VALUE = "text/csv";
	
	protected ProcessEngine engine;

	public TaskReportRestController(ProcessEngine engine) {
		this.engine = engine;
	}

	@GetMapping(path="/candidate-group-count", produces= {MediaType.APPLICATION_JSON_VALUE, "text/csv", "application/csv"})
	public ResponseEntity<?> getTaskCountByCandidateGroupReport(HttpServletRequest request) {

		if (VariantUtils.accept(request, MediaType.APPLICATION_JSON_VALUE)) {
			List<TaskCountByCandidateGroupResultDto> result = getTaskCountByCandidateGroupResultAsJson();
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result);
		} else if (VariantUtils.accept(request, APPLICATION_CSV_VALUE) || VariantUtils.accept(request, TEXT_CSV_VALUE)) {
			String csv = getReportResultAsCsv();
			String mediaType = VariantUtils.accept(request, APPLICATION_CSV_VALUE) ? APPLICATION_CSV_VALUE : TEXT_CSV_VALUE;
			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=task-count-by-candidate-group.csv").contentType(APPLICATION_CSV.equals(mediaType)? APPLICATION_CSV:TEXT_CSV).body(csv);
		}
		throw new InvalidRequestException(HttpStatus.NOT_ACCEPTABLE, "No acceptable content-type found");
	}

	protected List<TaskCountByCandidateGroupResult> queryTaskCountByCandidateGroupReport() {
		TaskCountByCandidateGroupResultDto reportDto = new TaskCountByCandidateGroupResultDto();
		return (List<TaskCountByCandidateGroupResult>) reportDto.executeTaskCountByCandidateGroupReport(engine);
	}

	protected List<TaskCountByCandidateGroupResultDto> getTaskCountByCandidateGroupResultAsJson() {
		List<TaskCountByCandidateGroupResult> reports = queryTaskCountByCandidateGroupReport();
		List<TaskCountByCandidateGroupResultDto> result = new ArrayList<TaskCountByCandidateGroupResultDto>();
		for (TaskCountByCandidateGroupResult report : reports) {
			result.add(TaskCountByCandidateGroupResultDto.fromTaskCountByCandidateGroupResultDto(report));
		}
		return result;
	}

	protected String getReportResultAsCsv() {
		List<TaskCountByCandidateGroupResult> reports = queryTaskCountByCandidateGroupReport();
		return TaskReportResultToCsvConverter.convertCandidateGroupReportResult(reports);
	}
}
