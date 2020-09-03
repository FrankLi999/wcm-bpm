package org.camunda.bpm.runtime.db;

import java.util.List;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.interceptor.Command;


public class CommandExecutorImpl implements CommandExecutor {

	  private QuerySessionFactory sessionFactory;

	  public CommandExecutorImpl() { }

	  public CommandExecutorImpl(ProcessEngineConfigurationImpl processEngineConfiguration, List<String> mappingFiles) {
	    sessionFactory = new QuerySessionFactory();
	    sessionFactory.initFromProcessEngineConfiguration(processEngineConfiguration, mappingFiles);
	  }

	  @Override
	  public <T> T executeCommand(Command<T> command) {
	    return sessionFactory.getCommandExecutorTxRequired().execute(command);
	  }
	}

