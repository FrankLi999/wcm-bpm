package com.bpwizard.wcm.repo.rest.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.jcr.RepositoryException;

import org.modeshape.jcr.JcrSession;
import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.bpwizard.wcm.repo.rest.jcr.model.RootNodeKeys;

@Service
public class RootNodeKeyService {
	@Autowired protected RepositoryManager repositoryManager;
	
	@Autowired
	@Qualifier("modeshapeJdbcTemplate")
	JdbcTemplate jdbcTemplate;
	
	private RootNodeKeys rootNodeKeys = null;
	
	private static final String selectJcrNodeType = "SELECT ID FROM MODESHAPE_REPOSITORY WHERE ID like ?";
	
	public synchronized RootNodeKeys getRootNodeKeys(String repository, String workspace) throws RepositoryException {
		if (rootNodeKeys == null) {
			JcrSession jcrSession = (JcrSession) repositoryManager.getSession(repository, workspace);
			rootNodeKeys = new RootNodeKeys();
			rootNodeKeys.setRootNodeKey(jcrSession.getRootNodeKey());
			rootNodeKeys.setJcrSystemKey(this.getJcrSystemKey());
		}
		return rootNodeKeys;
	}

	
	private String getJcrSystemKey() {
		Object[] params = { "%jcr:nodeTypes" };
	    int[] types = {Types.VARCHAR};
		String  jcrSystemKey = jdbcTemplate.queryForObject(selectJcrNodeType, params, types, new JcrSystemKeyRowMapper());
		return jcrSystemKey.substring(0, jcrSystemKey.indexOf("jcr:nodeTypes"));
	}
	
	public class JcrSystemKeyRowMapper implements RowMapper<String> {
	    @Override
	    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	return rs.getString("ID");
	    }
	}
}
