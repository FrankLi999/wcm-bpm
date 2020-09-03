package org.camunda.bpm.runtime.db;

import java.util.List;

//import org.camunda.bpm.cockpit.db.CommandExecutor;
//import org.camunda.bpm.cockpit.db.QueryParameters;
//import org.camunda.bpm.cockpit.db.QueryService;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.db.AuthorizationCheck;
import org.camunda.bpm.engine.impl.db.ListQueryParameterObject;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.util.QueryMaxResultsLimitUtil;

public class QueryServiceImpl implements QueryService {

	private CommandExecutor commandExecutor;

	public QueryServiceImpl(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> executeQuery(final String statement, final QueryParameters<T> parameter) {
		return commandExecutor.executeCommand(commandContext -> {
			ProcessEngineConfigurationImpl engineConfig = getProcessEngineConfiguration(commandContext);

			configureAuthCheck(parameter, engineConfig, commandContext);

			if (parameter.isMaxResultsLimitEnabled()) {
				QueryMaxResultsLimitUtil.checkMaxResultsLimit(parameter.getMaxResults(), engineConfig);
			}

			return (List<T>) commandContext.getDbSqlSession().selectList(statement, parameter);
		});
	}

	@SuppressWarnings("unchecked")
	public <T> T executeQuery(final String statement, final Object parameter, final Class<T> clazz) {
		return commandExecutor
				.executeCommand(commandContext -> (T) commandContext.getDbSqlSession().selectOne(statement, parameter));
	}

	public Long executeQueryRowCount(final String statement, final ListQueryParameterObject parameter) {
		return commandExecutor.executeCommand(commandContext -> {
			ProcessEngineConfigurationImpl engineConfig = getProcessEngineConfiguration(commandContext);

			configureAuthCheck(parameter, engineConfig, commandContext);

			return (Long) commandContext.getDbSqlSession().selectOne(statement, parameter);
		});
	}

	protected ProcessEngineConfigurationImpl getProcessEngineConfiguration(CommandContext commandContext) {
		QuerySessionFactory querySessionFactory = (QuerySessionFactory) commandContext.getProcessEngineConfiguration();

		ProcessEngineConfigurationImpl processEngineConfiguration = null;
		if (querySessionFactory != null) {
			processEngineConfiguration = querySessionFactory.getWrappedConfiguration();
		}

		if (processEngineConfiguration == null) {
			throw new ProcessEngineException("Process Engine Configuration missing!");
		}

		return processEngineConfiguration;
	}

	protected <T> void configureAuthCheck(ListQueryParameterObject parameter,
			ProcessEngineConfigurationImpl engineConfig, CommandContext commandContext) {
		AuthorizationCheck authCheck = parameter.getAuthCheck();

		commandContext.getAuthorizationManager().enableQueryAuthCheck(authCheck);

		boolean isEnableHistoricInstancePermissions = engineConfig.isEnableHistoricInstancePermissions();
		authCheck.setHistoricInstancePermissionsEnabled(isEnableHistoricInstancePermissions);
	}

}
