package com.bpwizard.wcm.repo.rest.jcr.model;

public class ColumnValue {
	private int type;
	private Object value;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "ColumnValue [type=" + type + ", value=" + value + "]";
	}
	
	
}
