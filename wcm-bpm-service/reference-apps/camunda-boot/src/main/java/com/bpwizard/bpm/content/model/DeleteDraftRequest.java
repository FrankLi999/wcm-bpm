package com.bpwizard.bpm.content.model;

public class DeleteDraftRequest {
	private String contentId;
	private String workflow;

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	@Override
	public String toString() {
		return "DeleteDraftRequest [contentId=" + contentId + ", workflow=" + workflow + "]";
	}
}
