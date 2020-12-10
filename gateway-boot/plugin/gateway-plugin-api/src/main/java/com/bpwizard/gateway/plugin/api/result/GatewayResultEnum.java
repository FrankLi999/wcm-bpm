package com.bpwizard.gateway.plugin.api.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The enum gateway result enum.
 */
@Getter
@RequiredArgsConstructor
public enum GatewayResultEnum {

    /**
     * Fail gateway result enum.
     */
    FAIL(-1, "Gateway internal error, Please try again a little later!"),

    /**
     * Success gateway result enum.
     */
    SUCCESS(200, "Successful!"),

    /**
     * Sign is not pass gateway result enum.
     */
    SIGN_IS_NOT_PASS(401, "Sign is not accepted!"),

    /**
     * Payload too large gateway result enum.
     */
    PAYLOAD_TOO_LARGE(403, "Payload is too large"),

    /**
     * Too many requests gateway result enum.
     */
    TOO_MANY_REQUESTS(429, "Too many requests，please try again a little later!"),

    /**
     * Sofa have body param gateway result enum.
     */
    SOFA_HAVE_BODY_PARAM(432, "sofa接口必须要有参数，Please add json format in body for SOFA api！"),

    /**
     * full selector type enum.
     */
    PARAM_ERROR(-100, "Full selector type is not correct!"),

    /**
     * Or match mode enum.
     */
    TIME_ERROR(-101, "Timer parameter is not correct or might be expired!"),

    /**
     * Rule not find gateway result enum.
     */
    RULE_NOT_FIND(-102, "Rule not found!"),

    /**
     * Service result error gateway result enum.
     */
    SERVICE_RESULT_ERROR(-103, "Server error"),

    /**
     * Service timeout gateway result enum.
     */
    SERVICE_TIMEOUT(-104, "Server timeout"),

    /**
     * Sing time is timeout gateway result enum.
     */
    SING_TIME_IS_TIMEOUT(-105, "Sign timeout!"),

    /**
     * Cannot find url gateway result enum.
     */
    CANNOT_FIND_URL(-106, "Not able to find proper url, plase check your configurations!"),

    /**
     * Cannot find selector gateway result enum.
     */
    CANNOT_FIND_SELECTOR(-107, "Not able to found selector, plase check your selector configurations！"),

    /**
     * The Cannot config springcloud serviceid.
     */
    CANNOT_CONFIG_SPRINGCLOUD_SERVICEID(-108, "Not able to found springcloud serviceId"),

    /**
     * The Springcloud serviceid is error.
     */
    SPRINGCLOUD_SERVICEID_IS_ERROR(-109, "springCloud serviceId does not exist or not configured! "),

    /**
     * The Sentinel block error.
     */
    SENTINEL_BLOCK_ERROR(-110, "the request block by sentinel!");

    private final int code;

    private final String msg;
}
