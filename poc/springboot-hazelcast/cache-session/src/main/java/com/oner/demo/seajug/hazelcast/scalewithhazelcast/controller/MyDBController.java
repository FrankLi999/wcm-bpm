package com.oner.demo.seajug.hazelcast.scalewithhazelcast.controller;


import com.oner.demo.seajug.hazelcast.scalewithhazelcast.model.MyPojo;
import com.oner.demo.seajug.hazelcast.scalewithhazelcast.services.MyPojoRepo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/db")
@AllArgsConstructor
public class MyDBController {

    MyPojoRepo myPojoRepo;

    @GetMapping("/{id}")
    MyPojo getSingle(@PathVariable long id) {
        return myPojoRepo.findById(id).orElseGet(() -> create());
    }

    @GetMapping("/create")
    MyPojo create() {
        MyPojo myPojo = new MyPojo();
        myPojo.setName(UUID.randomUUID().toString());
        myPojo.setAge(UUID.randomUUID().hashCode() % 60);
        return myPojoRepo.save(myPojo);
    }

    @GetMapping({"/all", "/"})
    List<MyPojo> getAll() {
        return myPojoRepo.findAll();
    }
}
