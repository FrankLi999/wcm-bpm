package com.bpwizard.wcm.repo.syndication;

import java.io.IOException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.bpwizard.spring.boot.commons.security.JSONWebSignatureService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service("syndicationService")
public class SyndicationService {
	private static final String selectWcmEventsBeforeSql = "SELECT * FROM SYN_WCM_EVENT WHERE library= :library and timeCcreated > :begin and timeCcreated < :end ORDER BY timeCcreated ASC LIMIT :pageSize OFFSET :offset";
	private static final String selectCndBeforeSql = "SELECT * FROM SYN_WCM_EVENT WHERE ITEMTYPE= 'cnd' and timeCcreated > :begin and timeCcreated < :end ORDER BY timeCcreated ASC";
	private static final String selectJcrNode = "SELECT * FROM modeshape_repository";
	private static final String updateJcrNode = "INSERT INTO modeshape_repository(ID, LAST_CHANGED, CONTENT) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE LAST_CHANGED=?, CONTENT=?";
	private static final String deleteJcrNode = "DELETE FROM modeshape_repository where ID = ?";

	@Autowired
	private JSONWebSignatureService jwsTokenService;
	
	RestTemplate restTemplate; // = new RestTemplate();
	
    public void syndicate(Syndication syndication) throws Exception {
    	System.out.println("/xxxxxxxxxx syndication");
    	System.out.println(syndication);
    	String token = getShortLivedAuthToken(syndication.getImportUser());
    	Timestamp endTimestamp = new Timestamp(System.currentTimeMillis());
    	 DataSource authoringDs = DataSourceBuilder.create()
    		.url(syndication.getAuthoringDBUrl())
    	 	.driverClassName("com.mysql.jdbc.Driver")
    	 	.username(syndication.getAuthoringDBUser())
    	 	.password(syndication.getAuthoringDBPassword()).build();
    	 
    	 JdbcTemplate authoringServerJdbcTemplate = new JdbcTemplate(authoringDs);
    	 
    	 DataSource renderingDs = DataSourceBuilder.create()
			 .url(syndication.getRenderingDBUrl())
	    	 .driverClassName("com.mysql.jdbc.Driver")
	    	 .username(syndication.getRenderingDBUser())
	    	 .password(syndication.getRenderingDBPassword()).build();
    	 JdbcTemplate renderingServerJdbcTemplate = new JdbcTemplate(renderingDs);
    	 
    	 
    	 List<WcmEvent> cndEvents = getCndAfter(authoringServerJdbcTemplate, syndication.getLastSyndication(), endTimestamp);
    	 for (WcmEvent cndEvent: cndEvents) {
    		 HttpHeaders headers = new HttpHeaders();
    		 headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    		 headers.setBearerAuth(token);
    		 MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
 			 body.add("file", new InputStreamResource(cndEvent.getContent()));
 			 HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
 			 String serverUrl = String.format("%s/wcm/api/import/%s/%s/nodetypes", syndication.getImportServiceUrl(), syndication.getRepository(), syndication.getWorkspace());
 			 restTemplate.postForEntity(serverUrl, requestEntity, Void.class);
    	 }

    	int pageSize = 20;
 		int pageIndex = 0;
 		int actualBatchSize = 0;
 		List<WcmEvent> wcmEvents = null;
 		do {
 			
 			wcmEvents = getWcmEventAfter(authoringServerJdbcTemplate, syndication, endTimestamp, pageIndex, pageSize);
 			actualBatchSize = wcmEvents.size();
 			if (actualBatchSize > 0) {
 				for (WcmEvent wcmEvent: wcmEvents) {
 					
 					publish(syndication,
 							authoringServerJdbcTemplate, 
 							renderingServerJdbcTemplate, 
 							getJcrNode(authoringServerJdbcTemplate, 
 							wcmEvent.getId()), 
 							wcmEvent,
 							token);
 				}
 			}
 		} while (actualBatchSize >= pageSize);	
    	 
    }
    
    private List<WcmEvent> getCndAfter(
    		JdbcTemplate authoringJdbcTemplate,
    		Timestamp startTimestamp,
    		Timestamp endTimestamp) {
		
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("begin", startTimestamp)
				.addValue("end", endTimestamp);
		List<WcmEvent> wcmEvents = authoringJdbcTemplate.query(
				selectCndBeforeSql, new WcmEventRowMapper(), namedParameters);

		return wcmEvents;
	}
    
    private List<WcmEvent> getWcmEventAfter(
    		JdbcTemplate authoringJdbcTemplate,
			Syndication syndication,
			Timestamp endTimestamp, 
			int pageIndex,
			int pageSize) {
		
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("library", syndication.getLibrary())
				.addValue("begin", syndication.getLastSyndication())
				.addValue("end", endTimestamp)
				.addValue("pageSize", pageSize)
				.addValue("offset", pageSize * pageIndex);
		List<WcmEvent> wcmEvents = authoringJdbcTemplate.query(
				selectWcmEventsBeforeSql, new WcmEventRowMapper(), namedParameters);

		return wcmEvents;
	}
    
