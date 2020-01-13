package com.bpwizard.wcm.repo.rest.jcr.model;

public class AccessControlList {
	public static final String ANONYMOUS_USER = "anonymous";
	
	private AccessControlEntry onSaveDraftPermissions;
	private AccessControlEntry onRejectDraftPermissions;
	private AccessControlEntry onPublishPermissions;
	
	public AccessControlEntry getOnSaveDraftPermissions() {
		return onSaveDraftPermissions;
	}
	public void setOnSaveDraftPermissions(AccessControlEntry onSaveDraftPermissions) {
		this.onSaveDraftPermissions = onSaveDraftPermissions;
	}
	public AccessControlEntry getOnRejectDraftPermissions() {
		return onRejectDraftPermissions;
	}
	public void setOnRejectDraftPermissions(AccessControlEntry onRejectDraftPermissions) {
		this.onRejectDraftPermissions = onRejectDraftPermissions;
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
				+ onRejectDraftPermissions + ", onPublishPermissions=" + onPublishPermissions + "]";
	}
}
