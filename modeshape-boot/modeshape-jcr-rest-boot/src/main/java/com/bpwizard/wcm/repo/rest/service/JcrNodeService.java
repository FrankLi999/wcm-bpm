package com.bpwizard.wcm.repo.rest.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import com.bpwizard.wcm.repo.rest.jcr.model.JcrNode;

@Service
@Transactional
public class JcrNodeService {
	private static final String selectJcrNode = "SELECT * FROM modeshape_repository";
	private static final String updateJcrNode = "INSERT INTO modeshape_repository(ID, LAST_CHANGED, CONTENT) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE LAST_CHANGED=?, CONTENT=?";
	private static final String deleteJcrNode = "DELETE FROM modeshape_repository where ID = ?";
	
	@Autowired
	@Qualifier("modeshapeJdbcTemplate")
	JdbcTemplate jdbcTemplate;
	
	SimpleJdbcInsert simpleJdbcInsert; 
	
	@PostConstruct
	private void postConstruct() {
		simpleJdbcInsert = 
			new SimpleJdbcInsert(jdbcTemplate).withTableName("modeshape_repository").usingGeneratedKeyColumns("ID");
	};
	
	public JcrNode getJcrNode(String id) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
		JcrNode jcrNode = jdbcTemplate.queryForObject(selectJcrNode, new JcrNodeRowMapper(), namedParameters);
		return jcrNode;
	}
	
	public Long addJcrNode(JcrNode jcrNode) {
		Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("ID", jcrNode.getId());
	    parameters.put("LAST_CHANGED", jcrNode.getLastUpdated());
	    parameters.put("CONTENT", jcrNode.getContent());
	    return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
	}
	
	public int updateJcrNode(JcrNode jcrNode) {
		Object[] params = { jcrNode.getId(), jcrNode.getLastUpdated(), jcrNode.getContent(), jcrNode.getLastUpdated(), jcrNode.getContent()};
	    int[] types = {Types.VARCHAR,Types.TIMESTAMP, Types.BLOB, Types.TIMESTAMP, Types.BLOB};
	    return jdbcTemplate.update(updateJcrNode, params, types);
	}

	public int deleteJcrNode(String id) {
		Object[] params = { id };
	    int[] types = {Types.BIGINT};
	    return jdbcTemplate.update(deleteJcrNode, params, types);
	}
	
	public class JcrNodeRowMapper implements RowMapper<JcrNode> {
	    @Override
	    public JcrNode mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	JcrNode jcrNode = new JcrNode();
	 
	    	jcrNode.setId(rs.getString("ID"));
	    	jcrNode.setLastUpdated(rs.getTimestamp("LAST_CHANGED"));
	    	jcrNode.setContent(rs.getBlob("Content").getBinaryStream());
	 
	        return jcrNode;
	    }
	}
}
