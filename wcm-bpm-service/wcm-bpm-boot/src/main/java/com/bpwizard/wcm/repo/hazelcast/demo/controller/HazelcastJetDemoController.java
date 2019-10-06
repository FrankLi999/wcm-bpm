package com.bpwizard.wcm.repo.hazelcast.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.config.AppConfig;
import com.bpwizard.wcm.repo.hazelcast.demo.model.HazelcastUser;
import com.bpwizard.wcm.repo.hazelcast.demo.service.TestCache;
import com.bpwizard.wcm.repo.hazelcast.demo.source.CustomSourceP;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
/**
 * Example of integrating Hazelcast Jet with Spring Boot. {@link AppConfig}
 * class is used as a configuration class and {@link SpringBootSample#submitJob()}
 * is mapped to url 'http://host:port/submitJob' using {@link RestController} and
 * {@link RequestMapping} annotations
 * <p>
 * Job uses a custom source implementation which has {@link com.hazelcast.spring.context.SpringAware}
 * annotation. This enables spring to auto-wire beans to created processors.
 */
@RestController
public class HazelcastJetDemoController {

    @Autowired
    JetInstance instance;

    @Autowired
    TestCache testCache;
    
    @RequestMapping("/jet/submitJob")
    public void submitJob() {
        Pipeline pipeline = Pipeline.create();
        pipeline.drawFrom(CustomSourceP.customSource())
                .drainTo(Sinks.logger());

        JobConfig jobConfig = new JobConfig()
                .addClass(HazelcastJetDemoController.class)
                .addClass(CustomSourceP.class);
        instance.newJob(pipeline, jobConfig).join();
    }

    @GetMapping("/jet/testcache")
    public HazelcastUser testCache() {
    	HazelcastUser user = new HazelcastUser();
    	user.setEmail("abc@abc.com").setName("a_name").setId(120);
        return this.testCache.getSomething(user);
    }
}