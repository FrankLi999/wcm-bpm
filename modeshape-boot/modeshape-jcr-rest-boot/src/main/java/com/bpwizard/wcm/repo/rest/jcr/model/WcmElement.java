package com.bpwizard.wcm.repo.rest.jcr.model;

public class WcmElement {
	private String id;
	private long timestamp;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "WcmElement [id=" + id + ", timestamp=" + timestamp + "]";
	}
}
