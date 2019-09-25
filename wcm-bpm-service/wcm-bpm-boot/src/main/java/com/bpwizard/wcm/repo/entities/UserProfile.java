package com.bpwizard.wcm.repo.entities;
//The DTO
public class UserProfile {
    private String id;
	private String email;
	private String password;
	private String name;
	private String firstName;
	private String lasstName;
	private String token;
	private String imageUrl;
	private String roles;
	
	public static UserProfile fromUserAndToken(User user, String token) {
		return null;
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
	public String getLasstName() {
		return lasstName;
	}
	public void setLasstName(String lasstName) {
		this.lasstName = lasstName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	@Override
	public String toString() {
		return "UserProfile [id=" + id + ", email=" + email + ", password=" + password + ", name=" + name
				+ ", firstName=" + firstName + ", lasstName=" + lasstName + ", token=" + token + ", imageUrl="
				+ imageUrl + ", roles=" + roles + "]";
	}
}
