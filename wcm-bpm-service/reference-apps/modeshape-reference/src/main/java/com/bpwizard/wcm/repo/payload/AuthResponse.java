package com.bpwizard.wcm.repo.payload;

import com.bpwizard.spring.boot.commons.service.repo.domain.User;

import java.util.Arrays;

import com.bpwizard.spring.boot.commons.service.repo.domain.Role;
public class AuthResponse {
    private String id;
	private String email;
	private String password;
	private String name;
	private String firstName;
	private String lastName;
	private String imageUrl;
	private String roles[];
    private String accessToken;
    private String tokenType = "Bearer";
    private String sessionId = "n/a";
    public static AuthResponse fromUserAndToken(User user, String accessToken, String sessionId) {
    	AuthResponse authResponse = new AuthResponse();
    	authResponse.setAccessToken(accessToken);
    	authResponse.setId(user.getId().toString());
    	authResponse.setEmail(user.getEmail());
    	authResponse.setImageUrl(user.getImageUrl());
    	authResponse.setName(user.getName());
    	authResponse.setFirstName(user.getFirstName());
    	authResponse.setLastName(user.getLastName());
    	
    	authResponse.setSessionId(sessionId);
    	authResponse.setRoles(user.getRoles().stream().map(Role::getName).toArray(String[]::new));
    	return authResponse;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return "AuthResponse [id=" + id + ", email=" + email + ", password=" + password + ", name=" + name
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", imageUrl=" + imageUrl + ", roles="
				+ Arrays.toString(roles) + ", accessToken=" + accessToken + ", tokenType=" + tokenType + ", sessionId="
				+ sessionId + "]";
	}

	

}
