package com.bpwizard.myresources.config;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "myservice")
// @PropertySource("classpath:my-service.properties")
@Validated
public class MyServiceConfiguration {

	private Mail mail;
    private String helloText;
    private String message;
    @Validated
    @Data
    public static class Mail {
    	@NotBlank
        private String host;

        @Min(1025)
        @Max(65536)
        private int port;
        
        @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$")
        private String from;
        
        private Credentials credentials;
        private List<String> defaultRecipients;
        private Map<String, String> additionalHeaders;
    }
    @Validated
    @Data
    public static class Credentials {

        @Length(max = 4, min = 1)
        private String authMethod;
        private String username;
        private String password;
    }
}
