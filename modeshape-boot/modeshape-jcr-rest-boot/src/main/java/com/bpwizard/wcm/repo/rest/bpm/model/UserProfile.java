package com.bpwizard.wcm.repo.rest.bpm.model;

import java.util.List;

public class UserProfile {
	private String name;
	private String email;
	private List<String> groups;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<String> getGroups() {
		return groups;
	}
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
	@Override
	public String toString() {
		return "UserProfile [name=" + name + ", email=" + email + ", groups=" + groups + "]";
	}
}
