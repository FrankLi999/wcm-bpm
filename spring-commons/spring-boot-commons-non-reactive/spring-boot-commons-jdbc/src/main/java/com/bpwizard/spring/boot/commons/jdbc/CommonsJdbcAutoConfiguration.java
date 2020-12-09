package com.bpwizard.spring.boot.commons.jdbc;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bpwizard.spring.boot.commons.web.CommonsWebAutoConfiguration;

@Configuration
@EnableTransactionManagement
//@EnableJdbcAuditing
@EnableJdbcRepositories
@AutoConfigureBefore({CommonsWebAutoConfiguration.class})
public class CommonsJdbcAutoConfiguration {
	
	//  @Bean
	//  public JdbcTemplate jdbcTemplate(DataSource datasource) {
	//   return new JdbcTemplate(datasource);
	//  }
	 
}
