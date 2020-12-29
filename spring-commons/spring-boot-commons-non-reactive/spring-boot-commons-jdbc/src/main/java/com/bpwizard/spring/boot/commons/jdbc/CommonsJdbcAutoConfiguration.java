package com.bpwizard.spring.boot.commons.jdbc;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bpwizard.spring.boot.commons.web.CommonsWebAutoConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
//@EnableJdbcAuditing
@EnableJdbcRepositories
@AutoConfigureBefore({CommonsWebAutoConfiguration.class})
public class CommonsJdbcAutoConfiguration {

	@Autowired
	private Environment env;

	@Bean(name = "accountDBJdbcTemplate")
	public NamedParameterJdbcTemplate accountDBJdbcTemplate(@Qualifier("accountDBDatasource") DataSource accountDBDatasource) {
	    return new NamedParameterJdbcTemplate(accountDBDatasource);
	}

	@Bean(name="accountDBDatasource")
	public DataSource accountDBDatasource() {
        return JdbcUtils.getDataSource("bpw.account.datasource", env);
	} 
}
