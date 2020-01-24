package com.oner.demo.seajug.hazelcast.scalewithhazelcast.services;

import com.oner.demo.seajug.hazelcast.scalewithhazelcast.model.MyPojo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@Order(Integer.MAX_VALUE)
@Component
@Slf4j
@AllArgsConstructor
public class DummyDataManager implements CommandLineRunner {

    MyPojoRepo pojoRepo;

    @Override
    public void run(String... args) throws Exception {
        List<MyPojo> all = pojoRepo.findAll();
        if(all.isEmpty()) {
            log.info("Loading some data");
            ThreadLocalRandom random = ThreadLocalRandom.current();
            Stream.of("Gokhan", "Marko", "Matko", "Peter", "Enes")
                    .forEach(name -> {
                        MyPojo myPojo = new MyPojo();
                        myPojo.setName(name);
                        myPojo.setAge(random.nextInt(45));
                        pojoRepo.save(myPojo);
                    });
        }
        log.info("Entries={}", pojoRepo.findAll());
    }
}
