package com.bpwizard.spring.boot.commons.util;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;
import com.bpwizard.spring.boot.commons.security.BlueTokenService;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * Useful helper methods
 * 
 * @author Sanjay Patel
 */
public class SecurityUtils {
	
	private static final Logger log = LogManager.getLogger(SecurityUtils.class);
	
//	public static final String AUTHORIZATION_REQUEST_COOKIE_NAME = "spring_oauth2_authorization_request";
//	public static final String BPW_REDIRECT_URI_COOKIE_PARAM_NAME = "spring_redirect_uri";
	
	public static final String AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
	public static final String BPW_REDIRECT_URI_COOKIE_PARAM_NAME = "redirect_uri";
	
	// Computed authorities
	public static final String GOOD_ADMIN = "GOOD_ADMIN";
	public static final String GOOD_USER = "GOOD_USER";
	
	// JWT Token related
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final int TOKEN_PREFIX_LENGTH = TOKEN_PREFIX.length();
	public static final String TOKEN_RESPONSE_HEADER_NAME = "Authorization";
	// public static final String TOKEN_RESPONSE_HEADER_NAME = "Spring-Authorization";

	public static ApplicationContext applicationContext;
	public static ObjectMapper objectMapper;
	
	public SecurityUtils(ApplicationContext applicationContext,
		ObjectMapper objectMapper) {
		
		SecurityUtils.applicationContext = applicationContext;
		SecurityUtils.objectMapper = objectMapper;
		
		log.info("Created");
	}
	
	
	/**
	 * Extracts the current-user from authentication object
	 * 
	 * @param auth
	 * @return
	 */
	public static <ID extends Serializable> UserDto currentUser(SecurityContext context) {
		
		return currentUser(context.getAuthentication());
	}

	
	/**
	 * Extracts the current-user from authentication object
	 * 
	 * @param auth
	 * @return
	 */
	public static <ID extends Serializable> UserDto currentUser(Authentication auth) {
		
	    if (auth != null) {
	      Object principal = auth.getPrincipal();
	      if (principal instanceof SpringPrincipal) {
	        return ((SpringPrincipal) principal).currentUser();
	      }
	    }
	    return null;	  
	}


	/**
	 * Throws AccessDeniedException is not authorized
	 * 
	 * @param authorized
	 * @param messageKey
	 */
	public static void ensureAuthority(boolean authorized, String messageKey) {
		
		if (!authorized)
			throw new AccessDeniedException(SpringExceptionUtils.getMessage(messageKey));
	}


	/**
	 * Constructs a map of the key-value pairs,
	 * passed as parameters
	 * 
	 * @param keyValPair
	 */
	@SuppressWarnings("unchecked")
	public static <K,V> Map<K,V> mapOf(Object... keyValPair) {
		
	    if(keyValPair.length % 2 != 0)
	        throw new IllegalArgumentException("Keys and values must be in pairs");
	
	    Map<K,V> map = new HashMap<K,V>(keyValPair.length / 2);
	
	    for(int i = 0; i < keyValPair.length; i += 2){
	        map.put((K) keyValPair[i], (V) keyValPair[i+1]);
	    }
	
	    return map;
	}


	/**
	 * Throws BadCredentialsException if not valid
	 * 
	 * @param valid
	 * @param messageKey
	 */
	public static void ensureCredentials(boolean valid, String messageKey) {
		
		if (!valid)
			throw new BadCredentialsException(SpringExceptionUtils.getMessage(messageKey));
	}

	
	/**
	 * Applies a JsonPatch to an object
	 */
    @SuppressWarnings("unchecked")
	public static <T> T applyPatch(T originalObj, String patchString)
			throws JsonProcessingException, IOException, JsonPatchException {

        // Parse the patch to JsonNode
        JsonNode patchNode = objectMapper.readTree(patchString);

        // Create the patch
        JsonPatch patch = JsonPatch.fromJson(patchNode);

        // Convert the original object to JsonNode
        JsonNode originalObjNode = objectMapper.valueToTree(originalObj);

        // Apply the patch
        TreeNode patchedObjNode = patch.apply(originalObjNode);

        // Convert the patched node to an updated obj
        return objectMapper.treeToValue(patchedObjNode, (Class<T>) originalObj.getClass());
    }


	/**
	 * Reads a resource into a String
	 */
	public static String toStr(Resource resource) throws IOException {
		
		String text = null;
	    try (Scanner scanner = new Scanner(resource.getInputStream(), StandardCharsets.UTF_8.name())) {
	        text = scanner.useDelimiter("\\A").next();
	    }
	    
	    return text;
	}
	
	public static ObjectMapper mapper() {
		
		return objectMapper;
	}


	/**
	 * Gets the reference to an application-context bean
	 *  
	 * @param clazz	the type of the bean
	 */
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}


	/**
	 * Generates a random unique string
	 */
	public static String uid() {
		
		return UUID.randomUUID().toString();
	}


	/**
	 * Serializes an object to JSON string
	 */
	public static <T> String toJson(T obj) {
	
		try {
			
			return objectMapper.writeValueAsString(obj);
			
		} catch (JsonProcessingException e) {
			
			throw new RuntimeException(e);
		}
	}


	/**
	 * Deserializes a JSON String
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
	
		try {
			
			return objectMapper.readValue(json, clazz);
			
		} catch (IOException e) {
			
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Serializes an object
	 */	
	public static String serialize(Serializable obj) {
		
		return Base64.getUrlEncoder().encodeToString(
				SerializationUtils.serialize(obj));
	}

	/**
	 * Deserializes an object
	 */	
	public static <T> T deserialize(String serializedObj) {

		return SerializationUtils.deserialize(
				Base64.getUrlDecoder().decode(serializedObj));
    }


	public static UserDto getUserDto(JWTClaimsSet claims) {

		Object userClaim = claims.getClaim(BlueTokenService.USER_CLAIM);
		
		if (userClaim == null)
			return null;
		
		return SecurityUtils.deserialize((String) userClaim);
	}
}
