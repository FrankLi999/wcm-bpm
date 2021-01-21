package com.bpwizard.wcm.repo.rest.jcr.model;

import java.sql.Timestamp;

public class UpdateSyndicationRequest {
	private Long syndicationId;
	private Timestamp lastSyndication;
	public Long getSyndicationId() {
		return syndicationId;
	}
	public void setSyndicationId(Long syndicationId) {
		this.syndicationId = syndicationId;
	}
	public Timestamp getLastSyndication() {
		return lastSyndication;
	}
	public void setLastSyndication(Timestamp lastSyndication) {
		this.lastSyndication = lastSyndication;
	}
	@Override
	public String toString() {
		return "UpdateSyndicationRequest [syndicationId=" + syndicationId + ", lastSyndication="
				+ lastSyndication + "]";
	}
}	
