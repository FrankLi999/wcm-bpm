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
package org.camunda.bpm.engine.rest.sub.management.controller;

import org.camunda.bpm.engine.AuthorizationException;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.management.JobDefinition;
import org.camunda.bpm.engine.rest.JobDefinitionRestService;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.management.JobDefinitionDto;
import org.camunda.bpm.engine.rest.dto.management.JobDefinitionSuspensionStateDto;
import org.camunda.bpm.engine.rest.dto.runtime.JobDefinitionPriorityDto;
import org.camunda.bpm.engine.rest.dto.runtime.RetriesDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.management.JobDefinitionResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author roman.smirnov
 */
@RestController
@RequestMapping(JobDefinitionRestService.PATH + "/{jobDefinitionId}")
public class JobDefinitionResourceRestController extends AbstractRestProcessEngineAware implements JobDefinitionResource {
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public JobDefinitionDto getJobDefinition(@PathVariable("jobDefinitionId") String jobDefinitionId) {
		ManagementService managementService = this.processEngine.getManagementService();
		JobDefinition jobDefinition = managementService.createJobDefinitionQuery().jobDefinitionId(jobDefinitionId)
				.singleResult();

		if (jobDefinition == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Job Definition with id " + jobDefinitionId + " does not exist");
		}

		return JobDefinitionDto.fromJobDefinition(jobDefinition);
	}

	@PutMapping(path="/suspended", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateSuspensionState(@PathVariable("jobDefinitionId") String jobDefinitionId, @RequestBody JobDefinitionSuspensionStateDto dto) {
		try {
			dto.setJobDefinitionId(jobDefinitionId);
			dto.updateSuspensionState(this.processEngine);

		} catch (IllegalArgumentException e) {
			String message = String.format(
					"The suspension state of Job Definition with id %s could not be updated due to: %s",
					jobDefinitionId, e.getMessage());
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, message);
		}

	}

	@PutMapping(path="/retries", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void setJobRetries(@PathVariable("jobDefinitionId") String jobDefinitionId, @RequestBody RetriesDto dto) {
		try {
			ManagementService managementService = this.processEngine.getManagementService();
			managementService.setJobRetriesByJobDefinitionId(jobDefinitionId, dto.getRetries());
		} catch (AuthorizationException e) {
			throw e;
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@PutMapping(path="/jobPriority", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void setJobPriority(@PathVariable("jobDefinitionId") String jobDefinitionId, @RequestBody JobDefinitionPriorityDto dto) {
		try {
			ManagementService managementService = this.processEngine.getManagementService();

			if (dto.getPriority() != null) {
				managementService.setOverridingJobPriorityForJobDefinition(jobDefinitionId, dto.getPriority(),
						dto.isIncludeJobs());
			} else {
				if (dto.isIncludeJobs()) {
					throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
							"Cannot reset priority for job definition " + jobDefinitionId + " with includeJobs=true");
				}

				managementService.clearOverridingJobPriorityForJobDefinition(jobDefinitionId);
			}

		} catch (AuthorizationException e) {
			throw e;
		} catch (NotFoundException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (ProcessEngineException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
