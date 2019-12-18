package com.oner.demo.seajug.hazelcast.scalewithhazelcast.model;


import lombok.Value;

@Value(staticConstructor = "of")
public class MyResult {
    long input;
    long output;
    long durationMillis;
}
