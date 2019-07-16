package com.bpwizard.wcm.repo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.wcm.repo.annotations.CurrentUser;
import com.bpwizard.wcm.repo.entities.User;
import com.bpwizard.wcm.repo.exception.ResourceNotFoundException;
import com.bpwizard.wcm.repo.repositories.UserRepository;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public User getCurrentUser(@CurrentUser SpringPrincipal userPrincipal) {
        return userRepository.findByEmail(userPrincipal.currentUser().getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.currentUser().getId()));
    }
}
