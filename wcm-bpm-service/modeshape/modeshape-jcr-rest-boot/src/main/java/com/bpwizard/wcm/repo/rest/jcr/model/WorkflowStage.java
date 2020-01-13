package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class WorkflowStage {
	private int order;
	private String name;
	private WorkflowAction entryActions[];
	private WorkflowAction exitActions[];
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
	public WorkflowAction[] getEntryActions() {
		return entryActions;
	}
	public void setEntryActions(WorkflowAction[] entryActions) {
		this.entryActions = entryActions;
	}
	public WorkflowAction[] getExitActions() {
		return exitActions;
	}
	public void setExitActions(WorkflowAction[] exitActions) {
		this.exitActions = exitActions;
	}
	@Override
	public String toString() {
		return "WorkflowStage [order=" + order + ", name=" + name + ", entryActions=" + Arrays.toString(entryActions)
				+ ", exitActions=" + Arrays.toString(exitActions) + ", toString()=" + super.toString() + "]";
	}
}
