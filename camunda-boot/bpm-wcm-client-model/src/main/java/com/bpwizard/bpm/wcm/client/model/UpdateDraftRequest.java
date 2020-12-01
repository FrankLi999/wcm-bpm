package com.bpwizard.bpm.wcm.client.model;

public class UpdateDraftRequest {
	private String contentId;

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	@Override
	public String toString() {
		return "UpdateDraftRequest [contentId=" + contentId + "]";
	}
}
