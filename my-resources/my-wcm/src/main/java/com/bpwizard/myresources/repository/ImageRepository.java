package com.bpwizard.myresources.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.bpwizard.myresources.model.Image;

import reactor.core.publisher.Mono;

public interface ImageRepository extends ReactiveCrudRepository<Image, String> {

	Mono<Image> findByName(String name);
}

