package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class FormRows extends BaseFormGroup {
	private FormRow[] rows;

	public FormRow[] getRows() {
		return rows;
	}

	public void setRows(FormRow[] rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "FormRows [rows=" + Arrays.toString(rows) + "]";
	}
}
