package com.bpwizard.myresources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import com.bpwizard.myresources.model.Image;
import com.bpwizard.myresources.model.UserC;
import com.bpwizard.myresources.repository.UserCRepository;

@Component
public class InitDatabase {
	private static final Logger log = LoggerFactory.getLogger(InitDatabase.class);

	@Bean
	CommandLineRunner init(MongoOperations operations) {
		return args -> {
			// tag::log[]
			operations.dropCollection(Image.class);

			operations.insert(new Image("1",
				"learning-spring-boot-cover.jpg"));
			operations.insert(new Image("2",
				"learning-spring-boot-2nd-edition-cover.jpg"));
			operations.insert(new Image("3",
				"bazinga.png"));

			operations.findAll(Image.class).forEach(image -> {
				System.out.println(image.toString());
			});
			// end::log[]
		};
	}
	
//	@Bean
//	public CommandLineRunner demo(UserCRepository userRepository) {
//		return (args) -> {
//			// save a couple of customers
//			userRepository.save(new UserC("Jack", "Bauer"));
//			userRepository.save(new UserC("Chloe", "O'Brian"));
//			userRepository.save(new UserC("Kim", "Bauer"));
//			userRepository.save(new UserC("David", "Palmer"));
//			userRepository.save(new UserC("Michelle", "Dessler"));
//		};
//	}
}