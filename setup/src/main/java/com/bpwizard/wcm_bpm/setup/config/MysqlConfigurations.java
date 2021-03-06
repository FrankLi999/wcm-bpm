package com.bpwizard.wcm_bpm.setup.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix="bpw.db")
@Getter @Setter
public class MysqlConfigurations {

	List<MysqlConfiguration> mysqlConfiguration;
}
