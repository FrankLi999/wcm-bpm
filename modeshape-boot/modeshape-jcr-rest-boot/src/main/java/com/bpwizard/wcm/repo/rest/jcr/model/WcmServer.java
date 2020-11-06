package com.bpwizard.wcm.repo.rest.jcr.model;

import org.springframework.data.annotation.Id;

public class WcmServer {
	@Id
	private Long id;
	private String host;
	private int port;
	
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

	@Override
	public String toString() {
		return "WcmServer [id=" + id + ", host=" + host + ", port=" + port + "]";
	}
}
