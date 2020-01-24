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
import org.camunda.bpm.engine.rest.dto.externaltask.ExternalTaskDto;
import org.camunda.bpm.engine.rest.dto.externaltask.ExternalTaskQueryDto;
import org.camunda.bpm.engine.rest.dto.externaltask.FetchExternalTasksDto;
import org.camunda.bpm.engine.rest.dto.externaltask.LockedExternalTaskDto;
import org.camunda.bpm.engine.rest.dto.externaltask.SetRetriesForExternalTasksDto;

/**
 * @author Thorben Lindhauer
 *
 */
public interface ExternalTaskRestService {

	public static final String PATH = "/camunda/api/engine/external-task";

	List<ExternalTaskDto> getExternalTasks(HttpServletRequest request, Integer firstResult, Integer maxResults);

	List<ExternalTaskDto> queryExternalTasks(ExternalTaskQueryDto query, Integer firstResult, Integer maxResults);

	CountResultDto getExternalTasksCount(HttpServletRequest request);

	CountResultDto queryExternalTasksCount(ExternalTaskQueryDto query);

	List<LockedExternalTaskDto> fetchAndLock(FetchExternalTasksDto fetchingDto);

	void setRetries(SetRetriesForExternalTasksDto retriesDto);

	BatchDto setRetriesAsync(SetRetriesForExternalTasksDto retriesDto);

}
