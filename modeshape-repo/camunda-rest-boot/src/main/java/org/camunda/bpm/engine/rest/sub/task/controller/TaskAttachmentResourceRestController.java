package org.camunda.bpm.engine.rest.sub.task.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.rest.TaskRestService;
import org.camunda.bpm.engine.rest.dto.task.AttachmentDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.sub.task.TaskAttachmentResource;
import org.camunda.bpm.engine.task.Attachment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(TaskRestService.PATH + "/{taskId}/attachment")
public class TaskAttachmentResourceRestController extends AbstractTaskResourceRestController implements TaskAttachmentResource {
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<AttachmentDto> getAttachments(@PathVariable("taskId") String taskId) {
		if (!isHistoryEnabled()) {
			return Collections.emptyList();
		}

		ensureTaskExists(taskId, HttpStatus.NOT_FOUND);

		List<Attachment> taskAttachments = engine.getTaskService().getTaskAttachments(taskId);

		List<AttachmentDto> attachments = new ArrayList<AttachmentDto>();
		for (Attachment attachment : taskAttachments) {
			attachments.add(AttachmentDto.fromAttachment(attachment));
		}

		return attachments;
	}

	@GetMapping(path="/{attachmentId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public AttachmentDto getAttachment(@PathVariable("taskId") String taskId, @PathVariable("attachmentId") String attachmentId) {
		ensureHistoryEnabled(HttpStatus.NOT_FOUND);

		Attachment attachment = engine.getTaskService().getTaskAttachment(taskId, attachmentId);

		if (attachment == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Task attachment with id " + attachmentId + " does not exist for task id '" + taskId + "'.");
		}

		return AttachmentDto.fromAttachment(attachment);
	}

	@GetMapping(path="/{attachmentId}/data", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> getAttachmentData(@PathVariable("taskId") String taskId, @PathVariable("attachmentId") String attachmentId) {
		ensureHistoryEnabled(HttpStatus.NOT_FOUND);

		InputStream attachmentData = engine.getTaskService().getTaskAttachmentContent(taskId, attachmentId);

		if (attachmentData != null) {
			return ResponseEntity.ok().body(new InputStreamResource(attachmentData));
		} else {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "Attachment content for attachment with id '"
					+ attachmentId + "' does not exist for task id '" + taskId + "'.");
		}
	}

	@DeleteMapping(path="/{attachmentId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public void deleteAttachment(@PathVariable("taskId") String taskId, @PathVariable("attachmentId") String attachmentId) {
		ensureHistoryEnabled(HttpStatus.FORBIDDEN);

		try {
			engine.getTaskService().deleteTaskAttachment(taskId, attachmentId);
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Deletion is not possible. No attachment exists for task id '" + taskId + "' and attachment id '"
							+ attachmentId + "'.");
		}
	}

	@PostMapping(path="/create", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public AttachmentDto addAttachment(
			@PathVariable("taskId") String taskId, 
			HttpServletRequest request, 
			@RequestParam(name="url", required=false) String url,
			@RequestParam(name="attachment-name", required=false) String attachmentName,
			@RequestParam(name="attachment-type", required=false) String attachmentType,
			@RequestParam(name="attachment-description", required=false) String attachmentDescription,
			@RequestParam(name="content", required=false) MultipartFile content) {

		ensureHistoryEnabled(HttpStatus.FORBIDDEN);
		ensureTaskExists(taskId, HttpStatus.BAD_REQUEST);

		if (url == null && content == null) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST,
					"No content or url to remote content exists to create the task attachment.");
		}

		Attachment attachment = null;
		try {
			if (content != null) {
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
				attachment = engine.getTaskService().createAttachment(attachmentType, taskId, null, attachmentName,
						attachmentDescription, byteArrayInputStream);
			} else if (url != null) {
				attachment = engine.getTaskService().createAttachment(attachmentType, taskId, null, attachmentName,
						attachmentDescription, url);
			}
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, "Task id is null");
		}  catch (IOException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, "Content is not available");
		}

		URI uri = UriComponentsBuilder.fromPath(rootResourcePath).path(TaskRestService.PATH)
				.path(taskId + "/attachment/" + attachment.getId()).build().toUri();

		AttachmentDto attachmentDto = AttachmentDto.fromAttachment(attachment);

		// GET /
		attachmentDto.addReflexiveLink(uri, HttpMethod.GET.name(), "self");

		return attachmentDto;
	}
}
