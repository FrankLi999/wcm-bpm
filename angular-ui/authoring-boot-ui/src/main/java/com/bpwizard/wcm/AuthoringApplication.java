package com.bpwizard.wcm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class AuthoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthoringApplication.class, args);
	}

	@RefreshScope
	@RestController
	class MessageRestController {

		@Value("${ui.config:Hello ui-config}")
		private String message;

		@RequestMapping("/message")
		String getMessage() {
			return this.message;
		}
	}
}
