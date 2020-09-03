package org.camunda.bpm.runtime.db;

import org.camunda.bpm.engine.impl.interceptor.Command;

public interface CommandExecutor {

  /**
   * Execute the given command and return the result
   * of the execution
   *
   * @param <T>
   * @param command
   *
   * @return
   */
  public <T> T executeCommand(Command<T> command);
}
