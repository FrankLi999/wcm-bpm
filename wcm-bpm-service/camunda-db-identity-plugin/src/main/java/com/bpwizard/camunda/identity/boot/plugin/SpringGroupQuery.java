package com.bpwizard.camunda.identity.boot.plugin;

import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.impl.GroupQueryImpl;
import org.camunda.bpm.engine.impl.Page;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;

import java.util.List;

public class SpringGroupQuery extends GroupQueryImpl {

	private static final long serialVersionUID = 1L;

	public SpringGroupQuery() {
        super();
    }

    public SpringGroupQuery(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public long executeCount(CommandContext commandContext) {
    	checkQueryOk();
        final SpringIdentityProvider provider = getSpringIdentityProvider(commandContext);
        return provider.findGroupCountByQueryCriteria(this);
    }

    @Override
    public List<Group> executeList(CommandContext commandContext, Page page) {
    	checkQueryOk();
        final SpringIdentityProvider provider = getSpringIdentityProvider(commandContext);
        return provider.findGroupByQueryCriteria(this);
    }

    protected SpringIdentityProvider getSpringIdentityProvider(CommandContext commandContext) {
        return (SpringIdentityProvider) commandContext.getReadOnlyIdentityProvider();
    }
}
