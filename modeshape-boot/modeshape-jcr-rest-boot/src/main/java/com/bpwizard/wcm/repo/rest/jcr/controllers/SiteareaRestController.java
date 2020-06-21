package com.bpwizard.wcm.repo.rest.jcr.controllers;

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
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteArea;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(SiteareaRestController.BASE_URI)
@Validated
public class SiteareaRestController extends BaseWcmRestController {

	public static final String BASE_URI = "/wcm/api/sitearea";
	private static final Logger logger = LogManager.getLogger(SiteareaRestController.class);

	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSiteArea(@RequestBody SiteArea sa, HttpServletRequest request)
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		String absPath = WcmUtils.nodePath(sa.getWcmPath(),sa.getName());
		try {	
			String repositoryName = sa.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, repositoryName, sa.getWorkspace(), absPath, sa.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
				session.getWorkspace().clone(WcmConstants.DEFAULT_WS, absPath, absPath, true);
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) {
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.CREATE_SA_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveSiteArea(@RequestBody SiteArea sa, HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		String absPath = WcmUtils.nodePath(sa.getWcmPath());
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String repositoryName = sa.getRepository();
			JsonNode saJson = sa.toJson();
			this.itemHandler.updateItem(baseUrl, repositoryName, sa.getWorkspace(), absPath, saJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, WcmConstants.DRAFT_WS, absPath, saJson);
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
		} catch (WcmRepositoryException e) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) {
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.UPDATE_SA_ERROR, new String[] {absPath}));
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@GetMapping(path = "/get/{repository}/{workspace}/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public SiteArea getSiteArea(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, @RequestParam("path") String wcmPath,
			HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String absPath = WcmUtils.nodePath(wcmPath);
			RestNode saNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace, absPath, WcmConstants.SITE_AREA_DEPTH);

			SiteArea sa = new SiteArea();
			sa.setRepository(repository);
			sa.setWorkspace(workspace);
			sa.setWcmPath(wcmPath);
			this.loadSiteArea(saNode, sa);

			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return sa;
		} catch (WcmRepositoryException e) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@PutMapping(path = "/lock/{repository}/{workspace}/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public SiteArea lockSiteArea(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, @RequestParam("path") String wcmPath,
			HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		String absPath = WcmUtils.nodePath(wcmPath);
		try {
			this.doLock(repository, workspace, absPath);
			SiteArea siteArea = this.getSiteArea(repository, workspace, absPath, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return siteArea;
		} catch (WcmRepositoryException e) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) {
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.LOCK_SA_ERROR, new String[] {absPath}));
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

}
