package com.bpwizard.wcm.repo.rest.jcr.model;

import java.sql.Timestamp;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;

public class Syndicator {
	@Id
	private Long id;
	private String repository;
	private String workspace;
	private String library;
	private WcmServer collector;
	private Timestamp lastSyndication;
	@CreatedBy
	private String createdBy;
	@LastModifiedBy
	private String updatedBy;
	@CreatedDate
	private Timestamp createdAt;
	@LastModifiedBy
	private Timestamp updatedAt;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
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
		return "Syndicator [id=" + id + ", repository=" + repository + ", workspace=" + workspace + ", library="
				+ library + ", collector=" + collector + ", lastSyndication=" + lastSyndication + ", createdBy="
				+ createdBy + ", updatedBy=" + updatedBy + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ "]";
	}

	
}
