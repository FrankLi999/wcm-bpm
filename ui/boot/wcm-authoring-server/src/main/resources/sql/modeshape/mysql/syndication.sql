CREATE TABLE `SYN_WCM_SERVER` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `HOST` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PORT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin

CREATE TABLE `SYN_SYNDICATION` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `COLLECTOR_ID` int(11) DEFAULT NULL,
  `LAST_SYNDICATION` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  CONSTRAINT `SYN_SYNDICATION_COLLECTOR_ID` FOREIGN KEY (`COLLECTOR_ID`) REFERENCES `SYN_WCM_SERVER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin

CREATE TABLE `SYN_COLLECTOR` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `SYNDICATOR_ID` int(11) DEFAULT NULL,
  `LAST_SYNDICATION` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  CONSTRAINT `SYN_COLLECTOR_SYNDICATOR_ID` FOREIGN KEY (`SYNDICATOR_ID`) REFERENCES `SYN_WCM_SERVER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin

CREATE TABLE IF NOT EXISTS `SYN_JCR_NODE_EVENT` (
  `ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `NODE_PATH` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `REPOSITORY` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `WORKSPACE` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `LIBRARY` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `OPERATION` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `ITEMTYPE` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `timeCreated` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CONTENT` longblob NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `SYN_JSON_NODE_EVENT` (
  `ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `NODE_PATH` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `REPOSITORY` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `WORKSPACE` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `LIBRARY` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `OPERATION` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `ITEMTYPE` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `timeCreated` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CONTENT` longblob NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;