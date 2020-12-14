package com.bpwizard.gateway.web.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.plugin.base.utils.GatewayResultWrap;

/**
 * GlobalErrorHandler.
 */
public class GlobalErrorHandler extends DefaultErrorWebExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorHandler.class);

    /**
     * Instantiates a new Global error handler.
     *
     * @param errorAttributes    the error attributes
     * @param resourceProperties the resource properties
     * @param errorProperties    the error properties
     * @param applicationContext the application context
     */
    public GlobalErrorHandler(final ErrorAttributes errorAttributes,
                              final WebProperties.Resources resourceProperties,
                              final ErrorProperties errorProperties,
                              final ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(final ServerRequest request, final boolean includeStackTrace) {
        logError(request);
        return response(request);
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected int getHttpStatus(final Map<String, Object> errorAttributes) {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    private Map<String, Object> response(final ServerRequest request) {
        Throwable ex = getError(request);
        Object error = GatewayResultWrap.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage());
        return JsonUtils.toObjectMap(JsonUtils.toJson(error));
    }

    private void logError(final ServerRequest request) {
        Throwable ex = getError(request);
        logger.error(request.exchange().getLogPrefix() + formatError(ex, request));
    }

    private String formatError(final Throwable ex, final ServerRequest request) {
        String reason = ex.getClass().getSimpleName() + ": " + ex.getMessage();
        return "Resolved [" + reason + "] for HTTP " + request.methodName() + " " + request.path();
    }

}


