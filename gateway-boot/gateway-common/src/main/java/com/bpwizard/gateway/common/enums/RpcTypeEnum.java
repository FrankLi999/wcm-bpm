package com.bpwizard.gateway.common.enums;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.bpwizard.gateway.common.dto.convert.rule.DivideRuleHandle;
import com.bpwizard.gateway.common.dto.convert.rule.SofaRuleHandle;
import com.bpwizard.gateway.common.dto.convert.rule.SpringCloudRuleHandle;
import com.bpwizard.gateway.common.exception.GatewayException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * RpcTypeEnum.
 */
@RequiredArgsConstructor
@Getter
public enum RpcTypeEnum {

    /**
     * Http rpc type enum.
     */
    HTTP("http", true) {

        @Override
        public Serializable ruleHandle(final String path) {
            DivideRuleHandle divideRuleHandle = new DivideRuleHandle();
            divideRuleHandle.setLoadBalance(getDefaultLoadBalance().getName());
            divideRuleHandle.setRetry(getDefaultRetry());
            return divideRuleHandle;
        }
    },

    /**
     * Sofa rpc type enum.
     */
    SOFA("sofa", true) {

        @Override
        public Serializable ruleHandle(final String path) {
            SofaRuleHandle sofaRuleHandle = new SofaRuleHandle();
            sofaRuleHandle.setLoadBalance(getDefaultLoadBalance().getName());
            sofaRuleHandle.setRetries(getDefaultRetries());
            sofaRuleHandle.setTimeout(getDefaultTimeout());
            return sofaRuleHandle;
        }
    },

    /**
     * Web socket rpc type enum.
     */
    WEB_SOCKET("websocket", true),

    /**
     * springCloud rpc type enum.
     */
    SPRING_CLOUD("springCloud", true),

    /**
     * motan.
     */
    MOTAN("motan", false),

    /**
     * grpc.
     */
    GRPC("grpc", false);


    private final String name;

    private final Boolean support;

    // some default values for rule handlers.
    private final LoadBalanceEnum defaultLoadBalance = LoadBalanceEnum.RANDOM;
    private final int defaultRetries = 0;
    private final long defaultTimeout = 3000;
    private final int defaultRetry = 0;
    /**
     * acquire operator supports.
     *
     * @return operator support.
     */
    public static List<RpcTypeEnum> acquireSupports() {
        return Arrays.stream(RpcTypeEnum.values())
                .filter(e -> e.support).collect(Collectors.toList());
    }

    /**
     * acquireByName.
     *
     * @param name this is rpc type
     * @return RpcTypeEnum rpc type enum
     */
    public static RpcTypeEnum acquireByName(final String name) {
        return Arrays.stream(RpcTypeEnum.values())
                .filter(e -> e.support && e.name.equals(name)).findFirst()
                .orElseThrow(() -> new GatewayException(String.format(" this rpc type can not support %s", name)));
    }

    /**
     * ruleHandle
     * This method is design for overwrite.
     * @param path this is access path
     * @return Default rpc rule handler.
     */
    public Serializable ruleHandle(final String path) {
        SpringCloudRuleHandle springCloudRuleHandle = new SpringCloudRuleHandle();
        springCloudRuleHandle.setPath(path);
        return springCloudRuleHandle;
    }
}
