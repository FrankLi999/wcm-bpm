package com.bpwizard.rules.drools;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackageClasses = { DroolsApp.class })
// @EnableAutoConfiguration(exclude = { TaxiFareConfiguration.class })
public class TestApplication {

}
