package org.camunda.bpm.engine.rest.sub.task;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.rest.dto.task.AttachmentDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface TaskAttachmentResource {

  List<AttachmentDto> getAttachments(String taskId);

  AttachmentDto getAttachment(String taskId, String attachmentId);

  ResponseEntity<Resource> getAttachmentData(String taskId, String attachmentId);

  void deleteAttachment(String taskId, String attachmentId);

  public AttachmentDto addAttachment(
			String taskId, 
			HttpServletRequest request, 
			String url,
			String attachmentName,
			String attachmentType,
			String attachmentDescription,
			MultipartFile content);

}