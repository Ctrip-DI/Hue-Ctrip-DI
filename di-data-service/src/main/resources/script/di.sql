DROP TABLE IF EXISTS `ctrip_yarn_jobs`;
CREATE TABLE  `ctrip_yarn_jobs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start_time` bigint(20) NOT NULL,
  `finish_time` bigint(20) NOT NULL,
  `job_id` varchar(256) DEFAULT NULL,
  `queue` varchar(128) DEFAULT NULL,
  `user` varchar(128) DEFAULT NULL,
  `status` varchar(64) DEFAULT NULL,
  `maps_total` int(11) NOT NULL,
  `reduces_total` int(11) NOT NULL,
  `date_str` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_date_index` (`user`,`date_str`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `spark_jobs`;
CREATE TABLE  `spark_jobs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start_time` bigint(20) NOT NULL,
  `finish_time` bigint(20) NOT NULL,
  `sql` varchar(2048) DEFAULT NULL,
  `user` varchar(128) DEFAULT NULL,
  `status` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_index` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ctrip_hive_jobs`;
CREATE TABLE  `ctrip_hive_jobs` (
  `dbname` varchar(100) NOT NULL DEFAULT '',
  `tablename` varchar(100) NOT NULL DEFAULT '',
  `pt_format` varchar(200) DEFAULT NULL,
  `keepdays` int(11) DEFAULT '0',
  `checkrate` float DEFAULT '0',
  `username` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`dbname`,`tablename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;