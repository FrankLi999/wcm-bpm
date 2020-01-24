package com.bpwizard.spring.boot.commons.mongo;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.bpwizard.spring.boot.commons.webflux.CommonsReactiveAutoConfiguration;

@Configuration
@EnableMongoAuditing
@AutoConfigureBefore({
	MongoReactiveAutoConfiguration.class,
	CommonsReactiveAutoConfiguration.class})
public class CommonsMongoAutoConfiguration {

}
