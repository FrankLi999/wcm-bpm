package com.bpwizard.bpm.wcm.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.bpm.wcm.client.model.CancelDraftRequest;
import com.bpwizard.bpm.wcm.client.model.ClaimEditTaskReuqest;
import com.bpwizard.bpm.wcm.client.model.ClaimReivewTaskReuqest;
import com.bpwizard.bpm.wcm.client.model.CompleteEditRequest;
import com.bpwizard.bpm.wcm.client.model.CompleteReviewRequest;
import com.bpwizard.bpm.wcm.client.model.DeleteDraftRequest;
import com.bpwizard.bpm.wcm.client.model.DraftItemRequest;
import com.bpwizard.bpm.wcm.client.model.StartFlowRequest;
import com.bpwizard.bpm.wcm.client.model.UpdateDraftRequest;
import com.bpwizard.bpm.wcm.service.ContentServerUtils;
import com.bpwizard.bpm.wcm.service.EditTaskService;
import com.bpwizard.bpm.wcm.service.ReviewTaskService;
import com.bpwizard.bpm.wcm.service.WcmFlowService;
import com.bpwizard.spring.boot.commons.web.util.WebUtils;

@RestController
@RequestMapping(ContentItemWorkflowRestController.BASE_URI)
@Validated
public class ContentItemWorkflowRestController { //extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(ContentItemWorkflowRestController.class);
	
	public static final String BASE_URI = "/bpm/api/contentItem";

	@Autowired
	private EditTaskService editTaskService;
	
	@Autowired
	private WcmFlowService wcmFlowService;
	
	@Autowired
	private ReviewTaskService reviewTaskService;

	@PostMapping(path = "/start-flow", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> startFlow(			
			@RequestBody StartFlowRequest startFlowRequest, 
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}

		String processInstanceId = this.startContentFlow(startFlowRequest);
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return ResponseEntity.ok(processInstanceId);
	}
	
	@PostMapping(path = "/cancel-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> canelDraft(
			@RequestBody CancelDraftRequest cancelDraftRequest,			
			HttpServletRequest request) {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		
		DeleteDraftRequest deleteDraftRequest = DeleteDraftRequest.createDeleteDraftRequest(
				cancelDraftRequest.getContentId(),
				"wcm_content_flow");
		String reviewTaskId = this.reviewTaskService.getReviewTaskId(
				cancelDraftRequest.getContentId(), 
				"review-content");
		if (StringUtils.hasText(reviewTaskId)) {
			this.deleteReviewingDraft(deleteDraftRequest);
		} else {
			String editTaskId = this.editTaskService.getEditTaskId(
				cancelDraftRequest.getContentId(), 
				"edit-task");
			if (StringUtils.hasText(editTaskId)) {
				this.deleteEditingingDraft(deleteDraftRequest);
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}
	
	@PostMapping(path = "/reject-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> rejectContentItemDraft(
			@RequestBody DraftItemRequest rejectRequest, 
			HttpServletRequest request) {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		
		String reviewTaskId = this.reviewTaskService.getReviewTaskId(
				rejectRequest.getContentId(), 
				"review-content"); 
        CompleteReviewRequest completeReviewRequest = CompleteReviewRequest.createCompleteReviewRequest(
        		reviewTaskId, 
        		false,
        		rejectRequest.getComment(),
        		"http://wcm-server.bpwizard.com:28080/wcm/api/contentItem/publish");
        this.completeReview(completeReviewRequest, request);
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();		
	}
	
	@PostMapping(path = "/approve-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> approveContentItemDraft(
			@RequestBody DraftItemRequest approvalRequest,  
			HttpServletRequest request) {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		
		String reviewTaskId = this.reviewTaskService.getReviewTaskId(
				approvalRequest.getContentId(), 
				"review-content"); 
        CompleteReviewRequest completeReviewRequest = CompleteReviewRequest.createCompleteReviewRequest(
        		reviewTaskId, 
        		true,
        		approvalRequest.getComment(),
        		"http://wcm-server.bpwizard.com:28080/wcm/api/contentItem/publish");
        this.completeReview(completeReviewRequest, request);

		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}
	
	@PostMapping(path = "/update-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateDraft(
			@RequestBody UpdateDraftRequest updateDraftRequest, 
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		

		String editTaskId = this.editTaskService.getEditTaskId(updateDraftRequest.getContentId(), "edit-task");
		CompleteEditRequest completeEditRequest = CompleteEditRequest.createCompleteEditRequest(
				editTaskId);
        this.completeEdit(completeEditRequest);
			
	        
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}
	
	@PostMapping(path = "/claim-review-task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> claimReviewTask(
			@RequestBody ClaimReivewTaskReuqest claimRequest,  
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		String taskId = this.reviewTaskService.claimTask(
				claimRequest.getContentId(), 
				"review-content", 
				WebUtils.currentUser().getUsername());
		
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return ResponseEntity.ok(taskId);
		
	}
	
	@PostMapping(path = "/claim-edit-task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> claimEditTask(
			@RequestBody ClaimEditTaskReuqest editRequest,  
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		String editTaskId = this.editTaskService.claimTask(
				editRequest.getContentId(), 
				"edit-task", 
				WebUtils.currentUser().getUsername());
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return ResponseEntity.ok(editTaskId);
	}

	protected String getContentReviewUrl(String wcmPath) {
		return String.format("http://wcm-authoring:3009/wcm-authoring/review?contentPath=%s",  wcmPath);
	}
	
	protected String getAuthorizationToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}
	
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

	    logger.traceExit();
		return processInstanceId;
	}
	
	protected String deleteReviewingDraft(@RequestBody DeleteDraftRequest deleteDraftRequest) {
		String businessKey = ContentServerUtils.getBusinessKey(deleteDraftRequest.getWorkflow(), deleteDraftRequest.getContentId());
		Map<String, Object> variables = new HashMap<>();
		variables.put("contentId", deleteDraftRequest.getContentId());
		this.wcmFlowService.sendMessage("deleteReviewingDraftMessage", businessKey, variables);
		return "Deleted";
	}

	protected String deleteEditingingDraft(@RequestBody DeleteDraftRequest deleteDraftRequest) {

		String businessKey = ContentServerUtils.getBusinessKey(deleteDraftRequest.getWorkflow(), deleteDraftRequest.getContentId());
		Map<String, Object> variables = new HashMap<>();
		variables.put("contentId", deleteDraftRequest.getContentId());
		this.wcmFlowService.sendMessage("deleteEditingDraftMessage", businessKey, variables);
		
		return "Deleted";
	}
	
	protected String completeReview(@RequestBody CompleteReviewRequest completeReviewRequest, HttpServletRequest request) {
		return this.reviewTaskService.completeReview(
				completeReviewRequest.getReviewTaskId(),
				completeReviewRequest.isApproved(), 
				completeReviewRequest.getComment(),
				this.getAuthorizationToken(request),
				completeReviewRequest.getPublishServiceUrl());
	}

	protected String completeEdit(CompleteEditRequest completeEditRequest) {
		return this.editTaskService.completeEdit(
				completeEditRequest.getTaskId());
	}
}
