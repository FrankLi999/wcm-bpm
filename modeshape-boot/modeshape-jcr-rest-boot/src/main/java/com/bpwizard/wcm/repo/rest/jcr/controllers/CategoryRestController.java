package com.bpwizard.wcm.repo.rest.jcr.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEventEntry;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(CategoryRestController.BASE_URI)
@Validated
public class CategoryRestController extends BaseWcmRestController {
	private static final Logger logger = LoggerFactory.getLogger(CategoryRestController.class);
	
	public static final String BASE_URI = "/wcm/api/category";
	
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCategory(
			@RequestBody Category category, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			String repositoryName = category.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = (StringUtils.hasText(category.getParent())) ? 
					String.format(WcmConstants.NODE_CATEGORY_SUB_PATH_PATTERN, category.getLibrary(), category.getParent(), category.getName()) :
					String.format(WcmConstants.NODE_CATEGORY_PATH_PATTERN, category.getLibrary(), category.getName());
			this.wcmItemHandler.addItem(
					WcmEventEntry.WcmItemType.category,
					baseUrl, 
					repositoryName,
					category.getWorkspace(),
					path, 
					category.toJson());
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
	
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to create category", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to create category", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}	
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCategory(
			@RequestBody Category category, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			String repositoryName = category.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = (StringUtils.hasText(category.getParent())) ? 
					String.format(WcmConstants.NODE_CATEGORY_SUB_PATH_PATTERN, category.getLibrary(), category.getParent(), category.getName()) :
					String.format(WcmConstants.NODE_CATEGORY_PATH_PATTERN, category.getLibrary(), category.getName());
			
			JsonNode categoryJson = category.toJson();
			this.wcmItemHandler.updateItem(WcmEventEntry.WcmItemType.category, baseUrl, repositoryName, category.getWorkspace(), path, categoryJson);
			if (this.authoringEnabled) {
				this.wcmItemHandler.updateItem(WcmEventEntry.WcmItemType.category, baseUrl, repositoryName, WcmConstants.DRAFT_WS, path, categoryJson);
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to update category", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to update category", t);
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
			logger.debug("Entry");
		}
  		String baseUrl = RestHelper.repositoryUrl(request);
  		String absPath = String.format(wcmPath.startsWith("/") ? WcmConstants.NODE_ROOT_PATH_PATTERN : WcmConstants.NODE_ROOT_REL_PATH_PATTERN, wcmPath);
  		try {
	  		this.wcmItemHandler.deleteItem(WcmEventEntry.WcmItemType.category, baseUrl, repository, workspace, absPath);
  			if (logger.isDebugEnabled()) {
  				logger.debug("Exit");
  			}
  	  		
  			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(String.format("Failed to delete item %s from expired repository. Content item does not exist", absPath), e);
			throw e;
	    } catch (Throwable t) {
	    	logger.error("Failed to purge category", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}

  	};
}
