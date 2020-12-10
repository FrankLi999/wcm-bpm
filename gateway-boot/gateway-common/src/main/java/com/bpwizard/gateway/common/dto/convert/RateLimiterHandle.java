package com.bpwizard.gateway.common.dto.convert;

import lombok.Data;

/**
 * this is rateLimiter plugin handle.
 */
@Data
public class RateLimiterHandle {

    /**
     * replenish rate.
     */
    private double replenishRate;

    /**
     * burst capacity.
     */
    private double burstCapacity;

    /**
     * loged.
     */
    private boolean loged;

}
