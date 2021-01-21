package com.bpwizard.wcm.repo.rest.jcr.model;

import java.security.Principal;
import java.util.Enumeration;

import com.bpwizard.spring.boot.commons.security.Group;

public class ModeshapeGroup implements Group {
	private String group;
    
    public ModeshapeGroup(String group) {
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
		return null;
	}

}
