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
import com.bpwizard.wcm.repo.rest.handler.WcmRequestHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.Form;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEventEntry;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(FormRestController.BASE_URI)
@Validated
public class FormRestController extends BaseWcmRestController {
	private static final Logger logger = LoggerFactory.getLogger(FormRestController.class);

	public static final String BASE_URI = "/wcm/api/form";
	
	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	@GetMapping(path = "/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Form> getForms(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			Map<String, Form> forms = this.wcmRequestHandler.getForms(repository, workspace, request);
	
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return forms;
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to get form", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to get form", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@GetMapping(path = "/get/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Form getForm(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@RequestParam("path") String formPath,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			Form form = this.wcmRequestHandler.getForm(repository, workspace, formPath, request);
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return form;
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to get form", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to get form", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createForm(
			@RequestBody Form form, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WcmConstants.NODE_FORM_PATH_PATTERN, form.getLibrary(), form.getName());
			String repositoryName = form.getRepository();
			this.wcmItemHandler.addItem(WcmEventEntry.WcmItemType.form, baseUrl,  repositoryName, WcmConstants.DEFAULT_WS, path, form.toJson());
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to create form", e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error("Failed to create form", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.CREATE_FORM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error("Failed to create form", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveForm(
			@RequestBody Form form, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WcmConstants.NODE_FORM_PATH_PATTERN, form.getLibrary(), form.getName());
			String repositoryName = form.getRepository();
				
			JsonNode formJson = form.toJson();
			this.wcmItemHandler.updateItem(WcmEventEntry.WcmItemType.form, baseUrl, repositoryName, WcmConstants.DEFAULT_WS, path, formJson);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to save form", e);
			throw e;
		} catch (RepositoryException re) {
			logger.error("Failed to save form", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.UPDATE_FORM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error("Failed to save form", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@DeleteMapping("/{repository}/{workspace}")
  	public ResponseEntity<?> purgeForm(
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
	  		
  			this.wcmItemHandler.deleteItem(WcmEventEntry.WcmItemType.form, baseUrl, repository, workspace, absPath);
  			
  	  		if (logger.isDebugEnabled()) {
  				logger.debug("Exit");
  			}
  	  		
  			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(String.format("Failed to delete item %s from expired repository. Content item does not exist", absPath), e);
			throw e;
	    } catch (Throwable t) {
	    	logger.error("Failed to purge form", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}

  	};
}
