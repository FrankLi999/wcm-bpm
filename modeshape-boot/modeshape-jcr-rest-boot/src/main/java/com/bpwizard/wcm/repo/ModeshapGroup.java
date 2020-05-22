package com.bpwizard.wcm.repo;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;

@SuppressWarnings("removal")
public class ModeshapGroup implements Group {
    private String group;
    
   public ModeshapGroup(String group) {
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
