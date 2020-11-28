package com.bpwizard.spring.boot.commons.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages= {"com.bpwizard.spring.boot.commons.service.repo"})
@EnableJpaRepositories({"com.bpwizard.spring.boot.commons.service.repo"})
@ComponentScan(basePackages={"com.bpwizard.spring.boot.commons"})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
