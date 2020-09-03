package org.camunda.bpm.runtime.rest.dto.query;

import java.util.Map;
import org.camunda.bpm.runtime.rest.dto.CalledProcessInstanceDto;

public class CalledProcessInstanceQueryDto extends AbstractProcessInstanceQueryDto<CalledProcessInstanceDto> {

	private static final long serialVersionUID = 1L;

	public CalledProcessInstanceQueryDto() {
	}

	public CalledProcessInstanceQueryDto(Map<String, String[]> queryParameter) {
		super(queryParameter);
	}
}
