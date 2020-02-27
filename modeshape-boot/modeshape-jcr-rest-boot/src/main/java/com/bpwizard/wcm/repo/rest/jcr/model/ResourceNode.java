package com.bpwizard.wcm.repo.rest.jcr.model;

import java.io.Serializable;

import org.modeshape.jcr.api.JcrConstants;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ResourceNode implements Serializable {
	private static final long serialVersionUID = 1810430835299134939L;
	private String title;
	private String description;
	private String lockOwner;
	private AccessControlEntry acl;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
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
		
		if (StringUtils.hasText(this.getTitle())) {
			jsonNode.put("bpw:title", this.getTitle());
		}
		
		if (StringUtils.hasText(this.getDescription())) {
			jsonNode.put("bpw:description", this.getDescription());
		}
		
		if (this.getAcl() != null) {
			this.addAccessControlEntry(children, "bpw:acl", this.getAcl());
		}
	}
	
	protected void addAccessControlEntry(ObjectNode contentItemAclNodeChildren, String entryName, AccessControlEntry aclEntry) {
		if (aclEntry == null) {
			return;
		}
		ObjectNode aclEntryNode = JsonUtils.createObjectNode();
		contentItemAclNodeChildren.set(entryName, aclEntryNode);
		
		
		aclEntryNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:AccessControlEntry");
		
		if (aclEntry.getViewers() != null && aclEntry.getViewers().length > 0) {
			ArrayNode valueArray = JsonUtils.creatArrayNode();
			for (String value : aclEntry.getViewers()) {
				valueArray.add(value);
			}
			aclEntryNode.set("bpw:viewers", valueArray);
		}
		
		if (aclEntry.getEditors() != null && aclEntry.getEditors().length > 0) {
			ArrayNode valueArray = JsonUtils.creatArrayNode();
			for (String value : aclEntry.getEditors()) {
				valueArray.add(value);
			}
			aclEntryNode.set("bpw:editors", valueArray);
		}
		
		if (aclEntry.getAdmins() != null && aclEntry.getAdmins().length > 0) {
			ArrayNode valueArray = JsonUtils.creatArrayNode();
			for (String value : aclEntry.getAdmins()) {
				valueArray.add(value);
			}
			aclEntryNode.set("bpw:admins", valueArray);
		}
		
		if (aclEntry.getReviewers() != null && aclEntry.getReviewers().length > 0) {
			ArrayNode valueArray = JsonUtils.creatArrayNode();
			for (String value : aclEntry.getReviewers()) {
				valueArray.add(value);
			}
			aclEntryNode.set("bpw:reviewers", valueArray);
		}		
	}
	
	@Override
	public String toString() {
		return "ResourceNode [title=" + title + ", description=" + description + ", lockOwner=" + lockOwner + ", acl="
				+ acl + "]";
	}
}
