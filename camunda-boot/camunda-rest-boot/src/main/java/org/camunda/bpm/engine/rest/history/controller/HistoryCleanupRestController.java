/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.rest.history.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.jobexecutor.historycleanup.BatchWindow;
import org.camunda.bpm.engine.impl.util.ClockUtil;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.history.HistoryCleanupConfigurationDto;
import org.camunda.bpm.engine.rest.dto.runtime.JobDto;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.history.HistoryCleanupRestService;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.runtime.Job;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController(value="HistoryCleanupApi")
@RequestMapping(HistoryRestService.PATH + HistoryCleanupRestService.PATH)
public class HistoryCleanupRestController extends AbstractRestProcessEngineAware implements HistoryCleanupRestService {
	@PostMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public JobDto cleanupAsync(@RequestParam(name="immediatelyDue", defaultValue="true") boolean immediatelyDue) {
		Job job = processEngine.getHistoryService().cleanUpHistoryAsync(immediatelyDue);
		return JobDto.fromJob(job);
	}

//	@GetMapping(path="/job", produces=MediaType.APPLICATION_JSON_VALUE)
//	public JobDto findCleanupJob() {
//		Job job = processEngine.getHistoryService().findHistoryCleanupJob();
//		if (job == null) {
//			throw new RestException(HttpStatus.NOT_FOUND, "History cleanup job does not exist");
//		}
//		return JobDto.fromJob(job);
//	}

	@GetMapping(path="/jobs", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<JobDto> findCleanupJobs() {
		List<Job> jobs = processEngine.getHistoryService().findHistoryCleanupJobs();
		if (jobs == null || jobs.isEmpty()) {
			throw new RestException(HttpStatus.NOT_FOUND, "History cleanup jobs are empty");
		}
		List<JobDto> dtos = new ArrayList<JobDto>();
		for (Job job : jobs) {
			JobDto dto = JobDto.fromJob(job);
			dtos.add(dto);
		}
		return dtos;
	}

	@GetMapping(path="/configuration", produces=MediaType.APPLICATION_JSON_VALUE)
	public HistoryCleanupConfigurationDto getHistoryCleanupConfiguration() {
		HistoryCleanupConfigurationDto configurationDto = new HistoryCleanupConfigurationDto();
		final ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) processEngine
				.getProcessEngineConfiguration();
		Date now = ClockUtil.getCurrentTime();
		final BatchWindow batchWindow = processEngineConfiguration.getBatchWindowManager()
				.getCurrentOrNextBatchWindow(now, processEngineConfiguration);
		if (batchWindow == null) {
			return configurationDto;
		}
		configurationDto.setBatchWindowStartTime(batchWindow.getStart());
		configurationDto.setBatchWindowEndTime(batchWindow.getEnd());
		return configurationDto;
	}
}
