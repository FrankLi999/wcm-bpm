package com.bpwizard.myresources.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
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
import com.bpwizard.myresources.model.UserC;

import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = MyResourcesApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class UserControllerTest {

	@LocalServerPort
	private int port;

	@Before
    public void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

	@Test
    public void addNewUserAndRetrieveItBack() {
        // User norbertSiegmund = new User("Norbert", "Siegmund");

        Long userId =
            given()
                .queryParam("firstName", "Norbert")
                .queryParam("lastName", "Siegmund")
            .when()
                .post(ApplicationConstants.API_BASE_PATH + "/user-c")
            .then()
                .statusCode(is(HttpStatus.SC_CREATED))
                .extract()
                    .body().as(Long.class);

	    UserC responseUser =
            given()
                    .pathParam("id", userId)
                .when()
                    .get("/api/user-c/{id}")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .assertThat()
                        .extract().as(UserC.class);

	    // Did Norbert came back?
        assertThat(responseUser.getFirstName(), is("Norbert"));
        assertThat(responseUser.getLastName(), is("Siegmund"));
    }

}
