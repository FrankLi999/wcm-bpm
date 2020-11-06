package com.bpwizard.wcm.repo.rest.jcr.model;

import java.sql.Timestamp;

public class Syndication {
	private Long id;
	private WcmServer collector;
	private Timestamp lastSyndication;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public WcmServer getCollector() {
		return collector;
	}
	
	public void setCollector(WcmServer collector) {
		this.collector = collector;
	}
	
	public Timestamp getLastSyndication() {
		return lastSyndication;
	}
	
	public void setLastSyndication(Timestamp lastSyndication) {
		this.lastSyndication = lastSyndication;
	}

	@Override
	public String toString() {
		return "Syndication [id=" + id + ", collector=" + collector
				+ ", lastSyndication=" + lastSyndication + "]";
	}

}
