package com.bpwizard.gateway.metrics.facade.executor;

import lombok.extern.slf4j.Slf4j;
import com.bpwizard.gateway.common.concurrent.GatewayThreadFactory;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Metrics thread pool executor.
 */
@Slf4j
public final class MetricsThreadPoolExecutor extends ThreadPoolExecutor {
    
    /**
     * Instantiates a new Metrics thread pool executor.
     *
     * @param threadCount core and max thread count
     * @param queueSize   queue size
     */
    public MetricsThreadPoolExecutor(final int threadCount, final int queueSize) {
        super(threadCount, threadCount, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(queueSize),
        		GatewayThreadFactory.create("metrics", true), new CallerWaitPolicy());
    }
    
    private static class CallerWaitPolicy implements RejectedExecutionHandler {
    
        @Override
        public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor) {
            try {
                log.warn("queue is full, trigger caller thread : {} wait", Thread.currentThread().getName());
                executor.getQueue().put(r);
            } catch (InterruptedException ex) {
                log.error("InterruptedException, discard {}", r);
            }
        }
    }
}

