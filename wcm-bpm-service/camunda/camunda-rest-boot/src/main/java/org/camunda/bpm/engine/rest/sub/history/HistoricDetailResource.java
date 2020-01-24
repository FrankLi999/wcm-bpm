package org.camunda.bpm.engine.rest.sub.history;

import org.camunda.bpm.engine.rest.dto.history.HistoricDetailDto;
import org.springframework.http.ResponseEntity;

public interface HistoricDetailResource {
	  HistoricDetailDto getResource(String id, boolean deserializeValue);
	  public ResponseEntity<?> getResourceBinary(String id);
}
