package com.example.demo.config;

import com.hazelcast.config.Config;
import com.hazelcast.core.ManagedContext;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JetConfig;
import com.hazelcast.spring.context.SpringManagedContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * Spring Context annotation-based configuration class
 */
@Configuration
@SpringBootApplication
@ComponentScan({"jet.spring"})
public class AppConfig {

    /**
     * DataSource bean for embedded database HSQL
     */
    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.HSQL)
                      .addScript("db/sql/create-db.sql")
                      .addScript("db/sql/insert-data.sql")
                      .build();
    }

    /**
     * Jdbc template bean to query {@code DataSource}
     */
    @Bean
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    /**
     * A {@code ManagedContext} implementation bean which enables {@code @SpringAware}
     * annotation for de-serialized objects.
     */
    @Bean
    public ManagedContext managedContext() {
        return new SpringManagedContext();
    }

    /**
     * {@code JetInstance} bean which configured programmatically with {@code SpringManagedContext}
     */
    @Bean
    public JetInstance instance() {
        Config config = new Config().setManagedContext(managedContext());
        JetConfig jetConfig = new JetConfig().setHazelcastConfig(config);
        return Jet.newJetInstance(jetConfig);
    }
}