package com.bpwizard.camunda.identity.boot.plugin;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.identity.*;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.bpwizard.wcm.repo.camunda.identity.service.SpringGroupService;
import com.bpwizard.wcm.repo.camunda.identity.service.SpringUserService;

import com.bpwizard.wcm.repo.camunda.identity.domain.SpringGroup;
import com.bpwizard.wcm.repo.camunda.identity.domain.SpringUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SpringIdentityProvider implements ReadOnlyIdentityProvider {

    private final SpringUserService userService;
    private final SpringGroupService groupService;
    private final PasswordEncoder passwordEncoder;
    
    public SpringIdentityProvider(
    		SpringUserService userService, 
    		SpringGroupService groupService,
    		PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.groupService = groupService;
        this.passwordEncoder = passwordEncoder;
    }

    // User ////////////////////////////////////////////

    @Override
    public User findUserById(String userId) {
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
        return findUserByQueryCriteria(query).size();
    }
    
    protected boolean isNotGroupMember(SpringUser user, String groupId) {
    	return user.getGroups().stream().filter(group -> group.getId().equals(groupId)).count() <= 0;
    }
    
    public List<User> findUserByQueryCriteria(SpringUserQuery query) {
    	//TODO: translate the SpringuserQuery into the Spring Data QBE <org.springframework.data.domain.Example>
        Collection<SpringUser> users = userService.findAll();

        if(query.getId() != null)
            users.removeIf(user -> !query.getId().equals(user.getId()));
        if(query.getFirstName() != null)
            users.removeIf(user -> !query.getFirstName().equals(user.getFirstName()));
        if(query.getLastName() != null)
            users.removeIf(user -> !query.getLastName().equals(user.getLastName()));
        if(query.getEmail() != null)
            users.removeIf(user -> !query.getEmail().equals(user.getEmail()));
        if(query.getGroupId() != null)
            users.removeIf(user -> isNotGroupMember(user, query.getGroupId()));
        if(query.getTenantId() != null)
            users.removeIf(user -> (user.getTenant() == null) || (!query.getTenantId().equals(user.getTenant().getId())));

        return new ArrayList<>(users);
    }

    @Override
    public boolean checkPassword(String userId, String password) {

        if(userId == null || password == null || userId.isEmpty() || password.isEmpty())
            return false;

        User user = findUserById(userId);

        if(user == null)
            return false;

        return user.getPassword().equals(passwordEncoder.encode(password));
    }

    // Group //////////////////////////////////////////

    @Override
    public Group findGroupById(String groupId) {
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
        return findGroupByQueryCriteria(query).size();
    }

    public List<Group> findGroupByQueryCriteria(SpringGroupQuery query) {
        //TODO: translate the SpringGroupQuery into the Spring Data QBE <org.springframework.data.domain.Example>
        return groupService.findAll().stream()
                .filter(group -> group.getId().equals(query.getId()))
                .filter(group -> group.getName().equals(query.getName()))
                .filter(group -> group.getType().equals(query.getType()))
                .collect(Collectors.toList());

    }

    // Tenant ////////////////////////////////////////

    @Override
    public Tenant findTenantById(String tenantId) {
        return null;
    }

    @Override
    public TenantQuery createTenantQuery() {
        return new SpringTenantQuery(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
    }

    @Override
    public TenantQuery createTenantQuery(CommandContext commandContext) {
        return new SpringTenantQuery();
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() {

    }
}
