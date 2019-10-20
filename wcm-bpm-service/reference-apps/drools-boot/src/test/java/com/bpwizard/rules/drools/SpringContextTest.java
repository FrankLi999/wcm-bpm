package com.bpwizard.rules.drools;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.bpwizard.rules.drools.config.TaxiFareConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TaxiFareConfiguration.class)
public class SpringContextTest {

    @Test
    public void whenSpringContextIsBootstrapped_thenNoExceptions() {
    }
}