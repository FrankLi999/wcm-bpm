package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class ComposedConstraint {
	private String name;
	FormControl constraints[];
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FormControl[] getConstraints() {
		return constraints;
	}
	public void setConstraints(FormControl[] constraints) {
		this.constraints = constraints;
	}
	@Override
	public String toString() {
		return "ComposedConstraint [name=" + name + ", constraints=" + Arrays.toString(constraints) + "]";
	}
}
