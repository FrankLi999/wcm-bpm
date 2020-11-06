package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.sql.Timestamp;
import java.util.List;

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
import com.bpwizard.wcm.repo.rest.jcr.model.SyndicationRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;

@RestController
@RequestMapping(SydicatorController.BASE_URI)
@Validated
public class SydicatorController extends BaseWcmRestController {
	public static final String BASE_URI = "/wcm/api/syndicator";
	private static final Logger logger = LogManager.getLogger(SydicatorController.class);
	
	@Autowired
	private WcmEventHandler wcmEventHandler;
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> syndicate(
			@RequestBody SyndicationRequest syndicationRequest,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		
		int pageSize = 20;
		int pageIndex = 0;
		List<WcmEvent> wcmEvents = null;
		do {
			wcmEvents = wcmEventHandler.getWcmEventAfter(syndicationRequest.getStartTime(), new Timestamp(System.currentTimeMillis()), pageIndex, pageSize);
		} while (wcmEvents.size() < pageSize);	
		return null;
	}
}
