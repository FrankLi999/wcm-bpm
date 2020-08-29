package com.bpwizard.wcm_bpm.setup.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix="bpw")
@Getter @Setter
public class MysqlConfiguration {
	private String mysqlUrl = "jdbc:mysql://localhost:3306";
	private String mysqlUser = "wcmbpm";
	private String mysqlPassword = "P@ssw0rd";
	private String mysqlAdminUser = "root";
	private String mysqlAdminPassword = "P@ssw0rd";
	private String dropSchemaStatement = "DROP schema %s";
	private String createSchemaStatement = "CREATE SCHEMA %s DEFAULT CHARACTER SET utf8 COLLATE utf8_bin";
	private String mysqlSchema = "wcm_bpm";
	private String mysqlScript = "classpath:wcm.sql";
	private String dropUserStatement ="DROP USER %s";
	private String createUserStatement = "CREATE USER '%s' IDENTIFIED BY '%s'";
	private String grantStatement = "GRANT ALL ON *.* TO '%s'@'%s' WITH GRANT OPTION";
	private String flushprivileges = "flush privileges";

}
