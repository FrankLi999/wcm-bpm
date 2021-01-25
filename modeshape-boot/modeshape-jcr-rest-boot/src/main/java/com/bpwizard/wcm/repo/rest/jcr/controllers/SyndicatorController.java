package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.bpwizard.wcm.repo.rest.handler.JcrNodeSyndicationHandler;
import com.bpwizard.wcm.repo.rest.handler.JsonNodeSyndicationHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.SyndicationRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.Syndicator;
import com.bpwizard.wcm.repo.rest.jcr.model.UpdateSyndicationRequest;
import com.bpwizard.wcm.repo.rest.service.SyndicatorRepository;

@RestController
@RequestMapping(SyndicatorController.BASE_URI)
@Validated
public class SyndicatorController extends BaseWcmRestController {
	public static final String BASE_URI = "/wcm/api/syndicator";
	private static final Logger logger = LoggerFactory.getLogger(SyndicatorController.class);
	
	@Autowired
	private SyndicatorRepository syndicatorService;

	@Autowired	
	protected JcrNodeSyndicationHandler jcrNodeWcmEventHandler;

	@Autowired	
	protected JsonNodeSyndicationHandler jsonNodeWcmEventHandler;
	
	@Value("${syndication.item.strategy}")
	private String syndicationItemStrategy; //jcr or json
	
	@PostMapping(path = "/syndicate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> syndicate(
			@RequestBody SyndicationRequest syndicationRequest,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			if ("jcr".equals(syndicationItemStrategy)) {
				jcrNodeWcmEventHandler.syndicate(syndicationRequest, request.getHeader("Authorization"));
			} else {
				jsonNodeWcmEventHandler.syndicate(syndicationRequest, request.getHeader("Authorization"));
			}
		} catch (Throwable t) {
	    	logger.error("Syndication error", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Syndicator>> getSyndications() {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		List<Syndicator> syndicators = syndicatorService.getSyndicators();
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.ok(syndicators);
	}
	
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Syndicator> getSyndicator(@PathVariable("id") long id, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		Syndicator syndicator = syndicatorService.getSyndicator(id);
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.ok(syndicator);
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSyndicator(
			@RequestBody Syndicator syndicator,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		syndicatorService.createSyndicator(syndicator);
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateSyndicator(
			@RequestBody UpdateSyndicationRequest syndicationRequest,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		syndicatorService.updateSyndicator(syndicationRequest);
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.accepted().build();				
	}
	
	@DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteSyndicator(
			@PathVariable("id") long id,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		syndicatorService.deleteSyndicator(id);
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.accepted().build();		
	}
}
