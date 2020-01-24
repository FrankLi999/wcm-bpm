package com.bpwizard.spring.boot.commons.service.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.service.SpringService;
import com.bpwizard.spring.boot.commons.web.util.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Authentication success handler for sending the response
 * to the client after successful authentication.
 * 
 * @author Sanjay Patel
 */
public class SpringAuthenticationSuccessHandler
	extends SimpleUrlAuthenticationSuccessHandler {
	
	private static final Logger log = LogManager.getLogger(SpringAuthenticationSuccessHandler.class);
	
    private ObjectMapper objectMapper;    
    private SpringService<?, ?> springService;
    private long defaultExpirationMillis;
    
	public SpringAuthenticationSuccessHandler(ObjectMapper objectMapper, SpringService<?, ?> springService, SpringProperties properties) {
		
		this.objectMapper = objectMapper;
		this.springService = springService;
		this.defaultExpirationMillis = properties.getJwt().getExpirationMillis();
		
		log.info("Created");
	}

	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request,
    		HttpServletResponse response,
            Authentication authentication)
    throws IOException, ServletException {

        // Instead of handle(request, response, authentication),
		// the statements below are introduced
    	response.setStatus(HttpServletResponse.SC_OK);
    	response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    	String expirationMillisStr = request.getParameter("expirationMillis");
    	long expirationMillis = expirationMillisStr == null ?
    			defaultExpirationMillis : Long.valueOf(expirationMillisStr);
 
    	// get the current-user
    	UserDto currentUser = WebUtils.currentUser();

    	springService.addAuthHeader(response, currentUser.getUsername(), expirationMillis);
    	
    	// write current-user data to the response  
    	response.getOutputStream().print(
    			objectMapper.writeValueAsString(currentUser));

    	// as done in the base class
    	clearAuthenticationAttributes(request);
        
        log.debug("Authentication succeeded for user: " + currentUser);        
    }
}
