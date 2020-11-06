package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import com.bpwizard.wcm.repo.rest.handler.WcmEventHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.Collector;
import com.bpwizard.wcm.repo.rest.jcr.model.UpdateCollectorRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;

@RestController
@RequestMapping(CollectorController.BASE_URI)
@Validated
public class CollectorController extends BaseWcmRestController {
	public static final String BASE_URI = "/wcm/api/collector";
	private static final Logger logger = LogManager.getLogger(CollectorController.class);
	
	@Autowired
	private WcmEventHandler wcmEventHandler;
	
	@PostMapping(path = "/collect", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> collectItems(
			@RequestBody List<WcmEvent> wcmItems,  HttpServletRequest request) 
			throws WcmRepositoryException {
		return null;
	}
	
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	List<Collector> getCollectors() {
		return null;
	}
	
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	Collector getCollector(@PathVariable("id") int id) {
		return null;
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCollector(
			@RequestBody Collector collector,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		return null;
	}
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCollector(
			@RequestBody UpdateCollectorRequest syndicationRequest,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		return null;
	}
	
	@DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteCollector(
			@PathVariable("id") int id,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		return null;
	}
}