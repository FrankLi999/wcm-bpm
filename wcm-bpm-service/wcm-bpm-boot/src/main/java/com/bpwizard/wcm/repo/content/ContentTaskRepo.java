package com.bpwizard.wcm.repo.content;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.content.model.ContentTask;

@Component
public class ContentTaskRepo {
	private Map<String, ContentTask> tasks = new HashMap<>();
	public ContentTask[] getActiveTasksByTopic(String topic) {
		return tasks.entrySet().stream().map(taskEntry -> taskEntry.getValue()).filter(task -> topic.equals(task.getTopic())).toArray(ContentTask[]::new);
	}
	
	public void registerContentTask(
			String activityId, 
			String topic, 
			String repository, 
			String workspace, 
			String contentPath, 
			String contentId) {
		
		ContentTask reviewTask = new ContentTask();
		
		reviewTask.setRepository(repository);
		reviewTask.setActivityId(activityId);
		reviewTask.setTopic(topic);
		reviewTask.setWorkspace(workspace);
		reviewTask.setContentPath(contentPath);
		reviewTask.setContentId(contentId);
		this.tasks.put(activityId, reviewTask);
	}
	
	public ContentTask findContentTask(String activityId) {
	    return this.tasks.get(activityId);
	}
	
	public void removeContentTask(String activityId) {
		this.tasks.remove(activityId);
	}
}
