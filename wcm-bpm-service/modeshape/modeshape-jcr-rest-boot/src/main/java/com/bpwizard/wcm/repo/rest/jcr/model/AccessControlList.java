package com.bpwizard.wcm.repo.rest.jcr.model;

public class AccessControlList {
	public static final String ANONYMOUS_USER = "anonymous";
	
	private AccessControlEntry onSaveDraftPermissions;
	private AccessControlEntry onReviewedDraftPermissions;
	private AccessControlEntry onPublishPermissions;
	
	public AccessControlEntry getOnSaveDraftPermissions() {
		return onSaveDraftPermissions;
	}
	public void setOnSaveDraftPermissions(AccessControlEntry onSaveDraftPermissions) {
		this.onSaveDraftPermissions = onSaveDraftPermissions;
	}
	public AccessControlEntry getOnReviewedDraftPermissions() {
		return onReviewedDraftPermissions;
	}
	public void setOnReviewedDraftPermissions(AccessControlEntry onReviewedDraftPermissions) {
		this.onReviewedDraftPermissions = onReviewedDraftPermissions;
	}
	public AccessControlEntry getOnPublishPermissions() {
		return onPublishPermissions;
	}
	public void setOnPublishPermissions(AccessControlEntry onPublishPermissions) {
		this.onPublishPermissions = onPublishPermissions;
	}
	
	@Override
	public String toString() {
		return "ContentItemACL [onSaveDraftPermissions=" + onSaveDraftPermissions + ", onRejectDraftPermissions="
				+ onReviewedDraftPermissions + ", onReviewedPermissions=" + onPublishPermissions + "]";
	}
}
