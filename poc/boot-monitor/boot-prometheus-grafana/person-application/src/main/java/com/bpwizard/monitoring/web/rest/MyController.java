package com.bpwizard.monitoring.web.rest;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

	@Autowired
	MeterRegistry registry;

	@RequestMapping(value = "/message/{message}", method = RequestMethod.GET)
	public String getMessage(@PathVariable("message") String message) {

		// counter to count different types of messages received
		registry.counter("custom.metrics.message", "value", message).increment();

		return message;
	}

}