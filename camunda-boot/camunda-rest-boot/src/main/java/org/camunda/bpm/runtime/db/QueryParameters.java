package org.camunda.bpm.runtime.db;

import org.camunda.bpm.engine.impl.db.ListQueryParameterObject;

public class QueryParameters<T> extends ListQueryParameterObject {

  private static final long serialVersionUID = 1L;

  protected boolean historyEnabled = true;
  protected boolean maxResultsLimitEnabled = true;

  public QueryParameters() { }

  public QueryParameters(int firstResult, int maxResults) {
    this.firstResult = firstResult;
    this.maxResults = maxResults;
  }

  public boolean isHistoryEnabled() {
    return historyEnabled;
  }

  public void setHistoryEnabled(boolean historyEnabled) {
    this.historyEnabled = historyEnabled;
  }

  public boolean isMaxResultsLimitEnabled() {
    return maxResultsLimitEnabled;
  }

  public void disableMaxResultsLimit() {
    maxResultsLimitEnabled = false;
  }

}
