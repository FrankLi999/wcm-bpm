package com.bpwizard.spring.boot.commons.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.bpwizard.spring.boot.commons.util.UserUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A lighter User class,
 * mainly used for holding logged-in user data 
 */
@Getter @Setter @ToString
public class UserDto implements Serializable {

	private static final long serialVersionUID = -9134054705405149534L;
	
	private String id;
	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private Set<String> roles = new HashSet<String>();
	private Serializable tag;
	private String email;
	private String imageUrl;

    private Boolean emailVerified = false;

    private String provider;

    private String providerId;
    
	private boolean unverified = false;
	private boolean blocked = false;
	private boolean admin = false;
	private boolean goodUser = false;
	private boolean goodAdmin = false;
	
	public void initialize() {
		
		unverified = roles.contains(UserUtils.Role.UNVERIFIED);
		blocked = roles.contains(UserUtils.Role.BLOCKED);
		admin = roles.contains(UserUtils.Role.ADMIN);
		goodUser = !(unverified || blocked);
		goodAdmin = goodUser && admin;
	}
}
