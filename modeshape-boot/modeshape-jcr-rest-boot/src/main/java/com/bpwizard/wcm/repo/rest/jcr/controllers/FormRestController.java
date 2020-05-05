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
import com.bpwizard.wcm.repo.rest.jcr.model.Form;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(FormRestController.BASE_URI)
@Validated
public class FormRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(FormRestController.class);

	public static final String BASE_URI = "/wcm/api/form";
	
	@GetMapping(path = "/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Form> getForms(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Map<String, Form> forms = this.doGetForms(repository, workspace, request);
	
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return forms;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
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
			logger.traceEntry();
		}
		Form form = this.doGetForm(repository, workspace, formPath, request);
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return form;
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createForm(
			@RequestBody Form form, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WcmConstants.NODE_FORM_PATH_PATTERN, form.getLibrary(), form.getName());
			String repositoryName = form.getRepository();
			this.itemHandler.addItem(baseUrl,  repositoryName, WcmConstants.DEFAULT_WS, path, form.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
				session.getWorkspace().clone(WcmConstants.DEFAULT_WS, path, path, true);
			}
			
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveForm(
			@RequestBody Form form, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WcmConstants.NODE_FORM_PATH_PATTERN, form.getLibrary(), form.getName());
			String repositoryName = form.getRepository();
			JsonNode formJson = form.toJson();
			this.itemHandler.updateItem(baseUrl, repositoryName, WcmConstants.DEFAULT_WS, path, formJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, WcmConstants.DRAFT_WS, path, formJson);
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
}
