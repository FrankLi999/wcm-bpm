package org.camunda.bpm.engine.rest.sub.identity;

import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.identity.TenantDto;

public interface TenantResource {
	TenantDto getTenant(String tenantId);
	void updateTenant(String tenantId, TenantDto tenantDto);
	void deleteTenant(String tenantId);
	ResourceOptionsDto availableOperations(String tenantId);
}
