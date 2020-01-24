package com.bpwizard.data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

//@Configuration
//@EnableMongoRepositories(basePackages = "com.bpwizard.data.repository")
public class SimpleMongoConfig {

    @Bean
    public MongoClient mongo() throws Exception {
        return MongoClients.create("mongodb://localhost:27017");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), "test");
    }

}
