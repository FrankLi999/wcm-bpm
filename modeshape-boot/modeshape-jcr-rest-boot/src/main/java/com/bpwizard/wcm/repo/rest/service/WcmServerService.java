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
import com.bpwizard.wcm.repo.rest.jcr.model.WcmServer;


@Service
@Transactional	
public class WcmServerService {
	@Autowired
	@Qualifier("modeshapeJdbcTemplate")
	JdbcTemplate jdbcTemplate;
	
	SimpleJdbcInsert simpleJdbcInsert;
	
	private static final String updateSql = "UPDATE SYN_WCM_SERVER SET HOST = ?, PORT = ?, updated_by=? WHERE id = ?";
	private static final String deleteSql = "DELETE FROM SYN_WCM_SERVER WHERE id = ?";
	private static final String selectByIdSql = "Select * from SYN_WCM_SERVER WHERE id = ?";
	private static final String selectByAllSql = "Select * from SYN_WCM_SERVER";

	@PostConstruct
	private void postConstruct() {
		simpleJdbcInsert = 
			new SimpleJdbcInsert(jdbcTemplate).withTableName("SYN_WCM_SERVER").usingGeneratedKeyColumns("ID");
	};
	
	public List<WcmServer> getWcmServers() {
		List<WcmServer> wcmServers = jdbcTemplate.query(selectByAllSql, new WcmServerRowMapper());
		return wcmServers;
	}
	
	public WcmServer getWcmServer(Long id) {
		Object[] args = { id }; 
		int[] argTypes = { Types.BIGINT };
		WcmServer wcmServer = jdbcTemplate.queryForObject(selectByIdSql, args, argTypes, new WcmServerRowMapper());

		return wcmServer;
	}
	
	public Long createWcmServer(
			WcmServer wcmServer)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("HOST", wcmServer.getHost());
	    parameters.put("PORT", wcmServer.getPort());
	    parameters.put("created_by", WebUtils.currentUserId());
	    parameters.put("created_at", new Timestamp(System.currentTimeMillis()));
	    
	    return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
	}
	
	public int UpdateWcmServer(
			WcmServer wcmServer) {
		Object[] params = { wcmServer.getHost(), wcmServer.getPort(), wcmServer.getId(), WebUtils.currentUserId()};
	    int[] types = {Types.VARCHAR, Types.BIGINT, Types.BIGINT, Types.VARCHAR};
	    return jdbcTemplate.update(updateSql, params, types);
	}
	
	public int deleteWcmServer(
			Long id) {
		Object[] params = { id};
	    int[] types = {Types.BIGINT};
	    return jdbcTemplate.update(deleteSql, params, types);
	}
	
	public class WcmServerRowMapper implements RowMapper<WcmServer> {
	    @Override
	    public WcmServer mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	WcmServer wcmServer = new WcmServer();
	 
	    	wcmServer.setId(rs.getLong("ID"));
	    	wcmServer.setHost(rs.getString("HOST"));
	    	wcmServer.setPort(rs.getInt("PORT"));
	    	wcmServer.setCreatedBy(rs.getString("CREATED_BY"));
	    	wcmServer.setUpdatedBy(rs.getString("UPDATED_BY"));
	    	wcmServer.setCreatedAt(rs.getTimestamp("CREATED_AT"));
	    	wcmServer.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));
	        return wcmServer;
	    }
	}
}
