package com.bpwizard.wcm.repo.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.service.SpringController;
import com.bpwizard.spring.boot.commons.service.repo.domain.User;
import com.bpwizard.spring.boot.commons.service.repo.exception.ResourceNotFoundException;
import com.bpwizard.wcm.repo.annotations.CurrentUser;
import com.bpwizard.wcm.repo.rest.bpm.model.UserProfile;

@RestController
@RequestMapping(CoreController.BASE_URI)
public class CoreController extends SpringController<User, Long> {
	
	public static final String BASE_URI = "/core/api";

	@GetMapping(path = "/user-profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserProfile userProfile(@CurrentUser SpringPrincipal userPrincipal) {
		UserProfile userProfile = new UserProfile();
		User user = this.springService.fetchUserByEmail(userPrincipal.currentUser().getEmail());
		if (user == null) {
			throw new ResourceNotFoundException("User", "id", userPrincipal.currentUser().getId());
		}
		userProfile.setName(user.getName());
		userProfile.setEmail(user.getEmail());
		List<String> groups = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList());
		userProfile.setGroups(groups);
		return userProfile;
	}
	
}