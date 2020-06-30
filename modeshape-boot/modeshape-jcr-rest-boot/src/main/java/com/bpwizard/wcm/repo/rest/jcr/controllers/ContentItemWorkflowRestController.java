package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.VersionManager;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import com.bpwizard.wcm.repo.rest.ModeshapeUtils;
import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.bpm.model.CompleteEditRequest;
import com.bpwizard.wcm.repo.rest.bpm.model.CompleteReviewRequest;
import com.bpwizard.wcm.repo.rest.bpm.model.ContentTask;
import com.bpwizard.wcm.repo.rest.bpm.model.DeleteDraftRequest;
import com.bpwizard.wcm.repo.rest.bpm.model.StartFlowRequest;
import com.bpwizard.wcm.repo.rest.bpm.service.ContentServerUtils;
import com.bpwizard.wcm.repo.rest.bpm.service.ContentTaskService;
import com.bpwizard.wcm.repo.rest.bpm.service.EditTaskService;
import com.bpwizard.wcm.repo.rest.bpm.service.MailService;
import com.bpwizard.wcm.repo.rest.bpm.service.ReviewTaskService;
import com.bpwizard.wcm.repo.rest.bpm.service.WcmFlowService;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.CancelDraftRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.ClaimEditTaskReuqest;
import com.bpwizard.wcm.repo.rest.jcr.model.ClaimReivewTaskReuqest;
import com.bpwizard.wcm.repo.rest.jcr.model.ContentItem;
import com.bpwizard.wcm.repo.rest.jcr.model.DraftItem;
import com.bpwizard.wcm.repo.rest.jcr.model.DraftItemRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.PublishItemRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.WorkflowNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;

@RestController
@RequestMapping(ContentItemWorkflowRestController.BASE_URI)
@Validated
public class ContentItemWorkflowRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(ContentItemWorkflowRestController.class);
	
	public static final String BASE_URI = "/wcm/api/contentItem";

