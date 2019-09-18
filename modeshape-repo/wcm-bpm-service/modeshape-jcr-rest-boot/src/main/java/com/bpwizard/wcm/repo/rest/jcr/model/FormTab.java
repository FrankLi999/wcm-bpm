package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class FormTab {
	private String tabName;
	private String tabTitle;
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
	public String getTabTitle() {
		return tabTitle;
	}
	public void setTabTitle(String tabTitle) {
		this.tabTitle = tabTitle;
	}
	@Override
	public String toString() {
		return "FormTab [tabName=" + tabName + ", tabTitle=" + tabTitle + ", formGroups=" + Arrays.toString(formGroups)
				+ "]";
	}
}
