package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class FormTab {
	private String tabName;
	private String tabTitle;
	private int order;
	private BaseFormGroup[] formGroups;
	private VisbleCondition condition;
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	public BaseFormGroup[] getFormGroups() {
		return formGroups;
	}
	public void setFormGroups(BaseFormGroup[] formGroups) {
		this.formGroups = formGroups;
	}
	public String getTabTitle() {
		return tabTitle;
	}
	public void setTabTitle(String tabTitle) {
		this.tabTitle = tabTitle;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public VisbleCondition getCondition() {
		return condition;
	}
	public void setCondition(VisbleCondition condition) {
		this.condition = condition;
	}
	@Override
	public String toString() {
		return "FormTab [tabName=" + tabName + ", tabTitle=" + tabTitle + ", order=" + order + ", formGroups="
				+ Arrays.toString(formGroups) + ", condition=" + condition + "]";
	}
}
