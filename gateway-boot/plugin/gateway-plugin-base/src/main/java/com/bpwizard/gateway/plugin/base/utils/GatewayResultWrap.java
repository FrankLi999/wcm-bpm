package com.bpwizard.gateway.plugin.base.utils;

import com.bpwizard.gateway.plugin.api.result.GatewayResult;

/**
 * The type Gateway result warp.
 */
public final class GatewayResultWrap<T> {
    
    /**
     * Success object.
     *
     * @param code    the code
     * @param message the message
     * @param object  the object
     * @return the object
     */
    public static Object success(final int code, final String message, final Object object) {
        return SpringBeanUtils.getInstance().getBean(GatewayResult.class).success(code, message, object);
    }

    /**
     * Error object.
     *
     * @param code    the code
     * @param message the message
     * @param object  the object
     * @return the object
     */
    public static Object error(final int code, final String message, final Object object) {
        return SpringBeanUtils.getInstance().getBean(GatewayResult.class).error(code, message, object);
    }
}
