package com.bpwizard.gateway.plugin.api.result;

/**
 * The type gateway default result.
 */
public class DefaultGatewayResult implements GatewayResult<DefaultGatewayEntity> {

    @Override
    public DefaultGatewayEntity success(final int code, final String message, final Object object) {
        return DefaultGatewayEntity.success(code, message, object);
    }

    @Override
    public DefaultGatewayEntity error(final int code, final String message, final Object object) {
        return DefaultGatewayEntity.error(code, message, object);
    }
}
