package org.camunda.bpm.engine.rest.sub.task;

import java.util.List;

import org.camunda.bpm.engine.rest.dto.task.CommentDto;

public interface TaskCommentResource {

	List<CommentDto> getComments(String taskId);

	CommentDto getComment(String taskId, String commentId);

	CommentDto createComment(String taskId, CommentDto commentDto);
}