    private void publish(
    		Syndication syndication,
    		JdbcTemplate authoringJdbcTemplate, 
    		JdbcTemplate renderingJdbcTemplate, 
    		JcrNode jcrNode, 
    		WcmEvent wcmEvent,
    		String token) throws JsonProcessingException, IOException {
    	
    	ItemNode itemNode = eventToItemNode(wcmEvent, jcrNode);
    	for (String removedId: itemNode.getRemovedNodeIds()) {
    		deleteJcrNode(renderingJdbcTemplate, removedId);
    	}
    	
    	for (String updatedId: itemNode.getUpdatedNodeIds()) {
    		JcrNode updatedNode = this.getJcrNode(authoringJdbcTemplate, updatedId);
    		updateJcrNode(renderingJdbcTemplate, updatedNode);
    	}
    	
    	updateJcrNode(renderingJdbcTemplate, itemNode.getJcrNode());
    	if (WcmEvent.WcmItemType.authoringTemplate.equals(wcmEvent.getItemType())) {
    		HttpHeaders headers = new HttpHeaders();
   		 	headers.setBearerAuth(token);
   		
			HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
			String serverUrl = String.format("%s/wcm/api/at/%s/%s/jcrTpe?path=%s", syndication.getImportServiceUrl(), 
					syndication.getRepository(), syndication.getWorkspace(), wcmEvent.getNodePath());
			restTemplate.postForEntity(serverUrl, requestEntity, Void.class);
    	}
    }
    
    private JcrNode getJcrNode(JdbcTemplate authoringJdbcTemplate, String id) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
		JcrNode jcrNode = authoringJdbcTemplate.queryForObject(selectJcrNode, new JcrNodeRowMapper(), namedParameters);
		return jcrNode;
	}
    
    private int updateJcrNode(JdbcTemplate renderingJdbcTemplate, JcrNode jcrNode) {
		Object[] params = { jcrNode.getId(), jcrNode.getLastUpdated(), jcrNode.getContent(), jcrNode.getLastUpdated(), jcrNode.getContent()};
	    int[] types = {Types.VARCHAR,Types.TIMESTAMP, Types.BLOB, Types.TIMESTAMP, Types.BLOB};
	    return renderingJdbcTemplate.update(updateJcrNode, params, types);
	}
    
    private ItemNode eventToItemNode(WcmEvent wcmEvent, JcrNode jcrNode) throws JsonProcessingException, IOException {
    	ItemNode itemNode = new ItemNode();
    	List<String> removedNodeIds = new ArrayList<>();
    	List<String> updatedNodeIds = new ArrayList<>();
    	ObjectNode contentNode = (ObjectNode) JsonUtils.inputStreamToJsonNode(wcmEvent.getContent());
		ArrayNode descendants = (ArrayNode) contentNode.get("descendants");
		if (descendants != null && descendants.size() > 0) {
			for (int i = 0; i < descendants.size(); i++) {
				updatedNodeIds.add(descendants.get(i).textValue());
			}
		}
		
		ArrayNode removedDescendants = (ArrayNode) contentNode.get("removedDescendants");
		if (removedDescendants != null && removedDescendants.size() > 0) {
			for (int i = 0; i < removedDescendants.size(); i++) {
				removedNodeIds.add(removedDescendants.get(i).textValue());
			}
		}
		itemNode.setRemovedNodeIds(removedNodeIds);
    	itemNode.setUpdatedNodeIds(updatedNodeIds);
    	itemNode.setJcrNode(jcrNode);
    	return itemNode;
	}
    
    private int deleteJcrNode(JdbcTemplate renderingJdbcTemplate, String id) {
		Object[] params = { id };
	    int[] types = {Types.BIGINT};
	    return renderingJdbcTemplate.update(deleteJcrNode, params, types);
	}
    
    private String getShortLivedAuthToken(String username) {
        String shortLivedAuthToken = jwsTokenService.createToken(
				JSONWebSignatureService.AUTH_AUDIENCE,
				username,
				(long) 864000000L);
   
        return shortLivedAuthToken;
    }
    
    private class WcmEventRowMapper implements RowMapper<WcmEvent> {
	    @Override
	    public WcmEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	WcmEvent wcmEvent = new WcmEvent();
	 
	    	wcmEvent.setId(rs.getString("ID"));
	    	wcmEvent.setRepository(rs.getString("REPOSITORY"));
	    	wcmEvent.setWorkspace(rs.getString("WORKSPACE"));
	    	wcmEvent.setLibrary(rs.getString("LIBRARY"));
	    	wcmEvent.setNodePath(rs.getString("NODE_PATH"));
	    	wcmEvent.setOperation(WcmEvent.Operation.valueOf(rs.getString("OPERATION")));
	    	wcmEvent.setItemType(WcmEvent.WcmItemType.valueOf(rs.getString("ITEMTYPE")));

	    	Blob b = rs.getBlob("CONTENT");
	    	wcmEvent.setContent(b.getBinaryStream());
	    	return wcmEvent;
	    }
	}
    
    private class JcrNodeRowMapper implements RowMapper<JcrNode> {
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
