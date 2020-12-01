package com.bpwizard.bpm.wcm.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.bpwizard.bpm.wcm.client.model.CancelDraftRequest;
import com.bpwizard.bpm.wcm.client.model.ClaimEditTaskReuqest;
import com.bpwizard.bpm.wcm.client.model.ClaimReivewTaskReuqest;
import com.bpwizard.bpm.wcm.client.model.DraftItemRequest;
import com.bpwizard.bpm.wcm.client.model.StartFlowRequest;
import com.bpwizard.bpm.wcm.client.model.UpdateDraftRequest;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;

@Service
public class BpmService {
	
	@Autowired
	RestTemplate restTemplate;
	
	// private String bpmServiceUrl = "http://localhost:28081/bpm/api/contentItem";
	private String startFlowServiceUrl = "http://localhost:28081/bpm/api/contentItem/start-flow";
	private String cancelDraftServiceUrl = "http://localhost:28081/bpm/api/contentItem/cancel-draft";
	private String rejectDraftServiceUrl = "http://localhost:28081/bpm/api/contentItem/reject-draft";
	private String approveDraftServiceUrl = "http://localhost:28081/bpm/api/contentItem/approve-draft";
	private String updateDraftServiceUrl = "http://localhost:28081/bpm/api/contentItem/update-draft";
	private String claimReviewTaskServiceUrl = "http://localhost:28081/bpm/api/contentItem/claim-review-task";
	private String claimEditTaskServiceUrl = "http://localhost:28081/bpm/api/contentItem/claim-edit-task";
	
	public String startContentFlow(
			StartFlowRequest startFlowRequest,
			HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", this.getAuthorizationToken(request));//Set the header for each request
		HttpEntity<StartFlowRequest> requestEntity = new HttpEntity<>(startFlowRequest, headers);
		ResponseEntity<String> resp = restTemplate.exchange(startFlowServiceUrl, HttpMethod.POST, requestEntity, String.class);
		if (!resp.getStatusCode().is2xxSuccessful()) {
			throw new WcmRepositoryException(new WcmError("WCM_START_CONTENT_FLOW", WcmErrors.CONTENT_ITEM_WORKFLOW_ERROR, null));
		}
		return resp.getBody();
	}
	
	public void canelDraft(
			CancelDraftRequest cancelDraftRequest,			
			HttpServletRequest request) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", this.getAuthorizationToken(request));//Set the header for each request
		HttpEntity<CancelDraftRequest> requestEntity = new HttpEntity<>(cancelDraftRequest, headers);
		ResponseEntity<Void> resp = restTemplate.exchange(cancelDraftServiceUrl, HttpMethod.POST, requestEntity, Void.class);
		if (!resp.getStatusCode().is2xxSuccessful()) {
			throw new WcmRepositoryException(new WcmError("WCM_CANCEL_DRAFT", WcmErrors.CONTENT_ITEM_WORKFLOW_ERROR, null));
		}		
	}
	
	public void rejectContentItemDraft(
			DraftItemRequest rejectRequest, 
			HttpServletRequest request) {
	
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", this.getAuthorizationToken(request));//Set the header for each request
		HttpEntity<DraftItemRequest> requestEntity = new HttpEntity<>(rejectRequest, headers);
		ResponseEntity<Void> resp = restTemplate.exchange(rejectDraftServiceUrl, HttpMethod.POST, requestEntity, Void.class);
		if (!resp.getStatusCode().is2xxSuccessful()) {
			throw new WcmRepositoryException(new WcmError("WCM_REJECT_DRAFT", WcmErrors.CONTENT_ITEM_WORKFLOW_ERROR, null));
		}	
	}
	
	
	
	public void approveContentItemDraft(
			@RequestBody DraftItemRequest approvalRequest,  
			HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", this.getAuthorizationToken(request));//Set the header for each request
		HttpEntity<DraftItemRequest> requestEntity = new HttpEntity<>(approvalRequest, headers);
		ResponseEntity<Void> resp = restTemplate.exchange(approveDraftServiceUrl, HttpMethod.POST, requestEntity, Void.class);
		if (!resp.getStatusCode().is2xxSuccessful()) {
			throw new WcmRepositoryException(new WcmError("WCM_APPROVE_DRAFT", WcmErrors.CONTENT_ITEM_WORKFLOW_ERROR, null));
		}	
	}
	
	public void updateDraft(
			UpdateDraftRequest updateDraftRequest, 
			HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", this.getAuthorizationToken(request));//Set the header for each request
		HttpEntity<UpdateDraftRequest> requestEntity = new HttpEntity<>(updateDraftRequest, headers);
		ResponseEntity<Void> resp = restTemplate.exchange(updateDraftServiceUrl, HttpMethod.POST, requestEntity, Void.class);
		if (!resp.getStatusCode().is2xxSuccessful()) {
			throw new WcmRepositoryException(new WcmError("WCM_UPDATE_DRAFT", WcmErrors.CONTENT_ITEM_WORKFLOW_ERROR, null));
		}	
	}
	
	public String claimReviewTask(
			ClaimReivewTaskReuqest claimRequest,  
			HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", this.getAuthorizationToken(request));//Set the header for each request
		HttpEntity<ClaimReivewTaskReuqest> requestEntity = new HttpEntity<>(claimRequest, headers);
		ResponseEntity<String> resp = restTemplate.exchange(claimReviewTaskServiceUrl, HttpMethod.POST, requestEntity, String.class);
		if (!resp.getStatusCode().is2xxSuccessful()) {
			throw new WcmRepositoryException(new WcmError("WCM_CLAIM_REVIEW_TASK", WcmErrors.CONTENT_ITEM_WORKFLOW_ERROR, null));
		}
		return resp.getBody();
	}
	
	public String claimEditTask(
			ClaimEditTaskReuqest editRequest,  
			HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", this.getAuthorizationToken(request));//Set the header for each request
		HttpEntity<ClaimEditTaskReuqest> requestEntity = new HttpEntity<>(editRequest, headers);
		ResponseEntity<String> resp = restTemplate.exchange(claimEditTaskServiceUrl, HttpMethod.POST, requestEntity, String.class);
		if (!resp.getStatusCode().is2xxSuccessful()) {
			throw new WcmRepositoryException(new WcmError("WCM_CLAIM_EDIT_TASK", WcmErrors.CONTENT_ITEM_WORKFLOW_ERROR, null));
		}
		return resp.getBody();
	}
	
	
	protected String getAuthorizationToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}
}
