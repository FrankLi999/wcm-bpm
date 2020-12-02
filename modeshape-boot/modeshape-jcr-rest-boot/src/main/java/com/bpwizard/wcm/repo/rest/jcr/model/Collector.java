package com.bpwizard.wcm.repo.rest.jcr.model;

import java.sql.Timestamp;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;

public class Collector {
	@Id
	private long id;
	private WcmServer syndicator;
	private Timestamp lastSyndication;
	@CreatedBy
	private String createdBy;
	@LastModifiedBy
	private String updatedBy;
	@CreatedDate
	private Timestamp createdAt;
	@LastModifiedBy
	private Timestamp updatedAt;
	  
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
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	@Override
	public String toString() {
		return "Collector [id=" + id + ", syndicator=" + syndicator + ", lastSyndication=" + lastSyndication
				+ ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
}
