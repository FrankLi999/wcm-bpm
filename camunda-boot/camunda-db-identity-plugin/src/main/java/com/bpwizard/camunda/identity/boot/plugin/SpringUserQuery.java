package com.bpwizard.camunda.identity.boot.plugin;

import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.Page;
import org.camunda.bpm.engine.impl.UserQueryImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;

import java.util.List;


public class SpringUserQuery extends UserQueryImpl {

	private static final long serialVersionUID = 1L;

	public SpringUserQuery() {
        super();
    }

    public SpringUserQuery(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    // results //////////////////////////////////////////////////////////

    @Override
    public long executeCount(CommandContext commandContext) {
    	checkQueryOk();
        final SpringIdentityProvider provider = getSpringIdentityProvider(commandContext);
        return provider.findUserCountByQueryCriteria(this);
    }

    @Override
    public List<User> executeList(CommandContext commandContext, Page page) {
    	checkQueryOk();
        final SpringIdentityProvider provider = getSpringIdentityProvider(commandContext);
        return provider.findUserByQueryCriteria(this);
    }

    protected SpringIdentityProvider getSpringIdentityProvider(CommandContext commandContext) {
        return (SpringIdentityProvider) commandContext.getReadOnlyIdentityProvider();
    }
}
