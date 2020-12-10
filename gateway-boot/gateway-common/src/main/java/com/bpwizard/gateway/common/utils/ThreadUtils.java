package com.bpwizard.gateway.common.utils;

import java.util.concurrent.TimeUnit;

/**
 * thread utils.
 */
public class ThreadUtils {

    /**
     * sleep current thread.
     *
     * @param timeUnit the time unit
     * @param time     the time
     */
    public static void sleep(final TimeUnit timeUnit, final int time) {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
