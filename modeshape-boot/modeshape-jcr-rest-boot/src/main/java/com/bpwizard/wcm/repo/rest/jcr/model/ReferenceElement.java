package com.bpwizard.wcm.repo.rest.jcr.model;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ReferenceElement extends BaseContentItemElement {
	private String reference;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	public ObjectNode toJson(ObjectNode elementsNode) {return null;}
	@Override
	public String toString() {
		return "ReferenceElement [reference=" + reference + ", getName()=" + getName() + ", getType()=" + getType()
				+ "]";
	}
}
