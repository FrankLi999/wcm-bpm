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
package org.camunda.bpm.engine.rest.sub.runtime.controller;

import org.camunda.bpm.engine.AuthorizationException;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.exception.NullValueException;
import org.camunda.bpm.engine.rest.JobRestService;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.runtime.JobDto;
import org.camunda.bpm.engine.rest.dto.runtime.JobDuedateDto;
import org.camunda.bpm.engine.rest.dto.runtime.JobSuspensionStateDto;
import org.camunda.bpm.engine.rest.dto.runtime.PriorityDto;
import org.camunda.bpm.engine.rest.dto.runtime.RetriesDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.runtime.JobResource;
import org.camunda.bpm.engine.runtime.Job;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(JobRestService.PATH + "/{jobId}")
public class JobResourceRestController extends AbstractRestProcessEngineAware implements JobResource {
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public JobDto getJob(@PathVariable("jobId") String jobId) {
		ManagementService managementService = this.processEngine.getManagementService();
		Job job = managementService.createJobQuery().jobId(jobId).singleResult();

		if (job == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "Job with id " + jobId + " does not exist");
		}

		return JobDto.fromJob(job);
	}
	
	@GetMapping(path="/stacktrace", produces=MediaType.TEXT_PLAIN_VALUE)
	public String getStacktrace(@PathVariable("jobId") String jobId) {
		try {
			ManagementService managementService = this.processEngine.getManagementService();
			String stacktrace = managementService.getJobExceptionStacktrace(jobId);
			return stacktrace;
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PutMapping(path="/retries", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void setJobRetries(@PathVariable("jobId") String jobId, @RequestBody RetriesDto dto) {
		try {
			ManagementService managementService = this.processEngine.getManagementService();
			managementService.setJobRetries(jobId, dto.getRetries());
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@PostMapping(path="/execute")
	public void executeJob(@PathVariable("jobId") String jobId) {
		try {
			ManagementService managementService = this.processEngine.getManagementService();
			managementService.executeJob(jobId);
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (RuntimeException r) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, r.getMessage());
		}
	}

	@PutMapping(path="/duedate", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void setJobDuedate(@PathVariable("jobId") String jobId, @RequestBody JobDuedateDto dto) {
		try {
			ManagementService managementService = this.processEngine.getManagementService();
			managementService.setJobDuedate(jobId, dto.getDuedate());
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@PostMapping(path="/duedate/recalculate")
	public void recalculateDuedate(
			@PathVariable("jobId") String jobId, 
			@RequestParam(name="creationDateBased", defaultValue="true") boolean creationDateBased) {
		try {
			ManagementService managementService = this.processEngine.getManagementService();
			managementService.recalculateJobDuedate(jobId, creationDateBased);
		} catch (AuthorizationException e) {
			throw e;
		} catch (NotFoundException e) {// rewrite status code from bad request (400) to not found (404)
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e, e.getMessage());
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@PutMapping(path="/suspended", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateSuspensionState(@PathVariable("jobId") String jobId, @RequestBody JobSuspensionStateDto dto) {
		dto.setJobId(jobId);
		dto.updateSuspensionState(this.processEngine);
	}

	@PutMapping(path="/priority", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void setJobPriority(@PathVariable("jobId") String jobId, @RequestBody PriorityDto dto) {
		if (dto.getPriority() == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Priority for job '" + jobId + "' cannot be null.");
		}

		try {
			ManagementService managementService = this.processEngine.getManagementService();
			managementService.setJobPriority(jobId, dto.getPriority());
		} catch (AuthorizationException e) {
			throw e;
		} catch (NotFoundException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (ProcessEngineException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@DeleteMapping("/")
	public void deleteJob(@PathVariable("jobId") String jobId) {
		try {
			this.processEngine.getManagementService().deleteJob(jobId);
		} catch (AuthorizationException e) {
			throw e;
		} catch (NullValueException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (ProcessEngineException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
