package com.bpwizard.wcm_bpm.setup;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm_bpm.setup.config.BaseMysqlConfiguration;
import com.bpwizard.wcm_bpm.setup.config.AccountDBConfiguration;
import com.bpwizard.wcm_bpm.setup.config.BpmDBConfiguration;
import com.bpwizard.wcm_bpm.setup.config.GatewayDBConfiguration;
import com.bpwizard.wcm_bpm.setup.config.WcmDBConfiguration;

@SpringBootApplication
public class SetupApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(SetupApplication.class);
	@Autowired
	AccountDBConfiguration accountDBConfiguration;
	
	@Autowired
	BpmDBConfiguration bpmDBConfiguration;

	@Autowired
	GatewayDBConfiguration gatewayDBConfiguration;

	@Autowired
	WcmDBConfiguration wcmDBConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(SetupApplication.class, args);
	}

	@Override
    public void run(String... args) {
		logger.info("EXECUTING : command line runner");
        setupDB("account database", accountDBConfiguration);
		setupDB("bpm database", bpmDBConfiguration);
		setupDB("gateway database", gatewayDBConfiguration);
		setupDB("wcm database", wcmDBConfiguration);
    }


	private static void setupDB(String db, BaseMysqlConfiguration mysqlConfiguration) {
        String adminUser = mysqlConfiguration.getMysqlAdminUser();
		String adminPwd = mysqlConfiguration.getMysqlAdminPassword();
		String mysqlUser = mysqlConfiguration.getMysqlUser();
		String mysqlPwd = mysqlConfiguration.getMysqlPassword();
		String mysqlSchema = mysqlConfiguration.getMysqlSchema();
		
		//try (Scanner scanner = new Scanner(System.in)) {
		Scanner scanner = new Scanner(System.in);
		System.out.println(String.format("Enter %s admin user name (%s): ", db, adminUser));
		String input = scanner.nextLine();
		adminUser = StringUtils.hasText(input) ? input : adminUser;
		
		System.out.println(String.format("Enter %s admin user password (%s): ", db, adminPwd));
		input = scanner.nextLine();
		adminPwd = StringUtils.hasText(input) ? input : adminPwd;
		
		System.out.println(String.format("Enter %s application user name (%s): ", db, mysqlUser));
		input = scanner.nextLine();
		mysqlUser = StringUtils.hasText(input) ? input : mysqlUser;
		
		System.out.println(String.format("Enter %s application user password (%s): ", db, mysqlPwd));
		input = scanner.nextLine();
		mysqlPwd = StringUtils.hasText(input) ? input : mysqlPwd;
		
		System.out.println(String.format("Enter %s application schema (%s): ", db, mysqlSchema));
		input = scanner.nextLine();
		mysqlSchema = StringUtils.hasText(input) ? input : mysqlSchema;
		//}
		logger.info("adminUser:" + adminUser);
		logger.info("adminPwd:" + adminPwd);
		logger.info("mysqlUser:" + mysqlUser);
		logger.info("mysqlPwd:" + mysqlPwd);
		logger.info("mysqlSchema:" + mysqlSchema);
        String mysqlDriverUrl = String.format(
        		"%s/?user=%s&password=%s", 
        		mysqlConfiguration.getMysqlUrl(),
        		adminUser,
        		adminPwd);
        
        try (Connection conn = DriverManager.getConnection(
        		mysqlDriverUrl);
        		Statement s = conn.createStatement()){
        	try {
        		s.executeUpdate(String.format(mysqlConfiguration.getDropSchemaStatement(), mysqlSchema));
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}
        	logger.info("create schema:" + String.format(mysqlConfiguration.getCreateSchemaStatement(), mysqlSchema));
        	s.executeUpdate(String.format(mysqlConfiguration.getCreateSchemaStatement(), mysqlSchema));
        	
        	s.executeUpdate(String.format(mysqlConfiguration.getDropUserStatement(), mysqlUser));
        	s.executeUpdate(String.format(mysqlConfiguration.getCreateUserStatement(), mysqlUser, mysqlPwd));
        	
        	s.executeUpdate(String.format(mysqlConfiguration.getGrantStatement(), mysqlUser, "%"));
        	s.executeUpdate(mysqlConfiguration.getFlushprivileges());
        } catch (Throwable t) {
        	logger.error("Error set up wcm database,", t);
        	t.printStackTrace();
        } 
        
        String mysqlSchemaDriverUrl = String.format(
        		"%s/%s?user=%s&password=%s", 
        		mysqlConfiguration.getMysqlUrl(),
        		mysqlSchema,
        		mysqlUser,
        		mysqlPwd);
        
        logger.info("mysqlSchemaDriverUrl:" + mysqlSchemaDriverUrl);
        try (Connection conn = DriverManager.getConnection(
        		mysqlSchemaDriverUrl);
        		Statement s = conn.createStatement()) {
        	
        	logger.info("mysqlSchemaDriverUrl:" + mysqlSchemaDriverUrl);
        	ScriptRunner sr = new ScriptRunner(conn);
        	logger.info("ScriptRunner:" + sr);
        	
        	sr.runScript(new FileReader(ResourceUtils.getFile(mysqlConfiguration.getMysqlScript())));
        } catch (Throwable t) {
        	logger.error("Error set up wcm database,", t);
        	t.printStackTrace();
        } 
        logger.info("Done!");
	}
}
