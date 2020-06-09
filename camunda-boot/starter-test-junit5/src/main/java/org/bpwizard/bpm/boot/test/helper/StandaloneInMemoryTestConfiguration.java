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
package org.bpwizard.bpm.boot.test.helper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.impl.persistence.StrongUuidGenerator;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.ProcessCoverageInMemProcessEngineConfiguration;

/**
 * Default in memory configuration, pre-configured with mock, dbSchema and
 * metrics.
 */
public class StandaloneInMemoryTestConfiguration extends ProcessCoverageInMemProcessEngineConfiguration {

	@SuppressWarnings("unchecked")
	public StandaloneInMemoryTestConfiguration(ProcessEnginePlugin... plugins) {
		this(Optional.ofNullable(plugins).map(Arrays::asList).orElse(Collections.EMPTY_LIST));
	}

	public StandaloneInMemoryTestConfiguration(List<ProcessEnginePlugin> plugins) {
		this.jdbcUrl = "jdbc:h2:mem:camundatest;DB_CLOSE_ON_EXIT=FALSE";
		this.idGenerator = new StrongUuidGenerator();

		this.jobExecutorActivate = false;
		// this.expressionManager = new MockExpressionManager();
		this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_DROP_CREATE;
		this.isDbMetricsReporterActivate = false;
		this.historyLevel = HistoryLevel.HISTORY_LEVEL_FULL;

		this.getProcessEnginePlugins().addAll(plugins);
	}

}
