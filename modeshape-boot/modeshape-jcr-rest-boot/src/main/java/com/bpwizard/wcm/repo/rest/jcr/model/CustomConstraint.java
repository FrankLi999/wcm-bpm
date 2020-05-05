package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class CustomConstraint {
	private JavascriptFunction functions[];

	public JavascriptFunction[] getFunctions() {
		return functions;
	}

	public void setFunctions(JavascriptFunction[] functions) {
		this.functions = functions;
	}

	@Override
	public String toString() {
		return "CustomConstraint [functions=" + Arrays.toString(functions) + "]";
	}
}
