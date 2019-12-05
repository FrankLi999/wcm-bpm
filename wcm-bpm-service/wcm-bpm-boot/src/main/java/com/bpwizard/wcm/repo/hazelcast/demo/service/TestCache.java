package com.bpwizard.wcm.repo.hazelcast.demo.service;



import java.util.Date;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bpwizard.wcm.repo.hazelcast.demo.model.HazelcastUser;

@Service
public class TestCache {
	
	@Cacheable(cacheNames={"wcm_bpm"}, key="#user.name")
	public HazelcastUser getSomething(HazelcastUser user) {
    	System.out.println(">>>>>>>>>>>>> cache miss");
		user.setName(user.getName() + new Date());
		
		return user;
	}
}
