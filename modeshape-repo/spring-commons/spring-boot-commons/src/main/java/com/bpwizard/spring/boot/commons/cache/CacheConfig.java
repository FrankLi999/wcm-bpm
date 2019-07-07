package com.bpwizard.spring.boot.commons.cache;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CacheConfig {
	private int backupCount;
	private String cacheName;
	private int timeToLiveSeconds;
	private int maxSize;
	private String maxSizePolicy;
	private String evictionPolicy;
}
