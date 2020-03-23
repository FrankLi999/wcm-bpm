package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
import com.bpwizard.wcm.repo.rest.jcr.model.BpmnWorkflow;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.validation.ValidateString;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(WorkflowRestController.BASE_URI)
@Validated
public class WorkflowRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(WorkflowRestController.class);

	public static final String BASE_URI = "/wcm/api/bpmnWorkflow";

//	@GetMapping(path = "/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public BpmnWorkflow getBpmnWorkflow(
//			@PathVariable("repository") String repository,
//		    @PathVariable("workspace") String workspace,
//		    @RequestParam("path") String nodePath, 
//			HttpServletRequest request) 
//			throws WcmRepositoryException {
//
//		if (logger.isDebugEnabled()) {
//			logger.traceEntry();
//		}
//		try {
//			String baseUrl = RestHelper.repositoryUrl(request);
//			RestNode workflowNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
//					nodePath, 2);
//			BpmnWorkflow bpmnWorkflow = this.toBpmnWorkflow(workflowNode);
//			bpmnWorkflow.setRepository(repository);
//			bpmnWorkflow.setWorkspace(workspace);
//			bpmnWorkflow.setLibrary(nodePath.split("/", 5)[3]);
//			if (logger.isDebugEnabled()) {
//				logger.traceExit();
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
			logger.traceEntry();
		}

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
			logger.traceExit();
		}
		return ResponseEntity.status(HttpStatus.OK).body(bpmnWorkflows);
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createBpmnWorkflow(
			@RequestBody BpmnWorkflow bpmnWorkflow, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_WORKFLOW_PATH_PATTERN, bpmnWorkflow.getLibrary(), bpmnWorkflow.getName());
			String repositoryName = bpmnWorkflow.getRepository();
			this.itemHandler.addItem(baseUrl,  repositoryName, DEFAULT_WS, path, bpmnWorkflow.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, DRAFT_WS);
				session.getWorkspace().clone(DEFAULT_WS, path, path, true);
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveBpmnWorkflow(@RequestBody BpmnWorkflow bpmnWorkflow, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_WORKFLOW_PATH_PATTERN, bpmnWorkflow.getLibrary(), bpmnWorkflow.getName());
			String repositoryName = bpmnWorkflow.getRepository();
			JsonNode atJson = bpmnWorkflow.toJson();
			this.itemHandler.updateItem(baseUrl, repositoryName, DEFAULT_WS, path, atJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, DRAFT_WS, path, atJson);
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}

	protected Stream<BpmnWorkflow> doGetBpmnWorkflows(BpmnWorkflow at, String baseUrl)
			throws WcmRepositoryException {
		try {
			RestNode atNode = (RestNode) this.itemHandler.item(baseUrl, at.getRepository(), at.getWorkspace(),
					String.format(WCM_WORKFLOW_ROOT_PATH_PATTERN, at.getLibrary()), 3);
			
			return atNode.getChildren().stream().filter(this::isBpmnWorkflow)
					.map(node -> this.toBpmnWorkflow(node, at.getRepository(), at.getWorkspace(), at.getLibrary()));
		} catch (RepositoryException e) {
			e.printStackTrace();
			throw new WcmRepositoryException(e);
		}
	}
	
	protected Stream<BpmnWorkflow> getBpmnWorkflowLibraries(String repository, String workspace,
			String baseUrl) throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					WCM_ROOT_PATH, 1);
			return bpwizardNode.getChildren().stream()
					.filter(this::notSystemLibrary)
					.map(node -> toBpmnWorkflowWithLibrary(node, repository, workspace));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
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
		bpmnWorkflow.setName(node.getName());
		
		bpmnWorkflow.setRepository(repository);
		bpmnWorkflow.setWorkspace(workspace);
		bpmnWorkflow.setLibrary(library);
		for (RestProperty restProperty : node.getJcrProperties()) {
			if ("bpw:title".equals(restProperty.getName())) {
				bpmnWorkflow.setTitle(restProperty.getValues().get(0));
			} else if ("bpw:description".equals(restProperty.getName())) {
				bpmnWorkflow.setDescription(restProperty.getValues().get(0));
			} else if ("bpw:bpmn".equals(restProperty.getName())) {
				bpmnWorkflow.setBpmn(restProperty.getValues().get(0));
			} 
		}
		return bpmnWorkflow;
	}
}
