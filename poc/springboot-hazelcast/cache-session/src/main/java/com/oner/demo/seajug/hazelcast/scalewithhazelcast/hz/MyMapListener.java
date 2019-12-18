package com.oner.demo.seajug.hazelcast.scalewithhazelcast.hz;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyMapListener<K, V> implements EntryAddedListener<K, V> {
    @Override
    public void entryAdded(EntryEvent<K, V> event) {
        log.info("Map entry added: {} => {}", event.getKey(), event.getValue());
    }
}
