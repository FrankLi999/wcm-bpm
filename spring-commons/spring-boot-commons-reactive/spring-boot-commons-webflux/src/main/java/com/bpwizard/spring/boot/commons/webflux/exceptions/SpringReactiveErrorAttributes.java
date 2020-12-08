package com.bpwizard.spring.boot.commons.webflux.exceptions;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.bpwizard.spring.boot.commons.exceptions.ErrorResponseComposer;
import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;

public class SpringReactiveErrorAttributes<T extends Throwable> extends DefaultErrorAttributes {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringReactiveErrorAttributes.class);

	/**
	 * Component that actually builds the error response
	 */
	private ErrorResponseComposer<T> errorResponseComposer;
	
    public SpringReactiveErrorAttributes(ErrorResponseComposer<T> errorResponseComposer) {

		this.errorResponseComposer = errorResponseComposer;
		logger.info("Created");
	}

	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request,
			boolean includeStackTrace) {
		
		Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);		
		addErrorDetails(errorAttributes, request);
		return errorAttributes;
	}
	
	/**
     * Handles exceptions
     */
	@SuppressWarnings("unchecked")
	protected void addErrorDetails(
			Map<String, Object> errorAttributes, ServerRequest request) {
		
		Throwable ex = getError(request);
		
		errorResponseComposer.compose((T)ex).ifPresent(errorResponse -> {
			
			// check for nulls - errorResponse may have left something for the DefaultErrorAttributes
			
			if (errorResponse.getExceptionId() != null)
				errorAttributes.put("exceptionId", errorResponse.getExceptionId());

			if (errorResponse.getMessage() != null)
				errorAttributes.put("message", errorResponse.getMessage());
			
			if (errorResponse.getErrorCode() != null)
				errorAttributes.put("errorCode", errorResponse.getErrorCode());
			if (errorResponse.getArguments() != null)
				errorAttributes.put("arguments", errorResponse.getArguments());
			
			Integer status = errorResponse.getStatus();
			
			if (status != null) {
				errorAttributes.put("status", status);
				errorAttributes.put("reasonPhrase", errorResponse.getReasonPhrase());
			}

			if (errorResponse.getErrors() != null)
				errorAttributes.put("errors", errorResponse.getErrors());			
		});
		
		if (errorAttributes.get("exceptionId") == null)
			errorAttributes.put("exceptionId", SpringExceptionUtils.getExceptionId(ex));		
	}
}
