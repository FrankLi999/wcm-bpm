package com.bpwizard.wcm.repo.rest.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpwizard.spring.boot.commons.web.util.WebUtils;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.Collector;
import com.bpwizard.wcm.repo.rest.jcr.model.UpdateCollectorRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmServer;

@Service
@Transactional	
public class CollectorRepository {
	@Autowired
	@Qualifier("modeshapeJdbcTemplate")
	JdbcTemplate jdbcTemplate;
	
	SimpleJdbcInsert simpleJdbcInsert;
	
	private static final String selectColumns = "SELECT c.ID as ID, c.LAST_SYNDICATION LAST_SYNDICATION, c.created_by as CREATED_BY, c.updated_by as UPDATED_BY, c.created_at as CREATED_AT, c.updated_at as UPDATED_AT, w.ID as SYNDICATOR_ID, w.HOST as SYNDICATOR_HOST, w.PORT as SYNDICATOR_PORT";
	private static final String updateSql = "UPDATE SYN_COLLECTOR SET LAST_SYNDICATION = ?, updated_by=? WHERE id = ?";
	private static final String deleteSql = "DELETE SYN_COLLECTOR WHERE id = ?";
	private static final String selectByIdSql = String.format("%s from SYN_COLLECTOR as c JOIN SYN_WCM_SERVER as w ON c.SYNDICATOR_ID = w.ID WHERE s.id = ?", selectColumns);
	private static final String selectByAllSql = String.format("%s  from SYN_COLLECTOR as c JOIN SYN_WCM_SERVER as w ON c.SYNDICATOR_ID = w.ID", selectColumns);
	
	@PostConstruct
	private void postConstruct() {
		simpleJdbcInsert = 
			new SimpleJdbcInsert(jdbcTemplate).withTableName("SYN_COLLECTOR").usingGeneratedKeyColumns("ID");
	};
	
	public List<Collector> getCollectors() {
		List<Collector> collectors = jdbcTemplate.query(selectByAllSql, new CollectorRowMapper());
		return collectors;
	}
	
	public Collector getCollector(Long id) {
		Object[] args = { id }; 
		int[] argTypes = { Types.BIGINT };
		Collector collector = jdbcTemplate.queryForObject(selectByIdSql, args, argTypes, new CollectorRowMapper());

		return collector;
	}
	
	public Long createCollector(
			Collector collector) {
		Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("SYNDICATOR_ID", collector.getSyndicator().getId());
	    parameters.put("created_by", WebUtils.currentUserId());
	    parameters.put("created_at", new Timestamp(System.currentTimeMillis()));
	    // TODO
	    parameters.put("LAST_SYNDICATION", "1970-02-01 00:00:00");
	    return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
	}
	
	public int updateColelctor(
			UpdateCollectorRequest syndicationRequest) 
			throws WcmRepositoryException {
		Object[] params = { syndicationRequest.getLastSyndication(), syndicationRequest.getCollectorId(), WebUtils.currentUserId()};
	    int[] types = {Types.TIMESTAMP, Types.BIGINT, Types.VARCHAR};
	    return jdbcTemplate.update(updateSql, params, types);
	}
	
	public int deleteCollector(Long id) {
		Object[] params = { id};
	    int[] types = {Types.BIGINT};
	    return jdbcTemplate.update(deleteSql, params, types);
	}
	
	public class CollectorRowMapper implements RowMapper<Collector> {
	    @Override
	    public Collector mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	Collector collector = new Collector();
	 
	    	WcmServer wcmServer = new WcmServer();
	    	collector.setSyndicator(wcmServer);
	    	collector.setLastSyndication(rs.getTimestamp("LAST_SYNDICATION"));
	    	collector.setId(rs.getLong("ID"));
	    	collector.setCreatedBy(rs.getString("CREATED_BY"));
	    	collector.setUpdatedBy(rs.getString("UPDATED_BY"));
	    	collector.setCreatedAt(rs.getTimestamp("CREATED_AT"));
	    	collector.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));
	    	wcmServer.setId(rs.getLong("SYNDICATOR_ID"));
	    	wcmServer.setHost(rs.getString("SYNDICATOR_HOST"));
	    	wcmServer.setPort(rs.getInt("SYNDICATOR_PORT"));
	 
	        return collector;
	    }
	}
	
	
}
