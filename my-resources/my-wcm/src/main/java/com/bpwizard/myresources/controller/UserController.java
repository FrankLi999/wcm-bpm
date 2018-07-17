package com.bpwizard.myresources.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.myresources.ApplicationConstants;
import com.bpwizard.myresources.model.UserC;
import com.bpwizard.myresources.repository.UserCRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(ApplicationConstants.API_BASE_PATH + "/user-c")
public class UserController {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserCRepository userRepository;
    
    @GetMapping()
    public Flux<UserC> users() {
        return Flux.fromIterable(userRepository.findAll());
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Mono<Long> addNewUser (@RequestParam String firstName, @RequestParam String lastName) {
    	UserC user = new UserC(firstName, lastName);
        userRepository.save(user);

        LOG.info(user.toString() + " successfully saved into DB");

        return Mono.just(user.getId());
    }

    @GetMapping("/{id}")
    public Mono<UserC> getUserById(@PathVariable("id") long id) {
        LOG.info("Reading user with id " + id + " from database.");
        return Mono.just(userRepository.findById(id).get());
    }
}
