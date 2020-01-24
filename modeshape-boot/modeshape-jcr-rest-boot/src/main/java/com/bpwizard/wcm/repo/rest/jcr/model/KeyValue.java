package com.bpwizard.wcm.repo.rest.jcr.model;

public class KeyValue {
	private String name;
	private String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "KeyValue [name=" + name + ", value=" + value + "]";
	}
}
