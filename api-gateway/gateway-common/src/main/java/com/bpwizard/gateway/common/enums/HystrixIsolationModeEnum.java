package com.bpwizard.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * hystrix execution isolation strategy.
 */
@RequiredArgsConstructor
@Getter
public enum HystrixIsolationModeEnum {
    /**
     * thread pool mode.
     */
    THREAD_POOL(0, "thread"),
    /**
     * semaphore mode.
     */
    SEMAPHORE(1, "semaphore");

    private final int code;

    private final String name;
}
