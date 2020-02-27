package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class Workflow extends ResourceNode {
	private static final long serialVersionUID = 1L;
	private String name;
	private String businessKey;
	private WorkflowStage workflowStages[];
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public WorkflowStage[] getWorkflowStages() {
		return workflowStages;
	}
	public void setWorkflowStages(WorkflowStage[] workflowStages) {
		this.workflowStages = workflowStages;
	}
	@Override
	public String toString() {
		return "Workflow [name=" + name + ", businessKey=" + businessKey + ", workflowStages="
				+ Arrays.toString(workflowStages) + ", toString()=" + super.toString() + "]";
	}
}
