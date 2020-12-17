/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bpwizard.bpm.demo.config;

import static org.camunda.bpm.engine.authorization.Authorization.ANY;
import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;
import static org.camunda.bpm.engine.authorization.Permissions.ACCESS;
import static org.camunda.bpm.engine.authorization.Permissions.ALL;
import static org.camunda.bpm.engine.authorization.Permissions.READ;
import static org.camunda.bpm.engine.authorization.Permissions.UPDATE;
import static org.camunda.bpm.engine.authorization.Resources.APPLICATION;
import static org.camunda.bpm.engine.authorization.Resources.FILTER;
import static org.camunda.bpm.engine.authorization.Resources.TASK;
import static org.camunda.bpm.engine.authorization.Resources.USER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.filter.Filter;
import org.camunda.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.camunda.bpm.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import com.bpwizard.spring.boot.commons.service.domain.Role;
import com.bpwizard.spring.boot.commons.service.domain.RoleService;
import com.bpwizard.spring.boot.commons.service.domain.User;
import com.bpwizard.spring.boot.commons.service.domain.UserService;

/**
 * Creates demo credentials to be used in the invoice showcase.
 *
 * @author drobisch
 */
public class DemoDataGenerator {

	private final static Logger logger = LoggerFactory.getLogger(DemoDataGenerator.class.getName());

	private RoleService<Role, Long> roleService;
	private UserService<User<Long>, Long> userService;
	private PasswordEncoder passwordEncoder;

