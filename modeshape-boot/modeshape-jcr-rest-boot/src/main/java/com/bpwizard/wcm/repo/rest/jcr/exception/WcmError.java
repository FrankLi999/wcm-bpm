package com.bpwizard.wcm.repo.rest.jcr.exception;

import java.util.Arrays;

import com.bpwizard.wcm.repo.rest.utils.WcmErrors;

public class WcmError {
	private String message;
	private String errorCode;
	private String[] arguments;
	
	public static final WcmError UNEXPECTED_ERROR = new WcmError("Unexpected Error", WcmErrors.UNKNOW_ERROR, null);
	public static final WcmError WCM_ERROR = new WcmError("Wcm Error", WcmErrors.WCM_ERROR, null);
	
	public static WcmError createWcmError(String message, String errorCode, String[] arguments) {
		return new WcmError(message, errorCode, arguments);
	}
	
	public WcmError(String message, String errorCode, String[] arguments) {
		this.message = message;
		this.errorCode = errorCode;
		this.arguments = arguments;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public String[] getArguments() {
		return arguments;
	}
	
	@Override
	public String toString() {
		return "WcmError [message=" + message + ", errorCode=" + errorCode + ", arguments=" + Arrays.toString(arguments)
				+ "]";
	}
}
