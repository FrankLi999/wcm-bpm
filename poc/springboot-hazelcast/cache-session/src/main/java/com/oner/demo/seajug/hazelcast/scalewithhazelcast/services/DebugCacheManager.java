package com.oner.demo.seajug.hazelcast.scalewithhazelcast.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Integer.MAX_VALUE)
@Component
@Slf4j
@AllArgsConstructor
public class DebugCacheManager implements CommandLineRunner {

    CacheManager cacheManager;

    @Override
    public void run(String... args) throws Exception {
        log.info("CacheManager {}",
                (this.cacheManager==null ? "null" : this.cacheManager.getClass().getCanonicalName()));
    }
}
