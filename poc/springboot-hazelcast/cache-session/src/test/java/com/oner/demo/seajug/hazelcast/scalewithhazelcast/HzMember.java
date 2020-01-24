package com.oner.demo.seajug.hazelcast.scalewithhazelcast;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.core.Hazelcast;

public class HzMember {

	public static void main(String[] args) {
		Hazelcast.newHazelcastInstance(new ClasspathXmlConfig("hazelcast.xml"));
	}

}
