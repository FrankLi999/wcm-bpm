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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpwizard.spring.boot.commons.web.util.WebUtils;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.Syndicator;
import com.bpwizard.wcm.repo.rest.jcr.model.UpdateSyndicationRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmServer;

@Service
@Transactional	
public class SyndicatorService {
	@Autowired
	@Qualifier("modeshapeJdbcTemplate")
	JdbcTemplate jdbcTemplate;
	
	SimpleJdbcInsert simpleJdbcInsert;
	
	private static final String selectColumns = "SELECT s.ID as ID, s.LAST_SYNDICATION LAST_SYNDICATION, s.created_by as CREATED_BY, s.updated_by as UPDATED_BY, s.created_at as CREATED_AT, s.updated_at as UPDATED_AT, w.ID as COLLECTOR_ID, w.HOST as COLLECTOR_HOST, w.PORT as COLLECTOR_PORT";
	private static final String updateSql = "UPDATE SYN_SYNDICATOR SET LAST_SYNDICATION = ? WHERE id = ?";
	private static final String deleteSql = "DELETE SYN_SYNDICATOR WHERE id = ?";
	private static final String selectByIdSql = String.format("%s from SYN_SYNDICATOR as s JOIN SYN_WCM_SERVER as w ON s.COLLECTOR_ID = w.ID WHERE s.id = :id", selectColumns);
	private static final String selectByAllSql = String.format("%s   from SYN_SYNDICATOR as s JOIN SYN_WCM_SERVER as w ON s.COLLECTOR_ID = w.ID", selectColumns);

	@PostConstruct
	private void postConstruct() {
		simpleJdbcInsert = 
			new SimpleJdbcInsert(jdbcTemplate).withTableName("SYN_SYNDICATION").usingGeneratedKeyColumns("ID");
	};
	
	public List<Syndicator> getSyndicators() {
		List<Syndicator> Syndications = jdbcTemplate.query(selectByIdSql, new SyndicationRowMapper());
		return Syndications;
	}
	
	public Syndicator getSyndicator(Long id) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
		Syndicator syndication = jdbcTemplate.queryForObject(
				selectByAllSql, new SyndicationRowMapper(), namedParameters);

		return syndication;
	}
	
	public Long createSyndicator(
			Syndicator syndication) {
		Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("COLLECTOR_ID", syndication.getCollector().getId());
	    parameters.put("created_by", WebUtils.currentUserId());
	    return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
	}
	
	public int updateSyndicator(
			UpdateSyndicationRequest syndicationRequest) 
			throws WcmRepositoryException {
		Object[] params = { syndicationRequest.getLastSyndication(), syndicationRequest.getSyndicationId(), WebUtils.currentUserId(), new Timestamp(System.currentTimeMillis())};
	    int[] types = {Types.TIMESTAMP, Types.BIGINT, Types.VARCHAR, Types.TIMESTAMP};
	    return jdbcTemplate.update(updateSql, params, types);
	}
	
	public int deleteSyndicator(Long id) {
		Object[] params = { id };
	    int[] types = {Types.BIGINT};
	    return jdbcTemplate.update(deleteSql, params, types);
	}
	
	public class SyndicationRowMapper implements RowMapper<Syndicator> {
	    @Override
	    public Syndicator mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	Syndicator syndication = new Syndicator();
	 
	    	WcmServer wcmServer = new WcmServer();
	    	syndication.setCollector(wcmServer);
	    	syndication.setId(rs.getLong("ID"));
	    	syndication.setLastSyndication(rs.getTimestamp("LAST_SYNDICATION"));
	    	syndication.setCreatedBy(rs.getString("CREATED_BY"));
	    	syndication.setUpdatedBy(rs.getString("UPDATED_BY"));
	    	syndication.setCreatedAt(rs.getTimestamp("CREATED_AT"));
	    	syndication.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));
	    	wcmServer.setId(rs.getLong("COLLECTOR_ID"));
	    	wcmServer.setHost(rs.getString("COLLECTOR_HOST"));
	    	wcmServer.setPort(rs.getInt("COLLECTOR_PORT"));
	 
	        return syndication;
	    }
	}
}