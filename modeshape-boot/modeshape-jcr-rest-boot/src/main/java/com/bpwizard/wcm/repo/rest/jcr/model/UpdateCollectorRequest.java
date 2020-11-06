package com.bpwizard.wcm.repo.rest.jcr.model;

import java.sql.Timestamp;

public class UpdateCollectorRequest {
	private String collectorId;
	private Timestamp lastSyndication;
	
	public String getCollectorId() {
		return collectorId;
	}
	public void setCollectorId(String collectorId) {
		this.collectorId = collectorId;
	}
	public Timestamp getLastSyndication() {
		return lastSyndication;
	}
	public void setLastSyndication(Timestamp lastSyndication) {
		this.lastSyndication = lastSyndication;
	}
	@Override
	public String toString() {
		return "UpdateCollectorRequest [collectorId=" + collectorId + ", lastSyndication=" + lastSyndication + "]";
	}
}
