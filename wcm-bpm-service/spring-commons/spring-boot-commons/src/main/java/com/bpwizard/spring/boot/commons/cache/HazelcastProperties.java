package com.bpwizard.spring.boot.commons.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Validated
//@PropertySource("classpath:hazelcast.properties")
//@ConfigurationProperties(prefix="hazelcast")
public class HazelcastProperties {
	
	public HazelcastProperties() {}
	private int port;
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
	
	List<CacheConfig> caches = new ArrayList<>();

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

	@Override
	public String toString() {
		return "HazelcastProperties [port=" + port + ", members=" + members + ", groupName=" + groupName
				+ ", instanceName=" + instanceName + ", keyStorePassword=" + keyStorePassword + ", enableSSL="
				+ enableSSL + ", keyStore=" + keyStore + ", keyManagerAlgorithm=" + keyManagerAlgorithm
				+ ", trustManagerAlgorithm=" + trustManagerAlgorithm + ", enableEncryption=" + enableEncryption
				+ ", encryptionAlgorithm=" + encryptionAlgorithm + ", encryptionPassword=" + encryptionPassword
				+ ", encryptionSalt=" + encryptionSalt + ", caches=" + caches + "]";
	}
}