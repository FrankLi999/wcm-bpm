package com.bpwizard.bpm.content.helper;

import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.ProcessCoverageInMemProcessEngineConfiguration;

/**
 * Default in memory configuration, pre-configured with mock, dbSchema and
 * metrics.
 */
public class StandaloneInMemoryTestConfiguration extends ProcessCoverageInMemProcessEngineConfiguration {
	public StandaloneInMemoryTestConfiguration() {
		// super();
		this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_DROP_CREATE;
		this.jdbcUrl = "jdbc:h2:mem:camundatest;DB_CLOSE_ON_EXIT=FALSE";
		// jobExecutorActivate = false;
		// expressionManager = new MockExpressionManager();
		//isDbMetricsReporterActivate = false;
		historyLevel = HistoryLevel.HISTORY_LEVEL_FULL;
		
		
	}
}