package com.bpwizard.wcm.repo.rest.jcr.model;

import java.security.Principal;

public class ModeshapePrincipal implements Principal {
	private String name;
	
	public ModeshapePrincipal(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
}
