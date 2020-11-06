package com.bpwizard.wcm.repo.rest.jcr.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.handler.WcmEventHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.ClearWcmEventRequest;

@RestController
@RequestMapping(WcmEventController.BASE_URI)
@Validated
public class WcmEventController extends BaseWcmRestController {
	public static final String BASE_URI = "/wcm/api/wcm_events";
	private static final Logger logger = LogManager.getLogger(WcmEventController.class);
	
	@Autowired
	private WcmEventHandler wcmEventHandler;
	
	@PostMapping(path = "/clear", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> clearWcmEvents(
			@RequestBody ClearWcmEventRequest syndicationRequest,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		wcmEventHandler.clearWcmEventBefore(syndicationRequest.getTimestampBefore());
		return null;
	}
}
