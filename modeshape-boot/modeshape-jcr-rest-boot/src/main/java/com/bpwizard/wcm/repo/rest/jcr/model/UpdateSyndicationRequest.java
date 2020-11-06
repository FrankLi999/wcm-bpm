package com.bpwizard.wcm.repo.rest.jcr.model;

import java.sql.Timestamp;

public class UpdateSyndicationRequest {
	private String syndicationId;
	private Timestamp lastSyndication;
	public String getSyndicationId() {
		return syndicationId;
	}
	public void setSyndicationId(String syndicationId) {
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
