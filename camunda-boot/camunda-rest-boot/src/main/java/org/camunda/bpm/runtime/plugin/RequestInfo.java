package org.camunda.bpm.runtime.plugin;

import javax.servlet.http.HttpServletRequest;

public class RequestInfo {
	protected HttpServletRequest request;

	public RequestInfo(HttpServletRequest request) {
		this.request = request;

	}

}
