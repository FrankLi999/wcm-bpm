package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
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
import com.bpwizard.wcm.repo.rest.jcr.model.RenderTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(RenderTemplateRestController.BASE_URI)
@Validated
public class RenderTemplateRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(RenderTemplateRestController.class);
	
	public static final String BASE_URI = "/wcm/api/rt";

	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	@GetMapping(path = "/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, RenderTemplate> getRenderTemplates(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Map<String, RenderTemplate> renderTemplates = this.wcmRequestHandler.getRenderTemplates(repository, workspace, request);
	
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return renderTemplates;
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}

	@GetMapping(path = "/get/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RenderTemplate getRenderTemplate(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@RequestParam("path") String rtPath,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			rtPath = rtPath.startsWith("/") ? rtPath : "/" + rtPath;
			String baseUrl = RestHelper.repositoryUrl(request);
			String library = rtPath.split("/", 5)[3];
			RestNode rtNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					rtPath, WcmConstants.RENDER_TEMPLATE_DEPATH);
			
			RenderTemplate rt = this.wcmRequestHandler.toRenderTemplate(rtNode, repository, workspace, library, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return rt;
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PutMapping(path = "/lock/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RenderTemplate lockRenderTemplate(
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
			RenderTemplate renderTemplate = this.getRenderTemplate(repository, workspace, absPath, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return renderTemplate;
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.LOCK_RT_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createRenderTemplate(
			@RequestBody RenderTemplate rt, 
			HttpServletRequest request)
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		String absPath = String.format(WcmConstants.NODE_RT_PATH_PATTERN, rt.getLibrary(), rt.getName());
		try {
			String repositoryName = rt.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			AuthoringTemplate at = rt.isQuery() ? null : this.wcmRequestHandler.getAuthoringTemplate(repositoryName, rt.getWorkspace(), rt.getResourceName(), request);
			this.itemHandler.addItem(baseUrl, repositoryName, WcmConstants.DEFAULT_WS, absPath, rt.toJson(at));
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
				session.getWorkspace().clone(WcmConstants.DEFAULT_WS, absPath, absPath, true);
				// session.save();
			}
			
			if (this.syndicationEnabled) {
				RestNode restNode = (RestNode)this.itemHandler.item(baseUrl, repositoryName, rt.getWorkspace(), absPath, WcmConstants.FULL_SUB_DEPTH);
				syndicationUtils.addNewItemEvent(
						restNode, 
						repositoryName, 
						rt.getWorkspace(), 
						absPath,
						WcmEvent.WcmItemType.renderTemplate);
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
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.CREATE_RT_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveRenderTemplate(
			@RequestBody RenderTemplate rt, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		String absPath = String.format(WcmConstants.NODE_RT_PATH_PATTERN, rt.getLibrary(), rt.getName());;
		try {
			String repositoryName = rt.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			List<String> currentDescendants = new ArrayList<String>();		
			if (this.syndicationEnabled) {
				RestNode restNode = (RestNode)this.itemHandler.item(baseUrl, repositoryName, rt.getWorkspace(), absPath, WcmConstants.FULL_SUB_DEPTH);
				syndicationUtils.populateDescendantIds(restNode, currentDescendants);
			}
			AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(repositoryName, rt.getWorkspace(), rt.getResourceName(), request);
			JsonNode rtJson = rt.toJson(at);
			this.itemHandler.updateItem(baseUrl, repositoryName, rt.getWorkspace(), absPath, rtJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, rt.getWorkspace(), absPath, rtJson);
			}
			if (this.syndicationEnabled) {
				RestNode restNode = (RestNode)this.itemHandler.item(baseUrl, repositoryName, rt.getWorkspace(), absPath, WcmConstants.FULL_SUB_DEPTH);
				
				syndicationUtils.addUpdateItemEvent(
						restNode, 
						repositoryName, 
						rt.getWorkspace(), 
						absPath,
						WcmEvent.WcmItemType.renderTemplate,
						currentDescendants);
			}
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) {
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.UPDATE_RT_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@DeleteMapping("/{repository}/{workspace}")
  	public ResponseEntity<?> purgeRenderTemplate(
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
	  		List<String> currentDescendants = new ArrayList<String>();	
	  		String nodeId = null;
			if (this.syndicationEnabled) {
				RestNode restNode = (RestNode)this.itemHandler.item(baseUrl, repository, workspace, absPath, WcmConstants.FULL_SUB_DEPTH);
				nodeId = restNode.getId();
				syndicationUtils.populateDescendantIds(restNode, currentDescendants);
			}	
		
  			this.wcmRequestHandler.purgeWcmItem(repository, workspace, absPath);
  			if (this.syndicationEnabled) {
				syndicationUtils.addDeleteItemEvent(
						nodeId, 
						repository, 
						workspace, 
						wcmPath,
						WcmEvent.WcmItemType.renderTemplate,
						currentDescendants);
			}
  			
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
