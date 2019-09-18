package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class FormTabs extends BaseFormGroup {
	private FormTab[] tabs;

	public FormTab[] getTabs() {
		return tabs;
	}

	public void setTabs(FormTab[] tabs) {
		this.tabs = tabs;
	}

	@Override
	public String toString() {
		return "FormTabs [tabs=" + Arrays.toString(tabs) + "]";
	}
}
