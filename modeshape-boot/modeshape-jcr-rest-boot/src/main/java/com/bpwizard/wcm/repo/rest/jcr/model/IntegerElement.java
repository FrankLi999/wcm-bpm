package com.bpwizard.wcm.repo.rest.jcr.model;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class IntegerElement extends BaseContentItemElement {
    private Long value;

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}
	public ObjectNode toJson(ObjectNode elementsNode) {return null;}
	@Override
	public String toString() {
		return "IntegerElement [value=" + value + ", getName()=" + getName() + ", getType()=" + getType() + "]";
	}
}
