package com.bpwizard.spring.boot.commons.cache;

public class CacheConfig {
	private int backupCount;
	private String cacheName;
	private int timeToLiveSeconds;
	private int maxSize;
	private String maxSizePolicy;
	private String evictionPolicy;
	public int getBackupCount() {
		return backupCount;
	}
	public void setBackupCount(int backupCount) {
		this.backupCount = backupCount;
	}
	public String getCacheName() {
		return cacheName;
	}
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	public int getTimeToLiveSeconds() {
		return timeToLiveSeconds;
	}
	public void setTimeToLiveSeconds(int timeToLiveSeconds) {
		this.timeToLiveSeconds = timeToLiveSeconds;
	}
	public int getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	public String getMaxSizePolicy() {
		return maxSizePolicy;
	}
	public void setMaxSizePolicy(String maxSizePolicy) {
		this.maxSizePolicy = maxSizePolicy;
	}
	public String getEvictionPolicy() {
		return evictionPolicy;
	}
	public void setEvictionPolicy(String evictionPolicy) {
		this.evictionPolicy = evictionPolicy;
	}
	@Override
	public String toString() {
		return "CacheConfig [backupCount=" + backupCount + ", cacheName=" + cacheName + ", timeToLiveSeconds="
				+ timeToLiveSeconds + ", maxSize=" + maxSize + ", maxSizePolicy=" + maxSizePolicy + ", evictionPolicy="
				+ evictionPolicy + "]";
	}
}
