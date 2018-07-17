package com.bpwizard.myresources.controller;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.bpwizard.myresources.ApplicationConstants;
import com.bpwizard.myresources.MyResourcesApplication;

import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = MyResourcesApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class HelloControllerTest {

	@LocalServerPort
	private int port;

	@Before
    public void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

	@Test
	public void saysHello() {
		when()
			.get(ApplicationConstants.API_BASE_PATH + "/hello")
		.then()
			.statusCode(HttpStatus.SC_OK)
			.assertThat()
				.body(is(equalTo(HelloController.HELLO_TEXT)));
	}
}
