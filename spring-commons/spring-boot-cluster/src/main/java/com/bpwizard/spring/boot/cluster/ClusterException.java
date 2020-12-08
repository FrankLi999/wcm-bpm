package com.bpwizard.spring.boot.cluster;

public class ClusterException extends Exception {

	private static final long serialVersionUID = -2269720939452317023L;
	
	public ClusterException(String message) {
		super(message);
	}

	public ClusterException(String message, Throwable cause) {
		super(message, cause);
	}
}
