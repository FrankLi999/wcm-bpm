package com.bpwizard.openshift.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class OpenshiftDeploymentInfo {
	private static final Logger logger = LogManager.getLogger(OpenshiftDemoApplication.class);
	@Value("${DB_HOST_PORT:notdefined}")
	private String dbHostPort;

	@Value("${DB_PASSWORD:pwdNotdefined}")
	private String dbPwd;
	
	@Value("${log.root.dir:noLogDir}")
	private String logRootDir;
	

	@Value("${DB_USERNAME:userNotdefined}")
	private String dbUser;
	
	@Value("${test.property:testPropertyNotdefined}")
	private String testProperty;
    
	@Value("${test.property1:testProperty1notdefined}")
	private String testProperty1;
	
    @Autowired
    private UserRepo userRepo;
    
	@GetMapping("/env")
	public String getEnv() {
		logger.traceEntry();
		if (logger.isTraceEnabled()) {
			logger.error("...>>>>>>>>>>>>>>>>>>>>>>>>... dbHostPort:" + dbHostPort);
			logger.trace("...>>>>>>>>>>>>>>>>>>>>>>>>... testProperty:" + testProperty);
			logger.trace("...>>>>>>>>>>>>>>>>>>>>>>>>... testProperty1:" + testProperty1);
			logger.trace("....>>>>>>>>>>>>>>>>>>>>>>>>... dbUser:" + dbUser);
			logger.trace("....>>>>>>>>>>>>>>>>>>>>>>>>... dbPwd:" + dbPwd);
			logger.error("....>>>>>>>>>>>>>>>>>>>>>>>>... logRootDir:" + logRootDir);
		}
		try {
			File file = new File("/var/spring-logs/openshift-demo-boot/test.txt");
			if (file.createNewFile()){
				if (logger.isDebugEnabled()) {
					logger.debug("/var/spring-logs/openshift-demo-boot/test.txt File Created in Project root directory");
				}
	            
	        } else {
	        	if (logger.isDebugEnabled()) {
					logger.debug("File /var/spring-logs/openshift-demo-boot/test.txt already exists in the project root directory");
				}
	        }
			FileOutputStream fos = new FileOutputStream("/var/spring-logs/openshift-demo-boot/test.txt");
			fos.write("Some data".getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			logger.error("Failed to create fail:", e);
			e.printStackTrace();
		}
		List<User> users = userRepo.findByName("can");
		if (logger.isTraceEnabled()) {
			logger.trace(">>>>>>>>>>>>>>>>>>>>>>>>... users:" + users);
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>... users:" + users);
		String userStr = (users != null && users.size()> 0) ? String.join(",", "" + users.get(0).getId(), users.get(0).getName(), users.get(0).getEmail()) : "n/a";
		if (logger.isTraceEnabled()) {
			logger.trace(">>>>>>>>>>>>>>>>>>>>>>>>... env:" + String.join("-", testProperty, dbHostPort, "user", userStr));
		}
		logger.traceExit();
		return String.join("**", logRootDir, dbPwd, dbUser, testProperty, testProperty1, dbHostPort, "user", userStr);
	}
}