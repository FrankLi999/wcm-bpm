package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

import org.modeshape.schematic.document.Document;
import org.modeshape.schematic.internal.document.BsonWriter;
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

import com.bpwizard.wcm.repo.rest.handler.WcmRequestHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.Collector;
import com.bpwizard.wcm.repo.rest.jcr.model.JcrNodeEntry;
import com.bpwizard.wcm.repo.rest.jcr.model.RootNodeKeys;
import com.bpwizard.wcm.repo.rest.jcr.model.UpdateCollectorRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEventEntry;
import com.bpwizard.wcm.repo.rest.service.BsonSyndicationReader;
import com.bpwizard.wcm.repo.rest.service.CollectorService;
import com.bpwizard.wcm.repo.rest.service.JcrNodeService;
import com.bpwizard.wcm.repo.rest.service.RootNodeKeyService;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;

@RestController
@RequestMapping(CollectorController.BASE_URI)
@Validated
public class CollectorController extends BaseWcmRestController {
	public static final String BASE_URI = "/wcm/api/collector";
	private static final Logger logger = LoggerFactory.getLogger(CollectorController.class);
	
	private final BsonSyndicationReader BSON_READER = new BsonSyndicationReader();
	private final BsonWriter BSON_WRITER = new BsonWriter();
	
	@Autowired
	private CollectorService collectorService;
	
	@Autowired
	private JcrNodeService jcrNodeService;
	
	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	@Autowired 
	private RootNodeKeyService rootNodeKeyService;
	@PostMapping(path = "/{repository}/{workspace}/element", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> collectElement(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
    		@RequestParam(name="id", required=true) String id,
    		@RequestParam(name="operation", defaultValue="") String operation,
    		@RequestParam(name="timestamp", required=true) long timestamp,
    		@RequestParam(name="rootNodeKey", required=true) String rootNodeKey,
    		@RequestParam(name="jcrSystemKey", required=true) String jcrSystemKey,
    		@RequestParam("content") MultipartFile content,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			RootNodeKeys renderSiteRootNodeKeys = rootNodeKeyService.getRootNodeKeys(repository, workspace);
			Map<String, String> rootNodeKeyMap = new HashMap<>();
			
			rootNodeKeyMap.put(rootNodeKey, renderSiteRootNodeKeys.getRootNodeKey());
			rootNodeKeyMap.put(jcrSystemKey, renderSiteRootNodeKeys.getJcrSystemKey());

			JcrNodeEntry jcrNode = new JcrNodeEntry();
			jcrNode.setId(this.getNodeKey(renderSiteRootNodeKeys.getRootNodeKey(), id));
			jcrNode.setLastUpdated(new Timestamp(timestamp));
			jcrNode.setContent(this.transformBsonStream(content.getInputStream(), rootNodeKeyMap));
			if (WcmEventEntry.Operation.create.name().equals(operation)) {
				jcrNodeService.addJcrNode(jcrNode);
			} else {
				jcrNodeService.updateJcrNode(jcrNode);
			} 
		} catch (Throwable t) {
			logger.error("Failed to collect element",t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PostMapping(path = "/{repository}/{workspace}/item", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> collectItems(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
    		@RequestParam(name="id", required=true) String id,
    		@RequestParam(name="library") String library,
    		@RequestParam(name="nodePath") String nodePath,
    		@RequestParam(name="itemType") String itemType,
    		@RequestParam(name="operation", defaultValue="") String operation,
    		@RequestParam(name="removedDescendants", required=false) List<String> removedDescendants,
    		@RequestParam(name="timestamp", required=true) long timestamp,
       		@RequestParam(name="rootNodeKey", required=true) String rootNodeKey,
    		@RequestParam(name="jcrSystemKey", required=true) String jcrSystemKey,
    		@RequestParam("content") MultipartFile content,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			RootNodeKeys rootNodeKeys = rootNodeKeyService.getRootNodeKeys(repository, workspace);
			Map<String, String> rootNodeKeyMap = new HashMap<>();
			
			rootNodeKeyMap.put(rootNodeKey, rootNodeKeys.getRootNodeKey());
			rootNodeKeyMap.put(jcrSystemKey, rootNodeKeys.getJcrSystemKey());
			for (String removedElementId: removedDescendants) {
				jcrNodeService.deleteJcrNode(this.getNodeKey(rootNodeKeys.getRootNodeKey(), removedElementId));
			}
			JcrNodeEntry jcrNode = new JcrNodeEntry();
			jcrNode.setId(this.getNodeKey(rootNodeKeys.getRootNodeKey(), id));
			jcrNode.setLastUpdated(new Timestamp(timestamp));
			if (WcmEventEntry.Operation.create.name().equals(operation)) {
				jcrNode.setContent(this.transformBsonStream(content.getInputStream(), rootNodeKeyMap));
				jcrNodeService.addJcrNode(jcrNode);
			} else if (WcmEventEntry.Operation.update.name().equals(operation)) {
				jcrNode.setContent(this.transformBsonStream(content.getInputStream(), rootNodeKeyMap));
				jcrNodeService.updateJcrNode(jcrNode);
			} else {
				jcrNodeService.deleteJcrNode(jcrNode.getId());
			}
			
			if (WcmEventEntry.WcmItemType.authoringTemplate.name().equals(itemType)) {
				// get at, create the corresponding JCR type
				AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(
						repository, 
						workspace, 
						nodePath.substring("/library".length()), 
						request);
				this.wcmUtils.registerNodeType(at.getWorkspace(), at);
			}
		} catch (RepositoryException re) { 
			logger.error("Failed to collect items", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.LOCK_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error("Failed to collect items", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
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
	
	private String getNodeKey(String rootNodeKey, String nodeId) {
		return String.format("%s%s", rootNodeKey, nodeId);
	}
	
	private InputStream transformBsonStream(InputStream input, Map<String, String> rootNodeKeyMap) throws IOException {
		Document document = BSON_READER.read(input, rootNodeKeyMap);
		System.out.println(">>>>>>>>>>>>> jcrNode content-----");
		System.out.println(document);
		return new ByteArrayInputStream(BSON_WRITER.write(document));
	}
}