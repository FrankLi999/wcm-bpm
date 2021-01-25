package com.bpwizard.wcm.repo.rest.jcr.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.ClearWcmEventRequest;
import com.bpwizard.wcm.repo.rest.service.JcrNodeEventRepository;

@RestController
@RequestMapping(WcmEventController.BASE_URI)
@Validated
public class WcmEventController extends BaseWcmRestController {
	public static final String BASE_URI = "/wcm/api/wcm_events";
	private static final Logger logger = LoggerFactory.getLogger(WcmEventController.class);
	
	@Autowired
	private JcrNodeEventRepository wcmEventHandler;
	
	@PostMapping(path = "/clear", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> clearWcmEvents(
			@RequestBody ClearWcmEventRequest syndicationRequest,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		
		wcmEventHandler.clearWcmEventBefore(syndicationRequest.getTimestampBefore());
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return null;
	}
}
