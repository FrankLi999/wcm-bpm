package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class RenderTemplateLayoutRow {
	private RenderTemplateLayoutColumn columns[];

	public RenderTemplateLayoutColumn[] getColumns() {
		return columns;
	}

	public void setColumns(RenderTemplateLayoutColumn[] columns) {
		this.columns = columns;
	}

	@Override
	public String toString() {
		return "RenderTemplateLayoutRow [columns=" + Arrays.toString(columns) + ", toString()=" + super.toString() + "]";
	}
}
