package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class FormColumn {
	private String id;
	private int fxFlex;
    private String[] formControls;
    private FieldLayout fieldLayouts[];
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getFxFlex() {
		return fxFlex;
	}
	public void setFxFlex(int fxFlex) {
		this.fxFlex = fxFlex;
	}
	public String[] getFormControls() {
		return formControls;
	}
	public void setFormControls(String[] formControls) {
		this.formControls = formControls;
	}

	public FieldLayout[] getFieldLayouts() {
		return fieldLayouts;
	}
	public void setFieldLayouts(FieldLayout[] fieldLayouts) {
		this.fieldLayouts = fieldLayouts;
	}
	@Override
	public String toString() {
		return "FormColumn [id=" + id + ", fxFlex=" + fxFlex + ", formControls=" + Arrays.toString(formControls)
				+ ", fieldLayouts=" + Arrays.toString(fieldLayouts) + "]";
	}
}
