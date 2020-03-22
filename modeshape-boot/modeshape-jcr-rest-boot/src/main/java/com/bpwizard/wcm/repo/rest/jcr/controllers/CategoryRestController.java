package com.bpwizard.wcm.repo.rest.jcr.controllers;

import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.Category;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(CategoryRestController.BASE_URI)
@Validated
public class CategoryRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(CategoryRestController.class);
	
	public static final String BASE_URI = "/wcm/api/category";
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
					String.format(WCM_CATEGORY_SUB_PATH_PATTERN, category.getLibrary(), category.getParent(), category.getName()) :
					String.format(WCM_CATEGORY_PATH_PATTERN, category.getLibrary(), category.getName());
			this.itemHandler.addItem(
					baseUrl, 
					repositoryName,
					category.getWorkspace(),
					path, 
					category.toJson());
			if (this.authoringEnabled) {
				Session session = repositoryManager.getSession(repositoryName, DRAFT_WS);
				session.getWorkspace().clone(DEFAULT_WS, path, path, true);
				// session.save();
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
	
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
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
					String.format(WCM_CATEGORY_SUB_PATH_PATTERN, category.getLibrary(), category.getParent(), category.getName()) :
					String.format(WCM_CATEGORY_PATH_PATTERN, category.getLibrary(), category.getName());
			JsonNode categoryJson = category.toJson();
			this.itemHandler.updateItem(baseUrl, repositoryName, category.getWorkspace(), path, categoryJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, DRAFT_WS, path, categoryJson);
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}	
}
