package com.bpwizard.wcm.repo.rest.bpm.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.bpwizard.wcm.repo.rest.jcr.model.PublishItemRequest;

public class PublishContentItemDelegate implements JavaDelegate {
	private static final Logger logger = LogManager.getLogger(PublishContentItemDelegate.class);
	
	@Autowired
	protected RepositoryManager repositoryManager;

	@Autowired
	RestTemplate restTemplate;
	
	@Override
	public void execute(DelegateExecution delegate) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repository = (String) delegate.getVariable("repository");
			String wcmPath = (String) delegate.getVariable("wcmPath");
			String contentId = (String) delegate.getVariable("contentId");
			String token = (String) delegate.getVariable("token");
			String publishServiceUrl = (String) delegate.getVariable("publishServiceUrl");
			// String baseUrl = (String) delegate.getVariable("baseUrl");
			PublishItemRequest publishRequest = PublishItemRequest.createPublishItemRequest(repository, wcmPath, contentId);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", token);//Set the header for each request
			HttpEntity<PublishItemRequest> requestEntity = new HttpEntity<>(publishRequest, headers);
			ResponseEntity<Void> resp = restTemplate.exchange(publishServiceUrl, HttpMethod.POST, requestEntity, Void.class);
			if (!resp.getStatusCode().is2xxSuccessful()) {
				throw new BpmnError("WCM_ERROR_PUBLISH");
			}
	        if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
		} catch (BpmnError e) {
			logger.error("WCM_ERROR_PUBLISH", e);
			throw e;
	    } catch (Throwable t) {
	    	logger.error("WCM_ERROR_PUBLISH", t);
			throw new BpmnError("WCM_ERROR_PUBLISH");
		}			
	}
}
