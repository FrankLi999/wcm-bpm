
CREATE DATABASE  IF NOT EXISTS  `gateway`  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;

USE `gateway`;

/*Table structure for table `dashboard_user` */
CREATE TABLE IF NOT EXISTS `dashboard_user` (
  `id` varchar(128) NOT NULL,
  `user_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` int(4) NOT NULL,
  `enabled` tinyint(4) NOT NULL,
  `date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `plugin` */
CREATE TABLE IF NOT EXISTS `plugin` (
  `id` varchar(128) NOT NULL,
  `name` varchar(62) COLLATE utf8mb4_unicode_ci NOT NULL,
  `config` text COLLATE utf8mb4_unicode_ci,
  `role` int(4) NOT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT '0',
  `date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `selector` */
CREATE TABLE IF NOT EXISTS `selector` (
  `id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL primary key,
  `plugin_id` varchar(128) NOT NULL,
  `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_mode` int(2) NOT NULL,
  `type` int(4) NOT NULL,
  `sort` int(4) NOT NULL,
  `handle` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `enabled` tinyint(4) NOT NULL,
  `loged` tinyint(4) NOT NULL,
  `continued` tinyint(4) NOT NULL,
  `date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  constraint unique_name unique (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `selector_condition` */
CREATE TABLE IF NOT EXISTS `selector_condition` (
  `id` varchar(128) NOT NULL,
  `selector_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `param_type` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `operator` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `param_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `param_value` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `rule` */
CREATE TABLE IF NOT EXISTS `rule` (
  `id` varchar(128) NOT NULL PRIMARY KEY,
  `selector_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_mode` int(2) NOT NULL,
  `name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `enabled` tinyint(4) NOT NULL,
  `loged` tinyint(4) NOT NULL,
  `sort` int(4) NOT NULL,
  `handle` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   constraint unique_name unique (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `rule_condition` */
CREATE TABLE IF NOT EXISTS `rule_condition` (
  `id` varchar(128) NOT NULL PRIMARY KEY,
  `rule_id` varchar(128) NOT NULL,
  `param_type` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `operator` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `param_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `param_value` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE  IF NOT EXISTS `meta_data` (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `path_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `rpc_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `service_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `parameter_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `rpc_ext` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `date_created` datetime(0) NOT NULL,
  `date_updated` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `enabled` tinyint(4) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `app_auth`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_secret` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `ext_info` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `enabled` tinyint(4) NOT NULL,
  `date_created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `date_updated` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `auth_param`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `auth_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `app_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `date_created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `date_updated` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for auth_path
-- ----------------------------
CREATE TABLE IF NOT EXISTS `auth_path`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `auth_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `enabled` tinyint(4) NOT NULL,
  `date_created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `date_updated` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

/*plugin*/
INSERT IGNORE INTO `plugin` (`id`, `name`,`role`, `enabled`, `date_created`, `date_updated`) VALUES ('1', 'sign','0', '0', '2018-06-14 10:17:35', '2018-06-14 10:17:35');
INSERT IGNORE INTO `plugin` (`id`, `name`,`role`,`config`,`enabled`, `date_created`, `date_updated`) VALUES ('2', 'waf', '0','{"model":"black"}','0', '2018-06-23 10:26:30', '2018-06-13 15:43:10');
INSERT IGNORE INTO `plugin` (`id`, `name`,`role`, `enabled`, `date_created`, `date_updated`) VALUES ('3', 'rewrite', '0','0', '2018-06-23 10:26:34', '2018-06-25 13:59:31');
INSERT IGNORE INTO `plugin` (`id`, `name`,`role`,`config`,`enabled`, `date_created`, `date_updated`) VALUES ('4', 'rate_limiter','0','{"master":"mymaster","mode":"Standalone","url":"192.168.1.1:6379","password":"abc"}', '0', '2018-06-23 10:26:37', '2018-06-13 15:34:48');
INSERT IGNORE INTO `plugin` (`id`, `name`,`role`, `enabled`, `date_created`, `date_updated`) VALUES ('5', 'divide', '0','1', '2018-06-25 10:19:10', '2018-06-13 13:56:04');
INSERT IGNORE INTO `plugin` (`id`, `name`,`role`,`config`,`enabled`, `date_created`, `date_updated`) VALUES ('6', 'dubbo','0','{"register":"zookeeper://localhost:2181"}', '0', '2018-06-23 10:26:41', '2018-06-11 10:11:47');
INSERT IGNORE INTO `plugin` (`id`, `name`,`role`,`config`,`enabled`, `date_created`, `date_updated`) VALUES ('7', 'monitor', '0','{"metricsName":"prometheus","host":"localhost","port":"9190","async":"true"}','0', '2018-06-25 13:47:57', '2018-06-25 13:47:57');
INSERT IGNORE INTO `plugin` (`id`, `name`,`role`, `enabled`, `date_created`, `date_updated`) VALUES ('8', 'springCloud','0', '0', '2018-06-25 13:47:57', '2018-06-25 13:47:57');
INSERT IGNORE INTO `plugin` (`id`, `name`,`role`, `enabled`, `date_created`, `date_updated`) VALUES ('9', 'hystrix', '0','0', '2020-01-15 10:19:10', '2020-01-15 10:19:10');

/**user**/
INSERT IGNORE INTO `dashboard_user` (`id`, `user_name`, `password`, `role`, `enabled`, `date_created`, `date_updated`) VALUES ('1', 'admin', '123456', '1', '1', '2018-06-23 15:12:22', '2018-06-23 15:12:23');