package com.bpwizard.wcm.repo.rest.jcr.model;

import java.sql.Timestamp;

public class SyndicationRequest {
	private long syndicationId;
	private Timestamp endTime;
	
	public long getSyndicationId() {
		return syndicationId;
	}
	
	public void setSyndicationId(long syndicationId) {
		this.syndicationId = syndicationId;
	}
	
	public Timestamp getEndTime() {
		return endTime;
	}
	
	public void setStartTime(Timestamp startTime) {
		this.endTime = startTime;
	}
	@Override
	public String toString() {
		return "SyndicationRequest [syndicationId=" + syndicationId + ", endTime=" + endTime + "]";
	}
}
