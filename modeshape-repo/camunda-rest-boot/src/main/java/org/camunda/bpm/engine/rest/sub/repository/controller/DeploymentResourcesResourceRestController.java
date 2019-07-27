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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Resource;
import org.camunda.bpm.engine.rest.DeploymentRestService;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.repository.DeploymentResourceDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.sub.repository.DeploymentResourcesResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sebastian Menski
 */
@RestController
@RequestMapping( DeploymentRestService.PATH + "/{deploymentId}/resources")
public class DeploymentResourcesResourceRestController extends AbstractRestProcessEngineAware implements DeploymentResourcesResource {

	protected static final Map<String, String> MEDIA_TYPE_MAPPING = new HashMap<String, String>();
	
	static {
		MEDIA_TYPE_MAPPING.put("bpmn", MediaType.APPLICATION_XML_VALUE);
		MEDIA_TYPE_MAPPING.put("cmmn", MediaType.APPLICATION_XML_VALUE);
		MEDIA_TYPE_MAPPING.put("dmn", MediaType.APPLICATION_XML_VALUE);
		MEDIA_TYPE_MAPPING.put("json", MediaType.APPLICATION_JSON_VALUE);
		MEDIA_TYPE_MAPPING.put("xml", MediaType.APPLICATION_XML_VALUE);

		MEDIA_TYPE_MAPPING.put("gif", "image/gif");
		MEDIA_TYPE_MAPPING.put("jpeg", "image/jpeg");
		MEDIA_TYPE_MAPPING.put("jpe", "image/jpeg");
		MEDIA_TYPE_MAPPING.put("jpg", "image/jpeg");
		MEDIA_TYPE_MAPPING.put("png", "image/png");
		MEDIA_TYPE_MAPPING.put("svg", "image/svg+xml");
		MEDIA_TYPE_MAPPING.put("tiff", "image/tiff");
		MEDIA_TYPE_MAPPING.put("tif", "image/tiff");

		MEDIA_TYPE_MAPPING.put("groovy", "text/plain");
		MEDIA_TYPE_MAPPING.put("java", "text/plain");
		MEDIA_TYPE_MAPPING.put("js", "text/plain");
		MEDIA_TYPE_MAPPING.put("php", "text/plain");
		MEDIA_TYPE_MAPPING.put("py", "text/plain");
		MEDIA_TYPE_MAPPING.put("rb", "text/plain");

		MEDIA_TYPE_MAPPING.put("html", "text/html");
		MEDIA_TYPE_MAPPING.put("txt", "text/plain");
	}

	
	@GetMapping(path="/resources", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<DeploymentResourceDto> getDeploymentResources(String deploymentId) {
		List<Resource> resources = this.processEngine.getRepositoryService().getDeploymentResources(deploymentId);

		List<DeploymentResourceDto> deploymentResources = new ArrayList<DeploymentResourceDto>();
		for (Resource resource : resources) {
			deploymentResources.add(DeploymentResourceDto.fromResources(resource));
		}

		if (!deploymentResources.isEmpty()) {
			return deploymentResources;
		} else {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Deployment resources for deployment id '" + deploymentId + "' do not exist.");
		}
	}

	@GetMapping(path="/resources/{resourceId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public DeploymentResourceDto getDeploymentResource(String deploymentId, String resourceId) {
		List<DeploymentResourceDto> deploymentResources = getDeploymentResources(deploymentId);
		for (DeploymentResourceDto deploymentResource : deploymentResources) {
			if (deploymentResource.getId().equals(resourceId)) {
				return deploymentResource;
			}
		}

		throw new InvalidRequestException(HttpStatus.NOT_FOUND, "Deployment resource with resource id '" + resourceId
				+ "' for deployment id '" + deploymentId + "' does not exist.");
	}

	@GetMapping(path="/resources/{resourceId}/data", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<org.springframework.core.io.Resource> getDeploymentResourceData(String deploymentId, String resourceId) {
		RepositoryService repositoryService = this.processEngine.getRepositoryService();
		InputStream resourceAsStream = repositoryService.getResourceAsStreamById(deploymentId, resourceId);

		if (resourceAsStream != null) {

			DeploymentResourceDto resource = getDeploymentResource(deploymentId, resourceId);

			String name = resource.getName();

			String filename = null;
			String mediaType = null;

			if (name != null) {
				name = name.replace("\\", "/");
				String[] filenameParts = name.split("/");
				if (filenameParts.length > 0) {
					int idx = filenameParts.length - 1;
					filename = filenameParts[idx];
				}

				String[] extensionParts = name.split("\\.");
				if (extensionParts.length > 0) {
					int idx = extensionParts.length - 1;
					String extension = extensionParts[idx];
					if (extension != null) {
						mediaType = MEDIA_TYPE_MAPPING.get(extension);
					}
				}
			}

			if (filename == null) {
				filename = "data";
			}

			if (mediaType == null) {
				mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
			}

			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + filename).contentType(MediaType.parseMediaType(mediaType)).body(new InputStreamResource(resourceAsStream));
		} else {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "Deployment resource '" + resourceId
					+ "' for deployment id '" + deploymentId + "' does not exist.");
		}
	}
}
