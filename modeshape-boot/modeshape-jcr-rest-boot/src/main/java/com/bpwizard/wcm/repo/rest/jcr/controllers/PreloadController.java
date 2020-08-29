package com.bpwizard.wcm.repo.rest.jcr.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.Preload;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;

@RestController
@RequestMapping(PreloadController.BASE_URI)
public class PreloadController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(WcmSystemRestController.class);
	public static final String BASE_URI = "/wcm/api";
	
	@Autowired
	ResourceLoader resourceLoader;

	
	//TODO: failed scenario - all or nothing
	@PostMapping(path= "/preload")
    public ResponseEntity<?> loadQueries(HttpServletRequest request) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	
    	try {
    		com.bpwizard.wcm.repo.rest.jcr.model.Preload[] preloads = this.readJson(
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
    				com.bpwizard.wcm.repo.rest.jcr.model.Preload.PreloadType.LIBRARY.name()) ||
    				preload.getPreloadType().equals(
    		    	com.bpwizard.wcm.repo.rest.jcr.model.Preload.PreloadType.ITEMS.name())) {
    				
    				loadItems(request, preload);
    			}
    		}
	    	logger.debug("Exiting ...");
	    	return ResponseEntity.status(HttpStatus.CREATED).build();
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_ITEMS, null));
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
	        logger.debug("Exiting ...");
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_CND, null));
    	}
	}
	
	protected void loadQueries(HttpServletRequest request, Preload preload) throws WcmRepositoryException {
		try {
			this.doLoadQueries(
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
    			this.itemHandler.addItems(
    				baseUrl, 
    				preload.getRepository(), 
    				workspace,
    				resourceLoader.getResource(preload.getPath()).getInputStream());
    		}
	    	logger.debug("Exiting ...");
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_ITEMS, null));
    	} 
	}
}
