package org.camunda.bpm.engine.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.rest.dto.ProcessEngineDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "namedProcessEngineApi")
@RequestMapping(NamedProcessEngineRestController.PATH)
public class NamedProcessEngineRestController extends AbstractRestProcessEngineAware {
	public static final String PATH = "/camunda/api/engine/engine";

	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProcessEngineDto> getProcessEngineNames() {
		// ProcessEngineProvider provider = getProcessEngineProvider();
		String engineName = this.processEngine.getName();

		List<ProcessEngineDto> results = new ArrayList<ProcessEngineDto>();
		//for (String engineName : engineNames) {
			ProcessEngineDto dto = new ProcessEngineDto();
			dto.setName(engineName);
			results.add(dto);
		//}

		return results;
	}
}
