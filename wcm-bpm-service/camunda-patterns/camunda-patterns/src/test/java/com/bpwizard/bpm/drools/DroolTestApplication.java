package com.bpwizard.bpm.drools;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = { "com.bpwizard.bpm.drools" })
// @EnableAutoConfiguration(exclude = { TaxiFareConfiguration.class })
public class DroolTestApplication {

}
