package com.example.demo.controller;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.example.demo.source.CustomSourceP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
public class SpringBootSampleController {

    @Autowired
    JetInstance instance;


    @RequestMapping("/jet/submitJob")
    public void submitJob() {
        Pipeline pipeline = Pipeline.create();
        pipeline.drawFrom(CustomSourceP.customSource())
                .drainTo(Sinks.logger());

        JobConfig jobConfig = new JobConfig()
                .addClass(SpringBootSampleController.class)
                .addClass(CustomSourceP.class);
        instance.newJob(pipeline, jobConfig).join();
    }


}