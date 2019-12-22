--
-- Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
-- under one or more contributor license agreements. See the NOTICE file
-- distributed with this work for additional information regarding copyright
-- ownership. Camunda licenses this file to you under the Apache License,
-- Version 2.0; you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

create table ACT_ID_GROUP (
    ID_ varchar(64),
    REV_ integer,
    NAME_ varchar(255),
    TYPE_ varchar(255),
    primary key (ID_)
);

create table ACT_ID_MEMBERSHIP (
    USER_ID_ varchar(64),
    GROUP_ID_ varchar(64),
    primary key (USER_ID_, GROUP_ID_)
);

create table ACT_ID_USER (
    ID_ varchar(64),
    REV_ integer,
    FIRST_ varchar(255),
    LAST_ varchar(255),
    EMAIL_ varchar(255),
    PWD_ varchar(255),
    SALT_ varchar(255),
    LOCK_EXP_TIME_ timestamp,
    ATTEMPTS_ integer,
    PICTURE_ID_ varchar(64),
    primary key (ID_)
);

create table ACT_ID_INFO (
    ID_ varchar(64),
    REV_ integer,
    USER_ID_ varchar(64),
    TYPE_ varchar(64),
    KEY_ varchar(255),
    VALUE_ varchar(255),
    PASSWORD_ longvarbinary,
    PARENT_ID_ varchar(255),
    primary key (ID_)
);

create table ACT_ID_TENANT (
    ID_ varchar(64),
    REV_ integer,
    NAME_ varchar(255),
    primary key (ID_)
);

create table ACT_ID_TENANT_MEMBER (
    ID_ varchar(64) not null,
    TENANT_ID_ varchar(64) not null,
    USER_ID_ varchar(64),
    GROUP_ID_ varchar(64),
    primary key (ID_)
);

CREATE TABLE `tenent` (
  id integer NOT NULL,
  created_by_id integer DEFAULT NULL,
  created_date timestamp, DEFAULT NULL,
  last_modified_by_id integer DEFAULT NULL,
  last_modified_date timestamp DEFAULT NULL,
  version integer DEFAULT NULL,
  name varchar(50) NOT NULL,
  primary key (id)
)

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL,
  `created_by_id` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by_id` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  `type` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8sewwnpamngi6b1dwaa88askk` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin

CREATE TABLE `usr` (
  `id` bigint(20) NOT NULL,
  `created_by_id` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_modified_by_id` bigint(20) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `credentials_updated_millis` bigint(20) NOT NULL,
  `email` varchar(250) COLLATE utf8_bin NOT NULL,
  `email_verified` bit(1) DEFAULT NULL,
  `first_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `last_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  `new_email` varchar(250) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_bin NOT NULL,
  `provider` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `provider_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_g9l96r670qkidthshajdtxrqf` (`email`),
  UNIQUE KEY `UKg9l96r670qkidthshajdtxrqf` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin

CREATE TABLE user_role (
  user_id bigint(20) NOT NULL,
  role_id bigint(20) NOT NULL,
  PRIMARY KEY (user_id, role_id),
  KEY FKa68196081fvovjhkek5m97n3y (role_id),
  CONSTRAINT FKa68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES role (id),
  CONSTRAINT FKfpm8swft53ulq2hl11yplpr5 FOREIGN KEY (user_id) REFERENCES usr (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin

CREATE TABLE tenant_membership (
  id bigint(20) AUTO_INCREMENT,
  tenant_id bigint(20) NOT NULL,
  role_id bigint(20) DEFAULT NULL,
  user_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

CREATE TABLE tenent_membership (
  id integer,
  tenent_id integer NOT NULL,
  role_id integer DEFAULT NULL,
  user_id integer DEFAULT NULL,
  PRIMARY KEY (id)
);

alter table ACT_ID_MEMBERSHIP
    add constraint ACT_FK_MEMB_GROUP
    foreign key (GROUP_ID_)
    references ACT_ID_GROUP;

alter table ACT_ID_MEMBERSHIP
    add constraint ACT_FK_MEMB_USER
    foreign key (USER_ID_)
    references ACT_ID_USER;

alter table ACT_ID_TENANT_MEMBER
    add constraint ACT_UNIQ_TENANT_MEMB_USER
    unique (TENANT_ID_, USER_ID_);

alter table ACT_ID_TENANT_MEMBER
    add constraint ACT_UNIQ_TENANT_MEMB_GROUP
    unique (TENANT_ID_, GROUP_ID_);

alter table ACT_ID_TENANT_MEMBER
    add constraint ACT_FK_TENANT_MEMB
    foreign key (TENANT_ID_)
    references ACT_ID_TENANT;

alter table ACT_ID_TENANT_MEMBER
    add constraint ACT_FK_TENANT_MEMB_USER
    foreign key (USER_ID_)
    references ACT_ID_USER;

alter table ACT_ID_TENANT_MEMBER
    add constraint ACT_FK_TENANT_MEMB_GROUP
    foreign key (GROUP_ID_)
    references ACT_ID_GROUP;
