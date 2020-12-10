package com.bpwizard.gateway.plugin.api.result;

/**
 * The interface Gateway result.
 */
public interface GatewayResult<T> {

    /**
     * Success t.
     *
     * @param code    the code
     * @param message the message
     * @param object  the object
     * @return the t
     */
    T success(int code, String message, Object object);

    /**
     * Error t.
     *
     * @param code    the code
     * @param message the message
     * @param object  the object
     * @return the t
     */
    T error(int code, String message, Object object);

}
