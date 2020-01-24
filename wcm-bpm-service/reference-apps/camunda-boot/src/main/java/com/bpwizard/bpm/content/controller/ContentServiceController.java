package com.bpwizard.bpm.content.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.bpm.content.ExternalEditService;
import com.bpwizard.bpm.content.ExternalReviewService;
import com.bpwizard.bpm.content.WcmFlowService;
import com.bpwizard.bpm.content.model.CompleteEditRequest;
import com.bpwizard.bpm.content.model.CompleteReviewRequest;
import com.bpwizard.bpm.content.model.DeleteDraftRequest;
import com.bpwizard.bpm.content.model.EditContentItemRequest;
import com.bpwizard.bpm.content.model.ReviewContentItemRequest;
import com.bpwizard.bpm.content.model.ReviewTask;
import com.bpwizard.bpm.content.model.StartFlowRequest;


@RestController
@RequestMapping(ContentServiceController.BASE_URI)
public class ContentServiceController {
	private static final Logger logger = LogManager.getLogger(ContentServiceController.class);
	public static final String BASE_URI = "/content/server";
	

	@Autowired
	private ExternalReviewService externalRevieService;
	
	@Autowired
	private ExternalEditService externalEditService;
	
	@Autowired
	private WcmFlowService wcmFlowService;

//	@Autowired
//    private SimpMessagingTemplate template;
	
//    @Autowired
//    private MailService mailService;
	
	@PostMapping(path="/create-draft-with-message", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String startContentFlowWithMessage(@RequestBody StartFlowRequest startFlowRequest) {
		
		String processInstanceId = wcmFlowService.startContentFlowWithMessage(
				startFlowRequest.getRepository(),
				startFlowRequest.getWorkspace(),
				startFlowRequest.getContentPath(),
				startFlowRequest.getContentId(),
				startFlowRequest.getWorkflow());
		// this.template.convertAndSend("/wcm-topic/review", new Greeting(startFlowRequest.getContentId()));
//		try {
//		    this.mailService.sendEmailWithAttachment(
//            		"Testing from Spring Boot",
//            		new String[] {"a@yahoo.com", "b@gmail.com"},
//            		"<h1>Check attachment for image!</h1>",
//            		"android.png");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
		return processInstanceId;
	}
	
    @PostMapping(path="/create-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String startContentFlow(@RequestBody StartFlowRequest startFlowRequest) {
		
		String processInstanceId = wcmFlowService.startContentFlow(
				startFlowRequest.getRepository(),
				startFlowRequest.getWorkspace(),
				startFlowRequest.getContentPath(),
				startFlowRequest.getContentId(),
				startFlowRequest.getWorkflow());
		// this.template.convertAndSend("/wcm-topic/review", new Greeting(startFlowRequest.getContentId()));
//		try {
//		    this.mailService.sendEmailWithAttachment(
//            		"Testing from Spring Boot",
//            		new String[] {"a@yahoo.com", "b@gmail.com"},
//            		"<h1>Check attachment for image!</h1>",
//            		"android.png");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
		return processInstanceId;
	}
	
	@GetMapping(path="/review-tasks/{topic}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ReviewTask[] getReviewTasks(@PathVariable("topic")  String topic) {
		return this.externalRevieService.getReviewTasks(topic);
	}
	
	@PostMapping(path="/review-content-item", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String reviewContentItem(@RequestBody ReviewContentItemRequest reviewContentItemRequest) {
		return this.externalRevieService.claimTask( 
				reviewContentItemRequest.getContentId(),
				reviewContentItemRequest.getReviewTopic(), 
				reviewContentItemRequest.getWorkerId());
	}
	
	@PostMapping(path="/complete-review-task", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String completeReview(@RequestBody CompleteReviewRequest completeReviewRequest) {
		return this.externalRevieService.completeReview(
				completeReviewRequest.getReviewTaskId(),
				completeReviewRequest.getReviewTopic(),
				completeReviewRequest.getWorkerId(), 
				completeReviewRequest.isApproved(), 
				completeReviewRequest.getComment());
	}
	
//	@GetMapping(path="/edit-tasks/{topic}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ReviewTask[] getEditTasks(@PathVariable("topic")  String topic) {
//		return this.externalEditService.getEditTasks(topic);
//	}
//	
	@PostMapping(path="/edit-content-item", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String editContentItem(@RequestBody EditContentItemRequest editContentItemRequest) {
		return this.externalEditService.claimTask(
				editContentItemRequest.getContentId(),
				editContentItemRequest.getEditTopic(), 
				editContentItemRequest.getWorkerId());
	}
	
	@PostMapping(path="/complete-edit-task", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String completeEdit(@RequestBody CompleteEditRequest completeEditRequest) {
		return this.externalEditService.completeEdit(
				completeEditRequest.getEditTaskId(),
				completeEditRequest.getEditTopic(),
				completeEditRequest.getWorkerId());
	}
	
	@DeleteMapping(path="/delete-reviewing-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String deleteReviewingDraft(@RequestBody DeleteDraftRequest deleteDraftRequest) {
		String businessKey = String.format("%s%s", deleteDraftRequest.getWorkflow(), deleteDraftRequest.getContentId());
		Map<String, Object> variables = new HashMap<>();
		variables.put("contentId", deleteDraftRequest.getClass());
		this.wcmFlowService.sendMessage("deleteReviewingDraftMessage", businessKey, variables);
		//this.wcmFlowService.deleteReviewingDraft(deleteDraftRequest.getWorkflow(), deleteDraftRequest.getContentId());
		return "Deleted";
	}
	
	@DeleteMapping(path="/delete-editing-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String deleteEditingingDraft(@RequestBody DeleteDraftRequest deleteDraftRequest) {
//		Map<String,Object> variables = new HashMap<>();
//		variables.put("contentId", deleteDraftRequest.getContentId());
		String signalName = String.format("deleteEditingDraftSignal-%s%s", 
				deleteDraftRequest.getWorkflow(), deleteDraftRequest.getContentId());
		this.wcmFlowService.sendSignal(signalName, null);
		return "Deleted";
	}
}
