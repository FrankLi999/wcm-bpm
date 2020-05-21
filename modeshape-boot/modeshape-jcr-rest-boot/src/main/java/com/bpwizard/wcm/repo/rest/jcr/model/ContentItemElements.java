package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ContentItemElements {
	private Map<String, Object> elements = new HashMap<>();

	public Map<String, Object> getElements() {
		return elements;
	}

	public void setElements(Map<String, Object> elements) {
		this.elements = elements;
	}

	@JsonAnySetter
    void setElement(String key, Object value) {
        elements.put(key, value);
    }
	
	@SuppressWarnings("unchecked")
	public void toJson(ObjectNode elementsNode, ObjectNode elementsChildren, AuthoringTemplate at) throws JsonProcessingException {
		for (String elementName: elements.keySet()) {
			FormControl formControl = at.getElements().get(elementName);
			// JsonNode jsonNode= getElements().get(elementName);
			Object value = getElements().get(elementName);
			if ("integer".equals(formControl.getDataType())) {
				if (formControl.isMultiple()) {					
					elementsNode.set(elementName, WcmUtils.toArrayNode((List<Integer>)value));
				} else {
					elementsNode.put(elementName, (Integer)value);
				}
			} else if ("boolean".equals(formControl.getDataType())) {
				if (formControl.isMultiple()) {					
					elementsNode.set(elementName, WcmUtils.toArrayNode((List<Boolean>)value));
				} else {
					elementsNode.put(elementName, (Boolean)value);
				}
			} else if ("number".equals(formControl.getDataType())) {
				if (formControl.isMultiple()) {					
					elementsNode.set(elementName, WcmUtils.toArrayNode((List)value));
				} else {
					elementsNode.put(elementName, value.toString());
				}
			} else if ("string".equals(formControl.getDataType())) {
				if (formControl.isMultiple()) {					
					elementsNode.set(elementName, WcmUtils.toArrayNode((List<String>)value));
				} else {
					elementsNode.put(elementName, (String)value);
				}
			} else if ("object".equals(formControl.getDataType())) {
				//TODO
//				if (formControl.isMultiple()) {
//					String values[] = (jsonNode instanceof ObjectNode) 
//							? new String[] {JsonUtils.writeValueAsString(jsonNode)}
//					        : WcmUtils.getValues((ArrayNode) jsonNode);
//					elementsNpde.set(elementName, WcmUtils.toArrayNode(values));
//				} else {
//					elementsNpde.put(elementName, JsonUtils.writeValueAsString(jsonNode));
//				}
			} else if ("array".equals(formControl.getDataType())) {
				//TODO
				//elementsNpde.put(elementName, JsonUtils.writeValueAsString(jsonNode));
			}
		} 
	}
	
	@Override
	public String toString() {
		return "ContentItemElements [elements=" + elements + "]";
	}
}
