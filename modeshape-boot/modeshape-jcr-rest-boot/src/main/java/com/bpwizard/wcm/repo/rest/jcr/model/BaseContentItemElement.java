package com.bpwizard.wcm.repo.rest.jcr.model;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class BaseContentItemElement {
   private String name;
   private boolean multiple;
   private String type; //bool, number, integer, string, binary, reference, json
   
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public ObjectNode toJson(ObjectNode elementsNode) {
		ObjectNode elementNode = JsonUtils.createObjectNode(); 
		elementNode.set("name", elementNode);
		elementNode.put("bpw:name", name);
		elementNode.put("bpw:type", type);
		return elementNode;
	}

	@Override
	public String toString() {
		return "BaseContentItemElement [name=" + name + ", multiple=" + multiple + ", type=" + type + "]";
	}
}
