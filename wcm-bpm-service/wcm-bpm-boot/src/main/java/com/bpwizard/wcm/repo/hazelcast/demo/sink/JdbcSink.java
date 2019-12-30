package com.bpwizard.wcm.repo.hazelcast.demo.sink;

import com.hazelcast.jet.IMapJet;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.Sources;
//import org.h2.tools.DeleteDbFiles;

import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
// import com.bpwizard.wcm.repo.hazelcast.demo.model.JdbcUser;
/**
 * Demonstrates dumping values from an IMap to a table in a relational database
 * using the JDBC connector.
 */
public class JdbcSink {

	private static final String MAP_NAME = "userMap";
	private static final String TABLE_NAME = "USER_TABLE";

	private JetInstance jet;
	private String dbDirectory;

	private static Pipeline buildPipeline(String connectionUrl) {
		Pipeline p = Pipeline.create();

//		p.drawFrom(Sources.<Integer, JdbcUser>map(MAP_NAME)).map(Map.Entry::getValue).drainTo(
//				Sinks.jdbc("INSERT INTO " + TABLE_NAME + "(id, name) VALUES(?, ?)", connectionUrl, (stmt, user) -> {
//					// Bind the values from the stream item to a PreparedStatement created from
//					// the above query.
//					stmt.setInt(1, user.getId());
//					stmt.setString(2, user.getName());
//				}));
		return p;
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("hazelcast.logging.type", "log4j");
		new JdbcSink().go();
	}

	private void go() throws Exception {
//		try {
//			setup();
//			Pipeline p = buildPipeline(connectionUrl());
//			jet.newJob(p).join();
//			printTable();
//		} finally {
//			cleanup();
//		}
	}

	private void setup() throws Exception {
//		dbDirectory = Files.createTempDirectory(JdbcSink.class.getName()).toString();
//
//		createTable();
//
//		jet = Jet.newJetInstance();
//		Jet.newJetInstance();
//
//		IMapJet<Integer, JdbcUser> map = jet.getMap(MAP_NAME);
//		// populate the source IMap
//		for (int i = 0; i < 100; i++) {
//			map.put(i, new JdbcUser(i, "name-" + i));
//		}
	}

	private void cleanup() {
		Jet.shutdownAll();
		// DeleteDbFiles.execute(dbDirectory, JdbcSink.class.getSimpleName(), true);
	}

//	private void createTable() throws SQLException {
//		try (Connection connection = DriverManager.getConnection(connectionUrl());
//				Statement statement = connection.createStatement()) {
//			statement.execute("CREATE TABLE " + TABLE_NAME + "(id int primary key, name varchar(255))");
//		}
//	}
//
//	private void printTable() throws SQLException {
//		try (Connection connection = DriverManager.getConnection(connectionUrl());
//				Statement statement = connection.createStatement();
//				ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME)) {
//			while (resultSet.next()) {
//				System.out.println(new JdbcUser(resultSet.getInt(1), resultSet.getString(2)));
//			}
//		}
//	}
//
//	private String connectionUrl() {
//		return "jdbc:h2:" + dbDirectory + "/" + JdbcSink.class.getSimpleName();
//	}
}