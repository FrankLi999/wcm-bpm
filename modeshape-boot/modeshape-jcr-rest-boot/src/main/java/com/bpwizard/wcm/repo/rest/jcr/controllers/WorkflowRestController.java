package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
import com.bpwizard.wcm.repo.rest.jcr.model.BpmnWorkflow;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;
import com.bpwizard.wcm.repo.validation.ValidateString;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(WorkflowRestController.BASE_URI)
@Validated
public class WorkflowRestController extends BaseWcmRestController {
	private static final Logger logger = LoggerFactory.getLogger(WorkflowRestController.class);

	public static final String BASE_URI = "/wcm/api/bpmnWorkflow";

	@Autowired
	private WcmRequestHandler wcmRequestHandler;
//	@GetMapping(path = "/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public BpmnWorkflow getBpmnWorkflow(
//			@PathVariable("repository") String repository,
//		    @PathVariable("workspace") String workspace,
//		    @RequestParam("path") String nodePath, 
//			HttpServletRequest request) 
//			throws WcmRepositoryException {
//
//		if (logger.isDebugEnabled()) {
//			logger.debug("Entry");
//		}
//		try {
//			String baseUrl = RestHelper.repositoryUrl(request);
//			RestNode workflowNode = (RestNode) this.wcmItemHandler.item(baseUrl, repository, workspace,
//					nodePath, 2);
//			BpmnWorkflow bpmnWorkflow = this.toBpmnWorkflow(workflowNode);
//			bpmnWorkflow.setRepository(repository);
//			bpmnWorkflow.setWorkspace(workspace);
//			bpmnWorkflow.setLibrary(nodePath.split("/", 5)[3]);
//			if (logger.isDebugEnabled()) {
//				logger.debug("Exit");
//			}
//			return bpmnWorkflow;
//		} catch (WcmRepositoryException e ) {
//			throw e;
//		} catch (Throwable t) {
//			throw new WcmRepositoryException(t);
//		}		
//	}
	
