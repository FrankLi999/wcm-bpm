package com.bpwizard.myresources.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.myresources.ApplicationConstants;
import com.bpwizard.myresources.config.MyServiceConfiguration;

import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(ApplicationConstants.API_BASE_PATH + "/hello")
public class HelloController {

    private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);

    public static final String HELLO_TEXT = "Hello from Spring Boot Backend!";

    @Autowired
    MyServiceConfiguration myserviceConfig;
    
    @GetMapping
    public Mono<String> sayHello() {
        LOG.info("GET called on /hello resource");
        return Mono.just(myserviceConfig.getMail().getDefaultRecipients().get(0) + myserviceConfig.getMail().getDefaultRecipients().get(1) + myserviceConfig.getMessage() + myserviceConfig.getHelloText() + HELLO_TEXT);
    }
}
