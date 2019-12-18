package com.oner.demo.seajug.hazelcast.scalewithhazelcast.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyComputationService {

    @Value("${mycompservice.cache-recursive:false}")
    boolean cacheRecursive;

    @Cacheable("fib")
    public long fib(long number) {
        log.debug("Calculating fib {}", number);
        if(number <= 1) {
            return number;
        } else {
            MyComputationService compService = cacheRecursive ?
                    MyBeanFactory.getBean(MyComputationService.class) : this;

            return compService.fib(number - 2) + compService.fib(number - 1);
        }
    }
}
