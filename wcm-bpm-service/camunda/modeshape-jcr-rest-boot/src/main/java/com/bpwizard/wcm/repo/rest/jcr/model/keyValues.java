package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class keyValues {
	KeyValue keyValues[];

	public KeyValue[] getKeyValues() {
		return this.keyValues;
	}

	public void setKeyValue(KeyValue[] keyValues) {
		this.keyValues = keyValues;
	}

	@Override
	public String toString() {
		return "keyValues [KeyValue=" + Arrays.toString(keyValues) + "]";
	}
}
