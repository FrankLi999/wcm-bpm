package com.bpwizard.wcm.repo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bpwizard.spring.boot.commons.jdbc.JdbcUtils;

public class ModeshapeAppConfig {

//	 @Bean(name = "modeshapeDatasource")
//	 @ConfigurationProperties(prefix = "spring.modeshape-ds")
//	 public DataSource modeshapeDatasource() {
//	  return  DataSourceBuilder.create().build();
//	 }
//
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private Environment env;
	
	@Bean(name = "modeshapeJdbcTemplate")
	public JdbcTemplate jdbcTemplate() {
	    return new JdbcTemplate(dataSource);
	}
	
	@Bean(name = "modeshapeJdbcTemplate")
	public JdbcTemplate modeshapeJdbcTemplate(@Qualifier("modeshapeDataSource") DataSource modeshapeDatasource) {
	    return new JdbcTemplate(modeshapeDatasource);
	}

	
	@Bean(name="modeshapeDataSource")
	public DataSource modeshapeDataSource() {
		return JdbcUtils.getDataSource("bpw.modeshape.datasource", env);
	}
}
