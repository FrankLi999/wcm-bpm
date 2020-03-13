package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class Grant {
	private String wcmPath;
	WcmPermission[] permissions;
	public String getWcmPath() {
		return wcmPath;
	}
	public void setWcmPath(String wcmPath) {
		this.wcmPath = wcmPath;
	}
	public WcmPermission[] getPermissions() {
		return permissions;
	}
	public void setPermissions(WcmPermission[] permissions) {
		this.permissions = permissions;
	}
	@Override
	public String toString() {
		return "Grant [wcmPath=" + wcmPath
				+ ", permissions=" + Arrays.toString(permissions) + "]";
	}
}
