package com.bpwizard.wcm.repo.rest.handler;

import java.io.InputStream;

import javax.jcr.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEventEntry;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;

@Component
public class JsonNodeCollectionHandler extends AbstractHandler {
	private static final Logger logger = LoggerFactory.getLogger(JsonNodeCollectionHandler.class);
	@Autowired
	private WcmRequestHandler wcmRequestHandler;

	@Autowired
	protected WcmUtils wcmUtils;
	@Autowired
	protected RestWcmItemHandler wcmItemHandler;
	
	public void collectJsonItem(
			String repository,
			String workspace,
    		String nodePath,
    		String itemType,
    		String operation,
    		InputStream inputStream,
			String baseUrl) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		
		try {
			
			if (WcmEventEntry.Operation.create.name().equals(operation)) {
				this.wcmItemHandler.addItem(WcmEventEntry.WcmItemType.valueOf(itemType), baseUrl,  
						repository, WcmConstants.DEFAULT_WS, nodePath.substring("/library".length()), 
						JsonUtils.inputStreamToJsonNode(inputStream));
				
			} else if (WcmEventEntry.Operation.update.name().equals(operation)) {
				this.wcmItemHandler.updateItem(WcmEventEntry.WcmItemType.valueOf(itemType), baseUrl,  
						repository, WcmConstants.DEFAULT_WS, nodePath.substring("/library".length()), 
						JsonUtils.inputStreamToJsonNode(inputStream));
				if (WcmEventEntry.WcmItemType.authoringTemplate.name().equals(itemType)) {
					AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(
							repository, 
							workspace, 
							nodePath.substring("/library".length()), 
							baseUrl);
					this.wcmUtils.registerNodeType(at.getWorkspace(), at);
				}
			} else if (WcmEventEntry.Operation.update.name().equals(operation)) {
				String wcmPath = nodePath.substring("/library".length());
				this.wcmItemHandler.updateItem(WcmEventEntry.WcmItemType.valueOf(itemType), baseUrl,  
						repository, WcmConstants.DEFAULT_WS, wcmPath, 
						JsonUtils.inputStreamToJsonNode(inputStream));
				if (WcmEventEntry.WcmItemType.authoringTemplate.name().equals(itemType)) {
					AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(
							repository, 
							workspace, 
							wcmPath, 
							baseUrl);
					this.wcmUtils.registerNodeType(at.getWorkspace(), at);
				}
			} else if (WcmEventEntry.Operation.delete.name().equals(operation)) {
				this.wcmItemHandler.deleteItem(repository, workspace, nodePath.substring("/library".length()));
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
}
