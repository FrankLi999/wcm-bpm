package com.bpwizard.wcm.repo.rest.jcr.model;

import java.sql.Timestamp;

public class ClearWcmEventRequest {
	Timestamp timestampBefore;

	public Timestamp getTimestampBefore() {
		return timestampBefore;
	}

	public void setTimestampBefore(Timestamp timestampBefore) {
		this.timestampBefore = timestampBefore;
	}

	@Override
	public String toString() {
		return "ClearWcmEventRequest [timestampBefore=" + timestampBefore + "]";
	}
}
