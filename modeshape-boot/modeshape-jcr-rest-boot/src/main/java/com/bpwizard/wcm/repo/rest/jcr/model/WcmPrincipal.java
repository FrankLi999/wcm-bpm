package com.bpwizard.wcm.repo.rest.jcr.model;

import java.security.Principal;

public class WcmPrincipal implements Principal {
	private String name;
	
	public WcmPrincipal(String name) {
		this.name = name;
	}
	@Override
	public String getName() {
		return this.name;
	}

}
