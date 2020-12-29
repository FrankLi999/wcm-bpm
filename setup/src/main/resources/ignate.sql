-- Apache Ignite tables
create table if not exists WCM_ENTRIES (akey binary(250) primary key, val varbinary(20480));
create table if not exists BPM_ENTRIES (akey binary(250) primary key, val varbinary(20480));
create table if not exists MICROFLOW_ENTRIES (akey binary(250) primary key, val varbinary(20480));