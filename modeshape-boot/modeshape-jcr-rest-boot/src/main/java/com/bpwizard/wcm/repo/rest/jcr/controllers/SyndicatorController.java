package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.handler.WcmEventHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.SyndicationRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.Syndicator;
import com.bpwizard.wcm.repo.rest.jcr.model.UpdateSyndicationRequest;
import com.bpwizard.wcm.repo.rest.service.SyndicatorService;

@RestController
@RequestMapping(SyndicatorController.BASE_URI)
@Validated
public class SyndicatorController extends BaseWcmRestController {
	public static final String BASE_URI = "/wcm/api/syndicator";
	private static final Logger logger = LogManager.getLogger(SyndicatorController.class);
	
	@Autowired
	private SyndicatorService syndicatorService;
	@Autowired
	private  WcmEventHandler  wcmEventService;
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> syndicate(
			@RequestBody SyndicationRequest syndicationRequest,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			wcmEventService.syndicate(syndicationRequest, request.getHeader("Authorization"));
		} catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Syndicator>> getSyndications() {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		List<Syndicator> syndicators = syndicatorService.getSyndicators();
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return ResponseEntity.ok(syndicators);
	}
	
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Syndicator> getSyndicator(@PathVariable("id") long id, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		Syndicator syndicator = syndicatorService.getSyndicator(id);
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return ResponseEntity.ok(syndicator);
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSyndicator(
			@RequestBody Syndicator syndicator,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		syndicatorService.createSyndicator(syndicator);
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateSyndicator(
			@RequestBody UpdateSyndicationRequest syndicationRequest,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		syndicatorService.updateSyndicator(syndicationRequest);
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return ResponseEntity.accepted().build();				
	}
	
	@DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteSyndicator(
			@PathVariable("id") long id,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		syndicatorService.deleteSyndicator(id);
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return ResponseEntity.accepted().build();		
	}
}
