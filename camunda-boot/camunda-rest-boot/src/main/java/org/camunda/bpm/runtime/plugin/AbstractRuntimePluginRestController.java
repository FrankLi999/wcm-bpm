package org.camunda.bpm.runtime.plugin;

import java.util.List;
import org.camunda.bpm.runtime.db.CommandExecutor;
import org.camunda.bpm.runtime.db.QueryParameters;
import org.camunda.bpm.runtime.db.QueryService;

import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.authorization.Permission;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.impl.db.AuthorizationCheck;
import org.camunda.bpm.engine.impl.db.PermissionCheck;
import org.camunda.bpm.engine.impl.db.TenantCheck;
import org.camunda.bpm.engine.impl.identity.Authentication;
import  org.camunda.bpm.runtime.CamundaRuntime;

/**
 * Base class for implementing a plugin REST resource.
 *
 */
public class AbstractRuntimePluginRestController extends AbstractPluginRestController<CamundaRuntimePlugin> {

  public AbstractRuntimePluginRestController() {
    super(CamundaRuntime.getRuntimeDelegate());
  }

  /**
   * Return a {@link CommandExecutor} for the current
   * engine to execute plugin commands.
   *
   * @return
   */
  protected CommandExecutor getCommandExecutor() {
    return CamundaRuntime.getCommandExecutor();
  }

  /**
   * Return a {@link QueryService} for the current
   * engine to execute queries against the engine datbase.
   *
   * @return
   */
  protected QueryService getQueryService() {
    return CamundaRuntime.getQueryService();
  }

  // authorization //////////////////////////////////////////////////////////////

  /**
   * Return <code>true</code> if authorization is enabled.
   */
  protected boolean isAuthorizationEnabled() {
    return getProcessEngine().getProcessEngineConfiguration().isAuthorizationEnabled();
  }

  /**
   * Return <code>true</code> if tenant check is enabled.
   */
  protected boolean isTenantCheckEnabled() {
    return getProcessEngine().getProcessEngineConfiguration().isTenantCheckEnabled()
        && getCurrentAuthentication() != null
        && !isCamundaAdmin(getCurrentAuthentication());
  }

  /**
   * Return <code>true</code> if the given authentication contains the group {@link Groups#CAMUNDA_ADMIN}.
   */
  protected boolean isCamundaAdmin(Authentication authentication) {
    List<String> groupIds = authentication.getGroupIds();
    if (groupIds != null) {
      return groupIds.contains(Groups.CAMUNDA_ADMIN);
    } else {
      return false;
    }
  }

  /**
   * Return the current authentication.
   */
  protected Authentication getCurrentAuthentication() {
    return getProcessEngine().getIdentityService().getCurrentAuthentication();
  }

  /**
   * Configure the authorization check for the given {@link QueryParameters}.
   */
  protected void configureAuthorizationCheck(QueryParameters<?> query) {
    Authentication currentAuthentication = getCurrentAuthentication();

    AuthorizationCheck authCheck = query.getAuthCheck();

    authCheck.getPermissionChecks().clear();

    if (isAuthorizationEnabled() && currentAuthentication != null) {
      authCheck.setAuthorizationCheckEnabled(true);
      String currentUserId = currentAuthentication.getUserId();
      List<String> currentGroupIds = currentAuthentication.getGroupIds();
      authCheck.setAuthUserId(currentUserId);
      authCheck.setAuthGroupIds(currentGroupIds);
    }
  }

  /**
   * Configure the tenant check for the given {@link QueryParameters}.
   */
  protected void configureTenantCheck(QueryParameters<?> query) {
    TenantCheck tenantCheck = query.getTenantCheck();

    if (isTenantCheckEnabled()) {
      Authentication currentAuthentication = getCurrentAuthentication();

      tenantCheck.setTenantCheckEnabled(true);
      tenantCheck.setAuthTenantIds(currentAuthentication.getTenantIds());
    } else {
      tenantCheck.setTenantCheckEnabled(false);
      tenantCheck.setAuthTenantIds(null);
    }
  }

  /**
   * Add a new {@link PermissionCheck} with the given values.
   */
  protected void addPermissionCheck(QueryParameters<?>  query, Resource resource, String queryParam, Permission permission) {
    if(!isPermissionDisabled(permission)){
      PermissionCheck permCheck = new PermissionCheck();
      permCheck.setResource(resource);
      permCheck.setResourceIdQueryParam(queryParam);
      permCheck.setPermission(permission);
      query.getAuthCheck().addAtomicPermissionCheck(permCheck);
    }
  }

  protected boolean isPermissionDisabled(Permission permission) {
    List<String> disabledPermissions = getProcessEngine().getProcessEngineConfiguration().getDisabledPermissions();
    for (String disabledPerm : disabledPermissions) {
      if (!disabledPerm.equals(permission.getName())) {
        return true;
      }
    }
    return false;
  }

}
