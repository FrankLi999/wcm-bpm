package com.bpwizard.wcm.repo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// @ComponentScan(basePackages={"org.modeshape.web.jcr.rest","com.bpwizard.wcm.repo"})
public class ModeshapeRepoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModeshapeRepoApplication.class, args);
    }
}
