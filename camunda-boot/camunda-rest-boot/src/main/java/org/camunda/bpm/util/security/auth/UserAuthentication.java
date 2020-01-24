package org.camunda.bpm.util.security.auth;

import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.authorization.Authorization;

/**
 * <p>
 * An authentication for a user
 * </p>
 *
 * @author Daniel Meyer
 * @author nico.rehwaldt
 */
public class UserAuthentication extends Authentication {

	private static final long serialVersionUID = 1L;

	protected List<String> groupIds;

	protected List<String> tenantIds;

	protected Set<String> authorizedApps;

	public UserAuthentication(String userId, String processEngineName) {
		super(userId, processEngineName);
	}

	public List<String> getGroupIds() {
		return groupIds;
	}

	public boolean isAuthorizedForApp(String app) {
		return authorizedApps.contains(Authorization.ANY) || authorizedApps.contains(app);
	}

	public Set<String> getAuthorizedApps() {
		return authorizedApps;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setGroupIds(List<String> groupIds) {
		this.groupIds = groupIds;
	}

	public void setAuthorizedApps(Set<String> authorizedApps) {
		this.authorizedApps = authorizedApps;
	}
}
