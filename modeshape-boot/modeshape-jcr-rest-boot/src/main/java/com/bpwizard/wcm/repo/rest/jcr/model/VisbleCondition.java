package com.bpwizard.wcm.repo.rest.jcr.model;

public class VisbleCondition {
	private String functionBody;
	private String condition;
	public String getFunctionBody() {
		return functionBody;
	}
	public void setFunctionBody(String functionBody) {
		this.functionBody = functionBody;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	@Override
	public String toString() {
		return "VisbleCondition [functionBody=" + functionBody + ", condition=" + condition + "]";
	}
}
