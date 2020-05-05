package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.HashMap;
import java.util.Map;

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
	
	public void toJson(ObjectNode elementsNpde, ObjectNode elementsChildren, AuthoringTemplate at) throws JsonProcessingException {
		for (String elementName: elements.keySet()) {
			FormControl formControl = at.getElements().get(elementName);
			// JsonNode jsonNode= getElements().get(elementName);
			Object jsonNode= getElements().get(elementName);
			System.out.println(">>>>>>>>>>>>>>> toJson content item elenment: " + elementName + "," + jsonNode + " .class:" + jsonNode.getClass());
			if ("integer".equals(formControl.getDataType())) {
//				if (formControl.isMultiple()) {					
//					elementsNpde.set(elementName, jsonNode.to);
//				} else {
//					elementsNpde.put(elementName, jsonNode.asText());
//				}
			} else if ("boolean".equals(formControl.getDataType())) {
				//float or //double or by default integer
			} else if ("number".equals(formControl.getDataType())) {
				//float or //double or by default integer
			} else if ("string".equals(formControl.getDataType())) {
				//format: binary, byte for file upload
			} else if ("object".equals(formControl.getDataType())) {
//				if (formControl.isMultiple()) {
//					String values[] = (jsonNode instanceof ObjectNode) 
//							? new String[] {JsonUtils.writeValueAsString(jsonNode)}
//					        : WcmUtils.getValues((ArrayNode) jsonNode);
//					elementsNpde.set(elementName, WcmUtils.toArrayNode(values));
//				} else {
//					elementsNpde.put(elementName, JsonUtils.writeValueAsString(jsonNode));
//				}
			} else if ("array".equals(formControl.getDataType())) {
				//elementsNpde.put(elementName, JsonUtils.writeValueAsString(jsonNode));
			}
		} 
	}
	
	@Override
	public String toString() {
		return "ContentItemElements [elements=" + elements + "]";
	}
}
