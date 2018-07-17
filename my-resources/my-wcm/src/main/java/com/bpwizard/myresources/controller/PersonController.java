package com.bpwizard.myresources.controller;

import com.bpwizard.myresources.ApplicationConstants;
import com.bpwizard.myresources.model.Person;
import com.bpwizard.myresources.repository.PersonRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(ApplicationConstants.API_BASE_PATH + "/person")
public class PersonController {

    @Autowired
    private PersonRespository personRespository;

    @GetMapping
    public Flux<Person> allPerson() {
    	System.out.println("persons:");
        return personRespository.findAll();
    }

    @GetMapping(path="/{id}")
    public Mono<Person> person(@PathVariable("id") String id) {
    	System.out.println("persons:");
        return personRespository.findById(id);
    }
}