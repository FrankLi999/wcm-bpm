package com.bpwizard.bpm.wcm.controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.VersionManager;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.bpm.wcm.client.model.CancelDraftRequest;
import com.bpwizard.bpm.wcm.client.model.ClaimEditTaskReuqest;
import com.bpwizard.bpm.wcm.client.model.ClaimReivewTaskReuqest;
import com.bpwizard.bpm.wcm.client.model.DraftItemRequest;
import com.bpwizard.bpm.wcm.client.model.EditAsDraftRequest;
import com.bpwizard.bpm.wcm.client.model.PublishItemRequest;
import com.bpwizard.bpm.wcm.client.model.StartFlowRequest;
import com.bpwizard.bpm.wcm.client.model.UpdateDraftRequest;
import com.bpwizard.bpm.wcm.service.BpmService;
import com.bpwizard.bpm.wcm.service.MailService;
import com.bpwizard.wcm.repo.rest.ModeshapeUtils;
import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.handler.WcmRequestHandler;
import com.bpwizard.wcm.repo.rest.jcr.controllers.BaseWcmRestController;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.ContentItem;
import com.bpwizard.wcm.repo.rest.jcr.model.DraftItem;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.jcr.model.WorkflowNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;

@RestController
@RequestMapping(ContentItemWorkflowRestController.BASE_URI)
@Validated
public class ContentItemWorkflowRestController extends BaseWcmRestController {
	private static final Logger logger = LoggerFactory.getLogger(ContentItemWorkflowRestController.class);
	
