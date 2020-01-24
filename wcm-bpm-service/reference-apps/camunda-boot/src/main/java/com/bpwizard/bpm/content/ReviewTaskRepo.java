package com.bpwizard.bpm.content;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bpwizard.bpm.content.model.ReviewTask;

@Component
public class ReviewTaskRepo {
	private Map<String, ReviewTask> tasks = new HashMap<>();
	public ReviewTask[] getReviewTasksByTopic(String topic) {
		return tasks.entrySet().stream().map(taskEntry -> taskEntry.getValue()).filter(task -> topic.equals(task.getTopic())).toArray(ReviewTask[]::new);
	}
	
	public void registerReviewTask(
			String activityId, 
			String topic, 
			String repository, 
			String workspace, 
			String contentPath, 
			String contentId) {
		
		ReviewTask reviewTask = new ReviewTask();
		
		reviewTask.setRepository(repository);
		reviewTask.setActivityId(activityId);
		reviewTask.setTopic(topic);
		reviewTask.setWorkspace(workspace);
		reviewTask.setContentPath(contentPath);
		reviewTask.setContentId(contentId);
		this.tasks.put(activityId, reviewTask);
	}
	
	public ReviewTask findReviewTask(String activityId) {
	    return this.tasks.get(activityId);
	}
	
	public void removeReviewTask(String activityId) {
		this.tasks.remove(activityId);
	}
}
