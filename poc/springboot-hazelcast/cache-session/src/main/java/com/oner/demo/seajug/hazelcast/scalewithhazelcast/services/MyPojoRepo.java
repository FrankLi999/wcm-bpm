package com.oner.demo.seajug.hazelcast.scalewithhazelcast.services;

import com.oner.demo.seajug.hazelcast.scalewithhazelcast.model.MyPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

public interface MyPojoRepo extends JpaRepository<MyPojo, Long> {

    @QueryHints ({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<MyPojo> findAll();

}
