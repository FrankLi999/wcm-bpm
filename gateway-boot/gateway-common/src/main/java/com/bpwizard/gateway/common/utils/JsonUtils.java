package com.bpwizard.gateway.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

/**
 * Jackson Json JsonUtils.
 */
@SuppressWarnings("deprecation")
public final class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("classFilter", SimpleBeanPropertyFilter.serializeAllExcept("class"));
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .registerModule(javaTimeModule)
                .setFilterProvider(filterProvider);

    }


    /**
     * To json node.
     *
     * @param object the object
     * @return the string
     */
    public static JsonNode toJsonNode(final Object object) {
        return mapper.valueToTree(object);
    }
    
    /**
     * To json string.
     *
     * @param object the object
     * @return the string
     */
    public static String toJson(final Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return "{}";
        }
    }
    
    public static <T> T fromJson(JsonNode jsonNode, Class<T> valueType) {
    	try {
	    	return mapper.treeToValue(jsonNode, valueType);
	    } catch (JsonMappingException e) {
	    	throw new IllegalStateException("Json mapping error: " + e);
	    } catch (JsonProcessingException e) {
	     	throw new IllegalStateException("Json processing error: " + e);
	    } 
    }
    
    
    public static <T> T fromJson(JsonNode jsonNode, TypeReference<T> ref) {
    	try {
	    	return mapper.readerFor(ref).readValue(jsonNode);
	    } catch (IOException e) {
	    	throw new IllegalStateException("Json mapping error: " + e);
	    }  
    }
    
    
    
    public static <T> T fromJson(String content, Class<T> valueType) {
	    try {
	    	return mapper.readValue(content, valueType);
	    } catch (JsonMappingException e) {
	    	throw new IllegalStateException("Json mapping error: " + e);
	    } catch (JsonProcessingException e) {
	     	throw new IllegalStateException("Json processing error: " + e);
	    } 
    }
    
    public static <T> List<T> fromList(String content, Class<T[]> valueType) {
    	try {
    		return Arrays.asList(mapper.readValue(content, valueType));
    	} catch (JsonMappingException e) {
        	throw new IllegalStateException("Json mapping error: " + e);
        } catch (JsonProcessingException e) {
         	throw new IllegalStateException("Json processing error: " + e);
        } 
    }
    
    /**
     * To object map map.
     *
     * @param json the json
     * @return the map
     */
    public static Map<String, Object> toObjectMap(final String json) {
    	try {
    		return mapper.readValue(json, new TypeReference<LinkedHashMap<String, Object>>(){});
	    } catch (JsonMappingException e) {
	    	throw new IllegalStateException("Json mapping error: " + e);
	    } catch (JsonProcessingException e) {
	     	throw new IllegalStateException("Json processing error: " + e);
	    } 
        
    }
    

    /**
     * To tree map tree map.
     *
     * @param json the json
     * @return the tree map
     */
    public static ConcurrentSkipListMap<String, Object> toTreeMap(final String json) {
    	try {
    		return mapper.readValue(json, new TypeReference<ConcurrentSkipListMap<String, Object>>(){});
	    } catch (JsonMappingException e) {
	    	throw new IllegalStateException("Json mapping error: " + e);
	    } catch (JsonProcessingException e) {
	     	throw new IllegalStateException("Json processing error: " + e);
	    } 

    }


    /**
     * toList Map.
     *
     * @param json json
     * @return hashMap list
     */
    public static List<Map<String, Object>> toListMap(final String json) {
    	try {
    		return mapper.readValue(json, new TypeReference<ArrayList<Map<String, Object>>>(){});
	    } catch (JsonMappingException e) {
	    	throw new IllegalStateException("Json mapping error: " + e);
	    } catch (JsonProcessingException e) {
	     	throw new IllegalStateException("Json processing error: " + e);
	    } 
    }

    /**
     * Remove class object.
     *
     * @param object the object
     * @return the object
     */
    @SuppressWarnings("rawtypes")
    public static Object removeClass(final Object object) {
        if (object instanceof Map) {
			Map map = (Map) object;
            Object result = map.get("result");
            if (result instanceof Map) {
                Map resultMap = (Map) result;
                resultMap.remove("class");
            }
            map.remove("class");
            return object;
        } else {
            return object;
        }
    }
    
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
