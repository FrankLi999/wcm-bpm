package com.bpwizard.spring.boot.commons.cache.ignite;

public class CacheConfig {
	private String atomicityMode;
	private String cacheMode;
	private String cacheName;
	private boolean writeThrough;
	private boolean readThrough;
	private boolean writeBehindEnabled;
	private int backups;
	private boolean statisticsEnabled;
	private long writeBehindFlushFrequency;	
	private boolean initSchema;
	private String dataSourceBean;
	private String createTableQuery;
    private String loadQuery;
    private String updateQuery;
	private String insertQuery;
	private String deleteQuery;
	
	public String getAtomicityMode() {
		return atomicityMode;
	}
	public void setAtomicityMode(String atomicityMode) {
		this.atomicityMode = atomicityMode;
	}
	public String getCacheMode() {
		return cacheMode;
	}
	public void setCacheMode(String cacheMode) {
		this.cacheMode = cacheMode;
	}
	public String getCacheName() {
		return cacheName;
	}
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	public boolean isWriteThrough() {
		return writeThrough;
	}
	public void setWriteThrough(boolean writeThrough) {
		this.writeThrough = writeThrough;
	}
	public boolean isReadThrough() {
		return readThrough;
	}
	public void setReadThrough(boolean readThrough) {
		this.readThrough = readThrough;
	}
	public boolean isWriteBehindEnabled() {
		return writeBehindEnabled;
	}
	public void setWriteBehindEnabled(boolean writeBehindEnabled) {
		this.writeBehindEnabled = writeBehindEnabled;
	}
	public int getBackups() {
		return backups;
	}
	public void setBackups(int backups) {
		this.backups = backups;
	}
	public boolean isStatisticsEnabled() {
		return statisticsEnabled;
	}
	public void setStatisticsEnabled(boolean statisticsEnabled) {
		this.statisticsEnabled = statisticsEnabled;
	}
	public long getWriteBehindFlushFrequency() {
		return writeBehindFlushFrequency;
	}
	public void setWriteBehindFlushFrequency(long writeBehindFlushFrequency) {
		this.writeBehindFlushFrequency = writeBehindFlushFrequency;
	}
	public boolean isInitSchema() {
		return initSchema;
	}
	public void setInitSchema(boolean initSchema) {
		this.initSchema = initSchema;
	}
	public String getDataSourceBean() {
		return dataSourceBean;
	}
	public void setDataSourceBean(String dataSourceBean) {
		this.dataSourceBean = dataSourceBean;
	}
	public String getCreateTableQuery() {
		return createTableQuery;
	}
	public void setCreateTableQuery(String createTableQuery) {
		this.createTableQuery = createTableQuery;
	}
	public String getLoadQuery() {
		return loadQuery;
	}
	public void setLoadQuery(String loadQuery) {
		this.loadQuery = loadQuery;
	}
	public String getUpdateQuery() {
		return updateQuery;
	}
	public void setUpdateQuery(String updateQuery) {
		this.updateQuery = updateQuery;
	}
	public String getInsertQuery() {
		return insertQuery;
	}
	public void setInsertQuery(String insertQuery) {
		this.insertQuery = insertQuery;
	}
	public String getDeleteQuery() {
		return deleteQuery;
	}
	public void setDeleteQuery(String deleteQuery) {
		this.deleteQuery = deleteQuery;
	}
	@Override
	public String toString() {
		return "CacheConfig [atomicityMode=" + atomicityMode + ", cacheMode=" + cacheMode + ", cacheName=" + cacheName
				+ ", writeThrough=" + writeThrough + ", readThrough=" + readThrough + ", writeBehindEnabled="
				+ writeBehindEnabled + ", backups=" + backups + ", statisticsEnabled=" + statisticsEnabled
				+ ", writeBehindFlushFrequency=" + writeBehindFlushFrequency + ", initSchema=" + initSchema
				+ ", dataSourceBean=" + dataSourceBean + ", createTableQuery=" + createTableQuery + ", loadQuery="
				+ loadQuery + ", updateQuery=" + updateQuery + ", insertQuery=" + insertQuery + ", deleteQuery="
				+ deleteQuery + "]";
	}
}
