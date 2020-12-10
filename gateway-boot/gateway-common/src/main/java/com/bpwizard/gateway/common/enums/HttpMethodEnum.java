package com.bpwizard.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import com.bpwizard.gateway.common.exception.GatewayException;

/**
 * this is http method support.
 */
@RequiredArgsConstructor
@Getter
public enum HttpMethodEnum {

    /**
     * Get http method enum.
     */
    GET("get", true),

    /**
     * Post http method enum.
     */
    POST("post", true),

    /**
     * Put http method enum.
     */
    PUT("put", true),

    /**
     * Delete http method enum.
     */
    DELETE("delete", true);

    private final String name;

    private final Boolean support;

    /**
     * convert by name.
     *
     * @param name name
     * @return {@link HttpMethodEnum }
     */
    public static HttpMethodEnum acquireByName(final String name) {
        return Arrays.stream(HttpMethodEnum.values())
                .filter(e -> e.support && e.name.equals(name)).findFirst()
                .orElseThrow(() -> new GatewayException(" this http method can not support!"));
    }

}
