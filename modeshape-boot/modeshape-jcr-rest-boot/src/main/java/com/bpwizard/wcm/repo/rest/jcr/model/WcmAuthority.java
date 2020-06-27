package com.bpwizard.wcm.repo.rest.jcr.model;

public class WcmAuthority {
	private boolean creator = true;
	private boolean editor = true;
	private boolean reviewer = true;
	private boolean admin = true;
	public boolean isCreator() {
		return creator;
	}
	public void setCreator(boolean creator) {
		this.creator = creator;
	}
	public boolean isEditor() {
		return editor;
	}
	public void setEditor(boolean editor) {
		this.editor = editor;
	}
	public boolean isReviewer() {
		return reviewer;
	}
	public void setReviewer(boolean reviewer) {
		this.reviewer = reviewer;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	@Override
	public String toString() {
		return "WcmAuthority [creator=" + creator + ", editor=" + editor + ", reviewer=" + reviewer + ", admin=" + admin
				+ "]";
	}
}
