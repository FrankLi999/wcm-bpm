package com.bpwizard.myresources.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.myresources.ApplicationConstants;
import com.bpwizard.myresources.model.Image;
import com.bpwizard.myresources.service.application.ImageService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(ApplicationConstants.API_BASE_PATH + "/images")
public class ImageController {
	private static final Logger log = LoggerFactory.getLogger(ImageController.class);
	private static final String FILENAME = "{filename:.+}";
    
	@Autowired
	private ImageService imageService;
    
	@GetMapping()
	Flux<Image> images() {
		return Flux.just(
			new Image("1", "learning-spring-boot-cover.jpg"),
			new Image("2", "learning-spring-boot-2nd-edition-cover.jpg"),
			new Image("3", "bazinga.png")
		);
	}

//	@PostMapping()
//	Mono<Void> create(@RequestBody Flux<Image> images) {
//		return images
//			.map(image -> {
//				log.info("We will save " + image +
//					" to a Reactive database soon!");
//				return image;
//			})
//			.then();
//	}

	@GetMapping(value ="/" + FILENAME + "/raw",
			produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String filename) {
		return imageService.findOneImage(filename)
			.map(resource -> {
				try {
					return ResponseEntity.ok()
						.contentLength(resource.contentLength())
						.body(new InputStreamResource(
							resource.getInputStream()));
				} catch (IOException e) {
					return ResponseEntity.badRequest()
						.body("Couldn't find " + filename +
							" => " + e.getMessage());
				}
			});
	}
	
	@PostMapping()
	public Mono<Void> createFile(@RequestPart(name = "file") Flux<FilePart> files) {
		return imageService.createImage(files);
			//.then(Mono.just("redirect:/"));
	}

	@PreAuthorize("hasRole('ADMIN') or @imageRepository.findByName(#filename).owner == authentication.name") 
	@DeleteMapping("/" + FILENAME)
	public Mono<Void> deleteFile(@PathVariable String filename) {
		return imageService.deleteImage(filename);
			//.then(Mono.just("redirect:/"));
	}

}
