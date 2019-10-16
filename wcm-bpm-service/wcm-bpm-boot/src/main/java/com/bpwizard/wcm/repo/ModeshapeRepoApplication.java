package com.bpwizard.wcm.repo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
// @ComponentScan(basePackages={"com.bpwizard.wcm.repo.domain"})
// @ComponentScan(basePackages={"org.modeshape.web.jcr.rest","com.bpwizard.wcm.repo"})
public class ModeshapeRepoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModeshapeRepoApplication.class, args);
    }
}
