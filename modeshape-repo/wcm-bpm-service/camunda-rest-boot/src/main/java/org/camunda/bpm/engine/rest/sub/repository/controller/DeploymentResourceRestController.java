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
package org.camunda.bpm.engine.rest.sub.repository.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.exception.NotValidException;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.engine.rest.DeploymentRestService;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.repository.DeploymentDto;
import org.camunda.bpm.engine.rest.dto.repository.DeploymentWithDefinitionsDto;
import org.camunda.bpm.engine.rest.dto.repository.RedeploymentDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.sub.repository.DeploymentResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping( DeploymentRestService.PATH + "/{deploymentId}")
public class DeploymentResourceRestController extends AbstractRestProcessEngineAware implements DeploymentResource {
	public final static String PATH = "/camunda/api/engine/sub/repository/deployment";
	public static final String CASCADE = "cascade";
	
	@Autowired
	DeploymentResourcesResourceRestController deploymentResourcesService;
	
	public DeploymentResourceRestController() {}

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public DeploymentDto getDeployment(@PathVariable("deploymentId") String deploymentId) {
		RepositoryService repositoryService = this.processEngine.getRepositoryService();
		Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();

		if (deployment == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Deployment with id '" + deploymentId + "' does not exist");
		}

		return DeploymentDto.fromDeployment(deployment);
	}

	@PostMapping(path="/redeploy", produces=MediaType.APPLICATION_JSON_VALUE)
	public DeploymentDto redeploy(@PathVariable("deploymentId") String deploymentId, HttpServletRequest request, @RequestBody RedeploymentDto redeployment) {
		DeploymentWithDefinitions deployment = null;
		try {
			deployment = tryToRedeploy(deploymentId, redeployment);

		} catch (NotFoundException e) {
			throw createInvalidRequestException(deploymentId, "redeploy", HttpStatus.NOT_FOUND, e);

		} catch (NotValidException e) {
			throw createInvalidRequestException(deploymentId, "redeploy", HttpStatus.BAD_REQUEST, e);
		}

		DeploymentWithDefinitionsDto deploymentDto = DeploymentWithDefinitionsDto.fromDeployment(deployment);

		URI uri = UriComponentsBuilder.fromPath(rootResourcePath).path(DeploymentRestService.PATH)
				.path(deployment.getId()).build().toUri();

		// GET /
		deploymentDto.addReflexiveLink(uri, HttpMethod.GET.name(), "self");

		return deploymentDto;
	}

	@DeleteMapping(path="/")
	public void deleteDeployment(
			@PathVariable("deploymentId") String deploymentId, 
			HttpServletRequest request) {
		RepositoryService repositoryService = this.processEngine.getRepositoryService();
		Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();

		if (deployment == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Deployment with id '" + deploymentId + "' do not exist");
		}

		boolean cascade = isQueryPropertyEnabled(request, CASCADE);
		boolean skipCustomListeners = isQueryPropertyEnabled(request, "skipCustomListeners");
		boolean skipIoMappings = isQueryPropertyEnabled(request, "skipIoMappings");

		repositoryService.deleteDeployment(deploymentId, cascade, skipCustomListeners, skipIoMappings);
	}
	
	protected DeploymentWithDefinitions tryToRedeploy(@PathVariable("deploymentId") String deploymentId, RedeploymentDto redeployment) {
		RepositoryService repositoryService = this.processEngine.getRepositoryService();

		DeploymentBuilder builder = repositoryService.createDeployment();
		builder.nameFromDeployment(deploymentId);

		String tenantId = getDeployment(deploymentId).getTenantId();
		if (tenantId != null) {
			builder.tenantId(tenantId);
		}

		if (redeployment != null) {
			builder = addRedeploymentResources(deploymentId, builder, redeployment);
		} else {
			builder.addDeploymentResources(deploymentId);
		}

		return builder.deployWithResult();
	}

	protected DeploymentBuilder addRedeploymentResources(@PathVariable("deploymentId") String deploymentId, DeploymentBuilder builder, RedeploymentDto redeployment) {
		builder.source(redeployment.getSource());

		List<String> resourceIds = redeployment.getResourceIds();
		List<String> resourceNames = redeployment.getResourceNames();

		boolean isResourceIdListEmpty = resourceIds == null || resourceIds.isEmpty();
		boolean isResourceNameListEmpty = resourceNames == null || resourceNames.isEmpty();

		if (isResourceIdListEmpty && isResourceNameListEmpty) {
			builder.addDeploymentResources(deploymentId);

		} else {
			if (!isResourceIdListEmpty) {
				builder.addDeploymentResourcesById(deploymentId, resourceIds);
			}
			if (!isResourceNameListEmpty) {
				builder.addDeploymentResourcesByName(deploymentId, resourceNames);
			}
		}
		return builder;
	}

	protected boolean isQueryPropertyEnabled(HttpServletRequest request, String property) {
		Map<String, String[]> queryParams = request.getParameterMap();

		return queryParams.containsKey(property) && queryParams.get(property).length > 0
				&& "true".equals(queryParams.get(property)[0]);
	}

	protected InvalidRequestException createInvalidRequestException(String deploymentId, String action, HttpStatus status,
			ProcessEngineException cause) {
		String errorMessage = String.format("Cannot %s deployment '%s': %s", action, deploymentId, cause.getMessage());
		return new InvalidRequestException(status, cause, errorMessage);
	}

}
