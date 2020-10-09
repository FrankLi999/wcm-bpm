/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.bpwizard.gateway.admin.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bpwizard.gateway.admin.result.GatewayAdminResult;
import com.bpwizard.gateway.common.exception.GatewayException;

/**
 * ControllerMethodResolver.
 * https://dzone.com/articles/global-exception-handling-with-controlleradvice
 */
@ControllerAdvice
public class ExceptionHandlers {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlers.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected GatewayAdminResult serverExceptionHandler(final Exception exception) {
        LOGGER.error(exception.getMessage(), exception);
        String message;
        if (exception instanceof GatewayException) {
        	GatewayException gatewayException = (GatewayException) exception;
            message = gatewayException.getMessage();
        } else {
            message = "System busy, please try a little later.";
        }
        return GatewayAdminResult.error(message);
    }

    @ResponseBody
    @ExceptionHandler(DuplicateKeyException.class)
    protected GatewayAdminResult serverExceptionHandler(final DuplicateKeyException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return GatewayAdminResult.error("Duplicate key, try again.");
    }
}
