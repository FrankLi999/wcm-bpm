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
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import com.bpwizard.wcm.repo.rest.jcr.model.JcrNode;
import com.bpwizard.wcm.repo.rest.jcr.model.JcrNodeEntry;

@Service
@Transactional
public class JcrNodeRepository {
	private static final String selectJcrNode = "SELECT * FROM MODESHAPE_REPOSITORY WHERE ID = ?";
	private static final String updateJcrNode = "INSERT INTO MODESHAPE_REPOSITORY(ID, LAST_CHANGED, CONTENT) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE LAST_CHANGED=?, CONTENT=?";
	private static final String deleteJcrNode = "DELETE FROM MODESHAPE_REPOSITORY where ID = ?";

	@Autowired
	@Qualifier("modeshapeJdbcTemplate")
	JdbcTemplate jdbcTemplate;
	
	SimpleJdbcInsert simpleJdbcInsert; 
	
	@PostConstruct
	private void postConstruct() {
		simpleJdbcInsert = 
			new SimpleJdbcInsert(jdbcTemplate).withTableName("MODESHAPE_REPOSITORY");// .usingGeneratedKeyColumns("ID");
	};
	
	public JcrNode getJcrNode(String id) {
		Object[] params = { id };
	    int[] types = {Types.VARCHAR};
		JcrNode jcrNode = jdbcTemplate.queryForObject(selectJcrNode, params, types, new JcrNodeRowMapper());
		return jcrNode;
	}
	
	public String addJcrNode(JcrNodeEntry jcrNode) {
		Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("ID", jcrNode.getId());
	    parameters.put("LAST_CHANGED", jcrNode.getLastUpdated());
	    parameters.put("CONTENT", jcrNode.getContent());
	    simpleJdbcInsert.execute(parameters);
	    return jcrNode.getId();
	}
	
	public int updateJcrNode(JcrNodeEntry jcrNode) {
		Object[] params = { jcrNode.getId(), jcrNode.getLastUpdated(), jcrNode.getContent(), jcrNode.getLastUpdated(), jcrNode.getContent()};
	    int[] types = {Types.VARCHAR, Types.TIMESTAMP,  Types.BLOB, Types.TIMESTAMP, Types.BLOB};
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
	        jcrNode.setContent(rs.getBytes("Content"));
	        return jcrNode;
	    }
	
	}
	
}
