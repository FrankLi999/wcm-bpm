package com.bpwizard.spring.boot.commons.demo;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;

import com.bpwizard.spring.boot.commons.util.SecurityUtils;

@Sql({"/test-data/initialize.sql", "/test-data/finalize.sql", })
public class BasicMvcTests extends AbstractMvcTests {
	
	@Test
	public void testPing() throws Exception {
		
		mvc.perform(get("/api/core/ping"))
				.andExpect(status().is(204));
	}
	
	@Test
	public void testGetContextLoggedIn() throws Exception {
		
		mvc.perform(get("/api/core/context")
				.header(HttpHeaders.AUTHORIZATION, tokens.get(ADMIN_ID)))
				.andExpect(status().is(200))
				.andExpect(header().string(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME, containsString(".")))
				.andExpect(jsonPath("$.context.reCaptchaSiteKey").isString())
				.andExpect(jsonPath("$.user.id").value(ADMIN_ID))
				.andExpect(jsonPath("$.user.roles[0]").value("ADMIN"))
				.andExpect(jsonPath("$.user.password").doesNotExist());
	}
	
	@Test
	public void testGetContextWithoutLoggedIn() throws Exception {
		
		mvc.perform(get("/api/core/context"))
				.andExpect(status().is(200))
				.andExpect(header().doesNotExist(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME))
				.andExpect(jsonPath("$.context.reCaptchaSiteKey").isString())
				.andExpect(jsonPath("$.user").doesNotExist());
	}	
}
