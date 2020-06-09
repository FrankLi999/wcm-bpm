package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class CustomConstraint {
	private JavascriptFunction javascriptFunction[];

	public JavascriptFunction[] getJavascriptFunction() {
		return javascriptFunction;
	}

	public void setJavascriptFunction(JavascriptFunction[] javascriptFunction) {
		this.javascriptFunction = javascriptFunction;
	}

	@Override
	public String toString() {
		return "CustomConstraint [javascriptFunction=" + Arrays.toString(javascriptFunction) + "]";
	}
}
