package com.bpwizard.bpm.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.client.spring.ExternalTaskSubscription;
import org.camunda.bpm.client.spring.boot.starter.CamundaBpmClientProperties;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskHandlerConfiguration {

	private final String workerId;

	public TaskHandlerConfiguration(CamundaBpmClientProperties properties) {
		workerId = properties.getWorkerId();
	}

	@ExternalTaskSubscription(topicName = "creditScoreChecker")
	@Bean
	public ExternalTaskHandler creditScoreChecker() {
		return (externalTask, externalTaskService) -> {

			// retrieve a variable from the Workflow Engine
			System.out.println(">>>>>>>>>>>>>> creditScoreChecker >>>>> externalTask : " + externalTask);
			System.out.println(">>>>>>>>>>>>>> creditScoreChecker >>>>> externalTask : " + externalTask.getId());
			int defaultScore = externalTask.getVariable("defaultScore");
			System.out.println(">>>>>>>>>>>>>> creditScoreChecker >>>>> defaultScore : " + defaultScore);
			List<Integer> creditScores = new ArrayList<>(Arrays.asList(defaultScore, 9, 1, 4, 10));

			// create an object typed variable
			ObjectValue creditScoresObject = Variables.objectValue(creditScores).create();

			// complete the external task
			externalTaskService.complete(externalTask, Variables.putValueTyped("creditScores", creditScoresObject));
			System.out.println(String.format("%s: The External Task %s has been checked!", workerId, externalTask.getId()));
		};
	}

	@ExternalTaskSubscription(topicName = "loanGranter")
	@Bean
	public ExternalTaskHandler loanGranter() {
		return (externalTask, externalTaskService) -> {
			System.out.println(">>>>>>>>>>>>>> loanGranter >>>>> externalTask : " + externalTask.getId());
			int score = externalTask.getVariable("score");
			System.out.println(">>>>>>>>>>>>>> loanGranter >>>>> score : " + score);
			externalTaskService.complete(externalTask);
			System.out.println(String.format("%s: The External Task %s has been granted with score %s!", workerId, externalTask.getId(), score));
		};
	}

	@ExternalTaskSubscription(topicName = "requestRejecter")
	@Bean
	public ExternalTaskHandler requestRejecter() {
		return (externalTask, externalTaskService) -> {
			System.out.println(">>>>>>>>>>>>>> requestRejecter >>>>> externalTask : " + externalTask.getId());
			int score = externalTask.getVariable("score");
			System.out.println(">>>>>>>>>>>>>> requestRejecter >>>>> score : " + score);
			externalTaskService.complete(externalTask);
			System.out.println(String.format("%s: The External Task %s has been rejected with score %s!", workerId, externalTask.getId(), score));
		};
	}
}
