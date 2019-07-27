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

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.exception.NotValidException;
import org.camunda.bpm.engine.rest.TaskRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.camunda.bpm.engine.rest.dto.task.TaskQueryDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.hal.Hal;
import org.camunda.bpm.engine.rest.hal.task.HalTaskList;
import org.camunda.bpm.engine.rest.util.VariantUtils;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(TaskRestService.PATH)
public class TaskRestController implements TaskRestService {

	public ObjectMapper objectMapper = new ObjectMapper();

	@Autowired 
	ProcessEngine engine;

	@GetMapping(path="/", produces= {MediaType.APPLICATION_JSON_VALUE, Hal.APPLICATION_HAL_JSON})
	public Object getTasks(
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		
	
		if (VariantUtils.accept(request, MediaType.APPLICATION_JSON)) {
			return getJsonTasks(request, firstResult, maxResults);
		} else if (VariantUtils.accept(request, Hal.APPLICATION_HAL_JSON)) {
			return getHalTasks(request, firstResult, maxResults);
		}
		
		throw new InvalidRequestException(HttpStatus.NOT_ACCEPTABLE, "No acceptable content-type found");
	}

	@PostMapping(path="/", consumes= {MediaType.APPLICATION_JSON_VALUE}, produces= {MediaType.APPLICATION_JSON_VALUE})
	public List<TaskDto> queryTasks(
			@RequestBody TaskQueryDto queryDto, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		queryDto.setObjectMapper(this.objectMapper);
		TaskQuery query = queryDto.toQuery(engine);

		List<Task> matchingTasks = executeTaskQuery(firstResult, maxResults, query);

		List<TaskDto> tasks = new ArrayList<TaskDto>();
		for (Task task : matchingTasks) {
			TaskDto returnTask = TaskDto.fromEntity(task);
			tasks.add(returnTask);
		}

		return tasks;
	}

	@GetMapping(path="/count", produces= {MediaType.APPLICATION_JSON_VALUE})
	public CountResultDto getTasksCount(HttpServletRequest request) {
		TaskQueryDto queryDto = new TaskQueryDto(this.objectMapper, request.getParameterMap());
		return queryTasksCount(queryDto);
	}
	
	@PostMapping(path="/count", consumes= {MediaType.APPLICATION_JSON_VALUE}, produces= {MediaType.APPLICATION_JSON_VALUE})
	public CountResultDto queryTasksCount(TaskQueryDto queryDto) {
		queryDto.setObjectMapper(this.objectMapper);
		TaskQuery query = queryDto.toQuery(engine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);

		return result;
	}

	public void createTask(TaskDto taskDto) {
		TaskService taskService = engine.getTaskService();

		Task newTask = taskService.newTask(taskDto.getId());
		taskDto.updateTask(newTask);

		try {
			taskService.saveTask(newTask);

		} catch (NotValidException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, "Could not save task: " + e.getMessage());
		}

	}

	@PostMapping(path="/create", produces= {MediaType.APPLICATION_JSON_VALUE})
	protected List<TaskDto> getJsonTasks(HttpServletRequest request, Integer firstResult, Integer maxResults) {
		TaskQueryDto queryDto = new TaskQueryDto(this.objectMapper, request.getParameterMap());
		return queryTasks(queryDto, firstResult, maxResults);
	}

	protected HalTaskList getHalTasks(HttpServletRequest request, Integer firstResult, Integer maxResults) {
		TaskQueryDto queryDto = new TaskQueryDto(this.objectMapper, request.getParameterMap());

		TaskQuery query = queryDto.toQuery(engine);

		// get list of tasks
		List<Task> matchingTasks = executeTaskQuery(firstResult, maxResults, query);

		// get total count
		long count = query.count();

		return HalTaskList.generate(matchingTasks, count, engine);
	}

	protected List<Task> executeTaskQuery(Integer firstResult, Integer maxResults, TaskQuery query) {

		// enable initialization of form key:
		query.initializeFormKeys();

		List<Task> matchingTasks;
		if (firstResult != null || maxResults != null) {
			matchingTasks = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingTasks = query.list();
		}
		return matchingTasks;
	}

	protected List<Task> executePaginatedQuery(TaskQuery query, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}
}
