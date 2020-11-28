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
import com.bpwizard.wcm.repo.rest.jcr.model.Collector;
import com.bpwizard.wcm.repo.rest.jcr.model.UpdateSyndicationRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmServer;

@Service
@Transactional	
public class CollectorService {
	@Autowired
	@Qualifier("modeshapeJdbcTemplate")
	JdbcTemplate jdbcTemplate;
	
//	@Autowired
//	WcmServerRepo wcmServerRepo;
	
	SimpleJdbcInsert simpleJdbcInsert;
	
	
	private static final String updateSql = "UPDATE SYN_COLLECTOR SET LAST_SYNDICATION = ? WHERE id = ?";
	private static final String deleteSql = "DELETE SYN_COLLECTOR WHERE id = ?";
	private static final String selectByIdSql = "SELECT c.ID as ID, c.LAST_SYNDICATION LAST_SYNDICATION, w.ID as SYNDICATOR_ID, w.HOST as SYNDICATOR_HOST, w.PORT as SYNDICATOR_PORT from SYN_COLLECTOR as c JOIN SYN_WCM_SERVER as w ON c.SYNDICATOR_ID = w.ID WHERE s.id = :id";
	private static final String selectByAllSql = "SELECT c.ID as ID, c.LAST_SYNDICATION LAST_SYNDICATION, w.ID as SYNDICATOR_ID, w.HOST as SYNDICATOR_HOST, w.PORT as SYNDICATOR_PORT from SYN_COLLECTOR as c JOIN SYN_WCM_SERVER as w ON c.SYNDICATOR_ID = w.ID";
	
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
	
	public List<Collector> getSyndications() {
		List<Collector> collectors = jdbcTemplate.query(selectByAllSql, new CollectorRowMapper());
		return collectors;
	}
	
	public Collector getSyndication(Long id) {
		// return wcmServerRepo.findById(id);
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", 1);
		Collector collector = jdbcTemplate.queryForObject(
				selectByIdSql, new CollectorRowMapper(), namedParameters);
				// selectByIdSql, new Object[] { id }, new WcmServerRowMapper());

		return collector;
	}
	
	public Long createCollector(
			Collector collector) {
		Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("SYNDICATOR_ID", collector.getSyndicator().getId());
	    return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
	}
	
	public int updateColelctor(
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
	
	public class CollectorRowMapper implements RowMapper<Collector> {
	    @Override
	    public Collector mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	Collector collector = new Collector();
	 
	    	WcmServer wcmServer = new WcmServer();
	    	collector.setSyndicator(wcmServer);
	    	collector.setLastSyndication(rs.getTimestamp("LAST_SYNDICATION"));
	    	collector.setId(rs.getLong("ID"));
	    	wcmServer.setId(rs.getLong("SYNDICATOR_ID"));
	    	wcmServer.setHost(rs.getString("SYNDICATOR_HOST"));
	    	wcmServer.setPort(rs.getInt("SYNDICATOR_PORT"));
	 
	        return collector;
	    }
	}
}