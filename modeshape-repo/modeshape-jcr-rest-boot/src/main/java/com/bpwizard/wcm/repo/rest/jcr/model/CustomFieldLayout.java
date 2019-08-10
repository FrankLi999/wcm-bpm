package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class CustomFieldLayout {
	private String name;
	private boolean multiple;
	private FieldLayout fieldLayouts[];
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isMultiple() {
		return multiple;
	}
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}
	public FieldLayout[] getFieldLayouts() {
		return fieldLayouts;
	}
	public void setFieldLayouts(FieldLayout[] fieldLayouts) {
		this.fieldLayouts = fieldLayouts;
	}
	@Override
	public String toString() {
		return "CustomFieldLayout [name=" + name + ", multiple=" + multiple + ", fieldLayouts="
				+ Arrays.toString(fieldLayouts) + "]";
	}
}
