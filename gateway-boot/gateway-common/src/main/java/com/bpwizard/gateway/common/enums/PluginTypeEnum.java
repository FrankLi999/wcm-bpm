package com.bpwizard.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Plugin Type.
 */
@RequiredArgsConstructor
@Getter
public enum PluginTypeEnum {

    /**
     * before plugin type enum.
     */
    BEFORE("before"),

    /**
     * function plugin type enum.
     */
    FUNCTION("function"),

    /**
     * last plugin type enum.
     */
    LAST("last");

    private final String name;

}
