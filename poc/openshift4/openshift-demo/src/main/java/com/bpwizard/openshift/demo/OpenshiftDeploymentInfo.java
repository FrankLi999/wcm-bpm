package com.bpwizard.openshift.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class OpenshiftDeploymentInfo {
	
	@Value("${DB_HOST_PORT:notdefined}")
	private String dbHostPort;

	@Value("${DB_PASSWORD:pwdNotdefined}")
	private String dbPwd;

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
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>... dbHostPort:" + dbHostPort);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>... testProperty:" + testProperty);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>... testProperty1:" + testProperty1);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>... dbUser:" + dbUser);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>... dbPwd:" + dbPwd);
		List<User> users = userRepo.findByName("ali");
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>... users:" + users);
		String userStr = (users != null && users.size()> 0) ? String.join(",", "" + users.get(0).getId(), users.get(0).getName(), users.get(0).getEmail()) : "n/a";
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>... env:" + String.join("-", testProperty, dbHostPort, "user", userStr));
		return String.join("-", dbPwd, dbUser, testProperty, testProperty1, dbHostPort, "user", userStr);
	}
}