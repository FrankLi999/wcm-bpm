package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class JavascriptFunction {
	private String name; //validateFn, onBlur, onClick
	private String params[];
	private String functionBody;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getParams() {
		return params;
	}
	public void setParams(String[] params) {
		this.params = params;
	}
	public String getFunctionBody() {
		return functionBody;
	}
	public void setFunctionBody(String functionBody) {
		this.functionBody = functionBody;
	}
	@Override
	public String toString() {
		return "JavascriptFunction [name=" + name + ", params=" + Arrays.toString(params) + ", functionBody="
				+ functionBody + "]";
	}
}
