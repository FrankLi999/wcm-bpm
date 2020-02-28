package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;

public class Workflow extends ResourceNode {
	private static final long serialVersionUID = 1L;
	
	private String repository;
	private String workspace;
	private String library;
	
	private String name;
	// private String businessKey;
	private String bpmn;
	private WorkflowStage workflowStages[];
	
	
	public String getRepository() {
		return repository;
	}
	
	public void setRepository(String repository) {
		this.repository = repository;
	}
	
	public String getWorkspace() {
		return workspace;
	}
	
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
	
	public String getLibrary() {
		return library;
	}
	
	public void setLibrary(String library) {
		this.library = library;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public WorkflowStage[] getWorkflowStages() {
		return workflowStages;
	}
	
	public void setWorkflowStages(WorkflowStage[] workflowStages) {
		this.workflowStages = workflowStages;
	}
	
	public String getBpmn() {
		return bpmn;
	}
	
	public void setBpmn(String bpmn) {
		this.bpmn = bpmn;
	}
	
	public JsonNode toJson() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Workflow [repository=" + repository + ", workspace=" + workspace + ", library=" + library + ", name="
				+ name + ", bpmn=" + bpmn + ", workflowStages="
				+ Arrays.toString(workflowStages) + ", toString()=" + super.toString() + "]";
	}
}
