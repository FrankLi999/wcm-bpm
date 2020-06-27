package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
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
import com.bpwizard.wcm.repo.rest.bpm.model.EditContentItemRequest;
import com.bpwizard.wcm.repo.rest.bpm.model.ReviewContentItemRequest;
import com.bpwizard.wcm.repo.rest.bpm.model.StartFlowRequest;
import com.bpwizard.wcm.repo.rest.bpm.service.ContentTaskService;
import com.bpwizard.wcm.repo.rest.bpm.service.EditTaskService;
import com.bpwizard.wcm.repo.rest.bpm.service.MailService;
import com.bpwizard.wcm.repo.rest.bpm.service.ReviewTaskService;
import com.bpwizard.wcm.repo.rest.bpm.service.WcmFlowService;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.ContentItem;
import com.bpwizard.wcm.repo.rest.jcr.model.DraftItem;
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

	@Autowired
	private ReviewTaskService externalRevieService;
	
	@Autowired
	private EditTaskService externalEditService;
	
	@Autowired
	private WcmFlowService wcmFlowService;
	
	@Autowired
	private ContentTaskService contentTaskService;

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
			if (contentItem.getAcl() != null) {
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
			
			RestNode saNode = (RestNode) this.itemHandler.item(baseUrl, repository, "draft",
					absPath, WcmConstants.CONTENT_ITEM_DEPATH);
			DraftItem[] draftItems = saNode.getChildren().stream()
					.filter(WcmUtils::isDraftContentItem)
					.map(node -> this.toContentItem(node, repository, "draft", String.format("%s/%s", siteArea, node.getName()), request))
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
	@PostMapping(path = "/update-drfat", consumes = MediaType.APPLICATION_JSON_VALUE)
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
	
	@PostMapping(path = "/edit-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
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
		try {
			this.setWorkflowStage(contentItem, WcmConstants.WORKFLOW_STATGE_PUBLISHED);
			AuthoringTemplate at = this.doGetAuthoringTemplate(contentItem.getRepository(), contentItem.getWorkspace(), 
					contentItem.getAuthoringTemplate(), request);
			contentItem.setAcl(at.getContentItemAcl().getOnPublishPermissions());
			String absPath = WcmUtils.nodePath(contentItem.getWcmPath());
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, contentItem.getRepository(), contentItem.getWorkspace(), absPath, contentItem.toJson(at));

			if (contentItem.getAcl() != null) {
				String repositoryName = contentItem.getRepository();
				String workspaceName = contentItem.getWorkspace();
				Session session = this.repositoryManager.getSession(repositoryName, workspaceName);
				ModeshapeUtils.grantPermissions(session, absPath, contentItem.getAcl());
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
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.EDIT_DRAFT_CONTENT_ITEM_ERROR, null));
	    } catch (Throwable t) {
	    	logger.error(t);
	    	throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@PostMapping(path = "/cancel-draft/{repository}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> canelDraft(
			@PathVariable("repository") String repository,
			@RequestParam("path") String contentItemPath,
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
			Session session = this.repositoryManager.getSession(repository, WcmConstants.DEFAULT_WS);
			session.getWorkspace().clone(WcmConstants.DRAFT_WS, contentItemPath, contentItemPath, true);
			// session.save();
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
	
	@PutMapping(path = "/reject-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> rejectContentItemDraft(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
			@RequestParam("path") String contentItemPath,
			@RequestBody String comment, 
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
	        Session session = this.repositoryManager.getSession(repository, WcmConstants.DRAFT_WS); 
	        Node contentNode = session.getNode(contentItemPath);
	        Node commentsNode = contentNode.getNode("comments");
	        Node commentNode = commentsNode.addNode("comment-"+ username + "-reject-" + System.currentTimeMillis(), "bpw:comment");
	        commentNode.setProperty("bpw:comment", comment);
	        commentNode.setProperty("bpw:reviewer", username);
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.doGetAuthoringTemplate(repository, workspace, 
					atPath, request);
			ModeshapeUtils.grantPermissions(session, contentItemPath, at.getContentItemAcl().getOnReviewedDraftPermissions());
			this.wcmUtils.unlock(repository, workspace, contentItemPath);
	        session.save();
	        //TODO: notify editor
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
	
	@PutMapping(path = "/approve-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> approveContentItemDraft(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
			@RequestParam("path") String contentItemPath,
			@RequestBody String comment, 
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
	        Session session = this.repositoryManager.getSession(repository, WcmConstants.DRAFT_WS); 
	        Node contentNode = session.getNode(contentItemPath);
	        Node commentsNode = contentNode.getNode("comments");
	        Node commentNode = commentsNode.addNode("comment-"+ username + "-approval-" + System.currentTimeMillis(), "bpw:comment");
	        commentNode.setProperty("bpw:comment", comment);
	        commentNode.setProperty("bpw:reviewer", username);
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.doGetAuthoringTemplate(repository, workspace, 
					atPath, request);
			ModeshapeUtils.grantPermissions(session, contentItemPath, at.getContentItemAcl().getOnReviewedDraftPermissions());
			this.wcmUtils.unlock(repository, workspace, contentItemPath);
	        session.save();
	        //TODO: notify editor
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
	
	@PutMapping(path = "/publish", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> publishContentItem(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
			@RequestParam("path") String contentItemPath,
			@RequestBody String comment, 
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
	        Session session = this.repositoryManager.getSession(repository, WcmConstants.DRAFT_WS); 
	        Node contentNode = session.getNode(contentItemPath);
	        contentNode.setProperty("bpw:currentLifecycleState", WcmConstants.WORKFLOW_STATGE_PUBLISHED);
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.doGetAuthoringTemplate(repository, workspace, 
					atPath, request);
			ModeshapeUtils.grantPermissions(session, contentItemPath, at.getContentItemAcl().getOnPublishPermissions());
			this.wcmUtils.unlock(repository, workspace, contentItemPath);
	        session.save();
	        Session defaultSession = this.repositoryManager.getSession(repository, WcmConstants.DEFAULT_WS);
	        defaultSession.getWorkspace().clone(WcmConstants.DRAFT_WS, contentItemPath, contentItemPath, true);
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
	
	protected String startContentFlowWithMessage(@RequestBody StartFlowRequest startFlowRequest) throws WcmRepositoryException  {
		
		String processInstanceId = wcmFlowService.startContentFlowWithMessage(
				startFlowRequest.getRepository(),
				startFlowRequest.getWorkspace(),
				startFlowRequest.getWcmPath(),
				startFlowRequest.getBaseUrl(),
				startFlowRequest.getWorkflow());
	//this.template.convertAndSend("/wcm-topic/review", new Greeting(startFlowRequest.getContentId()));
		try {
		    this.mailService.sendEmailWithAttachment(
            		"create-draft-with-message",
            		new String[] {"a@yahoo.com", "b@gmail.com"},
            		"<h1>create-draft-with-message. Check attachment for image!</h1>",
            		"android.png");
        } catch (MessagingException e) {
        	logger.error("Failed to send email", e);
        	throw new WcmRepositoryException(WcmError.createWcmError("Failed to notification content reviewers", WcmErrors.EMAIL_ERROR, new String[] {startFlowRequest.getWcmPath()}));
        } catch (IOException e) {
        	logger.error("Failed to send email", e);
        	throw new WcmRepositoryException(WcmError.createWcmError("Failed to notification content reviewers", WcmErrors.EMAIL_ERROR, new String[] {startFlowRequest.getWcmPath()}));
        }
		return processInstanceId;
	}
	
	protected String startContentFlow(StartFlowRequest startFlowRequest) {
    	logger.traceEntry();
		
    	String processInstanceId = wcmFlowService.startContentFlow(
				startFlowRequest.getRepository(),
				startFlowRequest.getWorkspace(),
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
	
	protected String reviewContentItem(@RequestBody ReviewContentItemRequest reviewContentItemRequest) {
		UserDetails principal = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
		
		//@CurrentUser SpringPrincipal userPrincipal, 
		return this.externalRevieService.claimTask(
				reviewContentItemRequest.getContentId(),
				reviewContentItemRequest.getTaskName(), 
				username);
	}
	
	protected String completeReview(@RequestBody CompleteReviewRequest completeReviewRequest) {
		return this.externalRevieService.completeReview(
				completeReviewRequest.getReviewTaskId(),
				completeReviewRequest.isApproved(), 
				completeReviewRequest.getComment());
	}
	
	protected String editContentItem(@RequestBody EditContentItemRequest editContentItemRequest) {
		UserDetails principal = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
		return this.externalEditService.claimTask(
				editContentItemRequest.getContentId(),
				editContentItemRequest.getTaskName(), 
				username);
	}
	
	protected String completeEdit(@RequestBody CompleteEditRequest completeEditRequest) {
		return this.externalEditService.completeEdit(
				completeEditRequest.getTaskId());
	}
	
	protected String deleteReviewingDraft(@RequestBody DeleteDraftRequest deleteDraftRequest) {
		String businessKey = String.format("%s%s", deleteDraftRequest.getWorkflow(), deleteDraftRequest.getContentId());
		Map<String, Object> variables = new HashMap<>();
		variables.put("contentId", deleteDraftRequest.getClass());
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
		String signalName = String.format("deleteEditingDraftSignal-%s%s", 
				deleteDraftRequest.getWorkflow(), deleteDraftRequest.getContentId());
		this.wcmFlowService.sendSignal(signalName, null);
		
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
		
		return "Deleted";
	}
	
	protected String getContentReviewUrl(ContentItem contentItem) {
		return String.format("http://wcm-ui:3009/wcm-authoring/review?contentPath=%s", contentItem.getWcmPath());
	}
}
