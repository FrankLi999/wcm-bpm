package com.bpwizard.gateway.plugin.api.result;

import lombok.Data;

import java.io.Serializable;

/**
 * GatewayWebResult .
 */
@Data
public class DefaultGatewayEntity implements Serializable {

    private static final long serialVersionUID = -2792556188993845048L;
    
    private static final int ERROR = 500;
    
    private static final int SUCCESSFUL = 200;

    private Integer code;

    private String message;

    private Object data;

    /**
     * Instantiates a new gateway result.
     */
    public DefaultGatewayEntity() {

    }

    /**
     * Instantiates a new gateway result.
     *
     * @param code    the code
     * @param message the message
     * @param data    the data
     */
    public DefaultGatewayEntity(final Integer code, final String message, final Object data) {

        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * return success.
     *
     * @return {@linkplain DefaultGatewayEntity}
     */
    public static DefaultGatewayEntity success() {
        return success("");
    }

    /**
     * return success.
     *
     * @param msg msg
     * @return {@linkplain DefaultGatewayEntity}
     */
    public static DefaultGatewayEntity success(final String msg) {
        return success(msg, null);
    }

    /**
     * return success.
     *
     * @param data this is result data.
     * @return {@linkplain DefaultGatewayEntity}
     */
    public static DefaultGatewayEntity success(final Object data) {
        return success(null, data);
    }

    /**
     * return success.
     *
     * @param msg  this ext msg.
     * @param data this is result data.
     * @return {@linkplain DefaultGatewayEntity}
     */
    public static DefaultGatewayEntity success(final String msg, final Object data) {
        return get(SUCCESSFUL, msg, data);
    }

    /**
     * Success gateway web result.
     *
     * @param code the code
     * @param msg  the msg
     * @param data the data
     * @return the gateway web result
     */
    public static DefaultGatewayEntity success(final int code, final String msg, final Object data) {
        return get(code, msg, data);
    }

    /**
     * return error .
     *
     * @param msg error msg
     * @return {@linkplain DefaultGatewayEntity}
     */
    public static DefaultGatewayEntity error(final String msg) {
        return error(ERROR, msg);
    }

    /**
     * return error .
     *
     * @param code error code
     * @param msg  error msg
     * @return {@linkplain DefaultGatewayEntity}
     */
    public static DefaultGatewayEntity error(final int code, final String msg) {
        return get(code, msg, null);
    }

    /**
     * return error .
     *
     * @param code error code
     * @param msg  error msg
     * @param data the data
     * @return {@linkplain DefaultGatewayEntity}
     */
    public static DefaultGatewayEntity error(final int code, final String msg, final Object data) {
        return get(code, msg, data);
    }

    /**
     * return timeout .
     *
     * @param msg error msg
     * @return {@linkplain DefaultGatewayEntity}
     */
    public static DefaultGatewayEntity timeout(final String msg) {
        return error(ERROR, msg);
    }

    private static DefaultGatewayEntity get(final int code, final String msg, final Object data) {
        return new DefaultGatewayEntity(code, msg, data);
    }

}
