package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class FormRow extends BaseFormGroup {
	private String rowName;
	private String rowTitle;
	private int order;
	private FormColumn[] columns;

	public FormColumn[] getColumns() {
		return columns;
	}
	
	public void setColumns(FormColumn[] columns) {
		this.columns = columns;
	}

	public String getRowName() {
		return rowName;
	}

	public void setRowName(String rowName) {
		this.rowName = rowName;
	}

	public String getRowTitle() {
		return rowTitle;
	}

	public void setRowTitle(String rowTitle) {
		this.rowTitle = rowTitle;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "FormRow [rowName=" + rowName + ", rowTitle=" + rowTitle + ", order=" + order + ", columns="
				+ Arrays.toString(columns) + ", toString()=" + super.toString() + "]";
	}
}
