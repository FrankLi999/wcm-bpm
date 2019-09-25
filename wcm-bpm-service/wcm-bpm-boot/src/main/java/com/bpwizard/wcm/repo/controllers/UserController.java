package com.bpwizard.wcm.repo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.wcm.repo.annotations.CurrentUser;
import com.bpwizard.wcm.repo.entities.User;
import com.bpwizard.wcm.repo.exception.ResourceNotFoundException;
import com.bpwizard.wcm.repo.payload.AuthResponse;
import com.bpwizard.wcm.repo.repositories.UserRepository;

@RestController
@RequestMapping("/user/api/rest")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser SpringPrincipal userPrincipal) {
        System.out.println(">>>>>>>>>>>>>> get current user:" + userPrincipal);
    	User user = userRepository.findByEmail(userPrincipal.currentUser().getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.currentUser().getId()));
        return ResponseEntity.ok(AuthResponse.fromUserAndToken(user, ""));
    }
}
