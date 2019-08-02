package com.bpwizard.wcm.repo.rest.jcr.model;

public class FormTab {
	private String tabName;
	private BaseFormGroup[] formGroups;
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
}
