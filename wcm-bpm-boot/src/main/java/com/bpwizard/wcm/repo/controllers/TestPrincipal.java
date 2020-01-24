package com.bpwizard.wcm.repo.controllers;

import java.security.Principal;

public class TestPrincipal implements Principal {
	private String name = "TestPrinciple";
	
	public TestPrincipal() {
	}
	
	public TestPrincipal(String name) {
		this.name = name;
	}
	@Override
	public String getName() {
		return this.name;
	}

}
