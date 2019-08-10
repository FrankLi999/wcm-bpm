package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class FormColumn {
	private String id;
	private int fxFlex;
    private String[] formControls;
    private CustomFieldLayout customeFieldLayouts[];
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
	public CustomFieldLayout[] getCustomeFieldLayouts() {
		return customeFieldLayouts;
	}
	public void setCustomeFieldLayouts(CustomFieldLayout[] customeFieldLayouts) {
		this.customeFieldLayouts = customeFieldLayouts;
	}
	@Override
	public String toString() {
		return "FormColumn [id=" + id + ", fxFlex=" + fxFlex + ", formControls=" + Arrays.toString(formControls)
				+ ", customeFieldLayouts=" + Arrays.toString(customeFieldLayouts) + "]";
	}
}
