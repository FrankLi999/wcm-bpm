package com.bpwizard.wcm.repo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtils {
	protected static final ObjectMapper mapper = new ObjectMapper();
	
	public static ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }
	
	public static ArrayNode creatArrayNode() {
        return mapper.createArrayNode();
    }
}
