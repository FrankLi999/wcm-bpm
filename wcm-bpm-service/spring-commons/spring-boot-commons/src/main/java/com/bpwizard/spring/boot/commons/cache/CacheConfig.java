package com.bpwizard.spring.boot.commons.cache;

import java.util.ArrayList;
import java.util.List;

public class CacheConfig {
	private int backupCount;
	private String cacheName;
	private int timeToLiveSeconds;
	private int maxSize;
	private String maxSizePolicy;
	private String evictionPolicy;
	
	List<EntryListener> entryListeners = new ArrayList<>();
	List<Index> indexes = new ArrayList<>();
	List<Attribute> attributes = new ArrayList<>();
	
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
	
	public List<EntryListener> getEntryListeners() {
		return entryListeners;
	}
	public void setEntryListeners(List<EntryListener> entryListeners) {
		this.entryListeners = entryListeners;
	}
	
	public List<Index> getIndexes() {
		return indexes;
	}
	public void setIndexes(List<Index> indexes) {
		this.indexes = indexes;
	}
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "CacheConfig [backupCount=" + backupCount + ", cacheName=" + cacheName + ", timeToLiveSeconds="
				+ timeToLiveSeconds + ", maxSize=" + maxSize + ", maxSizePolicy=" + maxSizePolicy + ", evictionPolicy="
				+ evictionPolicy + ", entryListeners=" + entryListeners + ", indexes=" + indexes + ", attributes="
				+ attributes + "]";
	}

	public static class EntryListener {
		private boolean includeValue;
		private boolean local;
		private String listener;
		public boolean isIncludeValue() {
			return includeValue;
		}
		public void setIncludeValue(boolean includeValue) {
			this.includeValue = includeValue;
		}
		public boolean isLocal() {
			return local;
		}
		public void setLocal(boolean local) {
			this.local = local;
		}
		public String getListener() {
			return listener;
		}
		public void setListener(String listener) {
			this.listener = listener;
		}
		@Override
		public String toString() {
			return "EntryListener [includeValue=" + includeValue + ", local=" + local + ", listener=" + listener + "]";
		}
	}

	public static class Index {
		private String attribute;
		private boolean ordered = false;
		public String getAttribute() {
			return attribute;
		}
		public void setAttribute(String attribute) {
			this.attribute = attribute;
		}
		public boolean isOrdered() {
			return ordered;
		}
		public void setOrdered(boolean ordered) {
			this.ordered = ordered;
		}
		@Override
		public String toString() {
			return "Index [attribute=" + attribute + ", ordered=" + ordered + "]";
		}
	}
	
	public static class Attribute {
		private String extractor;
		private String name;
		public String getExtractor() {
			return extractor;
		}
		public void setExtractor(String extractor) {
			this.extractor = extractor;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return "Attribute [extractor=" + extractor + ", name=" + name + "]";
		}
	}
}
