package org.camunda.bpm.engine.rest.sub.repository;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.rest.dto.repository.DeploymentDto;
import org.camunda.bpm.engine.rest.dto.repository.RedeploymentDto;

public interface DeploymentResource {

	public static final String CASCADE = "cascade";

	DeploymentDto getDeployment(String deploymentId);

//	  @Path("/resources")
//	  DeploymentResourcesResource getDeploymentResources();

	DeploymentDto redeploy(String deploymentId, HttpServletRequest request, RedeploymentDto redeployment);

	void deleteDeployment(String deploymentId, HttpServletRequest request);
}
