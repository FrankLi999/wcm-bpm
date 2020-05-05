package com.bpwizard.wcm.repo.rest.jcr.model;

import java.io.Serializable;
import java.util.Arrays;

import org.modeshape.jcr.api.JcrConstants;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AccessControlEntry implements Serializable {
	
	private static final long serialVersionUID = -3372806515417478914L;
	private String viewers[];
	private String editors[];
	private String admins[];
	private String reviewers[];
	
	public String[] getViewers() {
		return viewers;
	}
	public void setViewers(String[] viewers) {
		this.viewers = viewers;
	}
	public String[] getEditors() {
		return editors;
	}
	public void setEditors(String[] editors) {
		this.editors = editors;
	}
	public String[] getAdmins() {
		return admins;
	}
	public void setAdmins(String[] admins) {
		this.admins = admins;
	}
	public String[] getReviewers() {
		return reviewers;
	}
	public void setReviewers(String[] reviewers) {
		this.reviewers = reviewers;
	}
	
	public void toJson(ObjectNode aclEntryNode) {
		aclEntryNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:AccessControlEntry");
		
		if (this.getViewers() != null && this.getViewers().length > 0) {
			ArrayNode valueArray = JsonUtils.creatArrayNode();
			for (String value : this.getViewers()) {
				valueArray.add(value);
			}
			aclEntryNode.set("bpw:viewers", valueArray);
		}
		
		if (this.getEditors() != null && this.getEditors().length > 0) {
			ArrayNode valueArray = JsonUtils.creatArrayNode();
			for (String value : this.getEditors()) {
				valueArray.add(value);
			}
			aclEntryNode.set("bpw:editors", valueArray);
		}
		
		if (this.getAdmins() != null && this.getAdmins().length > 0) {
			ArrayNode valueArray = JsonUtils.creatArrayNode();
			for (String value : this.getAdmins()) {
				valueArray.add(value);
			}
			aclEntryNode.set("bpw:admins", valueArray);
		}
		
		if (this.getReviewers() != null && this.getReviewers().length > 0) {
			ArrayNode valueArray = JsonUtils.creatArrayNode();
			for (String value : this.getReviewers()) {
				valueArray.add(value);
			}
			aclEntryNode.set("bpw:reviewers", valueArray);
		}		
	}
	
	@Override
	public String toString() {
		return "ContentItemAccessControlEntry [viewers=" + Arrays.toString(viewers) + ", editors="
				+ Arrays.toString(editors) + ", admins=" + Arrays.toString(admins) + ", reviewers="
				+ Arrays.toString(reviewers) + "]";
	}
}