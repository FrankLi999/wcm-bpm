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

import com.bpwizard.wcm.repo.rest.jcr.model.WcmServer;


@Service
@Transactional	
public class WcmServerService {
	@Autowired
	@Qualifier("modeshapeJdbcTemplate")
	JdbcTemplate jdbcTemplate;
	
//	@Autowired
//	WcmServerRepo wcmServerRepo;
	
	SimpleJdbcInsert simpleJdbcInsert;
	
	private static final String updateSql = "UPDATE SYN_WCM_SERVER SET HOST = ?, PORT = ? WHERE id = ?";
	private static final String deleteSql = "DELETE SYN_WCM_SERVER WHERE id = ?";
	private static final String selectByIdSql = "Select * from SYN_WCM_SERVER WHERE id = :id";
	private static final String selectByAllSql = "Select * from SYN_WCM_SERVER";

	
	//  Stored procedure
	//	SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dataSource)
	//            .withProcedureName("READ_SYN_WCM_SERVER");
	// SqlParameterSource in = new MapSqlParameterSource().addValue("in_id", id);
    // Map<String, Object> out = simpleJdbcCall.execute(in);
	
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
		// return wcmServerRepo.findById(id);
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", 1);
		WcmServer wcmServer = jdbcTemplate.queryForObject(
				selectByIdSql, new WcmServerRowMapper(), namedParameters);
				// selectByIdSql, new Object[] { id }, new WcmServerRowMapper());

		return wcmServer;
	}
	
	public Long createWcmServer(
			WcmServer wcmServer)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("HOST", wcmServer.getHost());
	    parameters.put("PORT", wcmServer.getPort());
	    return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
	}
	
	public int UpdateWcmServer(
			WcmServer wcmServer) {
		// wcmServerRepo.save(wcmServer);
		Object[] params = { wcmServer.getHost(), wcmServer.getPort(), wcmServer.getId()};
	    int[] types = {Types.VARCHAR, Types.BIGINT, Types.BIGINT};
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
	 
	        return wcmServer;
	    }
	}
	
//	public int[] batchUpdateUsingJdbcTemplate(List<Employee> employees) {
//	    return jdbcTemplate.batchUpdate("INSERT INTO EMPLOYEE VALUES (?, ?, ?, ?)",
//	        new BatchPreparedStatementSetter() {
//	            @Override
//	            public void setValues(PreparedStatement ps, int i) throws SQLException {
//	                ps.setInt(1, employees.get(i).getId());
//	                ps.setString(2, employees.get(i).getFirstName());
//	                ps.setString(3, employees.get(i).getLastName());
//	                ps.setString(4, employees.get(i).getAddress();
//	            }
//	            @Override
//	            public int getBatchSize() {
//	                return 50;
//	            }
//	        });
//	}
	
	
//	SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(employees.toArray());
//	int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(
//	    "INSERT INTO EMPLOYEE VALUES (:id, :firstName, :lastName, :address)", batch);
//	return updateCounts;
}
