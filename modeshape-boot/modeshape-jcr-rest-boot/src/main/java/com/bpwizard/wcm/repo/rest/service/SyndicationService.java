package com.bpwizard.wcm.repo.rest.service;

import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.Syndication;
import com.bpwizard.wcm.repo.rest.jcr.model.UpdateSyndicationRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmServer;

@Service
@Transactional	
public class SyndicationService {
	@Autowired
	@Qualifier("modeshapeJdbcTemplate")
	JdbcTemplate jdbcTemplate;
	
//	@Autowired
//	WcmServerRepo wcmServerRepo;
	
	SimpleJdbcInsert simpleJdbcInsert;
	
	
	private static final String updateSql = "UPDATE SYN_SYNDICATION SET LAST_SYNDICATION = ? WHERE id = ?";
	private static final String deleteSql = "DELETE SYN_SYNDICATION WHERE id = ?";
	private static final String selectByIdSql = "SELECT s.ID as ID, a.LAST_SYNDICATION LAST_SYNDICATION, w.ID as COLLECTOR_ID, w.HOST as COLLECTOR_HOST, w.PORT as COLLECTOR_PORT from SYN_SYNDICATION as s JOIN SYN_WCM_SERVER as w ON s.COLLECTOR_ID = w.ID WHERE s.id = :id";
	private static final String selectByAllSql = "SELECT s.ID as ID, a.LAST_SYNDICATION LAST_SYNDICATION, w.ID as COLLECTOR_ID, w.HOST as COLLECTOR_HOST, w.PORT as COLLECTOR_PORT from SYN_SYNDICATION as s JOIN SYN_WCM_SERVER as w ON s.COLLECTOR_ID = w.ID";
	
	//  Stored procedure
	//	SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dataSource)
	//            .withProcedureName("READ_SYN_WCM_SERVER");
	// SqlParameterSource in = new MapSqlParameterSource().addValue("in_id", id);
    // Map<String, Object> out = simpleJdbcCall.execute(in);
	
	@PostConstruct
	private void postConstruct() {
		simpleJdbcInsert = 
			new SimpleJdbcInsert(jdbcTemplate).withTableName("SYN_SYNDICATION").usingGeneratedKeyColumns("ID");
	};
	
	public List<Syndication> getSyndications() {
		List<Syndication> Syndications = jdbcTemplate.query(selectByIdSql, new SyndicationRowMapper());
		return Syndications;
	}
	
	public Syndication getSyndication(Long id) {
		// return wcmServerRepo.findById(id);
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", 1);
		Syndication syndication = jdbcTemplate.queryForObject(
				selectByAllSql, new SyndicationRowMapper(), namedParameters);
				// selectByIdSql, new Object[] { id }, new WcmServerRowMapper());

		return syndication;
	}
	
	public Long createSyndicator(
			Syndication syndication) {
		Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("COLLECTOR_ID", syndication.getCollector().getId());
	    return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
	}
	
	public int updateSyndicator(
			UpdateSyndicationRequest syndicationRequest) 
			throws WcmRepositoryException {
		Object[] params = { syndicationRequest.getLastSyndication(), syndicationRequest.getSyndicationId()};
	    int[] types = {Types.TIMESTAMP, Types.BIGINT};
	    return jdbcTemplate.update(updateSql, params, types);
	}
	
	public int deleteSyndicator(Long id) {
		Object[] params = { id};
	    int[] types = {Types.BIGINT};
	    return jdbcTemplate.update(deleteSql, params, types);
	}
	
	public class SyndicationRowMapper implements RowMapper<Syndication> {
	    @Override
	    public Syndication mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	Syndication syndication = new Syndication();
	 
	    	WcmServer wcmServer = new WcmServer();
	    	syndication.setCollector(wcmServer);
	    	syndication.setId(rs.getLong("ID"));
	    	syndication.setLastSyndication(rs.getTimestamp("LAST_SYNDICATION"));
	    	wcmServer.setId(rs.getLong("COLLECTOR_ID"));
	    	wcmServer.setHost(rs.getString("COLLECTOR_HOST"));
	    	wcmServer.setPort(rs.getInt("COLLECTOR_PORT"));
	 
	        return syndication;
	    }
	}
}
