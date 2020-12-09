package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.Map;

import javax.jcr.RepositoryException;
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

import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.ContentAreaLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(ContentAreaLayoutRestController.BASE_URI)
@Validated
public class ContentAreaLayoutRestController extends BaseWcmRestController {
	private static final Logger logger = LoggerFactory.getLogger(ContentAreaLayoutRestController.class);
	
	public static final String BASE_URI = "/wcm/api/contentAreaLayout";
	
	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	@GetMapping(path = "/list/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, ContentAreaLayout> getContentAreaLayouts(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			Map<String, ContentAreaLayout> contentAreaLayouts = this.wcmRequestHandler.getContentAreaLayouts(repository, workspace, request);	
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return contentAreaLayouts;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@GetMapping(path = "/get/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ContentAreaLayout getContentAreaLayout(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
			@RequestParam("path") String absPath,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			RestNode contentAreaLayoutNode = (RestNode) this.wcmItemHandler.item(baseUrl, repository,
					workspace, absPath, WcmConstants.CONTENT_AREA_LAYOUT_DEPTH);
			ContentAreaLayout layout = new ContentAreaLayout();
			layout.setRepository(repository);
			layout.setWorkspace(workspace);
			String library = absPath.split("/")[2];
			layout.setLibrary(library);
			this.wcmRequestHandler.toContentAreaLayout(contentAreaLayoutNode, layout);
			return layout;
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e, new WcmError(e.getMessage(), WcmErrors.GET_CONTENT_AREA_LAYOUT_ERROR, new String[] {absPath}));
		}
		
	}
	
	@PutMapping(path = "/lock/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ContentAreaLayout lockContentAreaLayout(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
			@RequestParam("path") String absPath,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			this.wcmRequestHandler.lock(repository, workspace, absPath);
			ContentAreaLayout contentAreaLayout = this.getContentAreaLayout(repository, workspace, absPath, request);
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return contentAreaLayout;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.LOCK_CONTENT_AREA_LAYOUT_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createContentAreaLayout(
			@RequestBody ContentAreaLayout pageLayout, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {

			String repositoryName = pageLayout.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format( WcmConstants.NODE_CONTENT_LAYOUT_PATH_PATTERN, pageLayout.getLibrary(), pageLayout.getName());
			this.wcmItemHandler.addItem(WcmEvent.WcmItemType.contentAreaLayout, baseUrl, repositoryName, pageLayout.getWorkspace(), path, pageLayout.toJson());
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
	
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveContentAreaLayout(
			@RequestBody ContentAreaLayout pageLayout, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		String absPath = String.format( WcmConstants.NODE_CONTENT_LAYOUT_PATH_PATTERN, pageLayout.getLibrary(), pageLayout.getName());
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String repositoryName = pageLayout.getRepository();
			
			JsonNode layoutJson = pageLayout.toJson();
			this.wcmItemHandler.updateItem(WcmEvent.WcmItemType.contentAreaLayout, baseUrl, repositoryName, pageLayout.getWorkspace(), absPath, layoutJson);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.UPDATE_CONTENT_AREA_LAYOUT_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@DeleteMapping("/{repository}/{workspace}")
  	public ResponseEntity<?> purgeContentAreaLayout(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String wcmPath,
  			HttpServletRequest request) { 
  		if (logger.isDebugEnabled()) {
  			logger.debug("Entry");
		}
  		String baseUrl = RestHelper.repositoryUrl(request);
  		String absPath = String.format(wcmPath.startsWith("/") ? WcmConstants.NODE_ROOT_PATH_PATTERN : WcmConstants.NODE_ROOT_REL_PATH_PATTERN, wcmPath);
  		try {
	  		
  			this.wcmItemHandler.deleteItem(WcmEvent.WcmItemType.contentAreaLayout, baseUrl, repository, workspace, absPath);
  			
  	  		if (logger.isDebugEnabled()) {
  	  		logger.debug("Exit");
  			}
  	  		
  			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(String.format("Failed to delete item %s from expired repository. Content item does not exist", absPath), e);
			throw e;
	    } catch (Throwable t) {
	    	logger.error("Failed to purge content", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}

  	};
}
