package com.bpwizard.wcm.repo.rest.jcr.controllers;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
public class TestGroup implements Group {
	java.util.Vector<Principal> members = new java.util.Vector<>();
    
    public TestGroup() {
    	members.add(new TestPrincipal());
    }
	@Override
	public String getName() {
		return "testGroup";
	}

	@Override
	public boolean addMember(Principal user) {
		return false;
	}

	@Override
	public boolean removeMember(Principal user) {
		return false;
	}

	@Override
	public boolean isMember(Principal member) {
		return false;
	}

	@Override
	public Enumeration<? extends Principal> members() {
		return members.elements();
	}

}
