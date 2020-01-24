package org.camunda.bpm.engine.rest.sub.identity;

import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;

public interface TenantGroupMembersResource {
	void createMembership(String tenantId, String groupId);

	void deleteMembership(String tenantId, String groupId);

	ResourceOptionsDto availableOperations(String tenantId);
}
