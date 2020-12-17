package org.bpwizard.bpm.boot.test.helper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.impl.persistence.StrongUuidGenerator;
import org.camunda.bpm.engine.test.mock.MockExpressionManager;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.ProcessCoverageInMemProcessEngineConfiguration;

/**
 * Default in memory configuration, pre-configured with mock, dbSchema and
 * metrics.
 */
public class StandaloneInMemoryTestConfigurationWithMock extends ProcessCoverageInMemProcessEngineConfiguration {
	@SuppressWarnings("unchecked")
	public StandaloneInMemoryTestConfigurationWithMock(ProcessEnginePlugin... plugins) {
		this(Optional.ofNullable(plugins).map(Arrays::asList).orElse(Collections.EMPTY_LIST));
	}

	public StandaloneInMemoryTestConfigurationWithMock(List<ProcessEnginePlugin> plugins) {
		this.jdbcUrl = "jdbc:h2:mem:camundatest;DB_CLOSE_ON_EXIT=FALSE";
		this.idGenerator = new StrongUuidGenerator();

		this.jobExecutorActivate = false;
		this.expressionManager = new MockExpressionManager();
		this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_DROP_CREATE;
		this.isDbMetricsReporterActivate = false;
		this.historyLevel = HistoryLevel.HISTORY_LEVEL_FULL;

		this.getProcessEnginePlugins().addAll(plugins);
	}

}
