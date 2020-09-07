package com.bpwizard.gateway.common.dto.convert;

import com.bpwizard.gateway.common.constant.Constants;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * hystrix thread pool config.
 */
@Getter
@Setter
@EqualsAndHashCode
public class HystrixThreadPoolConfig {
    private int coreSize = Constants.HYSTRIX_THREAD_POOL_CORE_SIZE;

    private int maximumSize = Constants.HYSTRIX_THREAD_POOL_MAX_SIZE;

    private int keepAliveTimeMinutes = Constants.HYSTRIX_THREAD_KEEP_ALIVE_TIME_MINUTE;

    private int maxQueueSize = Constants.HYSTRIX_THREAD_POOL_QUEUE_SIZE;
}
