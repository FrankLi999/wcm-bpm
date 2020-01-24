package org.camunda.bpm.engine.rest.sub.history;

import org.springframework.http.ResponseEntity;

public interface HistoricVariableInstanceResource {
	  ResponseEntity<?> getResourceBinary(String id);
	  ResponseEntity<?> deleteVariableInstance(String id);
}
