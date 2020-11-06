package com.bpwizard.wcm.repo.rest.jcr.model;

import java.sql.Timestamp;

public class Collector {
	private long id;
	private WcmServer syndicator;
	private Timestamp lastSyndication;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public WcmServer getSyndicator() {
		return syndicator;
	}
	public void setSyndicator(WcmServer syndicator) {
		this.syndicator = syndicator;
	}
	public Timestamp getLastSyndication() {
		return lastSyndication;
	}
	public void setLastSyndication(Timestamp lastSyndication) {
		this.lastSyndication = lastSyndication;
	}
	@Override
	public String toString() {
		return "Collector [id=" + id + ", syndicator=" + syndicator + ", lastSyndication=" + lastSyndication + "]";
	}
}
