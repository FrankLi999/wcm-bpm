package com.oner.demo.seajug.hazelcast.scalewithhazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

@SpringBootApplication
@EnableCaching
@EnableHazelcastHttpSession
public class ScaleWithHazelcastApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScaleWithHazelcastApplication.class, args);
	}


	@Bean
	@Profile("hz-embedded")
	HazelcastInstance member() {
		return Hazelcast.newHazelcastInstance();
	}
}
