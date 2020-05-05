package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;
import java.util.Map;

public class ObjectConstraint {
	private Integer minProperties;
	private Integer maxProperties;
	private String propertyNamePattern;
	private Boolean allowAdditionalProperties;
	private String[] required;
	private Map<String, FormControl> additionalProperties;
	private Map<String, String[]> dependencies;
	private Map<String, FormControl> patternProperties;
	private Map<String, FormControl> schemaDependencies;
	public Integer getMinProperties() {
		return minProperties;
	}
	public void setMinProperties(Integer minProperties) {
		this.minProperties = minProperties;
	}
	public Integer getMaxProperties() {
		return maxProperties;
	}
	public void setMaxProperties(Integer maxProperties) {
		this.maxProperties = maxProperties;
	}
	public String getPropertyNamePattern() {
		return propertyNamePattern;
	}
	public void setPropertyNamePattern(String propertyNamePattern) {
		this.propertyNamePattern = propertyNamePattern;
	}
	public Boolean getAllowAdditionalProperties() {
		return allowAdditionalProperties;
	}
	public void setAllowAdditionalProperties(Boolean allowAdditionalProperties) {
		this.allowAdditionalProperties = allowAdditionalProperties;
	}
	public Map<String, FormControl> getAdditionalProperties() {
		return additionalProperties;
	}
	public void setAdditionalProperties(Map<String, FormControl> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	public Map<String, String[]> getDependencies() {
		return dependencies;
	}
	public void setDependencies(Map<String, String[]> dependencies) {
		this.dependencies = dependencies;
	}
	public Map<String, FormControl> getPatternProperties() {
		return patternProperties;
	}
	public void setPatternProperties(Map<String, FormControl> patternProperties) {
		this.patternProperties = patternProperties;
	}
	public Map<String, FormControl> getSchemaDependencies() {
		return schemaDependencies;
	}
	public void setSchemaDependencies(Map<String, FormControl> schemaDependencies) {
		this.schemaDependencies = schemaDependencies;
	}
	public String[] getRequired() {
		return required;
	}
	public void setRequired(String[] required) {
		this.required = required;
	}
	@Override
	public String toString() {
		return "ObjectConstraint [minProperties=" + minProperties + ", maxProperties=" + maxProperties
				+ ", propertyNamePattern=" + propertyNamePattern + ", allowAdditionalProperties="
				+ allowAdditionalProperties + ", required=" + Arrays.toString(required) + ", additionalProperties="
				+ additionalProperties + ", dependencies=" + dependencies + ", patternProperties=" + patternProperties
				+ ", schemaDependencies=" + schemaDependencies + "]";
	}
}
