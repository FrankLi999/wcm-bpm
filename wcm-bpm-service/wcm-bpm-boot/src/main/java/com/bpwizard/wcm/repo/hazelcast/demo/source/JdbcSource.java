package com.bpwizard.wcm.repo.hazelcast.demo.source;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Util;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.Sources;
import org.h2.tools.DeleteDbFiles;

import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import com.bpwizard.wcm.repo.hazelcast.demo.model.JdbcUser;

/**
 * Demonstrates dumping values from a table in a relational database to an IMap
 * using the JDBC connector.
 */
public class JdbcSource {
	private static final String MAP_NAME = "userMap";
    private static final String TABLE_NAME = "USER_TABLE";

    private JetInstance jet;
    private String dbDirectory;

    private static Pipeline buildPipeline(String connectionUrl) {
        Pipeline p = Pipeline.create();
        
        // A non-distributed, single-worker source which fetches the whole resultSet with a 
        // single query on single member.
        
        // This method executes exactly one query in the target database. If the underlying 
        // table is modified while being read, the behavior depends on the configured 
        // transaction isolation level in the target database. Refer to the documentation 
        // for the target database system.
        
        p.drawFrom(Sources.jdbc(
        		connectionUrl,
                "SELECT * FROM " + TABLE_NAME,
                resultSet -> new JdbcUser(resultSet.getInt(1), resultSet.getString(2))))
         .map(user -> Util.entry(user.getId(), user))
         .drainTo(Sinks.map(MAP_NAME));
        return p;
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("hazelcast.logging.type", "log4j");
        new JdbcSource().go();
    }

    private void go() throws Exception {
        try {
            setup();
            Pipeline p = buildPipeline(connectionUrl());
            jet.newJob(p).join();

            jet.getMap(MAP_NAME).values().forEach(System.out::println);
        } finally {
            cleanup();
        }
    }

    private void setup() throws Exception {
        dbDirectory = Files.createTempDirectory(JdbcSource.class.getName()).toString();
        createAndFillTable();

        jet = Jet.newJetInstance();
        Jet.newJetInstance();
    }

    private void cleanup() {
        Jet.shutdownAll();
        DeleteDbFiles.execute(dbDirectory, JdbcSource.class.getSimpleName(), true);
    }

    private void createAndFillTable() throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionUrl());
                Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE " + TABLE_NAME + "(id int primary key, name varchar(255))");
            try (PreparedStatement stmt =
                         connection.prepareStatement("INSERT INTO " + TABLE_NAME + "(id, name) VALUES(?, ?)")) {
                for (int i = 0; i < 100; i++) {
                    stmt.setInt(1, i);
                    stmt.setString(2, "name-" + i);
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        }
    }

    private String connectionUrl() {
        return "jdbc:h2:" + dbDirectory + "/" + JdbcSource.class.getSimpleName();
    }
}
