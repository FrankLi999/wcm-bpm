package com.bpwizard.wcm.repo.rest.jcr.controllers;

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
import com.bpwizard.wcm.repo.rest.jcr.model.PageConfig;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteConfig;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(SiteConfigRestController.BASE_URI)
@Validated
public class SiteConfigRestController extends BaseWcmRestController {

	public static final String BASE_URI = "/wcm/api/siteConfig";
	private static final Logger logger = LogManager.getLogger(SiteConfigRestController.class);

	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSiteConfig(@RequestBody SiteConfig siteConfig, HttpServletRequest request)
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = siteConfig.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String absPath = String.format(WcmConstants.NODE_SITECONFIG_PATH_PATTERN, siteConfig.getLibrary(), siteConfig.getName());
			this.wcmItemHandler.addItem(WcmEvent.WcmItemType.siteConfig, baseUrl, repositoryName, WcmConstants.DEFAULT_WS, absPath, siteConfig.toJson());
			
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}

			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveSiteConfig(@RequestBody SiteConfig siteConfig, HttpServletRequest request)
			throws WcmRepositoryException {
		try {
			String absPath = String.format(WcmConstants.NODE_SITECONFIG_PATH_PATTERN, siteConfig.getLibrary(), siteConfig.getName());
			String repositoryName = siteConfig.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			
			JsonNode jsonItem = siteConfig.toJson();
			this.wcmItemHandler.updateItem(WcmEvent.WcmItemType.siteConfig, baseUrl, repositoryName, WcmConstants.DEFAULT_WS, absPath, jsonItem);
		} catch (WcmRepositoryException e) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@GetMapping("/get/{repository}/{workspace}/{library}")
	public SiteConfig[] getSiteConfigs(
			HttpServletRequest request, 
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
			@PathVariable("library") String library) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String absPath = String.format(WcmConstants.NODE_SITECONFIG_PATH, library);
			RestNode siteConfigFolder = (RestNode) this.wcmItemHandler.item(baseUrl, repository, workspace, absPath, WcmConstants.SITE_CONFIG_DEPTH);
			SiteConfig[] siteConfigs = siteConfigFolder.getChildren().stream()
					.filter(node -> this.wcmRequestHandler.isSiteConfig(node))
					.map(node -> this.wcmRequestHandler.toSiteConfig(repository, workspace, library, node))
					.toArray(SiteConfig[]::new);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return siteConfigs;
		} catch (WcmRepositoryException e) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@PutMapping("/get/loclk/{workspace}/{library}/{siteConfig}")
	public SiteConfig lockSiteConfig(HttpServletRequest request, @PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, @PathVariable("library") String library,
			@PathVariable("siteConfigName") String siteConfigName) throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String absPath = String.format(WcmConstants.NODE_SITECONFIG_PATH_PATTERN, library, siteConfigName);
			this.wcmRequestHandler.lock(repository, workspace, absPath);
			SiteConfig siteConfig = this.wcmRequestHandler.getSiteConfig(request, repository, workspace, library, siteConfigName);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return siteConfig;
		} catch (WcmRepositoryException e) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	// http://localhost:8080/wcm/api/wcmSystem/bpwizard/default/camunda/bpm
	@GetMapping(path = "/pageConfig/{repository}/{workspace}/{library}/{siteConfig}", produces = MediaType.APPLICATION_JSON_VALUE)
	public PageConfig getPageConfig(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, @PathVariable("library") String library,
			@PathVariable("siteConfig") String siteConfigName, HttpServletRequest request)
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			PageConfig pageConfig = new PageConfig();
			SiteConfig siteConfig = this.wcmRequestHandler.getSiteConfig(request, repository, workspace, library, siteConfigName);
			pageConfig.setSiteConfig(siteConfig);
			pageConfig.setNavigations(
					this.wcmRequestHandler.getNavigations(baseUrl, repository, workspace, library, siteConfig.getRootSiteArea()));
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return pageConfig;
		} catch (WcmRepositoryException e) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@DeleteMapping("/{repository}/{workspace}")
  	public ResponseEntity<?> purgeSiteConfig(
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
	  		
  			this.wcmItemHandler.deleteItem(WcmEvent.WcmItemType.siteConfig, baseUrl, repository, workspace, absPath);
  			
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
