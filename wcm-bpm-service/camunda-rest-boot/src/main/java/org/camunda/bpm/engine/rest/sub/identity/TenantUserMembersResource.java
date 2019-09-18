package org.camunda.bpm.engine.rest.sub.identity;

import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;

public interface TenantUserMembersResource {
	void createMembership(String tenantId, String userId);
	void deleteMembership(String tenantId, String userId);
	ResourceOptionsDto availableOperations(String tenantId);
}
