package com.bpwizard.wcm.repo.rest.jcr.controllers;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.ModeshapeUtils;
import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.ContentItem;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(ContentItemRestController.BASE_URI)
@Validated
public class ContentItemRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(ContentItemRestController.class);
	
	public static final String BASE_URI = "/wcm/api/contentItem";

	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	@PostMapping(path = "/create-publish", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAndPublishContentItem(
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			contentItem.getProperties().setAuthor(WcmUtils.getCurrentUsername());
			this.wcmRequestHandler.setWorkflowStage(contentItem, WcmConstants.WORKFLOW_STATGE_PUBLISHED);
			AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(contentItem.getRepository(), contentItem.getWorkspace(), 
					contentItem.getAuthoringTemplate(), request);
			if (at.getContentItemAcl() != null) {
			    contentItem.setAcl(at.getContentItemAcl().getOnPublishPermissions());
			}
			String absPath = WcmUtils.nodePath(contentItem.getWcmPath(), contentItem.getProperties().getName());
			String baseUrl = RestHelper.repositoryUrl(request);
			
			this.wcmItemHandler.addItem(WcmEvent.WcmItemType.contentItem, baseUrl, contentItem.getRepository(), WcmConstants.DEFAULT_WS, absPath, contentItem.toJson(at));
			Session session = this.repositoryManager.getSession(contentItem.getRepository(), WcmConstants.DEFAULT_WS);
			if (contentItem.getAcl() != null) {
				ModeshapeUtils.grantPermissions(session, absPath, contentItem.getAcl());
			}
			
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) {
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.CREATE_PUBLISH_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	
	@PutMapping(path = "/update-published", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateContentItem(@RequestBody ContentItem contentItem, HttpServletRequest request) { 
		String absPath = WcmUtils.nodePath(contentItem.getWcmPath());
		try {
			this.wcmRequestHandler.setWorkflowStage(contentItem, WcmConstants.WORKFLOW_STATGE_PUBLISHED);
			AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(contentItem.getRepository(), contentItem.getWorkspace(), 
					contentItem.getAuthoringTemplate(), request);
			String baseUrl = RestHelper.repositoryUrl(request);
			if (at.getContentItemAcl() != null) {
			    contentItem.setAcl(at.getContentItemAcl().getOnPublishPermissions());
			}
			
			JsonNode contentItemJson = contentItem.toJson(at);
			this.wcmItemHandler.updateItem(WcmEvent.WcmItemType.contentItem, baseUrl, contentItem.getRepository(), contentItem.getWorkspace(), absPath, contentItemJson);
			Session session = this.repositoryManager.getSession(contentItem.getRepository(), WcmConstants.DEFAULT_WS);
			if (contentItem.getAcl() != null) {
				ModeshapeUtils.grantPermissions(session, absPath, contentItem.getAcl());
			}
			this.wcmUtils.unlock(contentItem.getRepository(), contentItem.getWorkspace(), absPath);
			
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.UPDATE_CONTENT_ITEM_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
	    	logger.error(t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@GetMapping(path = "/get/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ContentItem getContentItem(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
		    @RequestParam("path") String wcmPath,
		    HttpServletRequest request) 
		    throws WcmRepositoryException {
		
		return this.wcmRequestHandler.getContentItem(repository, workspace, wcmPath, request);
	}
	
	@PutMapping(path = "/lock/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ContentItem lockContentItem(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
		    @RequestParam("path") String absPath,
		    HttpServletRequest request) 
		    throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			this.wcmRequestHandler.lock(repository, workspace, absPath);
			ContentItem contentItem = this.getContentItem(repository, workspace, absPath, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return contentItem;
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) {
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.LOCK_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}
	
	@DeleteMapping("/{repository}/{workspace}")
  	public ResponseEntity<?> purgeContentItem(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String wcmPath,
  			HttpServletRequest request) { 
  		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
  		String baseUrl = RestHelper.repositoryUrl(request);
  		String absPath = String.format(wcmPath.startsWith("/") ? WcmConstants.NODE_ROOT_PATH_PATTERN : WcmConstants.NODE_ROOT_REL_PATH_PATTERN, wcmPath);
  		try {
	  		
  			this.wcmItemHandler.deleteItem(WcmEvent.WcmItemType.contentItem, baseUrl, repository, workspace, absPath);
  			
  	  		if (logger.isDebugEnabled()) {
  				logger.traceExit();
  			}
  	  		
  			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(String.format("Failed to delete item %s from expired repository. Content item does not exist", absPath), e);
			throw e;
	    } catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}

  	};
}
