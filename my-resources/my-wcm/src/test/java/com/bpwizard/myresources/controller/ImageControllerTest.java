package com.bpwizard.myresources.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.bpwizard.myresources.ApplicationConstants;
import com.bpwizard.myresources.MyResourcesApplication;
import com.bpwizard.myresources.service.application.ImageService;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)     
// @WebFluxTest()
// @DataMongoTest
//@WebFluxTest(controllers = ImageController.class)
@SpringBootTest(
		classes = MyResourcesApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ImageControllerTest {
	@Autowired 
    WebTestClient webClient; 
    
	@MockBean 
    ImageService imageService; 

    
    @Test 
    public void fetchingImageShouldWork() { 
     given(imageService.findOneImage(any())) 
      .willReturn(Mono.just( 
         new ByteArrayResource("data".getBytes()))); 
 
      webClient 
        .get().uri(ApplicationConstants.API_BASE_PATH + "/images/alpha.png/raw") 
        .exchange() 
        .expectStatus().isOk() 
        .expectBody(String.class).isEqualTo("data"); 
      verify(imageService).findOneImage("alpha.png"); 
      // verifyNoMoreInteractions(imageService); 
    } 
    
    @Test
	public void fetchingNullImageShouldFail() throws IOException {
		Resource resource = mock(Resource.class);
		given(resource.getInputStream())
			.willThrow(new IOException("Bad file"));
		given(imageService.findOneImage(any()))
			.willReturn(Mono.just(resource));

		webClient
			.get().uri(ApplicationConstants.API_BASE_PATH + "/images/alpha.png/raw")
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(String.class)
				.isEqualTo("Couldn't find alpha.png => Bad file");

		verify(imageService).findOneImage("alpha.png");
		verifyNoMoreInteractions(imageService);
	}
}
