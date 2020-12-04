package com.bpwizard.wcm.repo.rest.service;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.rest.handler.AbstractHandler;
import com.bpwizard.wcm.repo.rest.jcr.model.Syndicator;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Component
public class WcmEventService extends AbstractHandler {

	@Autowired(required = false)
	@Qualifier("modeshapeJdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
	
	private SimpleJdbcInsert simpleJdbcInsert;
	
	@Value("${kafka.topic.wcm-event}")
	private String wcmEventTopic;
	
	private static final String updateEventSql = "INSERT INTO SYN_WCM_EVENT(ID, REPOSITORY, WORKSPACE, LIBRARY, NODE_PATH, OPERATION, ITEMTYPE, timeCreated, CONTENT) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE SET LIBRARY=?, NODE_PATH = ?, OPERATION = ?, ITEMTYPE=?, timeCreated=?, CONTENT=?";
	private static final String clearWcmEventsSql = "DELETE SYN_WCM_EVENT WHERE timeCcreated < ?";
	private static final String selectWcmEventsBeforeSql = "SELECT * FROM SYN_WCM_EVENT WHERE library= :library and timeCcreated > :begin and timeCcreated < :end ORDER BY timeCcreated ASC LIMIT :pageSize OFFSET :offset";
	private static final String selectCndBeforeSql = "SELECT * FROM SYN_WCM_EVENT WHERE ITEMTYPE= 'cnd' and timeCcreated > :begin and timeCcreated < :end ORDER BY timeCcreated ASC";
	
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
		return doInsert(event);
	}

	public int updateWcmEvent(WcmEvent event) {
		return  doUpsert(event);
	}

	public List<WcmEvent> getCndAfter(
			Syndicator syndicator,
			Timestamp endTimestamp) {
		
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("begin", syndicator.getLastSyndication())
				.addValue("end", endTimestamp);
		List<WcmEvent> wcmEvents = jdbcTemplate.query(
				selectCndBeforeSql, new WcmEventRowMapper(), namedParameters);

		return wcmEvents;
	}
	
	public List<WcmEvent> getWcmEventAfter(
			Syndicator syndicator,
			Timestamp endTimestamp, 
			int pageIndex,
			int pageSize) {
		
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("library", syndicator.getLibrary())
				.addValue("begin", syndicator.getLastSyndication())
				.addValue("end", endTimestamp)
				.addValue("pageSize", pageSize)
				.addValue("offset", pageSize * pageIndex);
		List<WcmEvent> wcmEvents = jdbcTemplate.query(
				selectWcmEventsBeforeSql, new WcmEventRowMapper(), namedParameters);

		return wcmEvents;
	}

	public int[] batchInsert(List<WcmEvent> events) {
		MapSqlParameterSource parameters[] = events.stream().map(e -> insertParameters(e)).toArray(MapSqlParameterSource[]::new);
        return simpleJdbcInsert.executeBatch(parameters);
    }
	
	public int[] batchUpdate(List<WcmEvent> events) {
		List<Object[]> batchArgs = new ArrayList<>();
		for (WcmEvent event: events) {
			Object args[] = {event.getId(), event.getRepository(), event.getWorkspace(), event.getLibrary(), event.getNodePath(), event.getOperation().name(), event.getItemType().name(), event.getTimeCreated(), event.getContent(), event.getLibrary(), event.getNodePath(), event.getOperation().name(), event.getItemType().name(), event.getTimeCreated(), event.getContent()};
			batchArgs.add(args);
		}
		int[] argTypes = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.BLOB, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.BLOB};
		return jdbcTemplate.batchUpdate(updateEventSql, batchArgs, argTypes);
    }
	
	protected Long doInsert(WcmEvent event) {
		MapSqlParameterSource parameters = insertParameters(event);
		 return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
	}
	
	protected int doUpsert(WcmEvent event) {
		Object args[] = {event.getId(), event.getRepository(), event.getWorkspace(), event.getLibrary(), event.getNodePath(), event.getOperation().name(), event.getItemType().name(), event.getTimeCreated(), event.getContent(), event.getLibrary(), event.getNodePath(), event.getOperation().name(), event.getItemType().name(), event.getTimeCreated(), event.getContent()};
		int[] argTypes = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.BLOB, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.BLOB};
	    return jdbcTemplate.update(updateEventSql, args, argTypes);
	}
	
	protected MapSqlParameterSource insertParameters(WcmEvent event) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ID", event.getId());
		parameters.addValue("REPOSITORY", event.getRepository());
		parameters.addValue("WORKSPACE", event.getWorkspace());
		parameters.addValue("LIBRARY", event.getLibrary());
	    parameters.addValue("NODE_PATH", event.getNodePath());
	    parameters.addValue("ITEMTYPE", event.getItemType().name());
	    parameters.addValue("OPERATION", event.getOperation().name());
	    parameters.addValue("timeCreated", event.getTimeCreated());
	    parameters.addValue("CONTENT", event.getContent());
	    return parameters;
	}
	
	protected Set<String> arrayNodeToStrings(ArrayNode nodes) {
		Set<String> ids = new HashSet<>();
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
	    	wcmEvent.setLibrary(rs.getString("LIBRARY"));
	    	wcmEvent.setNodePath(rs.getString("NODE_PATH"));
	    	wcmEvent.setOperation(WcmEvent.Operation.valueOf(rs.getString("OPERATION")));
	    	wcmEvent.setItemType(WcmEvent.WcmItemType.valueOf(rs.getString("ITEMTYPE")));

	    	Blob b = rs.getBlob("CONTENT");
	    	wcmEvent.setContent(b.getBinaryStream());
	    	return wcmEvent;
	    }
	}

	
	

}
