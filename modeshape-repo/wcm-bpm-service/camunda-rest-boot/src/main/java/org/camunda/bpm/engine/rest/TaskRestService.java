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
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.camunda.bpm.engine.rest.dto.task.TaskQueryDto;

public interface TaskRestService {

	public static final String PATH = "/camunda/api/engine/task";
	
//	@Path("/{id}")
//	TaskResource getTask(@PathParam("id") String id);

	Object getTasks(HttpServletRequest request, Integer firstResult, Integer maxResults);

	/**
	 * Expects the same parameters as
	 * {@link TaskRestService#getTasks(UriInfo, Integer, Integer)} (as JSON message
	 * body) and allows more than one variable check.
	 * 
	 * @param query
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	List<TaskDto> queryTasks(TaskQueryDto query, Integer firstResult, Integer maxResults);

	CountResultDto getTasksCount(HttpServletRequest request);

	CountResultDto queryTasksCount(TaskQueryDto query);

	void createTask(TaskDto taskDto);

//	@Path("/report")
//	TaskReportResource getTaskReportResource();

}
