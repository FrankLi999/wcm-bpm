-- DROP TABLE users IF EXISTS;

CREATE TABLE hzelcast_jet_users (
  id bigint(20) NOT NULL,
  name VARCHAR(30),
  email  VARCHAR(50),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
INSERT INTO hzelcast_jet_users(id, name, email) VALUES (1, 'ali', 'ali@hazelcast.com');
INSERT INTO hzelcast_jet_users(id, name, email) VALUES (2, 'can', 'can@hazelcast.com');
INSERT INTO hzelcast_jet_users(id, name, email) VALUES (3, 'marko', 'marko@hazelcast.com');
INSERT INTO hzelcast_jet_users(id, name, email) VALUES (4, 'vilo', 'vilo@hazelcast.com');

