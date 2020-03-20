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
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteArea;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(SiteareaRestController.BASE_URI)
@Validated
public class SiteareaRestController extends BaseWcmRestController {

	public static final String BASE_URI = "/wcm/api/siteArea";
	private static final Logger logger = LogManager.getLogger(SiteareaRestController.class);

	// @PostMapping(path = "/sitearea/create", consumes =
	// MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSiteArea(@RequestBody SiteArea sa, HttpServletRequest request)
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String path = String.format("%s/%s", sa.getNodePath(), sa.getName());
			String repositoryName = sa.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, repositoryName, sa.getWorkspace(), path, sa.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, DRAFT_WS);
				session.getWorkspace().clone(DEFAULT_WS, path, path, true);
				// session.save();
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}

			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}

	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveSiteArea(@RequestBody SiteArea sa, HttpServletRequest request) throws WcmRepositoryException {
		try {
			String path = sa.getNodePath();
			String baseUrl = RestHelper.repositoryUrl(request);
			String repositoryName = sa.getRepository();
			JsonNode saJson = sa.toJson();
			this.itemHandler.updateItem(baseUrl, repositoryName, sa.getWorkspace(), path, saJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, DRAFT_WS, path, saJson);

//					Session session = this.repositoryManager.getSession(repositoryName, DRAFT_WS);
//					session.getWorkspace().clone(DEFAULT_WS, path, path, true);
				// session.save();
			}
		} catch (WcmRepositoryException e) {
			throw e;
		} catch (RepositoryException re) {
			throw new WcmRepositoryException(re);
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}

	@GetMapping(path = "/get/{repository}/{workspace}/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public SiteArea getSiteArea(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, @RequestParam("path") String saPath,
			HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			saPath = saPath.startsWith("/") ? saPath : "/" + saPath;
			RestNode saNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace, saPath, 4);

			SiteArea sa = new SiteArea();
			sa.setRepository(repository);
			sa.setWorkspace(workspace);
			sa.setNodePath(saPath);
			this.loadSiteArea(saNode, sa);

			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return sa;
		} catch (WcmRepositoryException e) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}

	@PutMapping(path = "/lock/{repository}/{workspace}/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public SiteArea lockSiteArea(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, @RequestParam("path") String absPath,
			HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			this.doLock(repository, workspace, absPath);
			SiteArea siteArea = this.getSiteArea(repository, workspace, absPath, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return siteArea;
		} catch (WcmRepositoryException e) {
			throw e;
		} catch (RepositoryException re) {
			throw new WcmRepositoryException(re);
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}

}
