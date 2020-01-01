package com.bpwizard.spring.boot.commons.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;

@Validated
//@PropertySource("classpath:hazelcast.properties")
//@ConfigurationProperties(prefix="hazelcast")
public class HazelcastProperties {
	
	public HazelcastProperties() {}
	private boolean enabled;
	private int port;
	private int cpMemberCounts = 0;
	private int cpGroupSize = 3;
	private String members;
	private String groupName;
	private String instanceName;
	
	private String keyStorePassword;
	private boolean enableSSL;
	private String keyStore;
	private String keyManagerAlgorithm;
	private String trustManagerAlgorithm;
	
	private boolean enableEncryption;
	private String encryptionAlgorithm;
	private String encryptionPassword;
	private String encryptionSalt;
	private String loggingType;
	
	List<CacheConfig> caches = new ArrayList<>();
	private ManagementCenter managementCenter = new ManagementCenter();
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public List<CacheConfig> getCaches() {
		return caches;
	}

	public void setCaches(List<CacheConfig> caches) {
		this.caches = caches;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	public boolean isEnableSSL() {
		return enableSSL;
	}

	public void setEnableSSL(boolean enableSSL) {
		this.enableSSL = enableSSL;
	}

	public String getKeyStore() {
		return keyStore;
	}

	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}

	public String getKeyManagerAlgorithm() {
		return keyManagerAlgorithm;
	}

	public void setKeyManagerAlgorithm(String keyManagerAlgorithm) {
		this.keyManagerAlgorithm = keyManagerAlgorithm;
	}

	public String getTrustManagerAlgorithm() {
		return trustManagerAlgorithm;
	}

	public void setTrustManagerAlgorithm(String trustManagerAlgorithm) {
		this.trustManagerAlgorithm = trustManagerAlgorithm;
	}

	public boolean isEnableEncryption() {
		return enableEncryption;
	}

	public void setEnableEncryption(boolean enableEncryption) {
		this.enableEncryption = enableEncryption;
	}

	public String getEncryptionAlgorithm() {
		return encryptionAlgorithm;
	}

	public void setEncryptionAlgorithm(String encryptionAlgorithm) {
		this.encryptionAlgorithm = encryptionAlgorithm;
	}

	public String getEncryptionPassword() {
		return encryptionPassword;
	}

	public void setEncryptionPassword(String encryptionPassword) {
		this.encryptionPassword = encryptionPassword;
	}

	public String getEncryptionSalt() {
		return encryptionSalt;
	}

	public void setEncryptionSalt(String encryptionSalt) {
		this.encryptionSalt = encryptionSalt;
	}

	public String getLoggingType() {
		return loggingType;
	}

	public void setLoggingType(String loggingType) {
		this.loggingType = loggingType;
	}

	
	public int getCpMemberCounts() {
		return cpMemberCounts;
	}

	public int getCpGroupSize() {
		return cpGroupSize;
	}
	public void setCpMemberCounts(int cpMemberCounts) {
		this.cpMemberCounts = cpMemberCounts;
	}

	public ManagementCenter getManagementCenter() {
		return managementCenter;
	}

	public void setManagementCenter(ManagementCenter managementCenter) {
		this.managementCenter = managementCenter;
	}

	public void setCpGroupSize(int cpGroupSize) {
		this.cpGroupSize = cpGroupSize;
	}

	@Override
	public String toString() {
		return "HazelcastProperties [enabled=" + enabled + ", port=" + port + ", cpMemberCounts=" + cpMemberCounts
				+ ", cpGroupSize=" + cpGroupSize + ", members=" + members + ", groupName=" + groupName
				+ ", instanceName=" + instanceName + ", keyStorePassword=" + keyStorePassword + ", enableSSL="
				+ enableSSL + ", keyStore=" + keyStore + ", keyManagerAlgorithm=" + keyManagerAlgorithm
				+ ", trustManagerAlgorithm=" + trustManagerAlgorithm + ", enableEncryption=" + enableEncryption
				+ ", encryptionAlgorithm=" + encryptionAlgorithm + ", encryptionPassword=" + encryptionPassword
				+ ", encryptionSalt=" + encryptionSalt + ", loggingType=" + loggingType + ", caches=" + caches
				+ ", managementCenter=" + managementCenter + "]";
	}



	public static class ManagementCenter {
		private String url = "";
		private boolean enabled;
		private long updateInterval;
		private boolean scriptingEnabled;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public boolean isEnabled() {
			return enabled;
		}
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		public long getUpdateInterval() {
			return updateInterval;
		}
		public void setUpdateInterval(long updateInterval) {
			this.updateInterval = updateInterval;
		}
		public boolean isScriptingEnabled() {
			return scriptingEnabled;
		}
		public void setScriptingEnabled(boolean scriptingEnabled) {
			this.scriptingEnabled = scriptingEnabled;
		}
		@Override
		public String toString() {
			return "ManagementCenter [url=" + url + ", enabled=" + enabled + ", updateInterval=" + updateInterval
					+ ", scriptingEnabled=" + scriptingEnabled + "]";
		}
	}
}