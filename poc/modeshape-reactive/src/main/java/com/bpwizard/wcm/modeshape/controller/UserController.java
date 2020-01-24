package com.bpwizard.wcm.modeshape.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.modeshape.user.domain.UserDto;
import com.bpwizard.wcm.modeshape.user.repository.UserRepository;

import reactor.core.publisher.Mono;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<UserDto> getCurrentUser(Mono<Principal> userPrincipal) {
        return userPrincipal
        	   .map(Principal::getName)
        	   .flatMap(login -> userRepository.findByLogin(login))
        	   .map(UserDto::fromUser);
        		
            //.orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.currentUser().getId()));
    }
}
