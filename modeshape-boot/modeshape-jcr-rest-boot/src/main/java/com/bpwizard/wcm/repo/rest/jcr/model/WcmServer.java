package com.bpwizard.wcm.repo.rest.jcr.model;

import java.sql.Timestamp;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;

public class WcmServer {
	@Id
	private Long id;
	private String host;
	private int port;
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
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
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
		return "WcmServer [id=" + id + ", host=" + host + ", port=" + port + ", createdBy=" + createdBy + ", updatedBy="
				+ updatedBy + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}
