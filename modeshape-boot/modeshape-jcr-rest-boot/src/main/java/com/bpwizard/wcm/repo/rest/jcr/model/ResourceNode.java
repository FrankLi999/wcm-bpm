package com.bpwizard.wcm.repo.rest.jcr.model;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ResourceNode extends ResourceMixin implements Serializable {
	private static final long serialVersionUID = 1810430835299134939L;
	private String lockOwner;
	private AccessControlEntry acl;
	
	public String getLockOwner() {
		return lockOwner;
	}
	public void setLockOwner(String lockOwner) {
		this.lockOwner = lockOwner;
	}
	public AccessControlEntry getAcl() {
		return acl;
	}
	public void setAcl(AccessControlEntry acl) {
		this.acl = acl;
	}
	
	protected void toJson(ObjectNode jsonNode, ObjectNode children) {		
//		if (StringUtils.hasText(this.getTitle())) {
//			jsonNode.put("bpw:title", this.getTitle());
//		}
//		
//		if (StringUtils.hasText(this.getDescription())) {
//			jsonNode.put("bpw:description", this.getDescription());
//		}
		
		if (this.getAcl() != null) {
			this.addAccessControlEntry(children, "bpw:acl", this.getAcl());
		}
	}
	
	public void addAccessControlEntry(ObjectNode children, String property, AccessControlEntry aclEntry) {
		ObjectNode aclEntryNode = JsonUtils.createObjectNode();
		children.set(property, aclEntryNode);		
		aclEntry.toJson(aclEntryNode);
	}
	
	@Override
	public String toString() {
		return "ResourceNode [lockOwner=" + lockOwner + ", acl=" + acl + ", getName()=" + getName() + ", getTitle()="
				+ getTitle() + ", getDescription()=" + getDescription() + "]";
	}
}