	public static final String BASE_URI = "/wcm/api/contentItem";

	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private BpmService bpmService;
	@PostMapping(path = "/create-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createDraft(			
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		contentItem.getProperties().setAuthor(WcmUtils.getCurrentUsername());
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.debug("Exit. Authring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		String absPath = WcmUtils.nodePath(contentItem.getWcmPath(), contentItem.getProperties().getName());
		String baseUrl = RestHelper.repositoryUrl(request);
		AuthoringTemplate at = null;
		String processInstanceId = null;
		RestNode newNode = null;
		try {
			this.wcmRequestHandler.setWorkflowStage(contentItem, WcmConstants.WORKFLOW_STATGE_DRAFT);
			at = this.wcmRequestHandler.getAuthoringTemplate(contentItem.getRepository(), WcmConstants.DRAFT_WS, 
					contentItem.getAuthoringTemplate(), request);
			if (at.getContentItemAcl() != null && at.getContentItemAcl().getOnSaveDraftPermissions() != null) {
				contentItem.setAcl(at.getContentItemAcl().getOnSaveDraftPermissions());
			}
			newNode = (RestNode)this.wcmItemHandler.addItem(WcmEvent.WcmItemType.contentItem, baseUrl, contentItem.getRepository(), WcmConstants.DRAFT_WS, absPath, contentItem.toJson(at)).getBody();
			if (at.getContentItemAcl() != null && at.getContentItemAcl().getOnSaveDraftPermissions() != null) {
				String repositoryName = contentItem.getRepository();
				Session session = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
				ModeshapeUtils.grantPermissions(session, absPath, at.getContentItemAcl().getOnSaveDraftPermissions());
			}
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to create draft", e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error("Failed to create draft", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.CREATE_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error("Failed to create draft", t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
		try {
			StartFlowRequest startFlowRequest = StartFlowRequest.createStartContentFlowRequest(
					contentItem.getProperties().getAuthor(),
					contentItem.getRepository(), 
					contentItem.getWorkspace(), 
					newNode.getId(),
					String.format("%s/%s", contentItem.getWcmPath(), contentItem.getProperties().getName()),
					this.getContentReviewUrl(contentItem.getWcmPath()),
					"wcm_content_flow");
			
			processInstanceId = this.startContentFlow(startFlowRequest, request);
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to start content workflow", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to start content workflow", t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
		
		try {
			WorkflowNode workflowNode = contentItem.getWorkflow();
			if (workflowNode == null) {
				workflowNode = new WorkflowNode();
				contentItem.setWorkflow(workflowNode);
			}
			workflowNode.setProcessInstanceId(processInstanceId);
			this.wcmItemHandler.updateItem(WcmEvent.WcmItemType.contentItem, baseUrl, contentItem.getRepository(), WcmConstants.DRAFT_WS, absPath, contentItem.toJson(at));
			//TODO: prepare for web socket push of reviewing tasks.
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to track content workflow", e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error("Failed to track content workflow", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.CREATE_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error("Failed to track content workflow", t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}
	
	@GetMapping(path = "/get-draft/{repository}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDraftItems(
			@PathVariable("repository") String repository,
		    @RequestParam("path") String siteArea,
		    HttpServletRequest request) 
		    throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}

		String absPath = WcmUtils.nodePath(siteArea);
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			
			RestNode saNode = (RestNode) this.wcmItemHandler.item(baseUrl, repository, WcmConstants.DRAFT_WS,
					absPath, WcmConstants.CONTENT_ITEM_DEPATH);
			DraftItem[] draftItems = saNode.getChildren().stream()
					.filter(WcmUtils::isDraftContentItem)
					.map(node -> this.wcmRequestHandler.toContentItem(node, repository, WcmConstants.DRAFT_WS, String.format("%s/%s", siteArea, node.getName()), request))
					.map(WcmUtils::toDraftItem)
					.toArray(DraftItem[]::new);
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return  ResponseEntity.status(HttpStatus.OK).body(draftItems);
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to get draft", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to get draft", t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}
	
	@PostMapping(path = "/edit-as-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> editDraft(
			@RequestBody EditAsDraftRequest draftRequest, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		if (!authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.debug("Exit. Authoring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		String processInstanceId = null;
		
		try {
			StartFlowRequest startFlowRequest = StartFlowRequest.createStartContentFlowRequest(
					draftRequest.getAuthor(),
					draftRequest.getRepository(), 
					WcmConstants.DRAFT_WS, 
					draftRequest.getContentId(),
					draftRequest.getWcmPath(),
					this.getContentReviewUrl(draftRequest.getWcmPath()),
					"wcm_content_flow");
			
			processInstanceId = this.startContentFlow(startFlowRequest, request);
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to start content workflow", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to start content workflow", t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
		
		try {
			String absPath = WcmUtils.nodePath(draftRequest.getWcmPath());
			Session draftSession = this.repositoryManager.getSession(draftRequest.getRepository(), WcmConstants.DRAFT_WS);
			VersionManager versionManager = draftSession.getWorkspace().getVersionManager();
	        versionManager.checkout(absPath);
	        Node contentNode = draftSession.getNode(absPath);
	        Node workflowNode = contentNode.getNode("bpw:workflow");
	        if (workflowNode == null) {
	        	workflowNode = contentNode.addNode("bpw:workflow");
	        	workflowNode.setPrimaryType("workflowNode");
	        }
	        workflowNode.setProperty("workflowStage", WcmConstants.WORKFLOW_STATGE_DRAFT);
	        workflowNode.setProperty("processInstanceId", processInstanceId);
	        String authoringTemplate = contentNode.getProperty("bpw:authoringTemplate").getString();
	        AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(draftRequest.getRepository(), WcmConstants.DRAFT_WS, 
	        		authoringTemplate, request);
			if (at.getContentItemAcl() != null && at.getContentItemAcl().getOnSaveDraftPermissions() != null) {
				String repositoryName = draftRequest.getRepository();
				Session session = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
				ModeshapeUtils.grantPermissions(session, absPath, at.getContentItemAcl().getOnSaveDraftPermissions());
			}
			versionManager.checkin(absPath);
			draftSession.save();
			
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to edit draft", e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error("Failed to edit draft", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.EDIT_DRAFT_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error("Failed to edit draft", t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "/cancel-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> canelDraft(
			@RequestBody CancelDraftRequest cancelDraftRequest,			
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		if (!authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.debug("Exit. Authoring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
			String absPath = WcmUtils.nodePath(cancelDraftRequest.getWcmPath());
			Session draftSession = this.repositoryManager.getSession(cancelDraftRequest.getRepository(), WcmConstants.DRAFT_WS);
			
			String processInstanceId = null;
			Node contentNode = draftSession.getNode(absPath);
	        Node workflowNode = contentNode.getNode("bpw:workflow");
	        if (workflowNode != null) {
	        	processInstanceId = workflowNode.getProperty("processInstanceId").getString();
	        }
			
			
			draftSession.removeItem(absPath);
			draftSession.save();
			try {
				draftSession.getWorkspace().clone(WcmConstants.DEFAULT_WS, absPath, absPath, true);
			} catch (Exception e) {
				logger.warn("Cancel Draft - not able to clone the corresponding item from the default the workspace");
			}
			
			if (StringUtils.hasText(processInstanceId)) {
				bpmService.canelDraft(cancelDraftRequest, request);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to cancel draft", e);
			throw e;
		} catch (RepositoryException re) {
			logger.error("Failed to cancel draft", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.CANCEL_DRAFT_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error("Failed to cancel draft", t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "/reject-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> rejectContentItemDraft(
			@RequestBody DraftItemRequest rejectRequest, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.debug("Exit. Authring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		
		try {
			
			UserDetails principal = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        String username = principal.getUsername();
	        Session session = this.repositoryManager.getSession(rejectRequest.getRepository(), WcmConstants.DRAFT_WS); 
	        VersionManager versionManager = session.getWorkspace().getVersionManager();
	        String absPath = WcmUtils.nodePath(rejectRequest.getWcmPath());
	        versionManager.checkout(absPath);
	        Node contentNode = session.getNode(absPath);
	        Node commentsNode = contentNode.getNode("comments");
	        Node commentNode = commentsNode.addNode("Reject-" + System.currentTimeMillis(), "bpw:Comment");
	        commentNode.setProperty("bpw:comment", rejectRequest.getComment());
	        commentNode.setProperty("bpw:reviewer", username);
	        commentNode.setProperty("bpw:approved", false);
	        Node workflowNode = contentNode.getNode("bpw:workflow");
	        workflowNode.setProperty("reviewer", (String)null);
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
	        String authorEmail = contentNode.getProperty("jcr:createdBy").getString();
			AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(rejectRequest.getRepository(), WcmConstants.DRAFT_WS, 
					atPath, request);
			if (at.getContentItemAcl() != null && at.getContentItemAcl().getOnReviewedDraftPermissions() != null) {
				ModeshapeUtils.grantPermissions(session, absPath, at.getContentItemAcl().getOnReviewedDraftPermissions());
			}
			versionManager.checkin(absPath);
	        session.save();
	        this.bpmService.rejectContentItemDraft(rejectRequest, request);
			this.notifyRejectedItem(authorEmail, rejectRequest);
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to reject draft", e);
			throw e;
		} catch (RepositoryException re) {
			logger.error("Failed to reject draft", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.REJECT_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error("Failed to reject draft", t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "/approve-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> approveContentItemDraft(
			@RequestBody DraftItemRequest approvalRequest,  
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.debug("Debug. Authring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {			
	        Session session = this.repositoryManager.getSession(approvalRequest.getRepository(), WcmConstants.DRAFT_WS);
	        VersionManager versionManager = session.getWorkspace().getVersionManager();
	        String absPath = WcmUtils.nodePath(approvalRequest.getWcmPath());
	        versionManager.checkout(absPath);
	        Node contentNode = session.getNode(absPath);
	        Node commentsNode = contentNode.getNode("comments");
		    
	        Node commentNode = commentsNode.addNode("Approval-" + System.currentTimeMillis(), "bpw:Comment");
	        commentNode.setProperty("bpw:comment", approvalRequest.getComment());
	        commentNode.setProperty("bpw:reviewer", WcmUtils.getCurrentUsername());
	        commentNode.setProperty("bpw:approved", true);
	        Node workflowNode = contentNode.getNode("bpw:workflow");
	        workflowNode.setProperty("reviewer", (String)null);
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(approvalRequest.getRepository(), WcmConstants.DRAFT_WS, 
					atPath, request);
			if (at.getContentItemAcl() != null && at.getContentItemAcl().getOnReviewedDraftPermissions() != null) {
				ModeshapeUtils.grantPermissions(session, absPath, at.getContentItemAcl().getOnReviewedDraftPermissions());
			}
			// this.wcmUtils.unlock(approvalRequest.getRepository(), WcmConstants.DRAFT_WS, absPath);
			versionManager.checkin(absPath);
			session.save();
			this.bpmService.approveContentItemDraft(approvalRequest, request);
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to approve draft", e);
			throw e;
		} catch (RepositoryException re) {
			logger.error("Failed to approve draft", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.APPROVE_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error("Failed to approve draft", t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "/update-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateDraft(
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		if (!authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.debug("Exit. Authoring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
			String absPath = WcmUtils.nodePath(contentItem.getWcmPath());
			String baseUrl = RestHelper.repositoryUrl(request);
			String editor = contentItem.getWorkflow().getEditor();
			contentItem.getWorkflow().setEditor(null);
			AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(
					contentItem.getRepository(), 
					WcmConstants.DRAFT_WS, 
					contentItem.getAuthoringTemplate(), 
					request);
			this.wcmItemHandler.updateItem(WcmEvent.WcmItemType.contentItem, baseUrl, contentItem.getRepository(), WcmConstants.DRAFT_WS, absPath, contentItem.toJson(at));
			if (contentItem.getAcl() != null) {
				String repositoryName = contentItem.getRepository();
				String workspaceName = contentItem.getWorkspace();
				Session session = this.repositoryManager.getSession(repositoryName, workspaceName);
				ModeshapeUtils.grantPermissions(session, absPath, contentItem.getAcl());
			}
			if (StringUtils.hasText(editor)) {
				UpdateDraftRequest updateDraftRequest = new UpdateDraftRequest();
				updateDraftRequest.setContentId(contentItem.getId());
				bpmService.updateDraft(updateDraftRequest, request);
			}
	        
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to update draft", e);
			throw e;
		} catch (RepositoryException re) {
			logger.error("Failed to update draft", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.LOCK_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error("Failed to update draft", t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "/claim-review-task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> claimReviewTask(
			@RequestBody ClaimReivewTaskReuqest claimRequest,  
			HttpServletRequest request)
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		String reviewTaskId = bpmService.claimReviewTask(
				claimRequest, request);
		if (reviewTaskId == null) {
			logger.error("Unable to find review-content task for content item with " + claimRequest.getWcmPath());
			throw new WcmRepositoryException(WcmError.WCM_ERROR);
		}
		try {
			Session session = this.repositoryManager.getSession(claimRequest.getRepository(), WcmConstants.DRAFT_WS); 
			VersionManager versionManager = session.getWorkspace().getVersionManager();
			String absPath = WcmUtils.nodePath(claimRequest.getWcmPath());
			versionManager.checkout(absPath);
		    Node contentNode = session.getNode(absPath);
		    Node workflowNode = contentNode.getNode("bpw:workflow");
		    String currentUser = WcmUtils.getCurrentUsername();
		    workflowNode.setProperty("reviewer", currentUser);
		    versionManager.checkin(absPath);
		    session.save();
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.ok(String.format("{\"reviewer\": \"%s\"}", currentUser));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e, WcmError.WCM_ERROR);
		}
	}
	
	@PostMapping(path = "/claim-edit-task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> claimEditTask(
			@RequestBody ClaimEditTaskReuqest editRequest,  
			HttpServletRequest request)
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		String editTaskId = this.bpmService.claimEditTask(
				editRequest,
				request);
		if (editTaskId == null) {
			logger.error("Unable to find edit-task for content item with " + editRequest.getWcmPath());
			throw new WcmRepositoryException(WcmError.WCM_ERROR);
		}
		try {
			Session session = this.repositoryManager.getSession(editRequest.getRepository(), WcmConstants.DRAFT_WS); 
			VersionManager versionManager = session.getWorkspace().getVersionManager();
			String absPath = WcmUtils.nodePath(editRequest.getWcmPath());
			versionManager.checkout(absPath);
		    Node contentNode = session.getNode(absPath);
		    Node workflowNode = contentNode.getNode("bpw:workflow");
		    String editor = WcmUtils.getCurrentUsername();
		    workflowNode.setProperty("editor", editor);
		    versionManager.checkin(absPath);
		    session.save();
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.ok(String.format("{\"editor\": \"%s\"}", editor));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e, WcmError.WCM_ERROR);
		}
	}
	
	@PostMapping(path = "/publish", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> publishContentItem(
		    @RequestBody PublishItemRequest publishRequest, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.debug("Exit. Authring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
	        Session session = this.repositoryManager.getSession(publishRequest.getRepository(), WcmConstants.DRAFT_WS); 
	        VersionManager versionManager = session.getWorkspace().getVersionManager();
			String absPath = WcmUtils.nodePath(publishRequest.getWcmPath());
			versionManager.checkout(absPath);
			
	        Node contentNode = session.getNode(absPath);
	        Node workflowNode = contentNode.getNode("bpw:workflow");
	        workflowNode.setProperty("workflowStage", WcmConstants.WORKFLOW_STATGE_PUBLISHED);
	        workflowNode.setProperty("processInstanceId", (String)null);
	        String atPath = workflowNode.getParent().getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.wcmRequestHandler.getAuthoringTemplate(publishRequest.getRepository(), WcmConstants.DRAFT_WS, 
					atPath, request);
			if (at.getContentItemAcl() != null && at.getContentItemAcl().getOnPublishPermissions() != null) {
				ModeshapeUtils.grantPermissions(session, absPath, at.getContentItemAcl().getOnPublishPermissions());
			}
			// this.wcmUtils.unlock(publishRequest.getRepository(), WcmConstants.DRAFT_WS, publishRequest.getWcmPath());
			versionManager.checkin(absPath);
			session.save();
			
			String baseUrl = RestHelper.repositoryUrl(request);
			boolean newItem = false;
			
			Set<String> currentDescendants = new HashSet<String>();		
			if (this.syndicationEnabled) {
				try {
					RestNode restNode = (RestNode)this.wcmItemHandler.item(baseUrl, publishRequest.getRepository(),  WcmConstants.DEFAULT_WS, absPath, WcmConstants.FULL_SUB_DEPTH);
					if (restNode == null) {
						newItem = true;
					} else {
						
					}
					wcmEventService.populateDescendantIds(restNode, currentDescendants);
				} catch (Throwable t) {
					//TODO: 
					logger.error("");
					newItem = true;
				}
				
				
			}	
			
	        Session defaultSession = this.repositoryManager.getSession(publishRequest.getRepository(), WcmConstants.DEFAULT_WS);
	        
	        
	        defaultSession.getWorkspace().clone(WcmConstants.DRAFT_WS, absPath, absPath, true);
	        // defaultSession.save();
			
	        if (this.syndicationEnabled) {
				RestNode restNode = (RestNode)this.wcmItemHandler.item(baseUrl, publishRequest.getRepository(), WcmConstants.DEFAULT_WS, absPath, WcmConstants.FULL_SUB_DEPTH);
				if (newItem) {
					wcmEventService.addNewItemEvent(
							restNode, 
							publishRequest.getRepository(), 
							WcmConstants.DEFAULT_WS, 
							absPath,
							WcmEvent.WcmItemType.contentItem);
				} else {
					wcmEventService.addUpdateItemEvent(
						restNode, 
						publishRequest.getRepository(), 
						WcmConstants.DEFAULT_WS, 
						absPath,
						WcmEvent.WcmItemType.contentItem,
						currentDescendants);
				}
			}
	        
	        if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to publish draft", e);
			throw e;
		} catch (RepositoryException re) {
			logger.error("Failed to publish draft", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.PUBLISH_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error("Failed to publish draft", t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}		
	}
	
	@PutMapping("/expire/{repository}/{workspace}")
  	public void expireContentItem(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String absPath,
  			HttpServletRequest request) { 
    	if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
  		try {
  			String baseUrl = RestHelper.repositoryUrl(request);
  			Set<String> currentDescendants = new HashSet<String>();	
  			String nodeId = null;
			if (this.syndicationEnabled) {
				RestNode restNode = (RestNode)this.wcmItemHandler.item(baseUrl, repository,  WcmConstants.DEFAULT_WS, absPath, WcmConstants.FULL_SUB_DEPTH);
				nodeId = restNode.getId();
				wcmEventService.populateDescendantIds(restNode, currentDescendants);
			}
			
  			Session session = this.repositoryManager.getSession(repository, WcmConstants.DEFAULT_WS);
  			Node node = session.getNode(absPath);
  			String workflowState = node.getProperty("bpw:currentLifecycleState").getValue().getString();
            if (WcmConstants.WORKFLOW_STATGE_PUBLISHED.equals(workflowState)) {
            	node.remove();
            	session.save();
            	if (this.authoringEnabled) {
	        		session = this.repositoryManager.getSession(repository, WcmConstants.DRAFT_WS);
	      			node = session.getNode(absPath);
	            	node.setProperty("bpw:currentLifecycleState", WcmConstants.EXPIRED_WS);
	            	session.save();
            	}
            	
            	if (this.syndicationEnabled) {
            		wcmEventService.addDeleteItemEvent(
    						nodeId, 
    						repository, 
    						WcmConstants.DEFAULT_WS, 
    						absPath,
    						WcmEvent.WcmItemType.contentItem,
    						currentDescendants);
    			}
      			
            }
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to expire content item", e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error("Failed to expire content item", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.EXPIRE_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error("Failed to expire content item", t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
  		if (logger.isDebugEnabled()) {
			logger.debug("Exit");
		}
  	};
  	
  	@PutMapping("/workflowState/{repository}/{workspace}")
  	public void updateWcmItemWorkflowStage(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
		    @RequestParam("state") String state,
  			@RequestParam("path") String absPath) { 
  		try {
  			Session session = this.repositoryManager.getSession(repository, workspace);
  			Node node = session.getNode(absPath);
            node.setProperty("bpw:currentLifecycleState", state);
  			session.save();
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to update workflow stage", e);
			throw e;
		} catch (RepositoryException re) {
			logger.error("Failed to update workflow stage", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.UPDATE_CONTENT_ITEM_WORKFLOW_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error("Failed to update workflow stage", t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
  	};
  	
  	protected String startContentFlow(StartFlowRequest startFlowRequest, HttpServletRequest request) {
    	logger.debug("Entry");
		
    	String processInstanceId = bpmService.startContentFlow(
    			startFlowRequest,
				request);
		try {
		    this.mailService.sendEmailWithAttachment(
            		"create-draft",
            		new String[] {"a@yahoo.com", "b@gmail.com"},
            		"<h1>create-draft. Check attachment for image!</h1>",
            		"android.png");
        } catch (MessagingException e) {
        	logger.error("Failed to send email", e);
        	throw new WcmRepositoryException(WcmError.createWcmError("Failed to notification content reviewers", WcmErrors.EMAIL_ERROR, new String[] {startFlowRequest.getWcmPath()}));

        } catch (IOException e) {
        	logger.error("Failed to send email", e);
        	throw new WcmRepositoryException(WcmError.createWcmError("Failed to notification content reviewers", WcmErrors.EMAIL_ERROR, new String[] {startFlowRequest.getWcmPath()}));

        }
    	logger.debug("Exit");
		return processInstanceId;
	}
	
	protected void notifyRejectedItem(String authorEmail, DraftItemRequest rejectRequest) {		
	    this.mailService.sendEmail(
        		"create-draft",
        		new String[] {authorEmail},
        		"<h1>Draft you created was rejected.</h1>. The content path is " + rejectRequest.getWcmPath());
    }
	
	
	protected String getContentReviewUrl(String wcmPath) {
		return String.format("http://wcm-authoring:3009/wcm-authoring/review?contentPath=%s",  wcmPath);
	}

}
