package com.bpwizard.myresources.controller.websocket;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Greg Turnquist
 */
//@Controller
public class ChatHomeController {

	// tag::code[]
	@GetMapping("/chat")
	public String index(@AuthenticationPrincipal Authentication auth, Model model) {
		model.addAttribute("authentication", auth);
		return "chat-index";
	}
	// end::code[]

}
