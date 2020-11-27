package com.bpwizard.wcm.repo.dto;

import java.util.Arrays;

import com.bpwizard.spring.boot.commons.service.repo.domain.Role;
import com.bpwizard.spring.boot.commons.service.repo.domain.User;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmSystem;
import com.bpwizard.wcm_bpm.dto.UserProfile;
public class WcmUserProfile extends UserProfile {
	
    private WcmSystem settings;
    public static WcmUserProfile fromUserAndToken(User user, WcmSystem settings) {
    	return WcmUserProfile.fromUserAndToken(user, null, 0, null, settings);
    }
    public static WcmUserProfile fromUserAndToken(User user, String accessToken, long expireIn, String sessionId, WcmSystem settings) {
    	WcmUserProfile userProfile = new WcmUserProfile();
    	userProfile.setAccessToken(accessToken);
    	userProfile.setId(user.getId().toString());
    	userProfile.setEmail(user.getEmail());
    	userProfile.setImageUrl(user.getImageUrl());
    	userProfile.setName(user.getName());
    	userProfile.setFirstName(user.getFirstName());
    	userProfile.setLastName(user.getLastName());
    	userProfile.setExpireIn(expireIn);
    	userProfile.setSessionId(sessionId);
    	userProfile.setRoles(user.getRoles().stream().map(Role::getName).toArray(String[]::new));
    	userProfile.setSettings(settings);
    	return userProfile;
    }

	
	public WcmSystem getSettings() {
		return settings;
	}
	public void setSettings(WcmSystem settings) {
		this.settings = settings;
	}
	@Override
	public String toString() {
		return "WcmUserProfile [settings=" + settings + ", getId()=" + getId() + ", getEmail()=" + getEmail()
				+ ", getPassword()=" + getPassword() + ", getName()=" + getName() + ", getFirstName()=" + getFirstName()
				+ ", getLastName()=" + getLastName() + ", getImageUrl()=" + getImageUrl() + ", getRoles()="
				+ Arrays.toString(getRoles()) + ", getAccessToken()=" + getAccessToken() + ", getExpireIn()="
				+ getExpireIn() + ", getTokenType()=" + getTokenType() + ", getSessionId()=" + getSessionId()
				+ ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}
}
