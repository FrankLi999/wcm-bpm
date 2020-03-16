package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class WcmProperties {
	WcmProperty properties[];

	public WcmProperty[] getProperties() {
		return this.properties;
	}

	public void setProperties(WcmProperty[] properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "WcmProperties [properties=" + Arrays.toString(properties) + "]";
	}

}
