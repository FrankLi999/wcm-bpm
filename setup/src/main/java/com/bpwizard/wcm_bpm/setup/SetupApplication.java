package com.bpwizard.wcm_bpm.setup;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm_bpm.setup.config.MysqlConfiguration;


@SpringBootApplication
public class SetupApplication implements CommandLineRunner {
//    private static Logger LOG = LoggerFactory
//    	      .getLogger(SetupApplication.class);
	@Autowired
	MysqlConfiguration mysqlConfiguration;
	
	public static void main(String[] args) {
		SpringApplication.run(SetupApplication.class, args);
	}

	@Override
    public void run(String... args) {
        // LOG.info("EXECUTING : command line runner");
        
		String adminUser = mysqlConfiguration.getMysqlAdminUser();
		String adminPwd = mysqlConfiguration.getMysqlAdminPassword();
		String mysqlUser = mysqlConfiguration.getMysqlUser();
		String mysqlPwd = mysqlConfiguration.getMysqlPassword();
		String mysqlSchema = mysqlConfiguration.getMysqlSchema();
		
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print(String.format("Enter MYSQL admin user name (%s): ", adminUser));
			String input = scanner.nextLine();
			adminUser = StringUtils.hasText(input) ? input : adminUser;
			
			System.out.print(String.format("Enter MYSQL admin user password (%s): ", adminPwd));
			input = scanner.nextLine();
			adminPwd = StringUtils.hasText(input) ? input : adminPwd;
			
			System.out.print(String.format("Enter MYSQL application user name (%s): ", mysqlUser));
			input = scanner.nextLine();
			mysqlUser = StringUtils.hasText(input) ? input : mysqlUser;
			
			System.out.print(String.format("Enter MYSQL application user password (%s): ", mysqlPwd));
			input = scanner.nextLine();
			mysqlPwd = StringUtils.hasText(input) ? input : mysqlPwd;
			
			System.out.print(String.format("Enter MYSQL application schema (%s): ", mysqlSchema));
			input = scanner.nextLine();
			mysqlSchema = StringUtils.hasText(input) ? input : mysqlSchema;
		}
		System.out.println("adminUser:" + adminUser);
		System.out.println("adminPwd:" + adminPwd);
		System.out.println("mysqlUser:" + mysqlUser);
		System.out.println("mysqlPwd:" + mysqlPwd);
		System.out.println("mysqlSchema:" + mysqlSchema);
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
        	System.out.println("create schema:" + String.format(mysqlConfiguration.getCreateSchemaStatement(), mysqlSchema));
        	s.executeUpdate(String.format(mysqlConfiguration.getCreateSchemaStatement(), mysqlSchema));
        	
        	s.executeUpdate(String.format(mysqlConfiguration.getDropUserStatement(), mysqlUser));
        	s.executeUpdate(String.format(mysqlConfiguration.getCreateUserStatement(), mysqlUser, mysqlPwd));
        	
        	s.executeUpdate(String.format(mysqlConfiguration.getGrantStatement(), mysqlUser, "%"));
        	s.executeUpdate(mysqlConfiguration.getFlushprivileges());
        } catch (Throwable t) {
        	t.printStackTrace();
        } 
        
        String mysqlSchemaDriverUrl = String.format(
        		"%s/%s?user=%s&password=%s", 
        		mysqlConfiguration.getMysqlUrl(),
        		mysqlSchema,
        		mysqlUser,
        		mysqlPwd);
        
        System.out.println("mysqlSchemaDriverUrl:" + mysqlSchemaDriverUrl);
        try (Connection conn = DriverManager.getConnection(
        		mysqlSchemaDriverUrl);
        		Statement s = conn.createStatement()) {
        	
        	System.out.println("mysqlSchemaDriverUrl:" + mysqlSchemaDriverUrl);
        	ScriptRunner sr = new ScriptRunner(conn);
        	System.out.println("ScriptRunner:" + sr);
        	
        	sr.runScript(new FileReader(ResourceUtils.getFile(mysqlConfiguration.getMysqlScript())));
        } catch (Throwable t) {
        	t.printStackTrace();
        } 
        System.out.println("Done!");
    }
}
