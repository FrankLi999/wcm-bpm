package com.bpwizard.bpm.config;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import com.bpwizard.spring.boot.commons.SpringProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private SpringProperties springProperties;

    ApiInfo apiInfo() {
      return new ApiInfoBuilder()
        .title(springProperties.getSwagger().getAppInfo().getTitle())
        .version(springProperties.getSwagger().getAppInfo().getVersion())
        .build();
    }

    @Bean
    public Docket customImplementation(){
        List<ApiKey> apikeys = new ArrayList<>();
        apikeys.add(apiKey());
	    ApiSelectorBuilder apiBuilder = new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .securitySchemes(apikeys)
            .select().paths(PathSelectors.any());
            
            if (ObjectUtils.isEmpty(springProperties.getSwagger().getBasePackages())) {
                for (String basepackage: springProperties.getSwagger().getBasePackages()) {
                    apiBuilder = apiBuilder.apis(RequestHandlerSelectors.basePackage(basepackage));
                }
            }
        
        Docket docket =   apiBuilder.build()
            .pathMapping(springProperties.getSwagger().getPathMapping())
            .useDefaultResponseMessages(false)
            .directModelSubstitute(LocalDate.class, String.class)
            .genericModelSubstitutes(ResponseEntity.class);
        return docket;
    }

    private ApiKey apiKey() {
        //return new ApiKey("Authorization", "api_key", "header");
        return new ApiKey(
            springProperties.getSwagger().getApiKey().getName(), 
            springProperties.getSwagger().getApiKey().getKeyname(), 
            springProperties.getSwagger().getApiKey().getPassAs());             // <<< === Create a Heaader (We are createing header named "Authorization" here)
    }

    @SuppressWarnings("deprecation")
	@Bean
    SecurityConfiguration security() {
        //return new SecurityConfiguration("emailSecurity_client", "secret", "Spring", "emailSecurity", "apiKey", ApiKeyVehicle.HEADER, "api_key", ",");
        return new SecurityConfiguration("emailSecurity_client", "secret", "Spring", "emailSecurity", "", ApiKeyVehicle.HEADER, "", ",");
    }
}
