package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.Library;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;
import com.bpwizard.wcm.repo.validation.RepositoryName;
import com.bpwizard.wcm.repo.validation.ValidateString;
import com.bpwizard.wcm.repo.validation.WorkspaceName;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(ResourceLibraryRestController.BASE_URI)
@Validated
public class ResourceLibraryRestController extends BaseWcmRestController {
	
	public static final String BASE_URI = "/wcm/api/library";
	private static final Logger logger = LogManager.getLogger(ResourceLibraryRestController.class);
	
	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	@GetMapping(path = "/{repository}/{workspace}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Library[]> getLibraries(			
			@PathVariable("repository") @RepositoryName() String repository,
			@PathVariable("workspace") @WorkspaceName() String workspace,
			@RequestParam(name="filter", defaultValue = "") String filter,
		    @RequestParam(name="sort", defaultValue = "asc") 
			@ValidateString(acceptedValues={"asc", "desc"}, message="Sort order can only be asc or desc")
			String sortDirection,
		    @RequestParam(name="pageIndex", defaultValue = "0") int pageIndex,
		    @RequestParam(name="pageSize", defaultValue = "3") @Min(3) @Max(10) int pageSize,
			HttpServletRequest request) 
					throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			RestNode libraryParentNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					WcmConstants.NODE_ROOT_PATH, WcmConstants.READ_DEPTH_TWO_LEVEL);
			Library[] libraries = libraryParentNode.getChildren().stream()
					.filter(this.wcmRequestHandler::isLibrary)
					.filter(this.wcmRequestHandler::notSystemLibrary)
					.map(node -> toLibrary(node, repository, workspace))
					.filter(library -> this.wcmRequestHandler.filterLibrary(library, filter))
					.toArray(Library[]::new);
			if ("asc".equals(sortDirection)) {
				Arrays.sort(libraries);
			} else if ("desc".equals(sortDirection)) {
				Arrays.sort(libraries, Collections.reverseOrder());
			}			
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.OK).body(libraries);
		} catch (RepositoryException e) {
			logger.error(e);
			throw new WcmRepositoryException(e, new WcmError(e.getMessage(), WcmErrors.GET_NODE_ERROR, null));
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createLibrary(
			@Valid @RequestBody Library library, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = library.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WcmConstants.NODE_LIB_PATH_PATTERN, library.getName());
			this.itemHandler.addItem(
					baseUrl, 
					repositoryName,
					library.getWorkspace(),
					path, 
					library.toJson());
			if (this.authoringEnabled) {
				Session session = repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
				session.getWorkspace().clone(WcmConstants.DEFAULT_WS, path, path, true);
				// session.save();
			}
			if (this.syndicationEnabled) {
				RestNode restNode = (RestNode)this.itemHandler.item(baseUrl, repositoryName, library.getWorkspace(), path, WcmConstants.READ_DEPTH_TWO_LEVEL);
				syndicationUtils.addNewItemEvent(
						restNode, 
						repositoryName, 
						library.getWorkspace(), 
						path,
						WcmEvent.WcmItemType.library);
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
	
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}	
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveLibrary(
			@Valid @RequestBody Library library, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = library.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WcmConstants.NODE_LIB_PATH_PATTERN, library.getName());
			List<String> currentDescendants = new ArrayList<String>();		
			if (this.syndicationEnabled) {
				RestNode restNode = (RestNode)this.itemHandler.item(baseUrl, repositoryName, library.getWorkspace(), path, WcmConstants.READ_DEPTH_TWO_LEVEL);
				syndicationUtils.populateDescendantIds(restNode, currentDescendants);
			}
			JsonNode jsonItem = library.toJson();
			this.itemHandler.updateItem(
					baseUrl, 
					repositoryName,
					library.getWorkspace(),
					path, 
					jsonItem);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(
						baseUrl, 
						repositoryName,
						library.getWorkspace(),
						path, 
						jsonItem);
			}
			if (this.syndicationEnabled) {
				RestNode restNode = (RestNode)this.itemHandler.item(baseUrl, repositoryName, library.getWorkspace(), path, WcmConstants.READ_DEPTH_TWO_LEVEL);
				
				syndicationUtils.addUpdateItemEvent(
						restNode, 
						repositoryName, 
						library.getWorkspace(),  
						path,
						WcmEvent.WcmItemType.library,
						currentDescendants);
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
	
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@DeleteMapping(path = "")
	public void deleteLibrary(
			@Valid @RequestBody Library library, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		String baseUrl = RestHelper.repositoryUrl(request);
		String absPath = String.format(WcmConstants.NODE_LIB_PATH_PATTERN, library.getName()); 
		try {
			String nodeId = null;
			if (this.syndicationEnabled) {
				RestNode restNode = (RestNode)this.itemHandler.item(baseUrl, library.getRepository(), 
						library.getWorkspace(), 
						absPath, WcmConstants.READ_DEPTH_TWO_LEVEL);
				nodeId = restNode.getId();
			}
			this.wcmRequestHandler.purgeWcmItem(
					library.getRepository(), 
					library.getWorkspace(), 
					absPath);
			
			if (this.syndicationEnabled) {
				syndicationUtils.addDeleteItemEvent(
						nodeId, 
						library.getRepository(), 
						library.getWorkspace(), 
						absPath,
						WcmEvent.WcmItemType.library,
						null);
			}
		} catch (WcmRepositoryException e) {
			logger.error(String.format("Failed to delete item %s from expired repository. Content item does not exist", absPath), e);
			throw e;
		} catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
	}
	
	private Library toLibrary(RestNode node, String repository, String workspace) {
		Library library = new Library();
		library.setWcmAuthority(WcmUtils.getWcmAuthority(null));
		library.setRepository(repository);
		library.setWorkspace(workspace);
		library.setName(node.getName());
		
		for (RestProperty property: node.getJcrProperties()) {
			if ("bpw:title".equals(property.getName())) {
				library.setTitle(property.getValues().get(0));
			} else if ("bpw:description".equals(property.getName())) {
				library.setDescription(property.getValues().get(0));
			} 
		}
		
		
		for (RestNode childNode: node.getChildren()) {
			if (WcmUtils.checkNodeType(childNode, "bpw:system_libraryType_ElementFolder")) {
				for (RestProperty property: childNode.getJcrProperties()) {
					if ("language".equals(property.getName())) {
						library.setLanguage(property.getValues().get(0));
						break;
					} 
				}
			}
		}
		return library;
	}
}
