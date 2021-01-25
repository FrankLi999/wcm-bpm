package com.bpwizard.wcm.repo.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

	public static JsonNode bytesToJsonNode(byte[] bytes) throws IOException {
        return mapper.readTree(bytes);
    }
	
	public static ArrayNode readTree(String fromValues[]) throws JsonProcessingException {
		ArrayNode arrayNode = creatArrayNode();
		for (String value: fromValues) {
			arrayNode.add(mapper.readTree(value));
		}
		return arrayNode;
    }

	public static byte[] writeValueAsBytes(JsonNode jsonNode) throws JsonProcessingException {
        return mapper.writeValueAsBytes(jsonNode);
    }
	
	public static InputStream writeValueAsStream(JsonNode jsonNode) throws JsonProcessingException {
        return new ByteArrayInputStream(mapper.writeValueAsBytes(jsonNode));
    }
	
	public static String writeValueAsString(JsonNode jsonNode) throws JsonProcessingException {
        return mapper.writeValueAsString(jsonNode);
    }
	
	public static String objectAsJsonString(Object value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }
	
	public static List<String> writeToStrings(ArrayNode values) throws JsonProcessingException {
		List<String> strings = new ArrayList<>();
		for (int i = 0; i < values.size(); i++) {
			strings.add(values.get(i).textValue());
		}
		return strings;
    }
}
