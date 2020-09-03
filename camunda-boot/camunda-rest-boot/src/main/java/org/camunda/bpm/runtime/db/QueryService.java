package org.camunda.bpm.runtime.db;

import java.util.List;

import org.camunda.bpm.engine.impl.db.ListQueryParameterObject;

public interface QueryService {

  public <T> List<T> executeQuery(final String statement, final QueryParameters<T> parameter);

  @Deprecated
  public <T> T executeQuery(final String statement, final Object parameter, final Class<T> clazz);

  public Long executeQueryRowCount(final String statement, final ListQueryParameterObject parameter);
}