	@GetMapping(path = "/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BpmnWorkflow[]> loadBpmnWorkflows(
		@PathVariable("repository") String repository,
		@PathVariable("workspace") String workspace,
		@RequestParam(name="filter", defaultValue = "") String filter,
	    @RequestParam(name="sort", defaultValue = "asc") 
		@ValidateString(acceptedValues={"asc", "desc"}, message="Sort order can only be asc or desc")
		String sortDirection,
	    @RequestParam(name="pageIndex", defaultValue = "0") int pageIndex,
	    @RequestParam(name="pageSize", defaultValue = "3") @Min(3) @Max(10) int pageSize,
		HttpServletRequest request) 
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}

		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			
			BpmnWorkflow[] bpmnWorkflows = this.getBpmnWorkflowLibraries(repository, workspace, baseUrl)
					.flatMap(library -> this.doGetBpmnWorkflows(library, baseUrl))
					.toArray(BpmnWorkflow[]::new);
			if ("asc".equals(sortDirection)) {
				Arrays.sort(bpmnWorkflows);
			} else if ("desc".equals(sortDirection)) {
				Arrays.sort(bpmnWorkflows, Collections.reverseOrder());
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.OK).body(bpmnWorkflows);
		} catch (WcmRepositoryException e) {
			logger.error("Failed to load bpmn workflow", e);
			throw e;
		}
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createBpmnWorkflow(
			@RequestBody BpmnWorkflow bpmnWorkflow, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		String absPath = String.format(WcmConstants.NODE_WORKFLOW_PATH_PATTERN, bpmnWorkflow.getLibrary(), bpmnWorkflow.getName());
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String repositoryName = bpmnWorkflow.getRepository();
			
			this.wcmItemHandler.addItem(WcmEvent.WcmItemType.workflow, baseUrl,  repositoryName, WcmConstants.DEFAULT_WS, absPath, bpmnWorkflow.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
				session.getWorkspace().clone(WcmConstants.DEFAULT_WS, absPath, absPath, true);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to create bpmn workflow", e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error("Failed to create bpmn workflow", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.CREATE_WORKFLOW_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
	    	logger.error("Failed to create bpmn workflow", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveBpmnWorkflow(@RequestBody BpmnWorkflow bpmnWorkflow, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		String absPath = String.format(WcmConstants.NODE_WORKFLOW_PATH_PATTERN, bpmnWorkflow.getLibrary(), bpmnWorkflow.getName());
		try {
			String baseUrl = RestHelper.repositoryUrl(request);			
			String repositoryName = bpmnWorkflow.getRepository();
			
			JsonNode atJson = bpmnWorkflow.toJson();
			this.wcmItemHandler.updateItem(WcmEvent.WcmItemType.workflow, baseUrl, repositoryName, WcmConstants.DEFAULT_WS, absPath, atJson);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to save bpmn workflow", e);
			throw e;
		} catch (RepositoryException re) {
			logger.error("Failed to save bpmn workflow", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.UPDATE_WORKFLOW_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
	    	logger.error("Failed to save bpmn workflow", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}

	@DeleteMapping("/{repository}/{workspace}")
  	public ResponseEntity<?> purgeWorkflow(
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
	  		
  			this.wcmItemHandler.deleteItem(WcmEvent.WcmItemType.workflow, baseUrl, repository, workspace, absPath);
  			
  	  		if (logger.isDebugEnabled()) {
  				logger.debug("Exit");
  			}
  	  		
  			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(String.format("Failed to delete item %s from expired repository. Content item does not exist", absPath), e);
			throw e;
	    } catch (Throwable t) {
	    	logger.error("Failed to purge bpmn workflow", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}

  	};
	protected Stream<BpmnWorkflow> doGetBpmnWorkflows(BpmnWorkflow at, String baseUrl)
			throws WcmRepositoryException {
		try {
			RestNode atNode = (RestNode) this.wcmItemHandler.item(baseUrl, at.getRepository(), at.getWorkspace(),
					String.format(WcmConstants.NODE_WORKFLOW_ROOT_PATH_PATTERN, at.getLibrary()), WcmConstants.BPMN_WORKFLOW_DEPTH);
			
			return atNode.getChildren().stream().filter(this.wcmRequestHandler::isBpmnWorkflow)
					.map(node -> this.toBpmnWorkflow(node, at.getRepository(), at.getWorkspace(), at.getLibrary()));
		} catch (RepositoryException re) {
			logger.error("Failed to get bpmn workflow", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.GET_WORKFLOW_ERROR, new String[] {baseUrl}));
		}
	}
	
	protected Stream<BpmnWorkflow> getBpmnWorkflowLibraries(String repository, String workspace,
			String baseUrl) throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.wcmItemHandler.item(baseUrl, repository, workspace,
					WcmConstants.NODE_ROOT_PATH, WcmConstants.READ_DEPTH_DEFAULT);
			return bpwizardNode.getChildren().stream()
					.filter(this.wcmRequestHandler::notSystemLibrary)
					.map(node -> toBpmnWorkflowWithLibrary(node, repository, workspace));
		} catch (RepositoryException re) {
			logger.error("Failed to get bpmn workflow", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.GET_NODE_ERROR, null));
		}
	}
	
	protected BpmnWorkflow toBpmnWorkflowWithLibrary(RestNode node, String repository, String workspace) {
		BpmnWorkflow bpmnWorkflowWithLibrary = new BpmnWorkflow();
		bpmnWorkflowWithLibrary.setRepository(repository);
		bpmnWorkflowWithLibrary.setWorkspace(workspace);
		bpmnWorkflowWithLibrary.setLibrary(node.getName());
		return bpmnWorkflowWithLibrary;
	}
	
	protected BpmnWorkflow toBpmnWorkflow(RestNode node, String repository, String workspace, String library) {
		BpmnWorkflow bpmnWorkflow = new BpmnWorkflow();
		bpmnWorkflow.setWcmAuthority(WcmUtils.getWcmAuthority(null));
		bpmnWorkflow.setName(node.getName());
		
		bpmnWorkflow.setRepository(repository);
		bpmnWorkflow.setWorkspace(workspace);
		bpmnWorkflow.setLibrary(library);
		for (RestNode childNode : node.getChildren()) {
			if (WcmConstants.WCM_ITEM_ELEMENTS.equals(childNode.getName())) {
				for (RestProperty restProperty : childNode.getJcrProperties()) {
					if ("bpw:bpmn".equals(restProperty.getName())) {
						bpmnWorkflow.setBpmn(restProperty.getValues().get(0));
						break;
					} 
				}
				
			} else if (WcmConstants.WCM_ITEM_PROPERTIES.equals(childNode.getName())) {
				for (RestProperty restProperty : childNode.getJcrProperties()) {
					if ("bpw:title".equals(restProperty.getName())) {
						bpmnWorkflow.setTitle(restProperty.getValues().get(0));
					} else if ("bpw:description".equals(restProperty.getName())) {
						bpmnWorkflow.setDescription(restProperty.getValues().get(0));
					} 
				}
			}
		}
		
		return bpmnWorkflow;
	}
}
