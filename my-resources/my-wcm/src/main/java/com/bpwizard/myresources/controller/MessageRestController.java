package com.bpwizard.myresources.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.myresources.ApplicationConstants;

@RefreshScope
@RestController
class MessageRestController {

    @Value("${my.message:Hello default}")
    private String message;

    @RequestMapping(ApplicationConstants.API_BASE_PATH + "/message")
    String getMessage() {
        return this.message;
    }
}