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
package org.camunda.bpm.engine.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.batch.BatchDto;
import org.camunda.bpm.engine.rest.dto.runtime.JobDto;
import org.camunda.bpm.engine.rest.dto.runtime.JobQueryDto;
import org.camunda.bpm.engine.rest.dto.runtime.JobSuspensionStateDto;
import org.camunda.bpm.engine.rest.dto.runtime.SetJobRetriesDto;

public interface JobRestService {

	static final String PATH = "/camunda/api/engine/job";

	List<JobDto> getJobs(
			HttpServletRequest request, 
			Integer firstResult,
			Integer maxResults);

	List<JobDto> queryJobs(
			JobQueryDto queryDto, 
			Integer firstResult,
			Integer maxResults);

	CountResultDto getJobsCount(HttpServletRequest request);

	CountResultDto queryJobsCount(JobQueryDto queryDto);

	BatchDto setRetries(SetJobRetriesDto setJobRetriesDto);

	void updateSuspensionState(JobSuspensionStateDto dto);
}
