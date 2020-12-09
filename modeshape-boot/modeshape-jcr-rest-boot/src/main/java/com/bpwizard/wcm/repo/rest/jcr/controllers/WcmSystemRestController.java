package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.Theme;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmOperation;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmRepository;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmSystem;

@RestController
@RequestMapping(WcmSystemRestController.BASE_URI)
@Validated
public class WcmSystemRestController {
	private static final Logger logger = LoggerFactory.getLogger(WcmSystemRestController.class);

	public static final String BASE_URI = "/wcm/api";

	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	// http://localhost:8080/wcm/api/wcmSystem/bpwizard/default/camunda/bpm
	@GetMapping(path = "/wcmSystem/{repository}/{workspace}/{library}/{siteConfig}", produces = MediaType.APPLICATION_JSON_VALUE)
	public WcmSystem getWcmSystem(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, @PathVariable("library") String library,
			@PathVariable("siteConfig") String siteConfigName, 
			@RequestParam(name="authoring", defaultValue = "true") boolean authoring,
			HttpServletRequest request)
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			WcmSystem wcmSystem = this.wcmRequestHandler.getWcmSystem(repository, workspace, library, siteConfigName, authoring, request);
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return wcmSystem;
		} catch (WcmRepositoryException e) {
			logger.error("Failed to get WcmSystem", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to get WcmSystem", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@GetMapping(path = "/wcmRepository/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public WcmRepository[] getWcmRepositories(HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			WcmRepository[] wcmRepositories = this.wcmRequestHandler.getWcmRepositories(request);
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return wcmRepositories;
		} catch (WcmRepositoryException e) {
			logger.error("Failed to get WcmRepositories", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to get WcmRepositories", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@GetMapping(path = "/wcmOperation/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, WcmOperation[]> getWcmOperations(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			Map<String, WcmOperation[]> wcmOperationMap = this.wcmRequestHandler.getWcmOperations(repository, workspace, request);
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return wcmOperationMap;
		} catch (WcmRepositoryException e) {
			logger.error("Failed to get Wcm operations", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to get Wcm operations", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@GetMapping(path = "/theme/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Theme[] getTheme(@PathVariable("repository") String repository, @PathVariable("workspace") String workspace,
			HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		
		Theme[] themes = this.wcmRequestHandler.getTheme(repository, workspace, request);

		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
		return themes;
	}
}