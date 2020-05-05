package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class BinaryElement extends BaseContentItemElement {
	private byte[] value;
	private String encoding;
	public ObjectNode toJson(ObjectNode elementsNode) {return null;}
	@Override
	public String toString() {
		return "BinaryElement [value=" + Arrays.toString(value) + ", encoding=" + encoding + ", getName()=" + getName()
				+ ", getType()=" + getType() + "]";
	}
}
