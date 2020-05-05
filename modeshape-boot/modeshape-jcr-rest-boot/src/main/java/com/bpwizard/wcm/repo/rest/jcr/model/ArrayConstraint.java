package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Map;

public class ArrayConstraint {
	private Integer minItems;
	private Integer maxItems;
	private Boolean uniqueItems;
	
	// private String items; //Items type
	private String contains; //contains type
	private Map<String, FormControl> additionalItems;
	public Integer getMinItems() {
		return minItems;
	}
	public void setMinItems(Integer minItems) {
		this.minItems = minItems;
	}
	public Integer getMaxItems() {
		return maxItems;
	}
	public void setMaxItems(Integer maxItems) {
		this.maxItems = maxItems;
	}
	public Boolean getUniqueItems() {
		return uniqueItems;
	}
	public void setUniqueItems(Boolean uniqueItems) {
		this.uniqueItems = uniqueItems;
	}

	public String getContains() {
		return contains;
	}
	public void setContains(String contains) {
		this.contains = contains;
	}
	public Map<String, FormControl> getAdditionalItems() {
		return additionalItems;
	}
	public void setAdditionalItems(Map<String, FormControl> additionalItems) {
		this.additionalItems = additionalItems;
	}
	@Override
	public String toString() {
		return "ArrayConstraint [minItems=" + minItems + ", maxItems=" + maxItems + ", uniqueItems=" + uniqueItems
				+ ", contains=" + contains + ", additionalItems=" + additionalItems + "]";
	}
}
