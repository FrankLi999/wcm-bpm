package com.bpwizard.wcm.repo.rest.jcr.model;

public class FormRow extends BaseFormGroup {
	private FormColumn[] columns;

	public FormColumn[] getColumns() {
		return columns;
	}
	public void setColumns(FormColumn[] columns) {
		this.columns = columns;
	}
}
