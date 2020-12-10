package com.bpwizard.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * MatchModeEnum.
 */
@RequiredArgsConstructor
@Getter
public enum MatchModeEnum {

    /**
     * And match mode enum.
     */
    AND(0, "and"),

    /**
     * Or match mode enum.
     */
    OR(1, "or");

    private final int code;

    private final String name;

    /**
     * get match mode name by code.
     *
     * @param code match mode code.
     * @return match mode name.
     */
    public static String getMatchModeByCode(final int code) {
        return Arrays.stream(MatchModeEnum.values())
                .filter(e -> e.code == code).findFirst()
                .orElse(MatchModeEnum.AND)
                .getName();
    }
}
