package com.bpwizard.spring.boot.commons.web.exceptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.networknt.schema.ValidationMessage;

@ControllerAdvice
public class JsonValidationExceptionHandler {
 
    @ExceptionHandler(JsonValidationFailedException.class)
    public ResponseEntity<Map<String, Object>> onJsonValidationFailedException(JsonValidationFailedException ex) {
        List<String> messages = ex.getValidationMessages().stream()
                .map(ValidationMessage::getMessage)
                .collect(Collectors.toList());
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Json validation failed");
        body.put("details", messages);
//        return ResponseEntity.badRequest().body(Map.of(
//            "message", "Json validation failed",
//            "details", messages
//        ));
      return ResponseEntity.badRequest().body(Collections.unmodifiableMap(body));
    }
}