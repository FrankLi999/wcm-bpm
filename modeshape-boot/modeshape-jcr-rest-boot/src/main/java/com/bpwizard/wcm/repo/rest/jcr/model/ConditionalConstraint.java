package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Map;

public class ConditionalConstraint {
	private Map<String, FormControl> ifCondition;
	private Map<String, FormControl> thenRule;
	private Map<String, FormControl> elseRule;
	public Map<String, FormControl> getIfCondition() {
		return ifCondition;
	}
	public void setIfCondition(Map<String, FormControl> ifCondition) {
		this.ifCondition = ifCondition;
	}
	public Map<String, FormControl> getThenRule() {
		return thenRule;
	}
	public void setThenRule(Map<String, FormControl> thenRule) {
		this.thenRule = thenRule;
	}
	public Map<String, FormControl> getElseRule() {
		return elseRule;
	}
	public void setElseRule(Map<String, FormControl> elseRule) {
		this.elseRule = elseRule;
	}
	@Override
	public String toString() {
		return "ConditionalConstraint [ifCondition=" + ifCondition + ", thenRule=" + thenRule + ", elseRule=" + elseRule
				+ "]";
	}
}
