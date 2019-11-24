package com.bpwizard.wcm;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value="/ssl")
public class SslTestController {
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	Environment env;
	
	@RequestMapping(value = "/data", method = RequestMethod.GET)
	public String getData() {
		System.out.println("Returning data from nt-gateway own data method");
		return "Hello from NT-GATEWAY-data method";
	}
	
	@RequestMapping(value = "/ssl-data", method = RequestMethod.GET)
	public String getMsData() {
		System.out.println("Got inside NT-GATEWAY-ms-data method");
		try {
			String msEndpoint = env.getProperty("endpoint.ms-service");
			System.out.println("MS Endpoint name : [" + msEndpoint + "]");
			
			return restTemplate.getForObject(new URI(msEndpoint), String.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "Exception occurred.. so, returning default data";
	}
	
	@Value("${ui.config:Hello ui-config}")
	private String uiConfig;

	@RequestMapping("/ui-config")
	String getUiConfig() {
		return this.uiConfig;
	}
}