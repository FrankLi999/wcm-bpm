package org.camunda.bpm.engine.rest.sub.runtime;

import org.camunda.bpm.engine.rest.dto.runtime.VariableInstanceDto;
import org.springframework.http.ResponseEntity;

public interface VariableInstanceResource {
	  public VariableInstanceDto getResource(String id, boolean deserializeObjectValue);
	  public ResponseEntity<?> getResourceBinary(String id);
}
