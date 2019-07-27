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

import static org.camunda.bpm.engine.authorization.Permissions.DELETE;
import static org.camunda.bpm.engine.authorization.Permissions.READ;
import static org.camunda.bpm.engine.authorization.Permissions.UPDATE;
import static org.camunda.bpm.engine.authorization.Resources.FILTER;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.EntityTypes;
import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.exception.NotValidException;
import org.camunda.bpm.engine.exception.NullValueException;
import org.camunda.bpm.engine.filter.Filter;
import org.camunda.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.camunda.bpm.engine.query.Query;
import org.camunda.bpm.engine.rest.FilterRestService;
import org.camunda.bpm.engine.rest.controller.AbstractAuthorizedRestResource;
import org.camunda.bpm.engine.rest.dto.AbstractQueryDto;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.runtime.FilterDto;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.camunda.bpm.engine.rest.dto.task.TaskQueryDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.hal.EmptyHalCollection;
import org.camunda.bpm.engine.rest.hal.EmptyHalResource;
import org.camunda.bpm.engine.rest.hal.Hal;
import org.camunda.bpm.engine.rest.hal.HalCollectionResource;
import org.camunda.bpm.engine.rest.hal.HalResource;
import org.camunda.bpm.engine.rest.hal.HalVariableValue;
import org.camunda.bpm.engine.rest.hal.task.HalTask;
import org.camunda.bpm.engine.rest.hal.task.HalTaskList;
import org.camunda.bpm.engine.rest.sub.runtime.FilterResource;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Sebastian Menski
 */
@RestController
@RequestMapping(FilterRestService.PATH + "/{filterId}")
public class FilterResourceRestController extends AbstractAuthorizedRestResource implements FilterResource {
	
	public static final Pattern EMPTY_JSON_BODY = Pattern.compile("\\s*\\{\\s*\\}\\s*");
	public static final String PROPERTIES_VARIABLES_KEY = "variables";
	public static final String PROPERTIES_VARIABLES_NAME_KEY = "name";
	
	protected FilterService filterService ;
	protected Filter dbFilter;
	
	@PostConstruct
	public void afterPropertiesSet() {
		filterService = processEngine.getFilterService();
	}
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public FilterDto getFilter(@PathVariable("filterId") String resourceId, @RequestParam("itemCount") Boolean itemCount) {
		Filter filter = getDbFilter(resourceId);
		FilterDto dto = FilterDto.fromFilter(filter);
		if (itemCount != null && itemCount) {
			dto.setItemCount(filterService.count(filter.getId()));
		}
		return dto;
	}

	@DeleteMapping(path="/")
	public void deleteFilter(@PathVariable("filterId") String resourceId) {
		try {
			filterService.deleteFilter(resourceId);
		} catch (NullValueException e) {
			throw filterNotFound(resourceId, e);
		}
	}

	@PutMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateFilter(@PathVariable("filterId") String resourceId, @RequestBody FilterDto filterDto) {
		Filter filter = getDbFilter(resourceId);

		try {
			filterDto.updateFilter(filter, processEngine);
		} catch (NotValidException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, "Unable to update filter with invalid content");
		}

