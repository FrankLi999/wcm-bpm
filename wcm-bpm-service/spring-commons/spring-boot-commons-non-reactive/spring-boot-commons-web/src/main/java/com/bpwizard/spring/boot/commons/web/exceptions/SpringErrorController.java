package com.bpwizard.spring.boot.commons.web.exceptions;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Used for handling exceptions that can't be handled by
 * <code>DefaultExceptionHandlerControllerAdvice</code>,
 * e.g. exceptions thrown in filters.
 */
public class SpringErrorController extends BasicErrorController {
	
    private static final Logger log = LogManager.getLogger(SpringErrorController.class);

    public SpringErrorController(ErrorAttributes errorAttributes,
			ServerProperties serverProperties,
			List<ErrorViewResolver> errorViewResolvers) {
		
		super(errorAttributes, serverProperties.getError(), errorViewResolvers);
		log.info("Created");
	}

    /**
     * Overrides the base method to add our custom logic
     */
	@Override	
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		
		Map<String, Object> body = getErrorAttributes(request,
			isIncludeStackTrace(request, MediaType.ALL));
		
		// if a status was put in SpringErrorAttributes, fetch that
		Object statusObj = body.get(SpringErrorAttributes.HTTP_STATUS_KEY);
				
		HttpStatus status;

		if (statusObj == null)             // if not put,
			status = getStatus(request);   // let the superclass make the status
		else {
			status = HttpStatus.valueOf((Integer) body.get(SpringErrorAttributes.HTTP_STATUS_KEY));
			body.remove(SpringErrorAttributes.HTTP_STATUS_KEY); // clean the status from the map
		}
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		return new ResponseEntity<Map<String, Object>>(body, headers, status);
	}
}
