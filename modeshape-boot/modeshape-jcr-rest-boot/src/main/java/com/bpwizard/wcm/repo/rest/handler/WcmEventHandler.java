package com.bpwizard.wcm.repo.rest.handler;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;

@Component
public class WcmEventHandler extends AbstractHandler {

	@Autowired(required = false)
	@Qualifier("modeshapeJdbcTemplate")
	protected JdbcTemplate jdbcTemplate;

	public static void clearWcmEventBefore(Timestamp timestamp) {

	}

	public void addWcmEvent(WcmEvent event) {

	}

	public void updateWcmEvent(WcmEvent event) {

	}
	
	public List<WcmEvent> getWcmEventAfter(
			Timestamp startTimestamp, 
			Timestamp endTimestamp, 
			int pageIndex,
			int pageSize) {

		return null;
	}

}
