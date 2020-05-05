package com.bpwizard.wcm.repo.rest.jcr.model;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class NumberElement extends BaseContentItemElement {
	private Double value;
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
	public ObjectNode toJson(ObjectNode elementsNode) {return null;}
	@Override
	public String toString() {
		return "NumberElement [value=" + value + ", getName()=" + getName() + ", getType()=" + getType() + "]";
	}
}
