package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class WcmHistory {
	private String wcmPath;
	private WcmVersion versions[];
	
	public String getWcmPath() {
		return wcmPath;
	}
	
	public void setWcmPath(String wcmPath) {
		this.wcmPath = wcmPath;
	}
	
	public WcmVersion[] getVersions() {
		return versions;
	}
	
	public void setVersions(WcmVersion[] versions) {
		this.versions = versions;
	}
	
	@Override
	public String toString() {
		return "WcmHistory [wcmPath=" + wcmPath + ", versions=" + Arrays.toString(versions) + ", toString()="
				+ super.toString() + "]";
	}
}
