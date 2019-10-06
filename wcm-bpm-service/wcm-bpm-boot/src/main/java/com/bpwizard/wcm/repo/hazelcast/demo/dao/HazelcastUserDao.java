package com.bpwizard.wcm.repo.hazelcast.demo.dao;

import java.util.List;

import com.bpwizard.wcm.repo.hazelcast.demo.model.HazelcastUser;

public interface HazelcastUserDao {

    HazelcastUser findByName(String name);

    List<HazelcastUser> findAll();

}