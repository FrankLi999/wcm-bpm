package com.bpwizard.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Plugin Role.
 */
@RequiredArgsConstructor
@Getter
public enum PluginRoleEnum {

    /**
     * Sys plugin role enum.
     */
    SYS(0, "sys"),

    /**
     * Custom plugin role enum.
     */
    CUSTOM(1, "custom");

    private final Integer code;

    private final String name;

}
