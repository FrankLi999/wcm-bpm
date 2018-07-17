package com.bpwizard.myresources.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.caryyu.spring.embedded.redisserver.RedisServerConfiguration;

@Configuration
public class RedisConfiguration {
	@Bean
	public RedisServerConfiguration redisServerConfiguration(){
	    return new RedisServerConfiguration();
	}
}
