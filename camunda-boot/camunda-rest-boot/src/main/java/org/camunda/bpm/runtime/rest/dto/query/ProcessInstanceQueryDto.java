package org.camunda.bpm.runtime.rest.dto.query;

import java.util.Map;

import org.camunda.bpm.runtime.rest.dto.ProcessInstanceDto;

public class ProcessInstanceQueryDto extends AbstractProcessInstanceQueryDto<ProcessInstanceDto> {

	  private static final long serialVersionUID = 1L;

	  public ProcessInstanceQueryDto() {
	  }

	  public ProcessInstanceQueryDto(Map<String, String[]> queryParameter) {
	    super(queryParameter);
	  }
	}
