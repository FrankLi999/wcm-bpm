package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.handler.JcrNodeCollectionHandler;
import com.bpwizard.wcm.repo.rest.handler.JsonNodeCollectionHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.Collector;
import com.bpwizard.wcm.repo.rest.jcr.model.UpdateCollectorRequest;
import com.bpwizard.wcm.repo.rest.service.CollectorRepository;

@RestController
@RequestMapping(CollectorController.BASE_URI)
@Validated
public class CollectorController extends BaseWcmRestController {
	public static final String BASE_URI = "/wcm/api/collector";
	private static final Logger logger = LoggerFactory.getLogger(CollectorController.class);
	
	@Autowired
	private JcrNodeCollectionHandler jcrNodeCollectionHandler;

	@Autowired
	private JsonNodeCollectionHandler jsonNodeCollectionHandler;
	
	@Autowired
	private CollectorRepository collectorService;
	
	@PostMapping(path = "/{repository}/{workspace}/jcrElement", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> collectJcrElement(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
    		@RequestParam(name="id", required=true) String id,
    		@RequestParam(name="operation", defaultValue="") String operation,
    		@RequestParam(name="timestamp", required=true) long timestamp,
    		@RequestParam(name="rootNodeKey", required=true) String rootNodeKey,
    		@RequestParam(name="jcrSystemKey", required=true) String jcrSystemKey,
    		@RequestParam("content") MultipartFile content,
			HttpServletRequest request) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		this.jcrNodeCollectionHandler.collectJcrElement(repository, workspace, id, operation, timestamp, 
				rootNodeKey, jcrSystemKey, content.getInputStream());
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PostMapping(path = "/{repository}/{workspace}/jcrItem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> collectJcrItem(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
    		@RequestParam(name="id", required=true) String id,
    		@RequestParam(name="nodePath") String nodePath,
    		@RequestParam(name="itemType") String itemType,
    		@RequestParam(name="operation", defaultValue="") String operation,
    		@RequestParam(name="removedDescendants", required=false) List<String> removedDescendants,
    		@RequestParam(name="timestamp", required=true) long timestamp,
       		@RequestParam(name="rootNodeKey", required=true) String rootNodeKey,
    		@RequestParam(name="jcrSystemKey", required=true) String jcrSystemKey,
    		@RequestParam("content") MultipartFile content,
			HttpServletRequest request) 
			throws WcmRepositoryException, IOException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		String baseUrl = RestHelper.repositoryUrl(request);
		this.jcrNodeCollectionHandler.collectJcrItem(repository, workspace, id, nodePath, itemType, 
				operation, removedDescendants, timestamp, rootNodeKey, jcrSystemKey, content.getInputStream(), baseUrl);
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PostMapping(path = "/{repository}/{workspace}/jsonItem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> collectJsonItem(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
    		@RequestParam(name="id", required=true) String id,
    		@RequestParam(name="nodePath") String nodePath,
    		@RequestParam(name="itemType") String itemType,
    		@RequestParam(name="operation", defaultValue="") String operation,
    		@RequestParam("content") MultipartFile content,
			HttpServletRequest request) 
			throws WcmRepositoryException, IOException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		String baseUrl = RestHelper.repositoryUrl(request);
		this.jsonNodeCollectionHandler.collectJsonItem(repository, workspace,
				nodePath, itemType, operation, content.getInputStream(), baseUrl);

		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Collector>> getCollectors() {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		List<Collector> collectors = collectorService.getCollectors();
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.ok(collectors);
	}
	
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collector> getCollector(@PathVariable("id") long id) {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		Collector collector = collectorService.getCollector(id);
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.ok(collector);
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCollector(
			@RequestBody Collector collector,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		collectorService.createCollector(collector);
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCollector(
			@RequestBody UpdateCollectorRequest updateCollectorRequest,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		collectorService.updateColelctor(updateCollectorRequest);
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.accepted().build();
	}
	
	@DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteCollector(
			@PathVariable("id") long id,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		collectorService.deleteCollector(id);
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.accepted().build();
	}
}