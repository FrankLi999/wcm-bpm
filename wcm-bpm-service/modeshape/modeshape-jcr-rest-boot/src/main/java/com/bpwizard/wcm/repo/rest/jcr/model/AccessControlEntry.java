package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class AccessControlEntry {
	
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
	
	@Override
	public String toString() {
		return "ContentItemAccessControlEntry [viewers=" + Arrays.toString(viewers) + ", editors="
				+ Arrays.toString(editors) + ", admins=" + Arrays.toString(admins) + ", reviewers="
				+ Arrays.toString(reviewers) + "]";
	}
}