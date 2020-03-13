package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class WcmPermission {
	private String principalId;
	private String principalType;
	//Viewer, Reviewer, Editor, Administrator 
	private String wcmRoles[];
	
	public String getPrincipalId() {
		return principalId;
	}
	
	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}
	
	public String getPrincipalType() {
		return principalType;
	}
	
	public void setPrincipalType(String principalType) {
		this.principalType = principalType;
	}
	
	public String[] getWcmRoles() {
		return wcmRoles;
	}
	
	public void setWcmRoles(String[] wcmRoles) {
		this.wcmRoles = wcmRoles;
	}

	@Override
	public String toString() {
		return "WcmPermission [principalId=" + principalId + ", principalType=" + principalType + ", wcmRoles="
				+ Arrays.toString(wcmRoles) + "]";
	}
}
