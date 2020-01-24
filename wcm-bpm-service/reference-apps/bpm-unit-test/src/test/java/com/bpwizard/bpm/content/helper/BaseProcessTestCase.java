package com.bpwizard.bpm.content.helper;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.camunda.demo.springboot.conf.TestApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE,
 	classes = {TestApplication.class})
@ContextConfiguration(classes = {StandaloneInMemoryTestConfiguration.class})
public abstract class BaseProcessTestCase {

}