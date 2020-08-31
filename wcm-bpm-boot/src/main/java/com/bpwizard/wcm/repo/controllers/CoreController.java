package com.bpwizard.wcm.repo.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.security.AuthenticationRequest;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.service.SpringController;
import com.bpwizard.spring.boot.commons.service.repo.domain.User;
import com.bpwizard.wcm.repo.annotations.CurrentUser;
import com.bpwizard.wcm.repo.dto.AuthResponse;
import com.bpwizard.wcm.repo.dto.UserProfile;

@RestController
@RequestMapping(CoreController.BASE_URI)
public class CoreController extends SpringController<User, Long> {
	
	public static final String BASE_URI = "/core/api";
	@Autowired
	protected SpringProperties properties;
	
	@PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {

        String shortLivedAuthToken = this.springService.authenticateUser(authenticationRequest);
        User user = this.springService.fetchUserByEmail(authenticationRequest.getEmail());
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        return ResponseEntity.ok(UserProfile.fromUserAndToken(user, shortLivedAuthToken, properties.getJwt().getShortLivedMillis(), sessionId));
    }
	
	@GetMapping(path = "/user-profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> userProfile(@CurrentUser SpringPrincipal userPrincipal) {
		User user = this.springService.fetchUserByEmail(userPrincipal.currentUser().getEmail());
		return ResponseEntity.ok(UserProfile.fromUserAndToken(user));
	}
}