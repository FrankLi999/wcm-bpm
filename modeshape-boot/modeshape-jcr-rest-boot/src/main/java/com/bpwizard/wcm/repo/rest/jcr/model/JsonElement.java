package com.bpwizard.wcm.repo.rest.jcr.model;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonElement extends BaseContentItemElement {
	private ObjectNode value;

	public ObjectNode getValue() {
		return value;
	}

	public void setValue(ObjectNode value) {
		this.value = value;
	}
	public ObjectNode toJson(ObjectNode elementsNode) {return null;}
	@Override
	public String toString() {
		return "JsonElement [value=" + value + ", getName()=" + getName() + ", getType()=" + getType() + "]";
	}
}
