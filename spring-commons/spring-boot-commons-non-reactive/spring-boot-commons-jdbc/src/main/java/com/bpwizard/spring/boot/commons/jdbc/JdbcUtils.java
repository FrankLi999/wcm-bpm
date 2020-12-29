package com.bpwizard.spring.boot.commons.jdbc;

import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.bpwizard.spring.boot.commons.exceptions.VersionException;
import com.bpwizard.spring.boot.commons.util.EnviornmentUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@SuppressWarnings("deprecation")
public class JdbcUtils {

	/**
	 * A convenient method for running code
	 * after successful database commit.
	 *  
	 * @param runnable
	 */
	public static void afterCommit(Runnable runnable) {
		
		TransactionSynchronizationManager.registerSynchronization(
		    new TransactionSynchronizationAdapter() {
		        @Override
		        public void afterCommit() {
		        	
		        	runnable.run();
		        }
		});				
	}


	/**
	 * Throws a VersionException if the versions of the
	 * given entities aren't same.
	 * 
	 * @param original
	 * @param updated
	 */
	public static <ID extends Serializable>
	void ensureCorrectVersion(SpringEntity<ID> original, SpringEntity<ID> updated) {
		
		if (original.getVersion() != updated.getVersion())
			throw new VersionException(original.getClass().getSimpleName(), original.getId().toString());
	}
	
	public static String sqlFragment(Pageable pageable) {
		StringBuilder sqlFragment = new StringBuilder("");
		if (pageable.getSort().isSorted()) {
			sqlFragment.append("ORDER BY ");
			int count = 0;
			for (Order order: pageable.getSort().toList()) {
				if (count > 0) {
					sqlFragment.append(", ");
				}
				sqlFragment.append(order.getProperty()).append(" ").append(order.getDirection().name());
				count++;
			}
		}
		if (pageable.isPaged()) {
			sqlFragment.append(" LIMIT").append(pageable.getPageSize()).append(" OFFSET").append(pageable.getOffset());
		}
		return sqlFragment.toString();
	}
	
	public static DataSource getDataSource(String propertyPrefix, Environment env) {
		HikariConfig config = new HikariConfig();
        config.setJdbcUrl(env.getProperty(String.format("%s.%s", propertyPrefix, "url")));
        config.setDriverClassName(env.getProperty(String.format("%s.%s", propertyPrefix, "driver-class-name")));
        config.setUsername(env.getProperty(String.format("%s.%s", propertyPrefix, "username")));
        config.setPassword(env.getProperty(String.format("%s.%s", propertyPrefix, "password")));
       
        config.setConnectionTestQuery(env.getProperty(String.format("%s.hikari.%s", propertyPrefix, "connection-test-query")));
        config.setIdleTimeout(EnviornmentUtils.getLong(env, String.format("%s.hikari.%s", propertyPrefix, "idle-timeout"), 600000));
        config.setMaxLifetime(EnviornmentUtils.getLong(env, String.format("%s.hikari.%s", propertyPrefix, "max-lifetime"), 1800000)); 
        config.setMinimumIdle(EnviornmentUtils.getInt(env, String.format("%s.hikari.%s", propertyPrefix, "minimum-idle"), 5)); 
        config.setMaximumPoolSize(EnviornmentUtils.getInt(env, String.format("%s.hikari.%s", propertyPrefix, "maximum-pool-size"), 5));
        config.setConnectionTimeout(EnviornmentUtils.getLong(env, String.format("%s.hikari.%s", propertyPrefix, "connection-timeout"), 30000));
        
        HikariDataSource hikariDS = new HikariDataSource(config);
        return hikariDS;
	}
}
