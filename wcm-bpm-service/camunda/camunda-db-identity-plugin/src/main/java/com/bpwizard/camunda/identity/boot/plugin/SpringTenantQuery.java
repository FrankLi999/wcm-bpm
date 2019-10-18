package com.bpwizard.camunda.identity.boot.plugin;

import java.util.List;

import org.camunda.bpm.engine.identity.Tenant;
import org.camunda.bpm.engine.impl.Page;
import org.camunda.bpm.engine.impl.TenantQueryImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;


public class SpringTenantQuery extends TenantQueryImpl {

	private static final long serialVersionUID = 1L;

	public SpringTenantQuery() {
        super();
    }

    public SpringTenantQuery(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public long executeCount(CommandContext commandContext) {
      checkQueryOk();
      SpringIdentityProvider identityProvider = getSpringIdentityProvider(commandContext);
      return identityProvider.findTenantCountByQueryCriteria(this);
    }

    @Override
    public List<Tenant> executeList(CommandContext commandContext, Page page) {
      checkQueryOk();
      SpringIdentityProvider identityProvider = getSpringIdentityProvider(commandContext);
      return identityProvider.findTenantByQueryCriteria(this);
    }

    protected SpringIdentityProvider getSpringIdentityProvider(CommandContext commandContext) {
        return (SpringIdentityProvider) commandContext.getReadOnlyIdentityProvider();
    }
    
}
