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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.EntityTypes;
import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.authorization.Authorization;//.ANY;
import org.camunda.bpm.engine.authorization.Permissions; //.CREATE;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;//.FILTER;
import org.camunda.bpm.engine.exception.NotValidException;
import org.camunda.bpm.engine.filter.Filter;
import org.camunda.bpm.engine.filter.FilterQuery;
import org.camunda.bpm.engine.rest.ExecutionRestService;
import org.camunda.bpm.engine.rest.FilterRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.runtime.FilterDto;
import org.camunda.bpm.engine.rest.dto.runtime.FilterQueryDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping(FilterRestService.PATH)
public class FilterRestController extends AbstractAuthorizedRestResource implements FilterRestService {

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<FilterDto> getFilters(
			HttpServletRequest request, 
			@RequestParam("itemCount") Boolean itemCount,
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		FilterService filterService = this.processEngine.getFilterService();
		FilterQuery query = getQueryFromQueryParameters(request.getParameterMap());

		List<Filter> matchingFilters = executeFilterQuery(query, firstResult, maxResults);

		List<FilterDto> filters = new ArrayList<FilterDto>();
		for (Filter filter : matchingFilters) {
			FilterDto dto = FilterDto.fromFilter(filter);
			if (itemCount != null && itemCount) {
				dto.setItemCount(filterService.count(filter.getId()));
			}
			filters.add(dto);
		}

		return filters;
	}

	
	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getFiltersCount(HttpServletRequest request) {
		FilterQuery query = getQueryFromQueryParameters(request.getParameterMap());
		return new CountResultDto(query.count());
	}

	@PostMapping(path="/create", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public FilterDto createFilter(@RequestBody FilterDto filterDto) {
		FilterService filterService = this.processEngine.getFilterService();

		String resourceType = filterDto.getResourceType();

		Filter filter;

		if (EntityTypes.TASK.equals(resourceType)) {
			filter = filterService.newTaskFilter();
		} else {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
					"Unable to create filter with invalid resource type '" + resourceType + "'");
		}

		try {
			filterDto.updateFilter(filter, this.processEngine);
		} catch (NotValidException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e,
					"Unable to create filter with invalid content");
		}

		filterService.saveFilter(filter);

		return FilterDto.fromFilter(filter);
	}

	@RequestMapping(path="/create", method=RequestMethod.OPTIONS, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourceOptionsDto availableOperations() {

		UriComponentsBuilder baseUriBuilder = UriComponentsBuilder.fromPath(this.rootResourcePath)
				.path(FilterRestService.PATH);

		ResourceOptionsDto resourceOptionsDto = new ResourceOptionsDto();

		// GET /
		URI baseUri = baseUriBuilder.build().toUri();
		resourceOptionsDto.addReflexiveLink(baseUri, HttpMethod.GET.name(), "list");

		// GET /count
		URI countUri = baseUriBuilder.cloneBuilder().path("/count").build().toUri();
		resourceOptionsDto.addReflexiveLink(countUri, HttpMethod.GET.name(), "count");

		// POST /create
		if (isAuthorized(Permissions.CREATE, Resources.FILTER, Authorization.ANY)) {
			URI createUri = baseUriBuilder.cloneBuilder().path("/create").build().toUri();
			resourceOptionsDto.addReflexiveLink(createUri, HttpMethod.POST.name(), "create");
		}

		return resourceOptionsDto;
	}

	protected List<Filter> executeFilterQuery(FilterQuery query, Integer firstResult, Integer maxResults) {
		if (firstResult != null || maxResults != null) {
			return executePaginatedQuery(query, firstResult, maxResults);
		} else {
			return query.list();
		}
	}

	protected List<Filter> executePaginatedQuery(FilterQuery query, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}

	protected FilterQuery getQueryFromQueryParameters(Map<String, String[]> queryParameters) {
		FilterQueryDto queryDto = new FilterQueryDto(this.objectMapper, queryParameters);
		return queryDto.toQuery(this.processEngine);
	}
	
	protected Resource getResource() {
		return Resources.FILTER;
	}
}
