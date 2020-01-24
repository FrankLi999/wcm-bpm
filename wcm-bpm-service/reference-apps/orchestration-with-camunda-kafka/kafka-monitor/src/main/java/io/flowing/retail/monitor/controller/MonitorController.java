package io.flowing.retail.monitor.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.flowing.retail.monitor.domain.PastEvent;
import io.flowing.retail.monitor.port.persistence.LogRepository;

@RestController
public class MonitorController {

	@GetMapping(path = "/event")
	public Map<String, List<PastEvent>> getAllEvents() {
		return LogRepository.instance.getAllPastEvents();
	}

	@GetMapping(path = "/event/{traceId}")
	public List<PastEvent> getAllEvents(@PathVariable String traceId) {
		return LogRepository.instance.getAllPastEvents(traceId);
	}

}