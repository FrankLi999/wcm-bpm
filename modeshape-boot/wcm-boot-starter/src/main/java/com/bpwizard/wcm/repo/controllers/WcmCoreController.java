package com.bpwizard.wcm.repo.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.security.AuthenticationRequest;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.service.SpringController;
import com.bpwizard.spring.boot.commons.service.annotations.CurrentUser;
import com.bpwizard.spring.boot.commons.service.domain.User;
import com.bpwizard.wcm.repo.dto.WcmUserProfile;
import com.bpwizard.wcm.repo.rest.handler.WcmRequestHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmSystem;

// @RestController
// @RequestMapping(WcmCoreController.BASE_URI)
public class WcmCoreController extends SpringController<User<Long>, Long> {
	private static final Logger logger = LoggerFactory.getLogger(WcmCoreController.class);
	public static final String BASE_URI = "/core/api/wcm";
	@Autowired
	protected SpringProperties properties;
	
	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	@PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
    		HttpServletRequest request,
    		@Valid @RequestBody AuthenticationRequest authenticationRequest,
    		@RequestParam(name="repository", defaultValue = "") String repository,
    		@RequestParam(name="workspace", defaultValue = "") String workspace,
    		@RequestParam(name="library", defaultValue = "") String library,
    		@RequestParam(name="config", defaultValue = "") String siteConfigName) {

        String shortLivedAuthToken = this.springService.authenticateUser(authenticationRequest);
        User user = this.springService.fetchUserByEmail(authenticationRequest.getEmail());
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        WcmSystem settings = null;
        if (StringUtils.hasText(repository) && StringUtils.hasText(workspace) && StringUtils.hasText(library) && StringUtils.hasText(siteConfigName)) {
        	try {
        		settings = this.wcmRequestHandler.getWcmSystem(repository, workspace, library, siteConfigName, false, request);
        	} catch (WcmRepositoryException e ) {
    			logger.error("Failed to authenticate user", e);
    			throw e;
    		} catch (Throwable t) {
    			logger.error("Failed to authenticate user", t);
    			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
    		}	
        }
        return ResponseEntity.ok(WcmUserProfile.fromUserAndToken(user, shortLivedAuthToken, properties.getJwt().getShortLivedMillis(), sessionId, settings));
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
		WcmSystem settings = null;
        if (StringUtils.hasText(repository) && StringUtils.hasText(workspace) && StringUtils.hasText(library) && StringUtils.hasText(siteConfigName)) {
        	try {
        		settings = this.wcmRequestHandler.getWcmSystem(repository, workspace, library, siteConfigName, false, request);
        	} catch (WcmRepositoryException e ) {
        		logger.error("Failed to resolve user profile", e);
    			throw e;
    		} catch (Throwable t) {
    			logger.error("Failed to resolve user profile", t);
    			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
    		}	
        }
		return ResponseEntity.ok(WcmUserProfile.fromUserAndToken(user,settings));
	}
}