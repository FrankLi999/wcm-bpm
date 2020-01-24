package org.camunda.bpm.engine.rest.sub.repository;

import java.util.List;

import org.camunda.bpm.engine.rest.dto.repository.DeploymentResourceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;

public interface DeploymentResourcesResource {
	List<DeploymentResourceDto> getDeploymentResources(String deploymentId);

	DeploymentResourceDto getDeploymentResource(String deploymentId, String resourceId);

	ResponseEntity<Resource> getDeploymentResourceData(String deploymentId, String resourceId);
}