		filterService.saveFilter(filter);
	}
	
	@GetMapping(path="/singleResult", produces= {MediaType.APPLICATION_JSON_VALUE, Hal.APPLICATION_HAL_JSON})
	public Object executeSingleResult(@PathVariable("filterId") String resourceId, HttpServletRequest request) {
		String contentType = request.getHeader("Accept");//.selectVariant(VARIANTS);
		if (contentType != null) {
			if (contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
				return executeJsonSingleResult(resourceId);
			} else if (contentType.startsWith(Hal.APPLICATION_HAL_JSON)) {
				return executeHalSingleResult(resourceId);
			}
		}
		throw new InvalidRequestException(HttpStatus.NOT_ACCEPTABLE, "No acceptable content-type found");
	}

	@PostMapping(path="/singleResult", consumes=MediaType.APPLICATION_JSON_VALUE, produces= {MediaType.APPLICATION_JSON_VALUE, Hal.APPLICATION_HAL_JSON})
	public Object querySingleResult(@PathVariable("filterId") String resourceId, HttpServletRequest request, @RequestBody String extendingQuery) {
		String contentType = request.getContentType();//.selectVariant(VARIANTS);
		if (contentType != null) {
			if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
				return queryJsonSingleResult(resourceId, extendingQuery);
			} else if (Hal.APPLICATION_HAL_JSON.equals(contentType)) {
				return queryHalSingleResult(resourceId, extendingQuery);
			}
		}
		throw new InvalidRequestException(HttpStatus.NOT_ACCEPTABLE, "No acceptable content-type found");
	}

	@GetMapping(path="/list", produces= {MediaType.APPLICATION_JSON_VALUE, Hal.APPLICATION_HAL_JSON})
	public Object executeList(
			@PathVariable("filterId") String resourceId, 
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		String contentType = request.getHeader("Accept");
		if (contentType != null) {
			if (contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
				return executeJsonList(resourceId, firstResult, maxResults);
			} else if (contentType.startsWith(Hal.APPLICATION_HAL_JSON)) {
				return executeHalList(resourceId, firstResult, maxResults);
			}
		}
		throw new InvalidRequestException(HttpStatus.NOT_ACCEPTABLE, "No acceptable content-type found");
	}

	@PostMapping(path="/list", produces= {MediaType.APPLICATION_JSON_VALUE, Hal.APPLICATION_HAL_JSON})
	public Object queryList(
			@PathVariable("filterId") String resourceId, 
			HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults,
			@RequestBody String extendingQuery) {
		
		String contentType = request.getHeader("Accept");
		if (contentType != null) {
			if (contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
				return queryJsonList(resourceId, extendingQuery, firstResult, maxResults);
			} else if (contentType.startsWith(Hal.APPLICATION_HAL_JSON)) {
				return queryHalList(resourceId, extendingQuery, firstResult, maxResults);
			}
		}
		throw new InvalidRequestException(HttpStatus.NOT_ACCEPTABLE, "No acceptable content-type found");
	}

	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto executeCount(@PathVariable("filterId") String resourceId) {
		return queryCount(resourceId, null);
	}

	@RequestMapping(path="/", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto queryCount(@PathVariable("filterId") String resourceId, @RequestBody String extendingQuery) {
		return new CountResultDto(executeFilterCount(resourceId, extendingQuery));
	}

	@RequestMapping(path="/", method={RequestMethod.OPTIONS}, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourceOptionsDto availableOperations(@PathVariable("filterId") String resourceId, HttpServletRequest request) {

		ResourceOptionsDto dto = new ResourceOptionsDto();

		UriComponentsBuilder baseUriBuilder = UriComponentsBuilder.fromPath(this.rootResourcePath)
				.path(FilterRestService.PATH).path(resourceId);

		URI baseUri = baseUriBuilder.build().toUri();

		if (this.isAuthorized(READ, FILTER, resourceId)) {
			dto.addReflexiveLink(baseUri, HttpMethod.GET.name(), "self");

			URI singleResultUri = baseUriBuilder.cloneBuilder().path("/singleResult").build().toUri();
			dto.addReflexiveLink(singleResultUri, HttpMethod.GET.name(), "singleResult");
			dto.addReflexiveLink(singleResultUri, HttpMethod.POST.name(), "singleResult");

			URI listUri = baseUriBuilder.cloneBuilder().path("/list").build().toUri();
			dto.addReflexiveLink(listUri, HttpMethod.GET.name(), "list");
			dto.addReflexiveLink(listUri, HttpMethod.POST.name(), "list");

			URI countUri = baseUriBuilder.cloneBuilder().path("/count").build().toUri();
			dto.addReflexiveLink(countUri, HttpMethod.GET.name(), "count");
			dto.addReflexiveLink(countUri, HttpMethod.POST.name(), "count");
		}

		if (this.isAuthorized(DELETE, FILTER, resourceId)) {
			dto.addReflexiveLink(baseUri, HttpMethod.DELETE.name(), "delete");
		}

		if (this.isAuthorized(UPDATE, FILTER, resourceId)) {
			dto.addReflexiveLink(baseUri, HttpMethod.PUT.name(), "update");
		}

		return dto;
	}

	@SuppressWarnings("rawtypes")
	protected Query convertQuery(String resourceId, String queryString) {
		if (isEmptyJson(queryString)) {
			return null;
		} else {
			String resourceType = getDbFilter(resourceId).getResourceType();
			AbstractQueryDto<?> queryDto = getQueryDtoForQuery(queryString, resourceType);
			queryDto.setObjectMapper(this.objectMapper);
			return queryDto.toQuery(processEngine);
		}
	}

	protected Object convertToDto(Object entity) {
		if (isEntityOfClass(entity, Task.class)) {
			return TaskDto.fromEntity((Task) entity);
		} else {
			throw unsupportedEntityClass(entity);
		}
	}

	protected List<Object> convertToDtoList(List<?> entities) {
		List<Object> dtoList = new ArrayList<>();
		for (Object entity : entities) {
			dtoList.add(convertToDto(entity));
		}
		return dtoList;
	}

	protected HalResource<?> convertToHalResource(String resourceId, Object entity) {
		if (isEntityOfClass(entity, Task.class)) {
			return convertToHalTask(resourceId, (Task) entity);
		} else {
			throw unsupportedEntityClass(entity);
		}
	}

	protected HalTask convertToHalTask(String resourceId, Task task) {
		HalTask halTask = HalTask.generate(task, this.processEngine);
		Map<String, List<VariableInstance>> variableInstances = getVariableInstancesForTasks(resourceId, halTask);
		if (variableInstances != null) {
			embedVariableValuesInHalTask(halTask, variableInstances);
		}
		return halTask;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected HalCollectionResource convertToHalCollection(String resourceId, List<?> entities, long count) {
		if (isEntityOfClass(entities.get(0), Task.class)) {
			return convertToHalTaskList(resourceId, (List<Task>) entities, count);
		} else {
			throw unsupportedEntityClass(entities.get(0));
		}
	}

	@SuppressWarnings("unchecked")
	protected HalTaskList convertToHalTaskList(String resourceId, List<Task> tasks, long count) {
		HalTaskList halTasks = HalTaskList.generate(tasks, count, this.processEngine);
		Map<String, List<VariableInstance>> variableInstances = getVariableInstancesForTasks(resourceId, halTasks);
		if (variableInstances != null) {
			for (HalTask halTask : (List<HalTask>) halTasks.getEmbedded("task")) {
				embedVariableValuesInHalTask(halTask, variableInstances);
			}
		}
		return halTasks;
	}

	protected void embedVariableValuesInHalTask(HalTask halTask,
			Map<String, List<VariableInstance>> variableInstances) {
		List<HalResource<?>> variableValues = getVariableValuesForTask(halTask, variableInstances);
		halTask.addEmbedded("variable", variableValues);
	}

	protected AbstractQueryDto<?> getQueryDtoForQuery(String queryString, String resourceType) {
		try {
			if (EntityTypes.TASK.equals(resourceType)) {
				return this.objectMapper.readValue(queryString, TaskQueryDto.class);
			} else {
				throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
						"Queries for resource type '" + resourceType + "' are currently not supported by filters.");
			}
		} catch (IOException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e,
					"Invalid query for resource type '" + resourceType + "'");
		}
	}

	protected List<HalResource<?>> getVariableValuesForTask(HalTask halTask,
			Map<String, List<VariableInstance>> variableInstances) {
		// converted variables values
		List<HalResource<?>> variableValues = new ArrayList<>();

		// variable scope ids to check, ordered by visibility
		LinkedHashSet<String> variableScopeIds = getVariableScopeIds(halTask);

		// names of already converted variables
		Set<String> knownVariableNames = new HashSet<>();

		for (String variableScopeId : variableScopeIds) {
			if (variableInstances.containsKey(variableScopeId)) {
				for (VariableInstance variableInstance : variableInstances.get(variableScopeId)) {
					if (!knownVariableNames.contains(variableInstance.getName())) {
						variableValues.add(HalVariableValue.generateVariableValue(variableInstance, variableScopeId));
						knownVariableNames.add(variableInstance.getName());
					}
				}
			}
		}

		return variableValues;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, List<VariableInstance>> getVariableInstancesForTasks(String resourceId, HalTaskList halTaskList) {
		List<HalTask> halTasks = (List<HalTask>) halTaskList.getEmbedded("task");
		return getVariableInstancesForTasks(resourceId, halTasks.toArray(new HalTask[halTasks.size()]));
	}

	protected Map<String, List<VariableInstance>> getVariableInstancesForTasks(String resourceId, HalTask... halTasks) {
		if (halTasks != null && halTasks.length > 0) {
			List<String> variableNames = getFilterVariableNames(resourceId);
			if (variableNames != null && !variableNames.isEmpty()) {
				LinkedHashSet<String> variableScopeIds = getVariableScopeIds(halTasks);
				return getSortedVariableInstances(variableNames, variableScopeIds);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<String> getFilterVariableNames(String resourceId) {
		Map<String, Object> properties = getDbFilter(resourceId).getProperties();
		if (properties != null) {
			try {
				List<Map<String, Object>> variables = (List<Map<String, Object>>) properties
						.get(PROPERTIES_VARIABLES_KEY);
				return collectVariableNames(variables);
			} catch (Exception e) {
				throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR, e,
						"Filter property '" + PROPERTIES_VARIABLES_KEY
								+ "' has to be a list of variable definitions with a '" + PROPERTIES_VARIABLES_NAME_KEY
								+ "' property");
			}
		} else {
			return null;
		}
	}

	private List<String> collectVariableNames(List<Map<String, Object>> variables) {
		if (variables != null && !variables.isEmpty()) {
			List<String> variableNames = new ArrayList<>();
			for (Map<String, Object> variable : variables) {
				variableNames.add((String) variable.get(PROPERTIES_VARIABLES_NAME_KEY));
			}
			return variableNames;
		} else {
			return null;
		}
	}

	protected LinkedHashSet<String> getVariableScopeIds(HalTask... halTasks) {
		// collect scope ids
		// the ordering is important because it specifies which variables are visible
		// from a single task
		LinkedHashSet<String> variableScopeIds = new LinkedHashSet<>();
		if (halTasks != null && halTasks.length > 0) {
			for (HalTask halTask : halTasks) {
				variableScopeIds.add(halTask.getId());
				variableScopeIds.add(halTask.getExecutionId());
				variableScopeIds.add(halTask.getProcessInstanceId());
				variableScopeIds.add(halTask.getCaseExecutionId());
				variableScopeIds.add(halTask.getCaseInstanceId());
			}
		}

		// remove null from set which was probably added due an unset id
		variableScopeIds.remove(null);

		return variableScopeIds;
	}

	protected Map<String, List<VariableInstance>> getSortedVariableInstances(Collection<String> variableNames,
			Collection<String> variableScopeIds) {
		List<VariableInstance> variableInstances = queryVariablesInstancesByVariableScopeIds(variableNames,
				variableScopeIds);
		Map<String, List<VariableInstance>> sortedVariableInstances = new HashMap<>();
		for (VariableInstance variableInstance : variableInstances) {
			String variableScopeId = ((VariableInstanceEntity) variableInstance).getVariableScopeId();
			if (!sortedVariableInstances.containsKey(variableScopeId)) {
				sortedVariableInstances.put(variableScopeId, new ArrayList<VariableInstance>());
			}
			sortedVariableInstances.get(variableScopeId).add(variableInstance);
		}
		return sortedVariableInstances;
	}

	protected List<VariableInstance> queryVariablesInstancesByVariableScopeIds(Collection<String> variableNames,
			Collection<String> variableScopeIds) {

		return this.processEngine.getRuntimeService().createVariableInstanceQuery().disableBinaryFetching()
				.disableCustomObjectDeserialization()
				.variableNameIn(variableNames.toArray(new String[variableNames.size()]))
				.variableScopeIdIn(variableScopeIds.toArray(new String[variableScopeIds.size()])).list();

	}

	protected boolean isEntityOfClass(Object entity, Class<?> entityClass) {
		return entityClass.isAssignableFrom(entity.getClass());
	}

	protected boolean isEmptyJson(String jsonString) {
		return jsonString == null || jsonString.trim().isEmpty() || EMPTY_JSON_BODY.matcher(jsonString).matches();
	}

	protected InvalidRequestException filterNotFound(String resourceId, Exception cause) {
		return new InvalidRequestException(HttpStatus.NOT_FOUND, cause,
				"Filter with id '" + resourceId + "' does not exist.");
	}

	protected InvalidRequestException invalidQuery(Exception cause) {
		return new InvalidRequestException(HttpStatus.BAD_REQUEST, cause, "Filter cannot be extended by an invalid query");
	}

	protected InvalidRequestException unsupportedEntityClass(Object entity) {
		return new InvalidRequestException(HttpStatus.BAD_REQUEST, "Entities of class '"
				+ entity.getClass().getCanonicalName() + "' are currently not supported by filters.");
	}

	protected Filter getDbFilter(String resourceId) {
		if (dbFilter == null) {
			dbFilter = filterService.getFilter(resourceId);

			if (dbFilter == null) {
				throw filterNotFound(resourceId, null);
			}
		}
		return dbFilter;
	}
	
	protected Object executeJsonSingleResult(String resourceId) {
		return queryJsonSingleResult(resourceId, null);
	}
	
	protected Object queryJsonSingleResult(String resourceId, String extendingQuery) {
		Object entity = executeFilterSingleResult(resourceId, extendingQuery);

		if (entity != null) {
			return convertToDto(entity);
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	protected HalResource executeHalSingleResult(String resourceId) {
		return queryHalSingleResult(resourceId, null);
	}
	
	@SuppressWarnings("rawtypes")
	protected HalResource queryHalSingleResult(String resourceId, String extendingQuery) {
		Object entity = executeFilterSingleResult(resourceId, extendingQuery);

		if (entity != null) {
			return convertToHalResource(resourceId, entity);
		} else {
			return EmptyHalResource.INSTANCE;
		}
	}

	@SuppressWarnings("unchecked")
	protected Object executeFilterSingleResult(String resourceId, String extendingQuery) {
		try {
			return filterService.singleResult(resourceId, convertQuery(resourceId, extendingQuery));
		} catch (NullValueException e) {
			throw filterNotFound(resourceId, e);
		} catch (NotValidException e) {
			throw invalidQuery(e);
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, "Filter does not returns a valid single result");
		}
	}
	
	protected List<Object> executeJsonList(String resourceId, Integer firstResult, Integer maxResults) {
		return queryJsonList(resourceId, null, firstResult, maxResults);
	}
	
	protected List<Object> queryJsonList(String resourceId, String extendingQuery, Integer firstResult, Integer maxResults) {
		List<?> entities = executeFilterList(resourceId, extendingQuery, firstResult, maxResults);

		if (entities != null && !entities.isEmpty()) {
			return convertToDtoList(entities);
		} else {
			return Collections.emptyList();
		}
	}

	@SuppressWarnings("rawtypes")
	protected HalResource executeHalList(String resourceId, Integer firstResult, Integer maxResults) {
		return queryHalList(resourceId, null, firstResult, maxResults);
	}

	@SuppressWarnings("rawtypes")
	protected HalResource queryHalList(String resourceId, String extendingQuery, Integer firstResult, Integer maxResults) {
		List<?> entities = executeFilterList(resourceId, extendingQuery, firstResult, maxResults);
		long count = executeFilterCount(resourceId, extendingQuery);

		if (entities != null && !entities.isEmpty()) {
			return convertToHalCollection(resourceId, entities, count);
		} else {
			return new EmptyHalCollection(count);
		}
	}

	protected List<?> executeFilterList(String resourceId, String extendingQueryString, Integer firstResult, Integer maxResults) {
		Query<?, ?> extendingQuery = convertQuery(resourceId, extendingQueryString);
		try {
			if (firstResult != null || maxResults != null) {
				if (firstResult == null) {
					firstResult = 0;
				}
				if (maxResults == null) {
					maxResults = Integer.MAX_VALUE;
				}
				return filterService.listPage(resourceId, extendingQuery, firstResult, maxResults);
			} else {
				return filterService.list(resourceId, extendingQuery);
			}
		} catch (NullValueException e) {
			throw filterNotFound(resourceId, e);
		} catch (NotValidException e) {
			throw invalidQuery(e);
		}
	}
	
	protected long executeFilterCount(String resourceId, String extendingQuery) {
		try {
			return filterService.count(resourceId, convertQuery(resourceId, extendingQuery));
		} catch (NullValueException e) {
			throw filterNotFound(resourceId, e);
		} catch (NotValidException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, "Filter cannot be extended by an invalid query");
		}
	}
	
	protected Resource getResource() {
		return Resources.FILTER;
	}
}
