package com.bpwizard.wcm_bpm.setup.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BaseMysqlConfiguration {
	private String mysqlUrl = "jdbc:mysql://localhost:3306";
	private String mysqlUser = "wcm";
	private String mysqlPassword = "P@ssw0rd";
	private String mysqlAdminUser = "mysqladmin";
	private String mysqlAdminPassword = "P@ssw0rd";
	private String dropSchemaStatement = "DROP schema %s";
	private String createSchemaStatement = "CREATE SCHEMA %s DEFAULT CHARACTER SET utf8 COLLATE utf8_bin";
	private String mysqlSchema = "wcm";
	private String mysqlScript = "classpath:wcm.sql";
	private String dropUserStatement ="DROP USER %s";
	private String createUserStatement = "CREATE USER '%s' IDENTIFIED BY '%s'";
	private String grantStatement = "GRANT ALL ON *.* TO '%s'@'%s' WITH GRANT OPTION";
	private String flushprivileges = "flush privileges";
}
