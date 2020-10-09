package com.bpwizard.wcm.repo.config;

//import static springfox.documentation.builders.PathSelectors.ant;
import static com.google.common.collect.Lists.newArrayList;

import java.time.LocalDate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig{

    ApiInfo apiInfo() {
      return new ApiInfoBuilder()
        .title("API Reference")
        .version("1.0.0")
        .build();
    }

    @Bean
    public Docket customImplementation(){
		return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(apiInfo())
      .securitySchemes(newArrayList(apiKey()))
      .select().paths(PathSelectors.any())
          //.apis(RequestHandlerSelectors.any())  // If you want to list all the apis including springboots own
          .apis(RequestHandlerSelectors.basePackage("com.bpwizard.wcm.repo.controllers"))
          .build()
      .pathMapping("/")
      .useDefaultResponseMessages(false)
      .directModelSubstitute(LocalDate.class, String.class)
      .genericModelSubstitutes(ResponseEntity.class)
      ;
    }

    private ApiKey apiKey() {
        //return new ApiKey("Authorization", "api_key", "header");
        return new ApiKey("Authorization", "", "header");             // <<< === Create a Heaader (We are createing header named "Authorization" here)
    }

    @SuppressWarnings("deprecation")
	@Bean
    SecurityConfiguration security() {
        //return new SecurityConfiguration("emailSecurity_client", "secret", "Spring", "emailSecurity", "apiKey", ApiKeyVehicle.HEADER, "api_key", ",");
        return new SecurityConfiguration("emailSecurity_client", "secret", "Spring", "emailSecurity", "", ApiKeyVehicle.HEADER, "", ",");
    }

    // This path will be called when swagger is loaded first time to get a token
    /*
    @Bean
    public UiConfiguration uiConfig() {
        return new UiConfiguration("session");
    }
    */

}
