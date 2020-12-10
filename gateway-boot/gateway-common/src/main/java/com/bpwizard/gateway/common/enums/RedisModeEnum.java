package com.bpwizard.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The enum Redis mode enum.
 */
@RequiredArgsConstructor
@Getter
public enum RedisModeEnum {

    /**
     * Cluster redis mode enum.
     */
    CLUSTER("cluster"),

    /**
     * Sentinel redis mode enum.
     */
    SENTINEL("sentinel"),

    /**
     * Standalone redis mode enum.
     */
    STANDALONE("Standalone");

    private final String name;

}
