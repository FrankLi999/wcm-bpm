package com.bpwizard.myresources.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
//import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.bpwizard.myresources.model.Person;
import reactor.core.publisher.Flux;


public interface PersonRespository extends ReactiveCrudRepository<Person, String> {
    Flux<Person> findByName(String name);
}