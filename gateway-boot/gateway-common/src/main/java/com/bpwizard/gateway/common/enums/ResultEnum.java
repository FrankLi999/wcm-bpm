package com.bpwizard.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * ResultEnum.
 */
@RequiredArgsConstructor
@Getter
public enum ResultEnum {

    /**
     * Success result enum.
     */
    SUCCESS("success"),

    /**
     * Time out result enum.
     */
    TIME_OUT("timeOut"),

    /**
     * Error result enum.
     */
    ERROR("error"),;

    private final String name;


}
