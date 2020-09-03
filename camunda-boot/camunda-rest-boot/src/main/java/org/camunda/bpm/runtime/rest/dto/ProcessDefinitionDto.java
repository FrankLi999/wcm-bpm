package org.camunda.bpm.runtime.rest.dto;

import java.util.List;

public class ProcessDefinitionDto {
	protected String id;
	protected String name;
	protected String key;
	protected long version;
	protected List<String> calledFromActivityIds;

	protected int failedJobs;

	public ProcessDefinitionDto() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public List<String> getCalledFromActivityIds() {
		return calledFromActivityIds;
	}

	public void setCalledFromActivityIds(List<String> calledFromActivityIds) {
		this.calledFromActivityIds = calledFromActivityIds;
	}
}
