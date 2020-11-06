package com.bpwizard.wcm.repo.rest.jcr.model;

import java.sql.Timestamp;

public class SyndicationRequest {
	
	private long syndicationId;
	private Timestamp startTime;
	
	public long getSyndicationId() {
		return syndicationId;
	}
	
	public void setSyndicationId(long syndicationId) {
		this.syndicationId = syndicationId;
	}
	
	public Timestamp getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		return "SyndicationRequest [syndicationId=" + syndicationId + ", startTime=" + startTime + "]";
	}
}
