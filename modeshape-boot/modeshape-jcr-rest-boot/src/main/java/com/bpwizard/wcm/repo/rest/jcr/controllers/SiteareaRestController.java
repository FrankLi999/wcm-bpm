package com.bpwizard.wcm.repo.rest.jcr.controllers;

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
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteArea;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(SiteareaRestController.BASE_URI)
@Validated
public class SiteareaRestController extends BaseWcmRestController {

	public static final String BASE_URI = "/wcm/api/sitearea";
	private static final Logger logger = LoggerFactory.getLogger(SiteareaRestController.class);

	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSiteArea(@RequestBody SiteArea sa, HttpServletRequest request)
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		String absPath = WcmUtils.nodePath(sa.getWcmPath(),sa.getName());
		try {	
			String repositoryName = sa.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			this.wcmItemHandler.addItem(WcmEvent.WcmItemType.siteArea, baseUrl, repositoryName, sa.getWorkspace(), absPath, sa.toJson());
			
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e) {
			logger.error("Failed to create site area", e);
			throw e;
		} catch (RepositoryException re) {
			logger.error("Failed to create site area", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.CREATE_SA_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
	    	logger.error("Failed to create site area", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveSiteArea(@RequestBody SiteArea sa, HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		String absPath = WcmUtils.nodePath(sa.getWcmPath());
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String repositoryName = sa.getRepository();
			
			JsonNode saJson = sa.toJson();
			this.wcmItemHandler.updateItem(WcmEvent.WcmItemType.siteArea, baseUrl, repositoryName, sa.getWorkspace(), absPath, saJson);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
		} catch (WcmRepositoryException e) {
			logger.error("Failed to save site area", e);
			throw e;
		} catch (RepositoryException re) {
			logger.error("Failed to save site area", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.UPDATE_SA_ERROR, new String[] {absPath}));
		} catch (Throwable t) {
			logger.error("Failed to save site area", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@GetMapping(path = "/get/{repository}/{workspace}/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public SiteArea getSiteArea(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, @RequestParam("path") String wcmPath,
			HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String absPath = WcmUtils.nodePath(wcmPath);
			RestNode saNode = (RestNode) this.wcmItemHandler.item(baseUrl, repository, workspace, absPath, WcmConstants.SITE_AREA_DEPTH);

			SiteArea sa = new SiteArea();
			sa.setRepository(repository);
			sa.setWorkspace(workspace);
			sa.setWcmPath(wcmPath);
			sa.setWcmAuthority(WcmUtils.getWcmAuthority(wcmPath));
			this.wcmRequestHandler.loadSiteArea(saNode, sa);

			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return sa;
		} catch (WcmRepositoryException e) {
			logger.error("Failed to get site area", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to get site area", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@PutMapping(path = "/lock/{repository}/{workspace}/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public SiteArea lockSiteArea(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, @RequestParam("path") String wcmPath,
			HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		String absPath = WcmUtils.nodePath(wcmPath);
		try {
			this.wcmRequestHandler.lock(repository, workspace, absPath);
			SiteArea siteArea = this.getSiteArea(repository, workspace, absPath, request);
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return siteArea;
		} catch (WcmRepositoryException e) {
			logger.error("Failed to lock site area", e);
			throw e;
		} catch (RepositoryException re) {
			logger.error("Failed to lock site area", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.LOCK_SA_ERROR, new String[] {absPath}));
		} catch (Throwable t) {
			logger.error("Failed to lock site area", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@DeleteMapping("/{repository}/{workspace}")
  	public ResponseEntity<?> purgeSiteArea(
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
	  		this.wcmItemHandler.deleteItem(WcmEvent.WcmItemType.siteArea, baseUrl, repository, workspace, absPath);
  			
  	  		if (logger.isDebugEnabled()) {
  				logger.debug("Exit");
  			}
  	  		
  			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(String.format("Failed to delete item %s from expired repository. Content item does not exist", absPath), e);
			throw e;
	    } catch (Throwable t) {
	    	logger.error("Failed to purge site area", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}

  	};
}
