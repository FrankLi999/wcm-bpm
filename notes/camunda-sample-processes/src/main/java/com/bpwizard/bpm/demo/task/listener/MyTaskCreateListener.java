package com.bpwizard.bpm.demo.task.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

public class MyTaskCreateListener implements TaskListener {

	public void notify(DelegateTask delegateTask) {
		// Custom logic goes here
	}

}