package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.List;
import java.util.Map;

public class QueryResult {
	List<Map<String, ColumnValue>> rows;

	public List<Map<String, ColumnValue>> getRows() {
		return rows;
	}

	public void setRows(List<Map<String, ColumnValue>> rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "QueryResult [rows=" + rows + "]";
	}
}
