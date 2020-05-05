package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class FormColumn {
	private String id;
	private int fxFlex;
	private int order;
	private boolean isArray = false;
	protected String filedPath;
    private String[] formControls;
    private BaseFormGroup[] formGroups;
	private VisbleCondition condition;
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
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public BaseFormGroup[] getFormGroups() {
		return formGroups;
	}
	public void setFormGroups(BaseFormGroup[] formGroups) {
		this.formGroups = formGroups;
	}
	public boolean isArray() {
		return isArray;
	}
	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}
	public String getFiledPath() {
		return filedPath;
	}
	public void setFiledPath(String filedPath) {
		this.filedPath = filedPath;
	}
	public VisbleCondition getCondition() {
		return condition;
	}
	public void setCondition(VisbleCondition condition) {
		this.condition = condition;
	}
	@Override
	public String toString() {
		return "FormColumn [id=" + id + ", fxFlex=" + fxFlex + ", order=" + order + ", isArray=" + isArray
				+ ", filedPath=" + filedPath + ", formControls=" + Arrays.toString(formControls) + ", formGroups="
				+ Arrays.toString(formGroups) + ", condition=" + condition + "]";
	}
}
