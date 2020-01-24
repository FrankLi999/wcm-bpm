package com.bpwizard.wcm.repo.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.service.repo.domain.User;
import com.bpwizard.spring.boot.commons.service.repo.domain.UserRepository;
import com.bpwizard.spring.boot.commons.service.repo.exception.ResourceNotFoundException;
import com.bpwizard.wcm.repo.annotations.CurrentUser;
import com.bpwizard.wcm.repo.payload.AuthResponse;


@RestController
@RequestMapping("/user/api")
public class UserController {
	private static final Logger logger = LogManager.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/me")
    // @PreAuthorize("hasRole('ROLE_admin') or hasRole('admin') or hasRole('user')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser SpringPrincipal userPrincipal) {

    	logger.info(".............................>>>>>>>> me: " + userPrincipal);
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	System.out.println(">>>>>>>>>>>>>>>>>> principal:" + principal);
    	 // Object name = SecurityContextHolder.getContext().getAuthentication().getName();
         String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
         System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>user session id:" + sessionId);
    	 User user = userRepository.findByEmail(userPrincipal.currentUser().getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.currentUser().getId()));
        return ResponseEntity.ok(AuthResponse.fromUserAndToken(user, "", sessionId));
    }
}
