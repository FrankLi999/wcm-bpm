package com.oner.demo.seajug.hazelcast.scalewithhazelcast.controller;


import com.oner.demo.seajug.hazelcast.scalewithhazelcast.model.MyResult;
import com.oner.demo.seajug.hazelcast.scalewithhazelcast.services.MyComputationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest")
@AllArgsConstructor
public class MyController {

    MyComputationService computationService;

    @GetMapping("/fib/{input}")
    MyResult fibonacci(@PathVariable long input) {
        long start = System.currentTimeMillis();
        long result = computationService.fib(input);
        long end = System.currentTimeMillis();

        return MyResult.of(input, result, end - start);
    }
}
