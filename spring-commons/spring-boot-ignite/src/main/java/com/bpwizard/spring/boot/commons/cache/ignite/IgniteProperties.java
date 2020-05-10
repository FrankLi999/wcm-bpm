package com.bpwizard.spring.boot.commons.cache.ignite;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;

@Validated
//@PropertySource("classpath:hazelcast.properties")
//@ConfigurationProperties(prefix="hazelcast")
public class IgniteProperties {
	
	public IgniteProperties() {}
	private boolean enabled;
	private String instanceName;
	private boolean peerClassLoadingEnabled;
	private String localHost;
	private String ipfinderAddresses;
	private int tcpDiscoveryLocalPort;
	private int tcpDiscoveryLocalPortRange;
	private String communicationSpiLocalAddress;
	private int communicationSpiLocalPort;
	private int communicationSpiSlowClientQueueLimit;
	List<CacheConfig> caches = new ArrayList<>();
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public boolean isPeerClassLoadingEnabled() {
		return peerClassLoadingEnabled;
	}
	public void setPeerClassLoadingEnabled(boolean peerClassLoadingEnabled) {
		this.peerClassLoadingEnabled = peerClassLoadingEnabled;
	}
	public String getLocalHost() {
		return localHost;
	}
	public void setLocalHost(String localHost) {
		this.localHost = localHost;
	}
	public String getIpfinderAddresses() {
		return ipfinderAddresses;
	}
	public void setIpfinderAddresses(String ipfinderAddresses) {
		this.ipfinderAddresses = ipfinderAddresses;
	}
	public int getTcpDiscoveryLocalPort() {
		return tcpDiscoveryLocalPort;
	}
	public void setTcpDiscoveryLocalPort(int tcpDiscoveryLocalPort) {
		this.tcpDiscoveryLocalPort = tcpDiscoveryLocalPort;
	}
	public int getTcpDiscoveryLocalPortRange() {
		return tcpDiscoveryLocalPortRange;
	}
	public void setTcpDiscoveryLocalPortRange(int tcpDiscoveryLocalPortRange) {
		this.tcpDiscoveryLocalPortRange = tcpDiscoveryLocalPortRange;
	}
	public String getCommunicationSpiLocalAddress() {
		return communicationSpiLocalAddress;
	}
	public void setCommunicationSpiLocalAddress(String communicationSpiLocalAddress) {
		this.communicationSpiLocalAddress = communicationSpiLocalAddress;
	}
	public int getCommunicationSpiLocalPort() {
		return communicationSpiLocalPort;
	}
	public void setCommunicationSpiLocalPort(int communicationSpiLocalPort) {
		this.communicationSpiLocalPort = communicationSpiLocalPort;
	}
	public int getCommunicationSpiSlowClientQueueLimit() {
		return communicationSpiSlowClientQueueLimit;
	}
	public void setCommunicationSpiSlowClientQueueLimit(int communicationSpiSlowClientQueueLimit) {
		this.communicationSpiSlowClientQueueLimit = communicationSpiSlowClientQueueLimit;
	}
	public List<CacheConfig> getCaches() {
		return caches;
	}
	public void setCaches(List<CacheConfig> caches) {
		this.caches = caches;
	}
	@Override
	public String toString() {
		return "IgniteProperties [enabled=" + enabled + ", instanceName=" + instanceName + ", peerClassLoadingEnabled="
				+ peerClassLoadingEnabled + ", localHost=" + localHost + ", ipfinderAddresses=" + ipfinderAddresses
				+ ", tcpDiscoveryLocalPort=" + tcpDiscoveryLocalPort + ", tcpDiscoveryLocalPortRange="
				+ tcpDiscoveryLocalPortRange + ", communicationSpiLocalAddress=" + communicationSpiLocalAddress
				+ ", communicationSpiLocalPort=" + communicationSpiLocalPort + ", communicationSpiSlowClientQueueLimit="
				+ communicationSpiSlowClientQueueLimit + ", caches=" + caches + "]";
	}
}