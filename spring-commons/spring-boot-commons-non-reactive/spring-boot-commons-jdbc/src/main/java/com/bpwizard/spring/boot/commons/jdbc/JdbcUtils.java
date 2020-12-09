package com.bpwizard.spring.boot.commons.jdbc;

import java.io.Serializable;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.bpwizard.spring.boot.commons.exceptions.VersionException;

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
}
