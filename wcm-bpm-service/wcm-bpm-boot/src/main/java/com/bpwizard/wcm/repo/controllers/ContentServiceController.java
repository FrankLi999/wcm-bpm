package com.bpwizard.wcm.repo.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.content.ContentTaskService;
import com.bpwizard.wcm.repo.content.EditTaskService;
import com.bpwizard.wcm.repo.content.MailService;
import com.bpwizard.wcm.repo.content.ReviewTaskService;
import com.bpwizard.wcm.repo.content.WcmFlowService;
import com.bpwizard.wcm.repo.content.model.CompleteEditRequest;
import com.bpwizard.wcm.repo.content.model.CompleteReviewRequest;
import com.bpwizard.wcm.repo.content.model.ContentTask;
import com.bpwizard.wcm.repo.content.model.DeleteDraftRequest;
import com.bpwizard.wcm.repo.content.model.EditContentItemRequest;
import com.bpwizard.wcm.repo.content.model.ReviewContentItemRequest;
import com.bpwizard.wcm.repo.content.model.StartFlowRequest;


@RestController
@RequestMapping(ContentServiceController.BASE_URI)
public class ContentServiceController {
	private static final Logger logger = LogManager.getLogger(ContentServiceController.class);
	public static final String BASE_URI = "/content/server";
	
	@Autowired
	private ReviewTaskService externalRevieService;
	
	@Autowired
	private EditTaskService externalEditService;
	
	@Autowired
	private WcmFlowService wcmFlowService;
	
	@Autowired
	private ContentTaskService contentTaskService;
	
//    @Autowired
//    private MailService mailService;
	
    @GetMapping(path="/content-tasks/{topic}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ContentTask[] getContentTasks(@PathVariable("topic")  String topic) {
		return this.contentTaskService.getContentTasks(topic);
	}
	
	@GetMapping(path="/active-tasks/{topic}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ContentTask[] getActiveTasks(@PathVariable("topic")  String topic) {
		return this.contentTaskService.getActiveContentTasks(topic);
	}
	
	@PostMapping(path="/create-draft-with-message", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String startContentFlowWithMessage(@RequestBody StartFlowRequest startFlowRequest) {
		
		String processInstanceId = wcmFlowService.startContentFlowWithMessage(
				startFlowRequest.getRepository(),
				startFlowRequest.getWorkspace(),
				startFlowRequest.getContentId(),
				startFlowRequest.getBaseUrl(),
				startFlowRequest.getWorkflow());
		// this.template.convertAndSend("/wcm-topic/review", new Greeting(startFlowRequest.getContentId()));
//		try {
//		    this.mailService.sendEmailWithAttachment(
//            		"create-draft-with-message",
//            		new String[] {"a@yahoo.com", "b@gmail.com"},
//            		"<h1>create-draft-with-message. Check attachment for image!</h1>",
//            		"android.png");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
		return processInstanceId;
	}
	
    @PostMapping(path="/create-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    // @PreAuthorize("hasRole('ROLE_admin') or hasRole('admin') or hasRole('user')")
	public String startContentFlow(@RequestBody StartFlowRequest startFlowRequest) {
    	logger.traceEntry();
		
    	String processInstanceId = wcmFlowService.startContentFlow(
				startFlowRequest.getRepository(),
				startFlowRequest.getWorkspace(),
				startFlowRequest.getContentId(),
				startFlowRequest.getBaseUrl(),
				startFlowRequest.getWorkflow());
//		try {
//		    this.mailService.sendEmailWithAttachment(
//            		"create-draft",
//            		new String[] {"a@yahoo.com", "b@gmail.com"},
//            		"<h1>create-draft. Check attachment for image!</h1>",
//            		"android.png");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    	logger.traceExit();
		return processInstanceId;
	}
	
	@PostMapping(path="/review-content-item", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String reviewContentItem(@RequestBody ReviewContentItemRequest reviewContentItemRequest) {
		UserDetails principal = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
		
		//@CurrentUser SpringPrincipal userPrincipal, 
		return this.externalRevieService.claimTask(
				reviewContentItemRequest.getContentId(),
				reviewContentItemRequest.getTaskName(), 
				username);
	}
	
	@PostMapping(path="/complete-review-task", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String completeReview(@RequestBody CompleteReviewRequest completeReviewRequest) {
		return this.externalRevieService.completeReview(
				completeReviewRequest.getReviewTaskId(),
				completeReviewRequest.isApproved(), 
				completeReviewRequest.getComment());
	}
	
	@PostMapping(path="/edit-content-item", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String editContentItem(@RequestBody EditContentItemRequest editContentItemRequest) {
		UserDetails principal = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
		return this.externalEditService.claimTask(
				editContentItemRequest.getContentId(),
				editContentItemRequest.getTaskName(), 
				username);
	}
	
	@PostMapping(path="/complete-edit-task", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String completeEdit(@RequestBody CompleteEditRequest completeEditRequest) {
		return this.externalEditService.completeEdit(
				completeEditRequest.getTaskId());
	}
	
	@DeleteMapping(path="/delete-reviewing-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String deleteReviewingDraft(@RequestBody DeleteDraftRequest deleteDraftRequest) {
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
	
	@DeleteMapping(path="/delete-editing-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String deleteEditingingDraft(@RequestBody DeleteDraftRequest deleteDraftRequest) {
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
}
