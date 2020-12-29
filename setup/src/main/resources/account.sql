CREATE TABLE IF NOT EXISTS `usr` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by_id` bigint DEFAULT NULL,
  `created_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `last_modified_by_id` bigint DEFAULT NULL,
  `updated_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `version` bigint DEFAULT 0,
  `attempts` int NOT NULL DEFAULT 0,
  `credentials_updated_millis` bigint NOT NULL DEFAULT 0,
  `email` varchar(250) COLLATE utf8_bin NOT NULL,
  `email_verified` bit(1) DEFAULT NULL,
  `first_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `last_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `lock_expiration_time` datetime DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  `new_email` varchar(250) COLLATE utf8_bin DEFAULT NULL,
  `new_password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_bin NOT NULL,
  `provider` varchar(255) COLLATE utf8_bin DEFAULT 'local',
  `provider_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `salt` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by_id` bigint DEFAULT NULL,
  `created_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `last_modified_by_id` bigint DEFAULT NULL,
  `updated_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `version` bigint DEFAULT 0,
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  `type` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `tenant` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by_id` bigint DEFAULT NULL,
  `created_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `last_modified_by_id` bigint DEFAULT NULL,
  `updated_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `version` bigint DEFAULT 0,
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `tenant_role` (
  `tenant_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  `created_by_id` bigint DEFAULT NULL,
  `created_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `last_modified_by_id` bigint DEFAULT NULL,
  `updated_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `version` bigint DEFAULT 0,  
  PRIMARY KEY (`role_id`,`tenant_id`),
  CONSTRAINT FOREIGN KEY (`tenant_id`) REFERENCES `tenant` (`id`),
  CONSTRAINT FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `tenant_user` (
  `user_id` bigint NOT NULL,
  `tenant_id` bigint NOT NULL,
  `created_by_id` bigint DEFAULT NULL,
  `created_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `last_modified_by_id` bigint DEFAULT NULL,
  `updated_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `version` bigint DEFAULT 0,
  PRIMARY KEY (`user_id`,`tenant_id`),
  CONSTRAINT FOREIGN KEY (`tenant_id`) REFERENCES `tenant` (`id`),
  CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `usr` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `role_user` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  `created_by_id` bigint DEFAULT NULL,
  `created_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `last_modified_by_id` bigint DEFAULT NULL,
  `updated_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `version` bigint DEFAULT 0,
  PRIMARY KEY (`user_id`,`role_id`),
  CONSTRAINT FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `usr` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `role_role` (
  `role_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`, `member_id`),
  CONSTRAINT FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT FOREIGN KEY (`member_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;