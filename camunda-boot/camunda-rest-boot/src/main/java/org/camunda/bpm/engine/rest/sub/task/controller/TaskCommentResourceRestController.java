package org.camunda.bpm.engine.rest.sub.task.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.rest.TaskRestService;
import org.camunda.bpm.engine.rest.dto.task.CommentDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.sub.task.TaskCommentResource;
import org.camunda.bpm.engine.task.Comment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(TaskRestService.PATH + "/{taskId}/comment")
public class TaskCommentResourceRestController extends AbstractTaskResourceRestController implements TaskCommentResource {

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<CommentDto> getComments(@PathVariable("taskId") String taskId) {
		if (!isHistoryEnabled()) {
			return Collections.emptyList();
		}

		ensureTaskExists(taskId, HttpStatus.NOT_FOUND);

		List<Comment> taskComments = engine.getTaskService().getTaskComments(taskId);

		List<CommentDto> comments = new ArrayList<CommentDto>();
		for (Comment comment : taskComments) {
			comments.add(CommentDto.fromComment(comment));
		}

		return comments;
	}

	@GetMapping(path="/{commentId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public CommentDto getComment(@PathVariable("taskId") String taskId, @PathVariable("commentId") String commentId) {
		ensureHistoryEnabled(HttpStatus.NOT_FOUND);

		Comment comment = engine.getTaskService().getTaskComment(taskId, commentId);
		if (comment == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND,
					"Task comment with id " + commentId + " does not exist for task id '" + taskId + "'.");
		}

		return CommentDto.fromComment(comment);
	}

	@PostMapping(path="/create", produces=MediaType.APPLICATION_JSON_VALUE)
	public CommentDto createComment(@PathVariable("taskId") String taskId, @RequestBody CommentDto commentDto) {
		ensureHistoryEnabled(HttpStatus.FORBIDDEN);
		ensureTaskExists(taskId, HttpStatus.BAD_REQUEST);

		Comment comment;

		try {
			comment = engine.getTaskService().createComment(taskId, null, commentDto.getMessage());
		} catch (ProcessEngineException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e, "Not enough parameters submitted");
		}

		URI uri = UriComponentsBuilder.fromPath(rootResourcePath).path(TaskRestService.PATH)
				.path(taskId + "/comment/" + comment.getId()).build().toUri();

		CommentDto resultDto = CommentDto.fromComment(comment);

		// GET /
		resultDto.addReflexiveLink(uri, HttpMethod.GET.name(), "self");

		return resultDto;
	}
}
