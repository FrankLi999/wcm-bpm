package com.bpwizard.wcm.repo.rest.jcr.model;

public class NumberConstraint {
	private Integer multipleOf;
	private Boolean exclusiveMaximum;
	private Integer maximum;
	private Boolean exclusiveMinimum;
	private Integer minimum;
	public Integer getMultipleOf() {
		return multipleOf;
	}
	public void setMultipleOf(Integer multipleOf) {
		this.multipleOf = multipleOf;
	}
	public Boolean getExclusiveMaximum() {
		return exclusiveMaximum;
	}
	public void setExclusiveMaximum(Boolean exclusiveMaximum) {
		this.exclusiveMaximum = exclusiveMaximum;
	}
	public Integer getMaximum() {
		return maximum;
	}
	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}
	public Boolean getExclusiveMinimum() {
		return exclusiveMinimum;
	}
	public void setExclusiveMinimum(Boolean exclusiveMinimum) {
		this.exclusiveMinimum = exclusiveMinimum;
	}
	public Integer getMinimum() {
		return minimum;
	}
	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}
	@Override
	public String toString() {
		return "NumberConstraint [multipleOf=" + multipleOf + ", exclusiveMaximum=" + exclusiveMaximum + ", maximum="
				+ maximum + ", exclusiveMinimum=" + exclusiveMinimum + ", minimum=" + minimum + "]";
	}
}
