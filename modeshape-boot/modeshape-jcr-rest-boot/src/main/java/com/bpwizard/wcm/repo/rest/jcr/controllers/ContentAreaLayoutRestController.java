package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.ContentAreaLayout;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(ContentAreaLayoutRestController.BASE_URI)
@Validated
public class ContentAreaLayoutRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(ContentAreaLayoutRestController.class);
	
	public static final String BASE_URI = "/wcm/api/contentAreaLayout";
	
	@GetMapping(path = "/list/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, ContentAreaLayout> getContentAreaLayouts(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Map<String, ContentAreaLayout> contentAreaLayouts = this.doGetContentAreaLayouts(repository, workspace, request);	
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return contentAreaLayouts;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
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
			RestNode contentAreaLayoutNode = (RestNode) this.itemHandler.item(baseUrl, repository,
					workspace, absPath, WcmConstants.CONTENT_AREA_LAYOUT_DEPTH);
			ContentAreaLayout layout = new ContentAreaLayout();
			layout.setRepository(repository);
			layout.setWorkspace(workspace);
			String library = absPath.split("/")[2];
			layout.setLibrary(library);
			this.toContentAreaLayout(contentAreaLayoutNode, layout);
			return layout;
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
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
			logger.traceEntry();
		}
		try {
			this.doLock(repository, workspace, absPath);
			ContentAreaLayout contentAreaLayout = this.getContentAreaLayout(repository, workspace, absPath, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return contentAreaLayout;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createContentAreaLayout(
			@RequestBody ContentAreaLayout pageLayout, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {

			String repositoryName = pageLayout.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format( WcmConstants.NODE_CONTENT_LAYOUT_PATH_PATTERN, pageLayout.getLibrary(), pageLayout.getName());
			this.itemHandler.addItem(baseUrl, repositoryName, pageLayout.getWorkspace(), path, pageLayout.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
				session.getWorkspace().clone(WcmConstants.DEFAULT_WS, path, path, true);
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
	public void saveContentAreaLayout(
			@RequestBody ContentAreaLayout pageLayout, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format( WcmConstants.NODE_CONTENT_LAYOUT_PATH_PATTERN, pageLayout.getLibrary(), pageLayout.getName());
			String repositoryName = pageLayout.getRepository();
			JsonNode layoutJson = pageLayout.toJson();
			this.itemHandler.updateItem(baseUrl, repositoryName, pageLayout.getWorkspace(), path, layoutJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, WcmConstants.DRAFT_WS, path, layoutJson);
//				
//				Session session = this.repositoryManager.getSession(repositoryName, DRAFT_WS);
//				session.getWorkspace().clone(DEFAULT_WS, path, path, true);
				// session.save();
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
}
