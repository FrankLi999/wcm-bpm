package com.bpwizard.wcm.repo.rest.modeshape.model;

public class BackupResponse {
    private String name;
    private String url;

    public BackupResponse(String name, String url) {
    	this.name = name;
    	this.url = url;
    }
	
    public String getName() {
		return name;
	}
	
    public String getUrl() {
		return url;
	}
}
