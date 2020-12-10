package com.bpwizard.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.bpwizard.gateway.common.exception.GatewayException;

/**
 * Param Type.
 */
@RequiredArgsConstructor
@Getter
public enum ParamTypeEnum {

    /**
     * Post param type enum.
     */
    POST("post", true),

    /**
     * Uri param type enum.
     */
    URI("uri", true),

    /**
     * Query param type enum.
     */
    QUERY("query", true),

    /**
     * Host param type enum.
     */
    HOST("host", true),

    /**
     * Ip param type enum.
     */
    IP("ip", true),

    /**
     * Header param type enum.
     */
    HEADER("header", true);

    private final String name;

    private final Boolean support;

    /**
     * acquire param type supports.
     *
     * @return param type support.
     */
    public static List<ParamTypeEnum> acquireSupport() {
        return Arrays.stream(ParamTypeEnum.values())
                .filter(e -> e.support).collect(Collectors.toList());
    }

    /**
     * get param type enum by name.
     *
     * @param name param type name.
     * @return param type enum.
     */
    public static ParamTypeEnum getParamTypeEnumByName(final String name) {
        return Arrays.stream(ParamTypeEnum.values())
                .filter(e -> e.getName().equals(name) && e.support).findFirst()
                .orElseThrow(() -> new GatewayException(" this  param type can not support!"));
    }
}
