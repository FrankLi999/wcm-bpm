package com.bpwizard.wcm.repo.rest.jcr.model;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class StringElement extends BaseContentItemElement {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public ObjectNode toJson(ObjectNode elementsNode) {return null;}
	@Override
	public String toString() {
		return "StringElement [value=" + value + ", getName()=" + getName() + ", getType()=" + getType() + "]";
	}
}
