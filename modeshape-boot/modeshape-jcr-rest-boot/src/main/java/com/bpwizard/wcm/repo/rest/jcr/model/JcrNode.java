package com.bpwizard.wcm.repo.rest.jcr.model;

import java.io.InputStream;
import java.sql.Timestamp;

public class JcrNode {
	
	private String id;
	private Timestamp lastUpdated;
	private InputStream content;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Timestamp getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public InputStream getContent() {
		return content;
	}
	public void setContent(InputStream content) {
		this.content = content;
	}
}
