package com.bpwizard.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * SelectorTypeEnum.
 *
 */
@RequiredArgsConstructor
@Getter
public enum SelectorTypeEnum {

    /**
     * full selector type enum.
     */
    FULL_FLOW(0, "Full Flow"),

    /**
     * Or match mode enum.
     */
    CUSTOM_FLOW(1, "Custom FLow");

    private final int code;

    private final String name;

    /**
     * get selector type name by code.
     *
     * @param code selector type code.
     * @return selector type name.
     */
    public static String getSelectorTypeByCode(final int code) {
        for (SelectorTypeEnum selectorTypeEnum : SelectorTypeEnum.values()) {
            if (selectorTypeEnum.getCode() == code) {
                return selectorTypeEnum.getName();
            }
        }
        return null;
    }
}
