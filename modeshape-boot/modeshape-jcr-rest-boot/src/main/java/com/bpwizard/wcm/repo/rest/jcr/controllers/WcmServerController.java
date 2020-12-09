package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.bpwizard.wcm.repo.rest.jcr.model.WcmServer;
import com.bpwizard.wcm.repo.rest.service.WcmServerService;

@RestController
@RequestMapping(WcmServerController.BASE_URI)
@Validated
public class WcmServerController {
	public static final String BASE_URI = "/wcm/api/wcm-server";
	private static final Logger logger = LoggerFactory.getLogger(WcmServerController.class);
	
	@Autowired
	WcmServerService wcmServerService;

	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<?> getWcmServers() {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		List<WcmServer> wcmServers = wcmServerService.getWcmServers();
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.ok(wcmServers);
	}
	
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<?> getWcmServer(@PathVariable("id") Long id) {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		WcmServer wcmServer = wcmServerService.getWcmServer(id);
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return (wcmServer == null ) ? ResponseEntity.ok(wcmServer) : ResponseEntity.notFound().build();
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createWcmServer(
			@RequestBody WcmServer wcmServer,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		wcmServerService.createWcmServer(wcmServer);
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.accepted().build();
	}
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> UpdateWcmServer(
			@RequestBody WcmServer wcmServer,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		wcmServerService.UpdateWcmServer(wcmServer);
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.accepted().build();
	}
	
	@DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteWcmServer(
			@PathVariable("id") Long id,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		wcmServerService.deleteWcmServer(id);
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.accepted().build();
	}
}
