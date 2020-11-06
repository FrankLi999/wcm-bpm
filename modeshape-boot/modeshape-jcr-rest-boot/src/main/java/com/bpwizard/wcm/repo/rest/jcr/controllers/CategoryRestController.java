package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.bpwizard.wcm.repo.rest.jcr.model.Category;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(CategoryRestController.BASE_URI)
@Validated
public class CategoryRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(CategoryRestController.class);
	
	public static final String BASE_URI = "/wcm/api/category";
	
	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCategory(
			@RequestBody Category category, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = category.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = (StringUtils.hasText(category.getParent())) ? 
					String.format(WcmConstants.NODE_CATEGORY_SUB_PATH_PATTERN, category.getLibrary(), category.getParent(), category.getName()) :
					String.format(WcmConstants.NODE_CATEGORY_PATH_PATTERN, category.getLibrary(), category.getName());
			this.itemHandler.addItem(
					baseUrl, 
					repositoryName,
					category.getWorkspace(),
					path, 
					category.toJson());
			if (this.authoringEnabled) {
				Session session = repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
				session.getWorkspace().clone(WcmConstants.DEFAULT_WS, path, path, true);
				// session.save();
			}
			
			if (this.syndicationEnabled) {
				RestNode restNode = (RestNode)this.itemHandler.item(baseUrl, repositoryName, category.getWorkspace(), path, WcmConstants.FULL_SUB_DEPTH);
				syndicationUtils.addNewItemEvent(
						restNode, 
						repositoryName, 
						category.getWorkspace(), 
						path,
						WcmEvent.WcmItemType.siteConfig);
			}
		
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
	
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}	
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCategory(
			@RequestBody Category category, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = category.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = (StringUtils.hasText(category.getParent())) ? 
					String.format(WcmConstants.NODE_CATEGORY_SUB_PATH_PATTERN, category.getLibrary(), category.getParent(), category.getName()) :
					String.format(WcmConstants.NODE_CATEGORY_PATH_PATTERN, category.getLibrary(), category.getName());
			
			List<String> currentDescendants = new ArrayList<String>();		
			if (this.syndicationEnabled) {
				RestNode restNode = (RestNode)this.itemHandler.item(baseUrl, repositoryName, category.getWorkspace(), path, WcmConstants.FULL_SUB_DEPTH);
				syndicationUtils.populateDescendantIds(restNode, currentDescendants);
			}		
			JsonNode categoryJson = category.toJson();
			this.itemHandler.updateItem(baseUrl, repositoryName, category.getWorkspace(), path, categoryJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, WcmConstants.DRAFT_WS, path, categoryJson);
			}
			if (this.syndicationEnabled) {
				RestNode restNode = (RestNode)this.itemHandler.item(baseUrl, repositoryName, category.getWorkspace(), path, WcmConstants.FULL_SUB_DEPTH);
				
				syndicationUtils.addUpdateItemEvent(
						restNode, 
						repositoryName, 
						category.getWorkspace(),  
						path,
						WcmEvent.WcmItemType.category,
						currentDescendants);
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@DeleteMapping("/{repository}/{workspace}")
  	public ResponseEntity<?> purgeCategory(
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
	  		List<String> currentDescendants = new ArrayList<String>();	
	  		String nodeId = null;
			if (this.syndicationEnabled) {
				RestNode restNode = (RestNode)this.itemHandler.item(baseUrl, repository, workspace, absPath, WcmConstants.FULL_SUB_DEPTH);
				nodeId = restNode.getId();
				syndicationUtils.populateDescendantIds(restNode, currentDescendants);
			}	
		
  			this.wcmRequestHandler.purgeWcmItem(repository, workspace, absPath);
  			if (this.syndicationEnabled) {
				syndicationUtils.addDeleteItemEvent(
						nodeId, 
						repository, 
						workspace, 
						wcmPath,
						WcmEvent.WcmItemType.category,
						currentDescendants);
			}
  			
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
