package com.bpwizard.camunda.identity.boot.plugin;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.authorization.Permission;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.GroupQuery;
import org.camunda.bpm.engine.identity.NativeUserQuery;
import org.camunda.bpm.engine.identity.Tenant;
import org.camunda.bpm.engine.identity.TenantQuery;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.identity.UserQuery;
import org.camunda.bpm.engine.impl.AbstractQuery;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.AbstractManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bpwizard.wcm.repo.camunda.identity.domain.SpringGroup;
import com.bpwizard.wcm.repo.camunda.identity.domain.SpringTenant;
import com.bpwizard.wcm.repo.camunda.identity.domain.SpringUser;
import com.bpwizard.wcm.repo.camunda.identity.service.SpringGroupService;
import com.bpwizard.wcm.repo.camunda.identity.service.SpringTenantService;
import com.bpwizard.wcm.repo.camunda.identity.service.SpringUserService;

public class SpringIdentityProvider extends AbstractManager implements ReadOnlyIdentityProvider {

	private final SpringUserService userService;
	private final SpringGroupService groupService;
	private final SpringTenantService tenantService;
	private final PasswordEncoder passwordEncoder;

	public SpringIdentityProvider(SpringUserService userService, SpringGroupService groupService,
			SpringTenantService tenantService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.groupService = groupService;
		this.tenantService = tenantService;
		this.passwordEncoder = passwordEncoder;
	}

	// User ////////////////////////////////////////////

	@Override
	public User findUserById(String userId) {
		checkAuthorization(Permissions.READ, Resources.USER, userId);
		return userService.findById(userId);
	}

	@Override
	public UserQuery createUserQuery() {
		return new SpringUserQuery(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
	}

	@Override
	public UserQuery createUserQuery(CommandContext commandContext) {
		return new SpringUserQuery();
	}

	@Override
	public NativeUserQuery createNativeUserQuery() {
		throw new BadUserRequestException("not supported");
	}

	public long findUserCountByQueryCriteria(SpringUserQuery query) {
		configureQuery(query, Resources.USER);
		return this.userService.countAll(query);
	}

	public List<User> findUserByQueryCriteria(SpringUserQuery query) {
		configureQuery(query, Resources.USER);
		List<SpringUser> users = userService.findAll(query);
		return new ArrayList<>(users);
	}

	@Override
	public boolean checkPassword(String userId, String password) {

		if (userId == null || password == null || userId.isEmpty() || password.isEmpty())
			return false;

		User user = findUserById(userId);

		if (user == null)
			return false;

		return user.getPassword().equals(passwordEncoder.encode(password));
	}

	// Group //////////////////////////////////////////

	@Override
	public Group findGroupById(String groupId) {
		checkAuthorization(Permissions.READ, Resources.GROUP, groupId);
		return groupService.findById(groupId);
	}

	@Override
	public GroupQuery createGroupQuery() {
		return new SpringGroupQuery(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
	}

	@Override
	public GroupQuery createGroupQuery(CommandContext commandContext) {
		return new SpringGroupQuery();
	}

	public long findGroupCountByQueryCriteria(SpringGroupQuery query) {
		configureQuery(query, Resources.GROUP);
		return this.groupService.countAll(query);
	}

	public List<Group> findGroupByQueryCriteria(SpringGroupQuery query) {
        configureQuery(query, Resources.GROUP);
		List<SpringGroup> groups = this.groupService.findAll(query);
		return new ArrayList<Group>(groups);
	}

	// Tenant ////////////////////////////////////////

	@Override
	public Tenant findTenantById(String tenantId) {
		checkAuthorization(Permissions.READ, Resources.TENANT, tenantId);
		return tenantService.findById(tenantId);
	}

	@Override
	public TenantQuery createTenantQuery() {
		return new SpringTenantQuery(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
	}

	@Override
	public TenantQuery createTenantQuery(CommandContext commandContext) {		
		return new SpringTenantQuery();
	}

	public long findTenantCountByQueryCriteria(SpringTenantQuery query) {
		configureQuery(query, Resources.TENANT);
		return this.tenantService.countAll(query);
	}

	public List<Tenant> findTenantByQueryCriteria(SpringTenantQuery query) {
		configureQuery(query, Resources.TENANT);
		List<SpringTenant> tenants = this.tenantService.findAll(query);
		return new ArrayList<Tenant>(tenants);
	}

	@Override
	public void flush() {
		// do nothing
	}

	@Override
	public void close() {
		// do nothing
	}

	// authorizations ////////////////////////////////////////////////////

	@Override
	protected void configureQuery(@SuppressWarnings("rawtypes") AbstractQuery query, Resource resource) {
		Context.getCommandContext().getAuthorizationManager().configureQuery(query, resource);
	}

	@Override
	protected void checkAuthorization(Permission permission, Resource resource, String resourceId) {
		Context.getCommandContext().getAuthorizationManager().checkAuthorization(permission, resource, resourceId);
	}
}
