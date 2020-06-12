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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.DeploymentQuery;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.engine.rest.DeploymentRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.repository.DeploymentDto;
import org.camunda.bpm.engine.rest.dto.repository.DeploymentQueryDto;
import org.camunda.bpm.engine.rest.dto.repository.DeploymentWithDefinitionsDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@RestController(value="deploymentDefinitionApi")
@RequestMapping(DeploymentRestService.PATH)
public class DeploymentRestController extends AbstractRestProcessEngineAware implements DeploymentRestService {

	public final static String DEPLOYMENT_NAME = "deployment-name";
	public final static String ENABLE_DUPLICATE_FILTERING = "enable-duplicate-filtering";
	public final static String DEPLOY_CHANGED_ONLY = "deploy-changed-only";
	public final static String DEPLOYMENT_SOURCE = "deployment-source";
	public final static String TENANT_ID = "tenant-id";

	protected static final Set<String> RESERVED_KEYWORDS = new HashSet<String>();

	static {
		RESERVED_KEYWORDS.add(DEPLOYMENT_NAME);
		RESERVED_KEYWORDS.add(ENABLE_DUPLICATE_FILTERING);
		RESERVED_KEYWORDS.add(DEPLOY_CHANGED_ONLY);
		RESERVED_KEYWORDS.add(DEPLOYMENT_SOURCE);
		RESERVED_KEYWORDS.add(TENANT_ID);
	}

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<DeploymentDto> getDeployments(HttpServletRequest request, 
			@RequestParam("firstResult") Integer firstResult, 
			@RequestParam("maxResults") Integer maxResults) {
		DeploymentQueryDto queryDto = new DeploymentQueryDto(this.objectMapper, request.getParameterMap());

		DeploymentQuery query = queryDto.toQuery(this.processEngine);

		List<Deployment> matchingDeployments;
		if (firstResult != null || maxResults != null) {
			matchingDeployments = executePaginatedQuery(query, firstResult, maxResults);
		} else {
			matchingDeployments = query.list();
		}

		List<DeploymentDto> deployments = new ArrayList<DeploymentDto>();
		for (Deployment deployment : matchingDeployments) {
			DeploymentDto def = DeploymentDto.fromDeployment(deployment);
			deployments.add(def);
		}
		return deployments;
	}

	@PostMapping(path="/create", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public DeploymentWithDefinitionsDto createDeployment(HttpServletRequest request,
			@RequestParam(name=ENABLE_DUPLICATE_FILTERING, defaultValue="false") boolean enableDuplicateFiltering,
			@RequestParam(name=DEPLOY_CHANGED_ONLY, defaultValue="false")  boolean deployChangedOnly,
			@RequestParam(name=DEPLOYMENT_NAME, defaultValue="") String deploymentName,
			@RequestParam(name=DEPLOYMENT_SOURCE, defaultValue="") String deploymentSource,
			@RequestParam(name=TENANT_ID, defaultValue="") String tenantId,			
			@RequestParam("files") MultipartFile[] files) throws IOException {
		DeploymentBuilder deploymentBuilder = extractDeploymentInformation(enableDuplicateFiltering, deployChangedOnly, deploymentName, deploymentSource, tenantId,	files);

		if (!deploymentBuilder.getResourceNames().isEmpty()) {
			DeploymentWithDefinitions deployment = deploymentBuilder.deployWithResult();

			DeploymentWithDefinitionsDto deploymentDto = DeploymentWithDefinitionsDto.fromDeployment(deployment);

			URI uri = UriComponentsBuilder.fromPath(rootResourcePath).path(DeploymentRestService.PATH)
					.path(deployment.getId()).build().toUri();

			// GET
			deploymentDto.addReflexiveLink(uri, HttpMethod.GET.name(), "self");

			return deploymentDto;

		} else {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
					"No deployment resources contained in the form upload.");
		}
	}

	@GetMapping(path="/count", produces=MediaType.APPLICATION_JSON_VALUE)
	public CountResultDto getDeploymentsCount(HttpServletRequest request) {
		DeploymentQueryDto queryDto = new DeploymentQueryDto(this.objectMapper, request.getParameterMap());

		DeploymentQuery query = queryDto.toQuery(this.processEngine);

		long count = query.count();
		CountResultDto result = new CountResultDto();
		result.setCount(count);
		return result;
	}
	
	private DeploymentBuilder extractDeploymentInformation(
			boolean enableDuplicateFiltering,
			boolean deployChangedOnly,
			String deploymentName,
			String deploymentSource,
			String tenantId,
			MultipartFile[] files) throws IOException {
		DeploymentBuilder deploymentBuilder = this.processEngine.getRepositoryService().createDeployment();

		//Set<String> partNames = payload.getPartNames();

		for (MultipartFile file : files) {
			//FormPart part = payload.getNamedPart(name);
			if (!RESERVED_KEYWORDS.contains(file.getName())) {
				String fileName = StringUtils.cleanPath(file.getOriginalFilename());
				if (fileName != null) {
					deploymentBuilder.addInputStream(fileName,
							new ByteArrayInputStream(file.getBytes()));
				} else {
					throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
							"No file name found in the deployment resource described by form parameter '" + fileName
									+ "'.");
				}
			}
		}

		if (StringUtils.hasText(deploymentName)) {
			deploymentBuilder.name(deploymentName);
		} 
		if (StringUtils.hasText(deploymentSource)) {
			deploymentBuilder.source(deploymentSource);
		} 
		if (StringUtils.hasText(tenantId)) {
			deploymentBuilder.tenantId(tenantId);
		}
		this.extractDuplicateFilteringForDeployment(enableDuplicateFiltering, deployChangedOnly, deploymentBuilder);
		return deploymentBuilder;
	}

	private void extractDuplicateFilteringForDeployment(
			boolean enableDuplicateFiltering,
			boolean deployChangedOnly,
			DeploymentBuilder deploymentBuilder) throws IOException {
		
		// deployChangedOnly overrides the enableDuplicateFiltering setting
		if (deployChangedOnly) {
			deploymentBuilder.enableDuplicateFiltering(true);
		} else if (enableDuplicateFiltering) {
			deploymentBuilder.enableDuplicateFiltering(false);
		}
	}

	private List<Deployment> executePaginatedQuery(DeploymentQuery query, Integer firstResult, Integer maxResults) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResults == null) {
			maxResults = Integer.MAX_VALUE;
		}
		return query.listPage(firstResult, maxResults);
	}
}
