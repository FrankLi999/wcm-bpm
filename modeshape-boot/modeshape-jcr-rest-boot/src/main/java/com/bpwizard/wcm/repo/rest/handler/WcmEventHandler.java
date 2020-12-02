package com.bpwizard.wcm.repo.rest.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class WcmEventHandler extends AbstractHandler {

	@Autowired(required = false)
	@Qualifier("modeshapeJdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
	
	SimpleJdbcInsert simpleJdbcInsert;
	
	private static final String deleteEventSql = "DELETE SYN_WCM_EVENT WHERE REPOSITORY = ? and WORKSPACE=? and WCM_APTH=?";
	private static final String clearWcmEventsSql = "DELETE SYN_WCM_EVENT WHERE timeCcreated < ?";
	private static final String selectWcmEventsBeforeSql = "SELECT * FROM SYN_WCM_EVENT WHERE timeCcreated > :begin and timeCcreated < :end ORDER BY timeCcreated ASC LIMIT :pageSize OFFSET :offset";
	
	@PostConstruct
	private void postConstruct() {
		simpleJdbcInsert = 
			new SimpleJdbcInsert(jdbcTemplate).withTableName("SYN_WCM_EVENT").usingGeneratedKeyColumns("ID");
	};
	

	public int clearWcmEventBefore(Timestamp timestamp) {
		Object[] params = { timestamp };
	    int[] types = {Types.TIMESTAMP};
	    return jdbcTemplate.update(clearWcmEventsSql, params, types);
	}

	public Long addWcmEvent(WcmEvent event) {
		return insertWcmEvent(event);
	}

	public Long updateWcmEvent(WcmEvent event) {
		return insertWcmEvent(event);
	}

	public List<WcmEvent> getWcmEventAfter(
			Timestamp startTimestamp, 
			Timestamp endTimestamp, 
			int pageIndex,
			int pageSize) {
		
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("begin", startTimestamp)
				.addValue("end", endTimestamp)
				.addValue("pageSize", pageSize)
				.addValue("offset", pageSize * pageIndex);
		List<WcmEvent> wcmEvents = jdbcTemplate.query(
				selectWcmEventsBeforeSql, new WcmEventRowMapper(), namedParameters);

		return wcmEvents;
	}

	protected int deleteWcmEvent(WcmEvent event) {
		Object[] params = { event.getRepository(), event.getWorkspace(), event.getWcmPath()};
	    int[] types = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
	    return jdbcTemplate.update(deleteEventSql, params, types);
	}
	
	public int[] batchInsert(List<WcmEvent> events) {
		MapSqlParameterSource parameters[] = events.stream().map(e -> insertParameters(e)).toArray(MapSqlParameterSource[]::new);
        return simpleJdbcInsert.executeBatch(parameters);
    }
	
	public Long insertWcmEvent(MapSqlParameterSource parameters) {
		 return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
	}
	
	protected Long insertWcmEvent(WcmEvent event) {
		MapSqlParameterSource parameters = insertParameters(event);
	    return insertWcmEvent(parameters);
	}
	
	protected MapSqlParameterSource insertParameters(WcmEvent event) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ID", event.getId());
		parameters.addValue("REPOSITORY", event.getRepository());
		parameters.addValue("WORKSPACE", event.getWorkspace());
	    parameters.addValue("WCM_APTH", event.getWcmPath());
	    parameters.addValue("ITEMTYPE", event.getItemType().name());
	    parameters.addValue("OPERATION", event.getOperation().name());
	    parameters.addValue("timeCreated", event.getTimeCreated());
	    parameters.addValue("CONTENT", content(event));
	    return parameters;
	}
	
	protected ByteArrayInputStream content(WcmEvent event) {
    	
		ObjectNode contentNode = JsonUtils.createObjectNode();
		if (!ObjectUtils.isEmpty(event.getDescendants())) {
			ArrayNode descendants = JsonUtils.creatArrayNode();
			for (String value : event.getDescendants()) {
				descendants.add(value);
			}
			contentNode.set("descendants", descendants);
		}
		if (!ObjectUtils.isEmpty(event.getRemovedDescendants())) {
			ArrayNode removedDescendants = JsonUtils.creatArrayNode();
			for (String value : event.getRemovedDescendants()) {
				removedDescendants.add(value);
				contentNode.set("removedDescendants", removedDescendants);
			}
		}
		try {
			return new ByteArrayInputStream(JsonUtils.writeValueAsString(contentNode).getBytes());
		} catch (JsonProcessingException e) {
			// TODO
			throw new RuntimeException(e);
		}
    }
	
	protected List<String> arrayNodeToStrings(ArrayNode nodes) {
		List<String> ids = new ArrayList<>();
		if (nodes != null) {
			for (int i = 0; i < nodes.size(); i++) {
				ids.add(nodes.get(i).textValue());
			}
		}
		return ids;
		
	}
	
	public class WcmEventRowMapper implements RowMapper<WcmEvent> {
	    @Override
	    public WcmEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	WcmEvent wcmEvent = new WcmEvent();
	 
	    	wcmEvent.setId(rs.getString("ID"));
	    	wcmEvent.setRepository(rs.getString("REPOSITORY"));
	    	wcmEvent.setWorkspace(rs.getString("WORKSPACE"));
	    	wcmEvent.setWcmPath(rs.getString("WCM_APTH"));
	    	wcmEvent.setOperation(WcmEvent.Operation.valueOf(rs.getString("OPERATION")));
	    	wcmEvent.setItemType(WcmEvent.WcmItemType.valueOf(rs.getString("ITEMTYPE")));

	    	Blob b = rs.getBlob("CONTENT");
	    	try {
	    		JsonNode contentNode = JsonUtils.inputStreamToJsonNode(b.getBinaryStream());
	    		ArrayNode descendants = (ArrayNode) contentNode.get("descendants");
	    		wcmEvent.setDescendants(arrayNodeToStrings(descendants));
	    		ArrayNode removedDescendants = (ArrayNode) contentNode.get("removedDescendants");
	    		wcmEvent.setRemovedDescendants(arrayNodeToStrings(removedDescendants));
	    		return wcmEvent;
	    	} catch (IOException e) {
	    		throw new SQLException(e);
	    	}
	    }
	}
	
	
}
