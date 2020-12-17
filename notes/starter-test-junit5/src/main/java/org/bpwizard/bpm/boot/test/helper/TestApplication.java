package org.bpwizard.bpm.boot.test.helper;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@TestPropertySource(locations = { "classpath:bpm-unit-test.properties" })
public class TestApplication {

}
