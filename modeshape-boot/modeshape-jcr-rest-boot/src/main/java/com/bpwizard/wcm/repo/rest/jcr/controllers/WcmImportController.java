package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.io.InputStream;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.Library;
import com.bpwizard.wcm.repo.rest.jcr.model.Preload;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(WcmImportController.BASE_URI)
public class WcmImportController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(WcmSystemRestController.class);
	public static final String BASE_URI = "/wcm/api/import";
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	
	
	//TODO: failed scenario - all or nothing
	@PostMapping(path= "/preload")
    public ResponseEntity<?> loadQueries(HttpServletRequest request) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	
    	try {
    		com.bpwizard.wcm.repo.rest.jcr.model.Preload[] preloads = this.wcmRequestHandler.readJson(
    				resourceLoader.getResource("classpath:/modeshape/data/preload-config.json").getInputStream(), 
    				com.bpwizard.wcm.repo.rest.jcr.model.Preload[].class);
    		for (com.bpwizard.wcm.repo.rest.jcr.model.Preload preload: preloads) {
    			if (preload.getPreloadType().equals(
        				com.bpwizard.wcm.repo.rest.jcr.model.Preload.PreloadType.CND.name())) {
        				loadCND(request, preload);
        		} else if (preload.getPreloadType().equals(
    				com.bpwizard.wcm.repo.rest.jcr.model.Preload.PreloadType.QUERY.name())) {
    				loadQueries(request, preload);
    			} else if (preload.getPreloadType().equals(
    		    	com.bpwizard.wcm.repo.rest.jcr.model.Preload.PreloadType.ITEMS.name())) {
    				
    				loadItems(request, preload);
    			} else if (preload.getPreloadType().equals(
        		    	com.bpwizard.wcm.repo.rest.jcr.model.Preload.PreloadType.LIBRARY.name())) {
    				
    				loadLibrary(request, preload);
    			}
    		}
	    	logger.debug("Exiting ...");
	    	return ResponseEntity.status(HttpStatus.CREATED).build();
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_ITEMS, null));
    	} 
    }
	
	@PostMapping(path= "/{repositoryName}/{workspaceName}/" + RestHelper.ITEMS_METHOD_NAME,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
			produces= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postItems( HttpServletRequest request,
			@PathVariable( "repositoryName" ) String rawRepositoryName,
			@PathVariable( "workspaceName" ) String rawWorkspaceName,
			@RequestParam("file") MultipartFile file ) throws WcmRepositoryException {
		
		logger.debug("Entering ...");
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			this.loadItems(baseUrl, rawRepositoryName, rawWorkspaceName, file.getInputStream());
	    	logger.debug("Exiting ...");
	    	return ResponseEntity.accepted().build();
		} catch (Throwable t) {
			throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_ITEMS, null));
		} 
	}
	
	/**
     * Imports a single CND file into the repository, using a {@link MediaType#_FORM_DATA} request. The CND file is
     * expected to be submitted from an HTML element with the name <i>file</i>
     *
     * @param request a non-null {@link HttpServletRequest} request
     * @param repositoryName a non-null {@link String} representing the name of a repository.
     * @param workspaceName a non-null {@link String} representing the name of a workspace.
     * @param allowUpdate an optional parameter which indicates whether existing node types should be updated (overridden) or not.
     * @param form a {@link FileUploadForm} instance representing the HTML form from which the cnd was submitted
     * @return a {@code non-null} {@link ResponseEntity}
     * @throws RepositoryException if any JCR operations fail
     * @throws IllegalArgumentException if the submitted form does not contain an HTML element named "file".
     */
    @PostMapping(path="/{repositoryName}/{workspaceName}/" + RestHelper.NODE_TYPES_METHOD_NAME, 
    	produces= MediaType.APPLICATION_JSON_VALUE, 
        consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postCND(HttpServletRequest request,
    		@PathVariable("repositoryName" ) String repositoryName,
    		@PathVariable("workspaceName" ) String workspaceName,
    		@RequestParam(name="allowUpdate", defaultValue="true") boolean allowUpdate,
    		@RequestParam("file") MultipartFile file) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	ResponseEntity<?> response = this.nodeTypeHandler.importCND(baseUrl, repositoryName, workspaceName, allowUpdate, file.getInputStream());
	    	if (this.syndicationEnabled && WcmConstants.DEFAULT_WS.equals(workspaceName)) {
    			syndicationUtils.addCNDEvent(repositoryName, workspaceName, file.getInputStream(), WcmEvent.WcmItemType.cnd);
    		}
	    	logger.debug("Exiting ...");
	    	return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_CND, null));
    	}
    }
    
	protected void loadCND(HttpServletRequest request, Preload preload) throws WcmRepositoryException {
		logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);

    		for (String workspace: preload.getWorkspace()) {
    			this.nodeTypeHandler.importCND(baseUrl,
    					preload.getRepository(), 
        				workspace,
        				true,
        				resourceLoader.getResource(preload.getPath()).getInputStream());
    		}
    		if (this.syndicationEnabled) {
    			syndicationUtils.addCNDEvent(preload.getRepository(), WcmConstants.DEFAULT_WS, resourceLoader.getResource(preload.getPath()).getInputStream(), WcmEvent.WcmItemType.cnd);
    		}
	        logger.debug("Exiting ...");
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_CND, null));
    	}
	}
	
	protected void loadQueries(HttpServletRequest request, Preload preload) throws WcmRepositoryException {
		try {
			this.wcmRequestHandler.loadQueries(
				request,
				preload.getRepository(),
				preload.getWorkspace()[0], 
				resourceLoader.getResource(preload.getPath()).getInputStream());
		} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_CND, null));
    	}
	}
	
	protected void loadItems(HttpServletRequest request, Preload preload) throws WcmRepositoryException {
		logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
    		for (String workspace: preload.getWorkspace()) {
    			this.loadItems(
    				baseUrl, 
    				preload.getRepository(), 
    				workspace,
    				resourceLoader.getResource(preload.getPath()).getInputStream());
    		}
	    	logger.debug("Exiting ...");
    	} catch (WcmRepositoryException e) {
    		throw e;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_ITEMS, null));
    	} 
	}
	
	protected void loadItems(String baseUrl, String repository, String workspace, InputStream is) throws WcmRepositoryException {
		try {
			this.wcmItemHandler.addItems(
					baseUrl, 
					repository, 
					workspace,
					is);
		} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_ITEMS, null));
    	} 
	}
	
	protected void loadLibrary(HttpServletRequest request, Preload preload) throws WcmRepositoryException {
		logger.debug("Entering ...");
    	try {
    		//TODO: validation
    		JsonNode jsonNode = JsonUtils.inputStreamToJsonNode(resourceLoader.getResource(preload.getPath()).getInputStream());
    		Library library = new Library();
    		library.setRepository(preload.getRepository());
    		library.setName(jsonNode.get("name").asText());
    		if (StringUtils.hasText(jsonNode.get("title").asText())) {
    			library.setTitle(jsonNode.get("title").asText());
    		}
    		if (StringUtils.hasText(jsonNode.get("description").asText())) {
    			library.setDescription(jsonNode.get("description").asText());
    		}
    		if (StringUtils.hasText(jsonNode.get("language").asText())) {
    			library.setLanguage(jsonNode.get("language").asText());
    		} else {
    			library.setLanguage("en");
    		}
    		if (null != jsonNode.get("rootContentAreaLayout")) {
    			library.setRootContentAreaLayout(jsonNode.get("rootContentAreaLayout").asText());
    		}

    		for (String workspace: preload.getWorkspace()) {
    			library.setWorkspace(workspace);
    			this.wcmRequestHandler.createLibrary(
    					library, 
    					request);
    		}
	    	logger.debug("Exiting ...");
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_ITEMS, null));
    	} 
	}
}
