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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.exception.NullValueException;
import org.camunda.bpm.engine.impl.util.EnsureUtil;
import org.camunda.bpm.engine.rest.JobRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.dto.runtime.JobDto;
import org.camunda.bpm.engine.rest.dto.runtime.JobQueryDto;
import org.camunda.bpm.engine.rest.dto.runtime.JobSuspensionStateDto;
import org.camunda.bpm.engine.rest.dto.runtime.SetJobRetriesDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.JobQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="jobApi")
@RequestMapping(JobRestService.PATH)
public class JobRestController extends AbstractRestProcessEngineAware implements JobRestService {

	@Override
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<JobDto> getJobs(HttpServletRequest request, @RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		JobQueryDto queryDto = new JobQueryDto(this.getObjectMapper(), request.getParameterMap());
		return queryJobs(queryDto, firstResult, maxResults);
	}

	@Override
	@PostMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<JobDto> queryJobs(
			@RequestBody JobQueryDto queryDto, 
			@RequestParam("firstResult") Integer firstResult,
			@RequestParam("maxResults") Integer maxResults) {
		queryDto.setObjectMapper(this.getObjectMapper());
		JobQuery query = queryDto.toQuery(this.getProcessEngine());

		List<Job> matchingJobs;
		if (firstResult != null || maxResults != null) {
			matchingJobs = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingJobs = query.list();
		}

		List<JobDto> jobResults = new ArrayList<JobDto>();
		for (Job job : matchingJobs) {
			JobDto resultJob = JobDto.fromJob(job);
			jobResults.add(resultJob);
		}
		return jobResults;
	}

	@Override
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getJobsCount(HttpServletRequest request) {
		JobQueryDto queryDto = new JobQueryDto(getObjectMapper(), request.getParameterMap());
		return queryJobsCount(queryDto);
	}

	@Override
	@PostMapping(path="/count", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryJobsCount(@RequestBody JobQueryDto queryDto) {
		ProcessEngine engine = getProcessEngine();
		queryDto.setObjectMapper(getObjectMapper());
		JobQuery query = queryDto.toQuery(engine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}

	@Override
	@PostMapping(path="/retries", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public BatchDto setRetries(@RequestBody SetJobRetriesDto setJobRetriesDto) {
		try {
			EnsureUtil.ensureNotNull("setJobRetriesDto", setJobRetriesDto);
			EnsureUtil.ensureNotNull("retries", setJobRetriesDto.getRetries());
		} catch (NullValueException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		JobQuery jobQuery = null;
		if (setJobRetriesDto.getJobQuery() != null) {
			jobQuery = setJobRetriesDto.getJobQuery().toQuery(getProcessEngine());
		}

		try {
			Batch batch = getProcessEngine().getManagementService().setJobRetriesAsync(setJobRetriesDto.getJobIds(),
					jobQuery, setJobRetriesDto.getRetries().intValue());
			return BatchDto.fromBatch(batch);
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping(path="/suspended", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateSuspensionState(@RequestBody JobSuspensionStateDto dto) {
		if (dto.getJobId() != null) {
			String message = "Either jobDefinitionId, processInstanceId, processDefinitionId or processDefinitionKey can be set to update the suspension state.";
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, message);
		}

		dto.updateSuspensionState(getProcessEngine());
	}
	
	private List<Job> executePaginatedQuery(JobQuery query, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}
}
