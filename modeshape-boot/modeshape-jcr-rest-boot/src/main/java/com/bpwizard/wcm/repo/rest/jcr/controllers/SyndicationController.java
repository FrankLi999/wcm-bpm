package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.Syndication;
import com.bpwizard.wcm.repo.rest.jcr.model.UpdateSyndicationRequest;

@RestController
@RequestMapping(SyndicationController.BASE_URI)
@Validated
public class SyndicationController extends BaseWcmRestController {
	public static final String BASE_URI = "/wcm/api/syndication";
	private static final Logger logger = LogManager.getLogger(SyndicationController.class);
	
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	List<Syndication> getSyndications() {
		return null;
	}
	
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	Syndication getSyndication(@PathVariable("id") int id, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return null;
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSyndicator(
			@RequestBody Syndication syndication,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		return null;
	}
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateSyndicator(
			@RequestBody UpdateSyndicationRequest syndicationRequest,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		return null;
	}
	
	@DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteSyndicator(
			@PathVariable("id") int id,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		return null;
	}
}
