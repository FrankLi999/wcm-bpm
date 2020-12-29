--DROP TABLE users IF EXISTS;

CREATE TABLE hzelcast_jet_users (
  id bigint(20) NOT NULL,
  name VARCHAR(30),
  email  VARCHAR(50),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;