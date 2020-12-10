package com.bpwizard.gateway.common.enums;

import java.util.Arrays;
import java.util.Objects;

import com.bpwizard.gateway.common.exception.GatewayException;

/**
 * configuration group.
 */
public enum ConfigGroupEnum {

    /**
     * App auth config group enum.
     */
    APP_AUTH,
    
    /**
     * Plugin config group enum.
     */
    PLUGIN,
    
    /**
     * Rule config group enum.
     */
    RULE,
    
    /**
     * Selector config group enum.
     */
    SELECTOR,

    /**
     * Meta data config group enum.
     */
    META_DATA;

    /**
     * Acquire by name config group enum.
     *
     * @param name the name
     * @return the config group enum
     */
    public static ConfigGroupEnum acquireByName(final String name) {
        return Arrays.stream(ConfigGroupEnum.values())
                .filter(e -> Objects.equals(e.name(), name))
                .findFirst().orElseThrow(() -> new GatewayException(" this ConfigGroupEnum can not support!"));
    }
}
