package com.bpwizard.wcm.repo.syndication;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class JsonUtils {
	protected static final ObjectMapper mapper = new ObjectMapper();
	
	public static ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }
	
	public static ArrayNode creatArrayNode() {
        return mapper.createArrayNode();
    }
	
	public static TextNode createTextNode(String fromValue) {
        return new TextNode(fromValue);
    }
	
	public static JsonNode readTree(String fromValue) {
        return mapper.convertValue(fromValue, JsonNode.class);
    }
	
	public static JsonNode inputStreamToJsonNode(InputStream is) throws IOException {
        return mapper.readTree(is);
    }
	
	public static ArrayNode readTree(String fromValues[]) throws JsonProcessingException {
		ArrayNode arrayNode = creatArrayNode();
		for (String value: fromValues) {
			arrayNode.add(mapper.readTree(value));
		}
		return arrayNode;
    }
	
	public static String writeValueAsString(JsonNode jsonNode) throws JsonProcessingException {
        return mapper.writeValueAsString(jsonNode);
    }
	
	public static String objectAsJsonString(Object value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }
}
