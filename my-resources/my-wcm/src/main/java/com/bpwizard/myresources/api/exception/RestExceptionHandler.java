package com.bpwizard.myresources.api.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// see: https://stackoverflow.com/questions/47631243/spring-5-reactive-webexceptionhandler-is-not-getting-called
// and https://docs.spring.io/spring-boot/docs/2.0.0.M7/reference/html/boot-features-developing-web-applications.html#boot-features-webflux-error-handling
// and https://stackoverflow.com/questions/48047645/how-to-write-messages-to-http-body-in-spring-webflux-webexceptionhandlder/48057896#48057896
@Component
@Order(-2)
@Slf4j
//@RestControllerAdvice
public class RestExceptionHandler implements WebExceptionHandler{

	private ObjectMapper objectMapper;

    public RestExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof WebExchangeBindException) {
            WebExchangeBindException webExchangeBindException = (WebExchangeBindException) ex;
            List<FieldErrorResource> errorResources = webExchangeBindException.getFieldErrors().stream().map(
            		fieldError -> new FieldErrorResource(
		                fieldError.getObjectName(),
		                fieldError.getField(),
		                fieldError.getCode(),
		                fieldError.getDefaultMessage())).collect(Collectors.toList());
            
            ErrorResource errors = new ErrorResource("validation_failure", "Validation failed.", errorResources);
            try {
                exchange.getResponse().setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY); //422
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);

                DataBuffer db = new DefaultDataBufferFactory().wrap(objectMapper.writeValueAsBytes(errors));

                // write the given data buffer to the response
                // and return a Mono that signals when it's done
                return exchange.getResponse().writeWith(Mono.just(db));

            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return Mono.empty();
            }
        } else if (ex instanceof ResourceNotFoundException) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);

            // marks the response as complete and forbids writing to it
            return exchange.getResponse().setComplete();
        } else if (ex instanceof InvalidRequestException) {
        	InvalidRequestException ire = (InvalidRequestException) ex;
            List<FieldErrorResource> errorResources = ire.getErrors().getFieldErrors().stream().map(
            		fieldError -> new FieldErrorResource(
		                fieldError.getObjectName(),
		                fieldError.getField(),
		                fieldError.getCode(),
		                fieldError.getDefaultMessage())).collect(Collectors.toList());
            
            ErrorResource errors = new ErrorResource("validation_failure", "Validation failed.", errorResources);
            try {
                exchange.getResponse().setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY); //422
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);

                DataBuffer db = new DefaultDataBufferFactory().wrap(objectMapper.writeValueAsBytes(errors));

                // write the given data buffer to the response
                // and return a Mono that signals when it's done
                return exchange.getResponse().writeWith(Mono.just(db));

            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return Mono.empty();
            }
        }
        return Mono.error(ex);
    }
}
