package com.bpwizard.spring.boot.commons.service.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.security.AuthenticationRequest;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.service.SpringController;
import com.bpwizard.spring.boot.commons.service.annotations.CurrentUser;
import com.bpwizard.spring.boot.commons.service.domain.User;
import com.bpwizard.spring.boot.commons.service.dto.UserProfile;

@RestController
@RequestMapping(CoreController.BASE_URI)
public class CoreController extends SpringController<User<Long>, Long> {
	private static final Logger logger = LoggerFactory.getLogger(CoreController.class);
	public static final String BASE_URI = "/core/api";
	@Autowired
	protected SpringProperties properties;
	
	@PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
    		HttpServletRequest request,
    		@Valid @RequestBody AuthenticationRequest authenticationRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering");
		}
        String shortLivedAuthToken = this.springService.authenticateUser(authenticationRequest);
        User user = this.springService.fetchUserByEmail(authenticationRequest.getEmail());
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting");
		}
        return ResponseEntity.ok(UserProfile.fromUserAndToken(user, shortLivedAuthToken, properties.getJwt().getShortLivedMillis(), sessionId));
    }
	
	@GetMapping(path = "/user-profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> userProfile(
			HttpServletRequest request,
			@CurrentUser SpringPrincipal userPrincipal,
			@RequestParam(name="repository", defaultValue = "") String repository,
    		@RequestParam(name="workspace", defaultValue = "") String workspace,
    		@RequestParam(name="library", defaultValue = "") String library,
    		@RequestParam(name="config", defaultValue = "") String siteConfigName) {
		User user = this.springService.fetchUserByEmail(userPrincipal.currentUser().getEmail());
		
		return ResponseEntity.ok(UserProfile.fromUserAndToken(user));
	}
}