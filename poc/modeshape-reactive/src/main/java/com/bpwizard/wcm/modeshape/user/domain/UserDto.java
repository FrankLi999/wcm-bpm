package com.bpwizard.wcm.modeshape.user.domain;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDto {
	private Long id;

    private String name;

    private String email;

    private String imageUrl;

    private Boolean emailVerified = false;

    private String password;

    private String provider;
    private String providerId;
    
    public static UserDto fromUser(User user) {
    	UserDto userDto = new UserDto();
    	userDto.setEmail(user.getLogin());
    	userDto.setEmailVerified(true);
    	userDto.setId(1L);
    	userDto.setImageUrl("https://qph.fs.quoracdn.net/main-thumb-206448635-200-rmvzpvteuozeytbhnnnhcoquhvkauzqf.jpeg");
    	userDto.setName("Frank Li");
    	userDto.setProvider("local");
    	userDto.setProvider("google_1");
    	return userDto;
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Boolean getEmailVerified() {
		return emailVerified;
	}
	public void setEmailVerified(Boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
    
    
}
