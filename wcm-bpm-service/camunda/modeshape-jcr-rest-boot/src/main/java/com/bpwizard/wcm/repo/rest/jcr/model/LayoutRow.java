package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class LayoutRow {
	private LayoutColumn columns[];

	public LayoutColumn[] getColumns() {
		return columns;
	}

	public void setColumns(LayoutColumn[] columns) {
		this.columns = columns;
	}

	@Override
	public String toString() {
		return "LayoutRow [columns=" + Arrays.toString(columns) + ", toString()=" + super.toString() + "]";
	}
}
