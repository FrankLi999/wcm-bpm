package com.bpwizard.wcm.repo.rest.jcr.model;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;

public class WcmGroup implements Group {
	java.util.Vector<Principal> members = new java.util.Vector<>();
    private String group;
    
    public WcmGroup(String group) {
    	this.group = group;
    }
	@Override
	public String getName() {
		return group;
	}

	public String getGroup() {
		return group;
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
