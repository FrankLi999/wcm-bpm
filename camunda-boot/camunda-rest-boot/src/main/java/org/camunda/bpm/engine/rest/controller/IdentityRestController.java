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
package org.camunda.bpm.engine.rest.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.GroupQuery;
import org.camunda.bpm.engine.identity.PasswordPolicyResult;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.rest.IdentityRestService;
import org.camunda.bpm.engine.rest.dto.identity.BasicUserCredentialsDto;
import org.camunda.bpm.engine.rest.dto.identity.CheckPasswordPolicyResultDto;
import org.camunda.bpm.engine.rest.dto.identity.PasswordDto;
import org.camunda.bpm.engine.rest.dto.identity.PasswordPolicyDto;
import org.camunda.bpm.engine.rest.dto.task.GroupDto;
import org.camunda.bpm.engine.rest.dto.task.GroupInfoDto;
import org.camunda.bpm.engine.rest.dto.task.UserDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="identityApi")
@RequestMapping(IdentityRestService.PATH)
public class IdentityRestController extends AbstractRestProcessEngineAware implements IdentityRestService {

	@Override
	@GetMapping(path="/groups", produces=MediaType.APPLICATION_JSON_VALUE)
	public GroupInfoDto getGroupInfo(@RequestParam("userId") String userId) {
		if (userId == null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, "No user id was supplied");
		}

		IdentityService identityService = getProcessEngine().getIdentityService();

		GroupQuery query = identityService.createGroupQuery();
		List<Group> userGroups = query.groupMember(userId).orderByGroupName().asc().list();

		Set<UserDto> allGroupUsers = new HashSet<UserDto>();
		List<GroupDto> allGroups = new ArrayList<GroupDto>();

		for (Group group : userGroups) {
			List<User> groupUsers = identityService.createUserQuery().memberOfGroup(group.getId()).list();
			for (User user : groupUsers) {
				if (!user.getId().equals(userId)) {
					allGroupUsers.add(new UserDto(user.getId(), user.getFirstName(), user.getLastName()));
				}
			}
			allGroups.add(new GroupDto(group.getId(), group.getName()));
		}

		return new GroupInfoDto(allGroups, allGroupUsers);
	}

	@Override
	@PostMapping(path="/verify", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public AuthenticationResult verifyUser(@RequestBody BasicUserCredentialsDto credentialsDto) {
		if (credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, "Username and password are required");
		}
		IdentityService identityService = getProcessEngine().getIdentityService();
		boolean valid = identityService.checkPassword(credentialsDto.getUsername(), credentialsDto.getPassword());
		if (valid) {
			return AuthenticationResult.successful(credentialsDto.getUsername());
		} else {
			return AuthenticationResult.unsuccessful(credentialsDto.getUsername());
		}
	}

	@Override
	@GetMapping(path="/password-policy", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPasswordPolicy() {
		boolean isEnabled = processEngine.getProcessEngineConfiguration().isEnablePasswordPolicy();

		if (isEnabled) {
			IdentityService identityService = processEngine.getIdentityService();

			return ResponseEntity.ok()
					.body(PasswordPolicyDto.fromPasswordPolicy(identityService.getPasswordPolicy()));

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		}
	}

	@Override
	@PostMapping(path="/password-policy", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> checkPassword(@RequestBody PasswordDto password) {
		boolean isEnabled = processEngine.getProcessEngineConfiguration().isEnablePasswordPolicy();

		if (isEnabled) {
			IdentityService identityService = processEngine.getIdentityService();

			PasswordPolicyResult result = identityService.checkPasswordAgainstPolicy(password.getPassword());

			return ResponseEntity.ok()
					.body(CheckPasswordPolicyResultDto.fromPasswordPolicyResult(result));

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		}
	}
}
