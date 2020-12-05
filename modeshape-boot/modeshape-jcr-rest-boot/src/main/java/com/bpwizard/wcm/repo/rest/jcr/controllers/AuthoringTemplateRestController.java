package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.Map;

import javax.jcr.RepositoryException;
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

import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(AuthoringTemplateRestController.BASE_URI)
@Validated
public class AuthoringTemplateRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(AuthoringTemplateRestController.class);

	public static final String BASE_URI = "/wcm/api/at";
	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	@GetMapping(path = "/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, AuthoringTemplate> getAuthoringTemplates(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Map<String, AuthoringTemplate> authoringTemplates = this.wcmRequestHandler.getAuthoringTemplates(repository, workspace, request);
	
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return authoringTemplates;
		} catch (WcmRepositoryException e) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@GetMapping(path = "/get/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AuthoringTemplate getAuthoringTemplate(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@RequestParam("path") String atPath,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(repository, workspace, atPath, request);
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return at;
	}

	@PutMapping(path = "/lock/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AuthoringTemplate lockAuthoringTemplate(
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
			AuthoringTemplate at = this.getAuthoringTemplate(repository, workspace, absPath, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return at;
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.LOCK_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "/{repository}/{workspace}/jcrTpe", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthoringTemplateJcrType(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@RequestParam("path") String atPath, 
			HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(repository, workspace, atPath, request);
			this.wcmUtils.registerNodeType(at.getWorkspace(), at);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.LOCK_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthoringTemplate(
			@RequestBody AuthoringTemplate at, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WcmConstants.NODE_AT_PATH_PATTERN, at.getLibrary(), at.getName());
			String repositoryName = at.getRepository();
			this.wcmItemHandler.addItem(WcmEvent.WcmItemType.authoringTemplate, baseUrl,  repositoryName, WcmConstants.DEFAULT_WS, path, at.toJson());
			this.wcmUtils.registerNodeType(at.getWorkspace(), at);
			if (this.authoringEnabled && WcmConstants.DEFAULT_WS.equals(at.getWorkspace())) {
				this.wcmUtils.registerNodeType(WcmConstants.DRAFT_WS, at);
				this.wcmUtils.registerNodeType(WcmConstants.EXPIRED_WS, at);
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
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.LOCK_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}

	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveAuthoringTemplate(
			@RequestBody AuthoringTemplate at, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WcmConstants.NODE_AT_PATH_PATTERN, at.getLibrary(), at.getName());
			String repositoryName = at.getRepository();
			
			JsonNode atJson = at.toJson();
			this.wcmItemHandler.updateItem(WcmEvent.WcmItemType.authoringTemplate, baseUrl, repositoryName, WcmConstants.DEFAULT_WS, path, atJson);
			this.wcmUtils.registerNodeType(at.getWorkspace(), at);
			if (this.authoringEnabled && WcmConstants.DEFAULT_WS.equals(at.getWorkspace())) {
				this.wcmUtils.registerNodeType(WcmConstants.DRAFT_WS, at);
				this.wcmUtils.registerNodeType(WcmConstants.EXPIRED_WS, at);
			}
			
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.UPDATE_AT_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@DeleteMapping("/{repository}/{workspace}")
  	public ResponseEntity<?> purgeAuthoringTemplate(
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
	  		this.wcmItemHandler.deleteItem(WcmEvent.WcmItemType.authoringTemplate, baseUrl, repository, workspace, absPath);
  			
	  		if (this.authoringEnabled && WcmConstants.DEFAULT_WS.equals(workspace)) {
//	  			this.wcmUtils.deleteNodeType(WcmConstants.DEFAULT_WS, wcmPath);
//				this.wcmUtils.deleteNodeType(WcmConstants.DRAFT_WS, wcmPath);
//				this.wcmUtils.deleteNodeType(WcmConstants.EXPIRED_WS, wcmPath);
			}
	  		
  			//TODO: delete the corresponding JCR type also.
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