	public DemoDataGenerator(RoleService<Role, Long> roleService, UserService<User<Long>, Long> userService,
			PasswordEncoder passwordEncoder) {
		this.roleService = roleService;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	public boolean createUsers(ProcessEngine engine) {

		if (this.userService.findByEmail("demo@example.com").isPresent()) {
			logger.info("Demo users have been created");
			return false;
		}
		logger.info("Generating demo data for invoice showcase");
		Map<String, Role> roles = new HashMap<>();
		roles.put("sales", this.createRole("sales"));
		roles.put("accounting", this.createRole("accounting"));
		roles.put("management", this.createRole("management"));

		Role adminRole = roleService.findByName(Groups.CAMUNDA_ADMIN).get();
		if (adminRole == null) {
			adminRole = this.createRole(Groups.CAMUNDA_ADMIN);
		}
		roles.put(Groups.CAMUNDA_ADMIN, adminRole);
		Role viewerRole = roleService.findByName("wcm-viewer").get();
		if (viewerRole == null) {
			viewerRole = this.createRole("wcm-viewer");
		}
		roles.put("wcm-viewer", viewerRole);
		
		Role reviewerRole = roleService.findByName("wcm-reviewer").get();
		if (reviewerRole == null) {
			reviewerRole = this.createRole("wcm-reviewer");
		}
		roles.put("wcm-reviewer", reviewerRole);

		Role admin = roleService.findByName("admin").get();
		if (admin == null) {
			admin = this.createRole("admin");
		}
		roles.put("admin", admin);
		
		// User user = 
		this.createUser("demo@example.com", "demo@example.com", "Demo", "Demo", "demo",
				new String[] { "sales", "accounting", "management", "camunda-admin", "admin", "wcm-viewer", "wcm-reviewer"}, roles);
		// User user2 = 
		this.createUser("john@example.com", "john@example.com", "John", "Dow", "john", new String[] { "sales", "admin", "wcm-viewer", "wcm-reviewer" },
				roles);
		// User user3 = 
		this.createUser("mary@example.com", "mary@example.com", "Mary", "Anne", "mary", new String[] { "accounting", "admin", "wcm-viewer", "wcm-reviewer" },
				roles);
		// User user4 = 
		this.createUser("peter@example.com", "peter@example.com", "Peter", "Meter", "peter",
				new String[] { "management", "admin", "wcm-viewer", "wcm-reviewer" }, roles);

		final AuthorizationService authorizationService = engine.getAuthorizationService();
		// create ADMIN authorizations on all built-in resources
		for (Resource resource : Resources.values()) {
			if (authorizationService.createAuthorizationQuery().groupIdIn(Groups.CAMUNDA_ADMIN).resourceType(resource)
					.resourceId(ANY).count() == 0) {
				AuthorizationEntity userAdminAuth = new AuthorizationEntity(AUTH_TYPE_GRANT);
				userAdminAuth.setGroupId(Groups.CAMUNDA_ADMIN);
				userAdminAuth.setResource(resource);
				userAdminAuth.setResourceId(ANY);
				userAdminAuth.addPermission(ALL);
				authorizationService.saveAuthorization(userAdminAuth);
			}
		}
		// authorize groups for tasklist only:

		Authorization salesTasklistAuth = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		salesTasklistAuth.setGroupId("sales");
		salesTasklistAuth.addPermission(ACCESS);
		salesTasklistAuth.setResourceId("tasklist");
		salesTasklistAuth.setResource(APPLICATION);
		authorizationService.saveAuthorization(salesTasklistAuth);

		Authorization salesReadProcessDefinition = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		salesReadProcessDefinition.setGroupId("sales");
		salesReadProcessDefinition.addPermission(Permissions.READ);
		salesReadProcessDefinition.addPermission(Permissions.READ_HISTORY);
		salesReadProcessDefinition.setResource(Resources.PROCESS_DEFINITION);
		// restrict to invoice process definition only
		salesReadProcessDefinition.setResourceId("invoice");
		authorizationService.saveAuthorization(salesReadProcessDefinition);

		Authorization accountingTasklistAuth = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		accountingTasklistAuth.setGroupId("accounting");
		accountingTasklistAuth.addPermission(ACCESS);
		accountingTasklistAuth.setResourceId("tasklist");
		accountingTasklistAuth.setResource(APPLICATION);
		authorizationService.saveAuthorization(accountingTasklistAuth);

		Authorization accountingReadProcessDefinition = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		accountingReadProcessDefinition.setGroupId("accounting");
		accountingReadProcessDefinition.addPermission(Permissions.READ);
		accountingReadProcessDefinition.addPermission(Permissions.READ_HISTORY);
		accountingReadProcessDefinition.setResource(Resources.PROCESS_DEFINITION);
		// restrict to invoice process definition only
		accountingReadProcessDefinition.setResourceId("invoice");
		authorizationService.saveAuthorization(accountingReadProcessDefinition);

		Authorization managementTasklistAuth = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		managementTasklistAuth.setGroupId("management");
		managementTasklistAuth.addPermission(ACCESS);
		managementTasklistAuth.setResourceId("tasklist");
		managementTasklistAuth.setResource(APPLICATION);
		authorizationService.saveAuthorization(managementTasklistAuth);

		Authorization managementReadProcessDefinition = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		managementReadProcessDefinition.setGroupId("management");
		managementReadProcessDefinition.addPermission(Permissions.READ);
		managementReadProcessDefinition.addPermission(Permissions.READ_HISTORY);
		managementReadProcessDefinition.setResource(Resources.PROCESS_DEFINITION);
		// restrict to invoice process definition only
		managementReadProcessDefinition.setResourceId("invoice");
		authorizationService.saveAuthorization(managementReadProcessDefinition);

		Authorization salesDemoAuth = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		salesDemoAuth.setGroupId("sales");
		salesDemoAuth.setResource(USER);
		salesDemoAuth.setResourceId("demo");
		salesDemoAuth.addPermission(READ);
		authorizationService.saveAuthorization(salesDemoAuth);

		Authorization salesJohnAuth = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		salesJohnAuth.setGroupId("sales");
		salesJohnAuth.setResource(USER);
		salesJohnAuth.setResourceId("john");
		salesJohnAuth.addPermission(READ);
		authorizationService.saveAuthorization(salesJohnAuth);

		Authorization manDemoAuth = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		manDemoAuth.setGroupId("management");
		manDemoAuth.setResource(USER);
		manDemoAuth.setResourceId("demo");
		manDemoAuth.addPermission(READ);
		authorizationService.saveAuthorization(manDemoAuth);

		Authorization manPeterAuth = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		manPeterAuth.setGroupId("management");
		manPeterAuth.setResource(USER);
		manPeterAuth.setResourceId("peter");
		manPeterAuth.addPermission(READ);
		authorizationService.saveAuthorization(manPeterAuth);

		Authorization accDemoAuth = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		accDemoAuth.setGroupId("accounting");
		accDemoAuth.setResource(USER);
		accDemoAuth.setResourceId("demo");
		accDemoAuth.addPermission(READ);
		authorizationService.saveAuthorization(accDemoAuth);

		Authorization accMaryAuth = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		accMaryAuth.setGroupId("accounting");
		accMaryAuth.setResource(USER);
		accMaryAuth.setResourceId("mary");
		accMaryAuth.addPermission(READ);
		authorizationService.saveAuthorization(accMaryAuth);

		Authorization taskMaryAuth = authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		taskMaryAuth.setUserId("mary");
		taskMaryAuth.setResource(TASK);
		taskMaryAuth.setResourceId(ANY);
		taskMaryAuth.addPermission(READ);
		taskMaryAuth.addPermission(UPDATE);
		authorizationService.saveAuthorization(taskMaryAuth);

		// create default filters

		FilterService filterService = engine.getFilterService();

		Map<String, Object> filterProperties = new HashMap<String, Object>();
		filterProperties.put("description", "Tasks assigned to me");
		filterProperties.put("priority", -10);
		addVariables(filterProperties);
		TaskService taskService = engine.getTaskService();
		TaskQuery query = taskService.createTaskQuery().taskAssigneeExpression("${currentUser()}");
		Filter myTasksFilter = filterService.newTaskFilter().setName("My Tasks").setProperties(filterProperties)
				.setOwner("demo@example.com").setQuery(query);
		filterService.saveFilter(myTasksFilter);

		filterProperties.clear();
		filterProperties.put("description", "Tasks assigned to my Groups");
		filterProperties.put("priority", -5);
		addVariables(filterProperties);
		query = taskService.createTaskQuery().taskCandidateGroupInExpression("${currentUserGroups()}").taskUnassigned();
		Filter groupTasksFilter = filterService.newTaskFilter().setName("My Group Tasks")
				.setProperties(filterProperties).setOwner("demo@example.com").setQuery(query);
		filterService.saveFilter(groupTasksFilter);

		// global read authorizations for these filters

		Authorization globalMyTaskFilterRead = authorizationService
				.createNewAuthorization(Authorization.AUTH_TYPE_GLOBAL);
		globalMyTaskFilterRead.setResource(FILTER);
		globalMyTaskFilterRead.setResourceId(myTasksFilter.getId());
		globalMyTaskFilterRead.addPermission(READ);
		authorizationService.saveAuthorization(globalMyTaskFilterRead);

		Authorization globalGroupFilterRead = authorizationService
				.createNewAuthorization(Authorization.AUTH_TYPE_GLOBAL);
		globalGroupFilterRead.setResource(FILTER);
		globalGroupFilterRead.setResourceId(groupTasksFilter.getId());
		globalGroupFilterRead.addPermission(READ);
		authorizationService.saveAuthorization(globalGroupFilterRead);

		// management filter

		filterProperties.clear();
		filterProperties.put("description", "Tasks for Group Accounting");
		filterProperties.put("priority", -3);
		addVariables(filterProperties);
		query = taskService.createTaskQuery().taskCandidateGroupIn(Arrays.asList("accounting")).taskUnassigned();
		Filter candidateGroupTasksFilter = filterService.newTaskFilter().setName("Accounting")
				.setProperties(filterProperties).setOwner("demo@example.com").setQuery(query);
		filterService.saveFilter(candidateGroupTasksFilter);

		Authorization managementGroupFilterRead = authorizationService
				.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
		managementGroupFilterRead.setResource(FILTER);
		managementGroupFilterRead.setResourceId(candidateGroupTasksFilter.getId());
		managementGroupFilterRead.addPermission(READ);
		managementGroupFilterRead.setGroupId("accounting");
		authorizationService.saveAuthorization(managementGroupFilterRead);

		// john's tasks

		filterProperties.clear();
		filterProperties.put("description", "Tasks assigned to John");
		filterProperties.put("priority", -1);
		addVariables(filterProperties);
		query = taskService.createTaskQuery().taskAssignee("john@example.com");
		Filter johnsTasksFilter = filterService.newTaskFilter().setName("John's Tasks").setProperties(filterProperties)
				.setOwner("demo@example.com").setQuery(query);
		filterService.saveFilter(johnsTasksFilter);

		// mary's tasks

		filterProperties.clear();
		filterProperties.put("description", "Tasks assigned to Mary");
		filterProperties.put("priority", -1);
		addVariables(filterProperties);
		query = taskService.createTaskQuery().taskAssignee("mary@example.com");
		Filter marysTasksFilter = filterService.newTaskFilter().setName("Mary's Tasks").setProperties(filterProperties)
				.setOwner("demo@example.com").setQuery(query);
		filterService.saveFilter(marysTasksFilter);

		// peter's tasks

		filterProperties.clear();
		filterProperties.put("description", "Tasks assigned to Peter");
		filterProperties.put("priority", -1);
		addVariables(filterProperties);
		query = taskService.createTaskQuery().taskAssignee("peter@example.com");
		Filter petersTasksFilter = filterService.newTaskFilter().setName("Peter's Tasks")
				.setProperties(filterProperties).setOwner("demo@example.com").setQuery(query);
		filterService.saveFilter(petersTasksFilter);

		// all tasks

		filterProperties.clear();
		filterProperties.put("description", "All Tasks - Not recommended to be used in production :)");
		filterProperties.put("priority", 10);
		addVariables(filterProperties);
		query = taskService.createTaskQuery();
		Filter allTasksFilter = filterService.newTaskFilter().setName("All Tasks").setProperties(filterProperties)
				.setOwner("demo@example.com").setQuery(query);
		filterService.saveFilter(allTasksFilter);
		return true;

	}

	protected void addVariables(Map<String, Object> filterProperties) {
		List<Object> variables = new ArrayList<Object>();

		addVariable(variables, "amount", "Invoice Amount");
		addVariable(variables, "invoiceNumber", "Invoice Number");
		addVariable(variables, "creditor", "Creditor");
		addVariable(variables, "approver", "Approver");

		filterProperties.put("variables", variables);
	}

	protected void addVariable(List<Object> variables, String name, String label) {
		Map<String, String> variable = new HashMap<String, String>();
		variable.put("name", name);
		variable.put("label", label);
		variables.add(variable);
	}

	protected User<Long> createUser(String email, String name, String firstName, String lastName, String password,
			String[] roleNames, Map<String, Role> roles) {
		logger.info("Creating the initial user: " + name);

		// create the user
		User<Long> newUser = new User<Long>();
		newUser.setName(name);
		newUser.setEmail(email);
		if (StringUtils.hasText(firstName)) {
			newUser.setFirstName(firstName);
		}
		if (StringUtils.hasText(lastName)) {
			newUser.setLastName(lastName);
		}
		newUser.setPassword(passwordEncoder.encode(password));

		for (String rolename : roleNames) {
			if (null != roles.get(rolename)) {
				newUser.getRoles().add(roles.get(rolename).getName());
			}
		}
		userService.create(newUser);
		return newUser;
	}

	protected Role createRole(String roleName) {
		Role role = new Role();
		role.setName(roleName);
		// role =
		roleService.save(role);
		return role;
	}
}
