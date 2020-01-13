package com.bpwizard.wcm.repo.rest.jcr.model;

public class WorkflowAction {
	private int order;
	private String name;
	private boolean entry;
	private String referenceId;
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEntry() {
		return entry;
	}
	public void setEntry(boolean entry) {
		this.entry = entry;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	@Override
	public String toString() {
		return "WorkflowAction [order=" + order + ", name=" + name + ", entry=" + entry + ", referenceId=" + referenceId
				+ "]";
	}
}
