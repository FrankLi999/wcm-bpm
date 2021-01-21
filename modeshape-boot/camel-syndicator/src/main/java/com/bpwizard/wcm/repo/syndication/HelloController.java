package com.bpwizard.wcm.repo.syndication;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(HelloController.BASE_URI)
public class HelloController {
	public static final String BASE_URI = "/hello";

	@GetMapping(path = "/world", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Syndication> hello() {
		Syndication syndication = new Syndication();
		syndication.setName("zzzzzzzzzzzz");
		return ResponseEntity.ok(syndication);
	}
}
