package com.bpwizard.wcm.repo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariDataSource;

public class ModeshapeAppConfig {

//	 @Bean(name = "modeshapeDatasource")
//	 @ConfigurationProperties(prefix = "spring.modeshape-ds")
//	 public DataSource modeshapeDatasource() {
//	  return  DataSourceBuilder.create().build();
//	 }
//
	 @Bean(name = "modeshapeJdbcTemplate")
	 public JdbcTemplate modeshapeJdbcTemplate(@Qualifier("modeshapeDataSource") DataSource modeshapeDatasource) {
	  return new JdbcTemplate(modeshapeDatasource);
	 }
	 
	
	@Autowired
	private Environment env;
	
	@Bean(name="modeshapeDataSource")
	public DataSource modeshapeDataSource() {
//        DriverManagerDataSource dataSource
//                = new DriverManagerDataSource();
//        dataSource.setDriverClassName(
//                env.getProperty("bpw.modeshape.datasource.driver-class-name"));
//        dataSource.setUrl(env.getProperty("bpw.modeshape.datasource.url"));
//        dataSource.setUsername(env.getProperty("bpw.modeshape.datasource.username"));
//        dataSource.setPassword(env.getProperty("bpw.modeshape.datasource.password"));
		
		HikariDataSource hikariDS = new HikariDataSource();
		hikariDS.setJdbcUrl(env.getProperty("bpw.modeshape.datasource.url"));
		hikariDS.setDriverClassName(env.getProperty("bpw.modeshape.datasource.driver-class-name"));
		hikariDS.setUsername(env.getProperty("bpw.modeshape.datasource.username"));
		hikariDS.setPassword(env.getProperty("bpw.modeshape.datasource.password"));
 
        return hikariDS;
	}
}