//	@Autowired
//	private ReviewTaskService reviewTaskService;
	
	@Autowired
	private EditTaskService editTaskService;
	
	@Autowired
	private WcmFlowService wcmFlowService;
	
	@Autowired
	private ContentTaskService contentTaskService;
	
	@Autowired
	private ReviewTaskService reviewTaskService;

	@Autowired
	private MailService mailService;
	
	@PostMapping(path = "/create-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createDraft(			
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		contentItem.getProperties().setAuthor(WcmUtils.getCurrentUsername());
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		String absPath = WcmUtils.nodePath(contentItem.getWcmPath(), contentItem.getProperties().getName());
		String baseUrl = RestHelper.repositoryUrl(request);
		AuthoringTemplate at = null;
		String processInstanceId = null;
		RestNode newNode = null;
		try {
			this.setWorkflowStage(contentItem, WcmConstants.WORKFLOW_STATGE_DRAFT);
			at = this.doGetAuthoringTemplate(contentItem.getRepository(), WcmConstants.DRAFT_WS, 
					contentItem.getAuthoringTemplate(), request);
			if (at.getContentItemAcl() != null && at.getContentItemAcl().getOnSaveDraftPermissions() != null) {
				contentItem.setAcl(at.getContentItemAcl().getOnSaveDraftPermissions());
			}
			newNode = (RestNode)this.itemHandler.addItem(baseUrl, contentItem.getRepository(), WcmConstants.DRAFT_WS, absPath, contentItem.toJson(at)).getBody();
			if (at.getContentItemAcl() != null && at.getContentItemAcl().getOnSaveDraftPermissions() != null) {
				String repositoryName = contentItem.getRepository();
				Session session = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
				ModeshapeUtils.grantPermissions(session, absPath, at.getContentItemAcl().getOnSaveDraftPermissions());
			}
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.CREATE_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
		try {
			StartFlowRequest startFlowRequest = StartFlowRequest.createStartContentFlowRequest(
					contentItem.getProperties().getAuthor(),
					contentItem.getRepository(), 
					contentItem.getWorkspace(), 
					newNode.getId(),
					String.format("%s/%s", contentItem.getWcmPath(), contentItem.getProperties().getName()),
					this.getContentReviewUrl(contentItem),
					"wcm_content_flow");
			
			processInstanceId = this.startContentFlow(startFlowRequest);
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
			this.itemHandler.updateItem(baseUrl, contentItem.getRepository(), WcmConstants.DRAFT_WS, absPath, contentItem.toJson(at));
			//TODO: prepare for web socket push of reviewing tasks.
			if (logger.isDebugEnabled()) {
				logger.traceExit();
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
			logger.traceEntry();
		}

		String absPath = WcmUtils.nodePath(siteArea);
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			
			RestNode saNode = (RestNode) this.itemHandler.item(baseUrl, repository, WcmConstants.DRAFT_WS,
					absPath, WcmConstants.CONTENT_ITEM_DEPATH);
			DraftItem[] draftItems = saNode.getChildren().stream()
					.filter(WcmUtils::isDraftContentItem)
					.map(node -> this.toContentItem(node, repository, WcmConstants.DRAFT_WS, String.format("%s/%s", siteArea, node.getName()), request))
					.map(WcmUtils::toDraftItem)
					.toArray(DraftItem[]::new);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return  ResponseEntity.status(HttpStatus.OK).body(draftItems);
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}
	
	@PostMapping(path = "/edit-as-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> editDraft(
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authoring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		String processInstanceId = null;
		try {
			StartFlowRequest startFlowRequest = StartFlowRequest.createStartContentFlowRequest(
					contentItem.getProperties().getAuthor(),
					contentItem.getRepository(), 
					contentItem.getWorkspace(), 
					contentItem.getId(),
					contentItem.getWcmPath(),
					this.getContentReviewUrl(contentItem),
					"wcm_content_flow");
			
			processInstanceId = this.startContentFlow(startFlowRequest);
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to start content workflow", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to start content workflow", t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
		
		try {
			
			
			String absPath = WcmUtils.nodePath(contentItem.getWcmPath());
			Session draftSession = this.repositoryManager.getSession(contentItem.getRepository(), WcmConstants.DRAFT_WS);
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
	        AuthoringTemplate at = this.doGetAuthoringTemplate(contentItem.getRepository(), WcmConstants.DRAFT_WS, 
					contentItem.getAuthoringTemplate(), request);
			if (at.getContentItemAcl() != null && at.getContentItemAcl().getOnSaveDraftPermissions() != null) {
				String repositoryName = contentItem.getRepository();
				Session session = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
				ModeshapeUtils.grantPermissions(session, absPath, at.getContentItemAcl().getOnSaveDraftPermissions());
			}
			versionManager.checkin(absPath);
			draftSession.save();
			
			Session defaultSession = this.repositoryManager.getSession(contentItem.getRepository(), WcmConstants.DEFAULT_WS);
			defaultSession.removeItem(absPath);
			defaultSession.save();			
			
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.EDIT_DRAFT_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "/cancel-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> canelDraft(
			@RequestBody CancelDraftRequest cancelDraftRequest,			
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authoring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
			String absPath = WcmUtils.nodePath(cancelDraftRequest.getWcmPath());
			Session draftSession = this.repositoryManager.getSession(cancelDraftRequest.getRepository(), WcmConstants.DRAFT_WS);
			draftSession.removeItem(absPath);
			draftSession.save();
			try {
				draftSession.getWorkspace().clone(WcmConstants.DEFAULT_WS, absPath, absPath, true);
			} catch (Exception e) {
				logger.warn("Cancel Draft - not able to clone the corresponding item from the default the workspace");
			}
			
			DeleteDraftRequest deleteDraftRequest = DeleteDraftRequest.createDeleteDraftRequest(
					cancelDraftRequest.getContentId(),
					"wcm_content_flow");
			if (StringUtils.hasText(cancelDraftRequest.getReviewTaskId())) {
				this.deleteReviewingDraft(deleteDraftRequest);
			} else if (StringUtils.hasText(cancelDraftRequest.getReviewTaskId())) {
				this.deleteEditingingDraft(deleteDraftRequest);
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
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.CANCEL_DRAFT_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "/reject-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> rejectContentItemDraft(
			@RequestBody DraftItemRequest rejectRequest, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authring is not enabled");
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
	        workflowNode.setProperty("reviewTaskId", (String)null);
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
	        String authorEmail = contentNode.getProperty("jcr:createdBy").getString();
			AuthoringTemplate at = this.doGetAuthoringTemplate(rejectRequest.getRepository(), WcmConstants.DRAFT_WS, 
					atPath, request);
			if (at.getContentItemAcl() != null && at.getContentItemAcl().getOnReviewedDraftPermissions() != null) {
				ModeshapeUtils.grantPermissions(session, absPath, at.getContentItemAcl().getOnReviewedDraftPermissions());
			}
			// this.wcmUtils.unlock(rejectRequest.getRepository(), WcmConstants.DRAFT_WS, rejectRequest.getWcmPath());
			versionManager.checkin(absPath);
	        session.save();

	        CompleteReviewRequest completeReviewRequest = CompleteReviewRequest.createCompleteReviewRequest(
	        		rejectRequest.getReviewTaskId(), 
	        		false,
	        		rejectRequest.getComment(),
	        		this.getAuthorizationToken(request),
	        		"http://wcm-server:28080/wcm/api/contentItem/publish");
	        this.completeReview(completeReviewRequest);
	        this.notifyRejectedItem(authorEmail, rejectRequest);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) {
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.REJECT_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "/approve-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> approveContentItemDraft(
			@RequestBody DraftItemRequest approvalRequest,  
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authring is not enabled");
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
	        workflowNode.setProperty("reviewTaskId", (String)null);
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.doGetAuthoringTemplate(approvalRequest.getRepository(), WcmConstants.DRAFT_WS, 
					atPath, request);
			if (at.getContentItemAcl() != null && at.getContentItemAcl().getOnReviewedDraftPermissions() != null) {
				ModeshapeUtils.grantPermissions(session, absPath, at.getContentItemAcl().getOnReviewedDraftPermissions());
			}
			// this.wcmUtils.unlock(approvalRequest.getRepository(), WcmConstants.DRAFT_WS, absPath);
			versionManager.checkin(absPath);
			session.save();
	        
	        CompleteReviewRequest completeReviewRequest = CompleteReviewRequest.createCompleteReviewRequest(
	        		approvalRequest.getReviewTaskId(), 
	        		true,
	        		approvalRequest.getComment(),
	        		this.getAuthorizationToken(request),
	        		"http://wcm-server:28080/wcm/api/contentItem/publish");
	        this.completeReview(completeReviewRequest);

			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) {
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.APPROVE_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "/update-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateDraft(
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authoring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
			String absPath = WcmUtils.nodePath(contentItem.getWcmPath());
			String baseUrl = RestHelper.repositoryUrl(request);
			String editTaskId = contentItem.getWorkflow().getEditTaskId();
			contentItem.getWorkflow().setEditTaskId(null);
			AuthoringTemplate at = this.doGetAuthoringTemplate(
					contentItem.getRepository(), 
					WcmConstants.DRAFT_WS, 
					contentItem.getAuthoringTemplate(), 
					request);
			this.itemHandler.updateItem(baseUrl, contentItem.getRepository(), WcmConstants.DRAFT_WS, absPath, contentItem.toJson(at));
			if (contentItem.getAcl() != null) {
				String repositoryName = contentItem.getRepository();
				String workspaceName = contentItem.getWorkspace();
				Session session = this.repositoryManager.getSession(repositoryName, workspaceName);
				ModeshapeUtils.grantPermissions(session, absPath, contentItem.getAcl());
			}
			if (StringUtils.hasText(editTaskId)) {
				CompleteEditRequest completeEditRequest = CompleteEditRequest.createCompleteEditRequest(
						editTaskId);
		        this.completeEdit(completeEditRequest);
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
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.LOCK_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "/claim-review-task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> claimReviewTask(
			@RequestBody ClaimReivewTaskReuqest claimRequest,  
			HttpServletRequest request)
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		String reviewTaskId = this.reviewTaskService.claimTask(
				claimRequest.getContentId(), 
				"review-content", 
				WcmUtils.getCurrentUsername());
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
		    workflowNode.setProperty("reviewTaskId", reviewTaskId);
		    versionManager.checkin(absPath);
		    session.save();
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.ok(String.format("{\"reviewTaskId\": \"%s\"}", reviewTaskId));
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
			logger.traceEntry();
		}
		String editTaskId = this.editTaskService.claimTask(
				editRequest.getContentId(), 
				"edit-task", 
				WcmUtils.getCurrentUsername());
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
		    workflowNode.setProperty("editTaskId", editTaskId);
		    versionManager.checkin(absPath);
		    session.save();
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.ok(String.format("{\"editTaskId\": \"%s\"}", editTaskId));
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
			logger.traceEntry();
		}
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authring is not enabled");
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
	        String atPath = workflowNode.getParent().getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.doGetAuthoringTemplate(publishRequest.getRepository(), WcmConstants.DRAFT_WS, 
					atPath, request);
			if (at.getContentItemAcl() != null && at.getContentItemAcl().getOnPublishPermissions() != null) {
				ModeshapeUtils.grantPermissions(session, absPath, at.getContentItemAcl().getOnPublishPermissions());
			}
			// this.wcmUtils.unlock(publishRequest.getRepository(), WcmConstants.DRAFT_WS, publishRequest.getWcmPath());
			versionManager.checkin(absPath);
			session.save();
	        Session defaultSession = this.repositoryManager.getSession(publishRequest.getRepository(), WcmConstants.DEFAULT_WS);
	        defaultSession.getWorkspace().clone(WcmConstants.DRAFT_WS, absPath, absPath, true);
	        // defaultSession.save();
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) {
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.PUBLISH_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}		
	}
	
	@PutMapping("/expire/{repository}/{workspace}")
  	public void expireContentItem(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String absPath) { 
    	if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
  		try {
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
            }
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.EXPIRE_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
  		if (logger.isDebugEnabled()) {
			logger.traceExit();
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
			logger.error(e);
			throw e;
		} catch (RepositoryException re) {
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.UPDATE_CONTENT_ITEM_WORKFLOW_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
  	};
  	
  	
  	
	protected ContentTask[] getContentTasks(@PathVariable("topic")  String topic) {
		return this.contentTaskService.getContentTasks(topic);
	}
	
	protected ContentTask[] getActiveTasks(@PathVariable("topic")  String topic) {
		return this.contentTaskService.getActiveContentTasks(topic);
	}
	
//	protected String startContentFlowWithMessage(@RequestBody StartFlowRequest startFlowRequest) throws WcmRepositoryException  {
//		
//		String processInstanceId = wcmFlowService.startContentFlowWithMessage(
//				startFlowRequest.getRepository(),
//				startFlowRequest.getWorkspace(),
//				startFlowRequest.getContentId(),
//				startFlowRequest.getWcmPath(),
//				startFlowRequest.getBaseUrl(),
//				startFlowRequest.getWorkflow());
//	//this.template.convertAndSend("/wcm-topic/review", new Greeting(startFlowRequest.getContentId()));
//		try {
//		    this.mailService.sendEmailWithAttachment(
//            		"create-draft-with-message",
//            		new String[] {"a@yahoo.com", "b@gmail.com"},
//            		"<h1>create-draft-with-message. Check attachment for image!</h1>",
//            		"android.png");
//        } catch (MessagingException e) {
//        	logger.error("Failed to send email", e);
//        	throw new WcmRepositoryException(WcmError.createWcmError("Failed to notification content reviewers", WcmErrors.EMAIL_ERROR, new String[] {startFlowRequest.getWcmPath()}));
//        } catch (IOException e) {
//        	logger.error("Failed to send email", e);
//        	throw new WcmRepositoryException(WcmError.createWcmError("Failed to notification content reviewers", WcmErrors.EMAIL_ERROR, new String[] {startFlowRequest.getWcmPath()}));
//        }
//		return processInstanceId;
//	}
	
	protected String startContentFlow(StartFlowRequest startFlowRequest) {
    	logger.traceEntry();
		
    	String processInstanceId = wcmFlowService.startContentFlow(
    			startFlowRequest.getAuthor(),
    			startFlowRequest.getRepository(),
				startFlowRequest.getWorkspace(),
				startFlowRequest.getContentId(),
				startFlowRequest.getWcmPath(),				
				startFlowRequest.getBaseUrl(),
				startFlowRequest.getWorkflow());
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
    	logger.traceExit();
		return processInstanceId;
	}
	
	protected void notifyRejectedItem(String authorEmail, DraftItemRequest rejectRequest) {		
	    this.mailService.sendEmail(
        		"create-draft",
        		new String[] {authorEmail},
        		"<h1>Draft you created was rejected.</h1>. The content path is " + rejectRequest.getWcmPath());
    }
	
//	protected String reviewContentItem(@RequestBody ReviewContentItemRequest reviewContentItemRequest) {
//		UserDetails principal = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = principal.getUsername();
//		
//		//@CurrentUser SpringPrincipal userPrincipal, 
//		return this.reviewTaskService.claimTask(
//				reviewContentItemRequest.getContentId(),
//				reviewContentItemRequest.getTaskName(), 
//				username);
//	}
	
	protected String completeReview(@RequestBody CompleteReviewRequest completeReviewRequest) {
		return this.reviewTaskService.completeReview(
				completeReviewRequest.getReviewTaskId(),
				completeReviewRequest.isApproved(), 
				completeReviewRequest.getComment(),
				completeReviewRequest.getToken(),
				completeReviewRequest.getPublishServiceUrl());
	}
	
//	protected String editContentItem(@RequestBody EditContentItemRequest editContentItemRequest) {
//		UserDetails principal = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = principal.getUsername();
//		return this.editTaskService.claimTask(
//				editContentItemRequest.getContentId(),
//				editContentItemRequest.getTaskName(), 
//				username);
//	}
	
	protected String completeEdit(CompleteEditRequest completeEditRequest) {
		return this.editTaskService.completeEdit(
				completeEditRequest.getTaskId());
	}
	
	protected String deleteReviewingDraft(@RequestBody DeleteDraftRequest deleteDraftRequest) {
		String businessKey = ContentServerUtils.getBusinessKey(deleteDraftRequest.getWorkflow(), deleteDraftRequest.getContentId());
		Map<String, Object> variables = new HashMap<>();
		variables.put("contentId", deleteDraftRequest.getContentId());
		this.wcmFlowService.sendMessage("deleteReviewingDraftMessage", businessKey, variables);
//		try {
//		    this.mailService.sendEmailWithAttachment(
//            		"delete-reviewing-draft",
//            		new String[] {"a@yahoo.com", "b@gmail.com"},
//            		"<h1>delete-reviewing-draft. Check attachment for image!</h1>",
//            		"android.png");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
		return "Deleted";
	}
	
	protected String deleteEditingingDraft(@RequestBody DeleteDraftRequest deleteDraftRequest) {
//		Map<String,Object> variables = new HashMap<>();
//		variables.put("contentId", deleteDraftRequest.getContentId());
//		String signalName = String.format("deleteEditingDraftSignal-%s%s", 
//				deleteDraftRequest.getWorkflow(), deleteDraftRequest.getContentId());
//		this.wcmFlowService.sendSignal(signalName, null);
		
//		try {
//		    this.mailService.sendEmailWithAttachment(
//            		"delete-editing-draft",
//            		new String[] {"a@yahoo.com", "b@gmail.com"},
//            		"<h1>delete-editing-draft. Check attachment for image!</h1>",
//            		"android.png");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
		String businessKey = ContentServerUtils.getBusinessKey(deleteDraftRequest.getWorkflow(), deleteDraftRequest.getContentId());
		Map<String, Object> variables = new HashMap<>();
		variables.put("contentId", deleteDraftRequest.getContentId());
		this.wcmFlowService.sendMessage("deleteEditingDraftMessage", businessKey, variables);
		
		return "Deleted";
	}
	
	protected String getContentReviewUrl(ContentItem contentItem) {
		return String.format("http://wcm-ui:3009/wcm-authoring/review?contentPath=%s", contentItem.getWcmPath());
	}
	
	protected String getAuthorizationToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}
}
