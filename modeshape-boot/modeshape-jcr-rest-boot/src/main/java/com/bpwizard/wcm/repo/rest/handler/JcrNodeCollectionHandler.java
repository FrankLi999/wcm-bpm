package com.bpwizard.wcm.repo.rest.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.modeshape.schematic.document.Document;
import org.modeshape.schematic.internal.document.BsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.JcrNodeEntry;
import com.bpwizard.wcm.repo.rest.jcr.model.RootNodeKeys;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEventEntry;
import com.bpwizard.wcm.repo.rest.service.BsonSyndicationReader;
import com.bpwizard.wcm.repo.rest.service.JcrNodeRepository;
import com.bpwizard.wcm.repo.rest.service.RootNodeKeyRepository;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;

@Component
public class JcrNodeCollectionHandler extends AbstractHandler {
	private static final Logger logger = LoggerFactory.getLogger(JcrNodeCollectionHandler.class);
	
	private final BsonSyndicationReader BSON_READER = new BsonSyndicationReader();
	private final BsonWriter BSON_WRITER = new BsonWriter();
	
	@Autowired
	private JcrNodeRepository jcrNodeService;
	
	@Autowired 
	private RootNodeKeyRepository rootNodeKeyService;
	
	@Autowired
	protected WcmUtils wcmUtils;
	
	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	public void collectJcrElement(
			String repository,
			String workspace,
    		String id,
    		String operation,
    		long timestamp,
    		String authoringSiteRootNodeKey,
    		String authoringSiteJcrSystemKey,
    		InputStream content) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			RootNodeKeys renderSiteRootNodeKeys = rootNodeKeyService.getRootNodeKeys(repository, workspace);
			Map<String, String> rootNodeKeyMap = new HashMap<>();
			
			rootNodeKeyMap.put(authoringSiteRootNodeKey, renderSiteRootNodeKeys.getRootNodeKey());
			rootNodeKeyMap.put(authoringSiteJcrSystemKey, renderSiteRootNodeKeys.getJcrSystemKey());

			JcrNodeEntry jcrNode = new JcrNodeEntry();
			jcrNode.setId(this.getNodeKey(renderSiteRootNodeKeys.getRootNodeKey(), id));
			jcrNode.setLastUpdated(new Timestamp(timestamp));
			jcrNode.setContent(this.transformBsonStream(content, rootNodeKeyMap));
			if (WcmEventEntry.Operation.create.name().equals(operation)) {
				jcrNodeService.addJcrNode(jcrNode);
			} else {
				jcrNodeService.updateJcrNode(jcrNode);
			} 
		} catch (Throwable t) {
			logger.error("Failed to collect element",t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}
	
	public void collectJcrItem(
			String repository,
			String workspace,
    		String id,
    		String nodePath,
    		String itemType,
    		String operation,
    		List<String> removedDescendants,
    		long timestamp,
       		String rootNodeKey,
    		String jcrSystemKey,
    		InputStream content,
    		String baseUrl) 
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
				jcrNode.setContent(this.transformBsonStream(content, rootNodeKeyMap));
				jcrNodeService.addJcrNode(jcrNode);
			} else if (WcmEventEntry.Operation.update.name().equals(operation)) {
				jcrNode.setContent(this.transformBsonStream(content, rootNodeKeyMap));
				jcrNodeService.updateJcrNode(jcrNode);
			} else {
				jcrNodeService.deleteJcrNode(jcrNode.getId());
			}
			
			if (WcmEventEntry.WcmItemType.authoringTemplate.name().equals(itemType)) {
				AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(
						repository, 
						workspace, 
						nodePath.substring("/library".length()), 
						baseUrl);
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
	}
	
	private String getNodeKey(String rootNodeKey, String nodeId) {
		return String.format("%s%s", rootNodeKey, nodeId);
	}
	
	private InputStream transformBsonStream(InputStream input, Map<String, String> rootNodeKeyMap) throws IOException {
		Document document = BSON_READER.read(input, rootNodeKeyMap);
		return new ByteArrayInputStream(BSON_WRITER.write(document));
	}
}
