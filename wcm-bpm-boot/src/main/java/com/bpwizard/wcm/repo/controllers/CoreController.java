package com.bpwizard.wcm.repo.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.spring.boot.commons.dto.AuthResponse;
import com.bpwizard.spring.boot.commons.dto.LoginRequest;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.service.SpringController;
import com.bpwizard.spring.boot.commons.service.repo.domain.User;
import com.bpwizard.spring.boot.commons.service.repo.exception.ResourceNotFoundException;
import com.bpwizard.spring.boot.commons.service.repo.service.DefaultSpringService;
import com.bpwizard.wcm.repo.annotations.CurrentUser;
import com.bpwizard.wcm.repo.rest.bpm.model.UserProfile;

@RestController
@RequestMapping(CoreController.BASE_URI)
public class CoreController extends SpringController<User, Long> {
	
	public static final String BASE_URI = "/core/api";
	
	@PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        AuthResponse authResponse = ((DefaultSpringService)this.springService).authenticateUser(loginRequest);
        return ResponseEntity.ok(authResponse);
    }
	
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
	
	@GetMapping("/me")
    // @PreAuthorize("hasRole('ROLE_admin') or hasRole('admin') or hasRole('user')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser SpringPrincipal userPrincipal) {
		AuthResponse authResponse = ((DefaultSpringService)this.springService).getCurrentUser(userPrincipal);
        return ResponseEntity.ok(authResponse);
    }
	
}