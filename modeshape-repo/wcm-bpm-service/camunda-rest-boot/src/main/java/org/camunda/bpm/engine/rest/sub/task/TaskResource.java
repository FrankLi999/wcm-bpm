package org.camunda.bpm.engine.rest.sub.task;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.task.CompleteTaskDto;
import org.camunda.bpm.engine.rest.dto.task.FormDto;
import org.camunda.bpm.engine.rest.dto.task.IdentityLinkDto;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.camunda.bpm.engine.rest.dto.task.UserIdDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface TaskResource {

  Object getTask(String taskId, HttpServletRequest request);

  FormDto getForm(String taskId);

  ResponseEntity<?> submit(String taskId, CompleteTaskDto dto);

  ResponseEntity<Resource> getRenderedForm(String taskId);

  ResponseEntity<Resource> getDeployedForm(String taskId);

  void claim(String taskId, UserIdDto dto);

  void unclaim(String taskId);

  ResponseEntity<?> complete(String taskId, CompleteTaskDto dto);

  void resolve(String taskId, CompleteTaskDto dto);

  void delegate(String taskId, UserIdDto delegatedUser);

  void setAssignee(String taskId, UserIdDto dto);

  List<IdentityLinkDto> getIdentityLinks(String taskId, String type);

  void addIdentityLink(String taskId, IdentityLinkDto identityLink);

  void deleteIdentityLink(String taskId, IdentityLinkDto identityLink);

  Map<String, VariableValueDto> getFormVariables(
	  String taskId,
	  String variableNames,
      boolean deserializeValues);

  public void updateTask(String taskId, TaskDto task);
  void deleteTask(String taskId);
}
