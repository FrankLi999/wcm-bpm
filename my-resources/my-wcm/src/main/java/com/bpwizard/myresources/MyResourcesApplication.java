package com.bpwizard.myresources;

//import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
// import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
// import springfox.documentation.swagger2.annotations.EnableSwagger2;
// import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.cloud.openfeign.EnableFeignClients;
// @SpringBootApplication
@SpringCloudApplication
@EnableReactiveMongoRepositories
// @EnableSwagger2
// @EnableDiscoveryClient
// @EnableFeignClients
public class MyResourcesApplication {

	public static void main(String[] args) {
//		final SpringApplication application = new SpringApplication(MyResourcesApplication.class);
//        application.setBannerMode(Banner.Mode.OFF);
//        application.setWebApplicationType(WebApplicationType.SERVLET);
//        application.run(args);
        
		SpringApplication.run(MyResourcesApplication.class, args);
	}
	
//	@Bean
//	public Docket swaggerPersonApi10() {
//		return new Docket(DocumentationType.SWAGGER_2)
//				.select()
//					.apis(RequestHandlerSelectors.basePackage("com.bpwizard.myresources.api"))
//					.paths(PathSelectors.any())
//				.build()
//				.apiInfo(new ApiInfoBuilder().version("1.0").title("My Resources API").description("My Resources API v1.0").build());
//	}
	
//	@Bean
//	HiddenHttpMethodFilter hiddenHttpMethodFilter() {
//		return new HiddenHttpMethodFilter();
//	}
}
