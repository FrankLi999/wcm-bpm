package com.bpwizard.wcm.repo.rest.jcr.model;

import java.io.InputStream;
import java.sql.Timestamp;

public class SyndicationItem {

	private String id;
	private Timestamp lastChanged;
	private InputStream content;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Timestamp getLastChanged() {
		return lastChanged;
	}
	public void setLastChanged(Timestamp lastChanged) {
		this.lastChanged = lastChanged;
	}
	public InputStream getContent() {
		return content;
	}
	public void setContent(InputStream content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "SyndicationItem [id=" + id + ", lastChanged=" + lastChanged + ", content=" + content + "]";
	}
}
