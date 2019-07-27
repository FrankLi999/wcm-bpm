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
package org.camunda.bpm.engine.rest.sub.identity.controller;

import javax.annotation.PostConstruct;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.rest.controller.AbstractAuthorizedRestResource;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.springframework.http.HttpStatus;

/**
 * @author Daniel Meyer
 *
 */
public abstract class AbstractIdentityRestService extends AbstractAuthorizedRestResource {

	protected IdentityService identityService;

	@PostConstruct
	public void afterPropertySet() {
		this.identityService = this.processEngine.getIdentityService();
	}
	
	public void ensureNotReadOnly() {
		if (identityService.isReadOnly()) {
			throw new InvalidRequestException(HttpStatus.FORBIDDEN, "Identity service implementation is read-only.");
		}
	}
//	
//	public void deleteMembership(String userId, String groupId) {
//		identityService.deleteMembership(userId, groupId);
//	}
//	
//	public boolean isReadOnly() {
//		return identityService.isReadOnly();
//	}
//
//	public void saveGroup(Group dbGroup) {
//		identityService.saveGroup(dbGroup);
//	}
//	
//	public void deleteGroup(String groupId) {
//		identityService.deleteGroup(groupId);
//	}
//	
//	public Group findGroupObject(String groupId) throws ProcessEngineException {
//		return identityService.createGroupQuery().groupId(groupId).singleResult();
//	}
//	
//	public void deleteUser(String userId) {
//		identityService.deleteUser(userId);
//	}
//	
//	public void unlockUser(String userId) {
//		identityService.unlockUser(userId);
//	}
//	
//	public boolean checkPassword(String userId, String password) {
//		return identityService.checkPassword(userId, password);
//	}
//	
//	public Authentication getCurrentAuthentication() {
//		return identityService.getCurrentAuthentication();
//	}
//	
//	public void saveUser(User user) {
//		identityService.saveUser(user);
//	}
//	
//	public User findUserObject(String userId) {
//		return identityService.createUserQuery().userId(userId).singleResult();
//	}
//	
//	public void saveTenant(Tenant tenant) {
//		identityService.saveTenant(tenant);
//	}
//	
//	public void deleteTenant(String tenantId) {
//		identityService.deleteTenant(tenantId);
//	}
//	
//	public Tenant findTenantObject(String tenantId) {
//		return identityService.createTenantQuery().tenantId(tenantId).singleResult();
//	}
}
