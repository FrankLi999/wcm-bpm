package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class CommonConstraint {
	private String[] enumeration;
	private String[] defaultValues;
	private String constant;

	public String[] getEnumeration() {
		return enumeration;
	}
	public void setEnumeration(String[] enumeration) {
		this.enumeration = enumeration;
	}
	public String[] getDefaultValues() {
		return defaultValues;
	}
	public void setDefaultValues(String[] defaultValues) {
		this.defaultValues = defaultValues;
	}
	public String getConstant() {
		return constant;
	}
	public void setConstant(String constant) {
		this.constant = constant;
	}
	@Override
	public String toString() {
		return "CommonConstraint [enumeration=" + Arrays.toString(enumeration) + ", defaultValues="
				+ Arrays.toString(defaultValues) + ", constant=" + constant + "]";
	}
}
