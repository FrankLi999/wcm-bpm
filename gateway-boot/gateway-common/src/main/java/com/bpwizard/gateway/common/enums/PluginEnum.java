package com.bpwizard.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * PluginEnum.
 */
@RequiredArgsConstructor
@Getter
public enum PluginEnum {

    /**
     * Global plugin enum.
     */
    GLOBAL(1, 0, "global"),

    /**
     * Sign plugin enum.
     */
    SIGN(2, 0, "sign"),

    /**
     * Waf plugin enum.
     */
    WAF(10, 0, "waf"),

    /**
     * Rate limiter plugin enum.
     */
    RATE_LIMITER(20, 0, "rate_limiter"),

    /**
     * Rewrite plugin enum.
     */
    REWRITE(30, 0, "rewrite"),

    /**
     * Redirect plugin enum.
     */
    REDIRECT(40, 0, "redirect"),


    /**
     * Hystrix plugin enum.
     */
    HYSTRIX(45, 0, "hystrix"),

    /**
     * Divide plugin enum.
     */
    DIVIDE(50, 0, "divide"),

    /**
     * springCloud plugin enum.
     */
    SPRING_CLOUD(50, 0, "springCloud"),

    /**
     * webSocket plugin enum.
     */
    WEB_SOCKET(55, 0, "webSocket"),

    /**
     * Monitor plugin enum.
     */
    MONITOR(80, 0, "monitor"),

    /**
     * Response plugin enum.
     */
    RESPONSE(100, 0, "response");

    private final int code;

    private final int role;

    private final String name;

    /**
     * get plugin enum by name.
     *
     * @param name plugin name.
     * @return plugin enum.
     */
    public static PluginEnum getPluginEnumByName(final String name) {
        return Arrays.stream(PluginEnum.values())
                .filter(pluginEnum -> pluginEnum.getName().equals(name))
                .findFirst().orElse(PluginEnum.GLOBAL);
    }
}
