package com.bpwizard.wcm.repo.rest.jcr.model;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class BoolElement extends BaseContentItemElement {
    private Boolean value;

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}
	public ObjectNode toJson(ObjectNode elementsNode) {return null;}
	@Override
	public String toString() {
		return "BoolElement [value=" + value + ", getName()=" + getName() + ", getType()=" + getType() + "]";
	}
}
