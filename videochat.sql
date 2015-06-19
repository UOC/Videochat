# ************************************************************
# Sequel Pro SQL dump
# Versión 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.5.43-0+deb7u1-log)
# Base de datos: videochat
# Tiempo de Generación: 2015-06-19 06:33:01 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Volcado de tabla lti_consumer
# ------------------------------------------------------------

DROP TABLE IF EXISTS `lti_consumer`;

CREATE TABLE `lti_consumer` (
  `consumer_key` varchar(255) NOT NULL,
  `name` varchar(45) NOT NULL,
  `secret` varchar(32) NOT NULL,
  `lti_version` varchar(12) DEFAULT NULL,
  `consumer_name` varchar(255) DEFAULT NULL,
  `consumer_version` varchar(255) DEFAULT NULL,
  `consumer_guid` varchar(255) DEFAULT NULL,
  `css_path` varchar(255) DEFAULT NULL,
  `protected` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `enable_from` datetime DEFAULT NULL,
  `enable_until` datetime DEFAULT NULL,
  `last_access` date DEFAULT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`consumer_key`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Volcado de tabla lti_context
# ------------------------------------------------------------

DROP TABLE IF EXISTS `lti_context`;

CREATE TABLE `lti_context` (
  `consumer_key` varchar(255) NOT NULL,
  `context_id` varchar(255) NOT NULL,
  `lti_context_id` varchar(255) DEFAULT NULL,
  `lti_resource_id` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `settings` text,
  `primary_consumer_key` varchar(255) DEFAULT NULL,
  `primary_context_id` varchar(255) DEFAULT NULL,
  `share_approved` tinyint(1) DEFAULT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`consumer_key`,`context_id`),
  KEY `lti_context_context_FK1` (`primary_consumer_key`,`primary_context_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Volcado de tabla lti_nonce
# ------------------------------------------------------------

DROP TABLE IF EXISTS `lti_nonce`;

CREATE TABLE `lti_nonce` (
  `consumer_key` varchar(255) NOT NULL,
  `value` varchar(32) NOT NULL,
  `expires` datetime NOT NULL,
  PRIMARY KEY (`consumer_key`,`value`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Volcado de tabla lti_share_key
# ------------------------------------------------------------

DROP TABLE IF EXISTS `lti_share_key`;

CREATE TABLE `lti_share_key` (
  `share_key_id` varchar(32) NOT NULL,
  `primary_consumer_key` varchar(255) NOT NULL,
  `primary_context_id` varchar(255) NOT NULL,
  `auto_approve` tinyint(1) NOT NULL,
  `expires` datetime NOT NULL,
  PRIMARY KEY (`share_key_id`),
  KEY `lti_share_key_context_FK1` (`primary_consumer_key`,`primary_context_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Volcado de tabla lti_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `lti_user`;

CREATE TABLE `lti_user` (
  `consumer_key` varchar(255) NOT NULL,
  `context_id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `lti_result_sourcedid` varchar(255) NOT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`consumer_key`,`context_id`,`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Volcado de tabla vc_chat
# ------------------------------------------------------------

DROP TABLE IF EXISTS `vc_chat`;

CREATE TABLE `vc_chat` (
  `chat_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `chat_meeting_room_id` int(11) unsigned NOT NULL,
  `chat_user_id` int(11) unsigned NOT NULL,
  `chat_message` varchar(255) NOT NULL DEFAULT '',
  `chat_sent_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`chat_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Volcado de tabla vc_course
# ------------------------------------------------------------

DROP TABLE IF EXISTS `vc_course`;

CREATE TABLE `vc_course` (
  `course_id` int(11) NOT NULL AUTO_INCREMENT,
  `course_coursekey` varchar(255) NOT NULL,
  `course_title` varchar(255) NOT NULL,
  `course_lang` varchar(10) NOT NULL,
  `course_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `course_last_modified` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`course_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Volcado de tabla vc_meeting
# ------------------------------------------------------------

DROP TABLE IF EXISTS `vc_meeting`;

CREATE TABLE `vc_meeting` (
  `meeting_room_id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) NOT NULL,
  `meeting_room_topic` varchar(255) NOT NULL,
  `meeting_room_description` varchar(250) DEFAULT NULL,
  `meeting_room_path` varchar(250) NOT NULL,
  `meeting_room_number_participants` decimal(2,0) NOT NULL,
  `meeting_room_finished` decimal(1,0) NOT NULL,
  `meeting_room_start_meeting` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `meeting_room_end_meeting` timestamp NULL DEFAULT NULL,
  `meeting_room_recorded` decimal(1,0) NOT NULL,
  `meeting_room_start_record` timestamp NULL DEFAULT NULL,
  `meeting_room_end_record` timestamp NULL DEFAULT NULL,
  `meeting_room_deleted` decimal(1,0) DEFAULT '0',
  `meeting_room_time_deleted` timestamp NULL DEFAULT NULL,
  `meeting_room_userid_deleted` int(11) DEFAULT NULL,
  PRIMARY KEY (`meeting_room_id`),
  KEY `vc_meeting_ibfk_1` (`room_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Volcado de tabla vc_room
# ------------------------------------------------------------

DROP TABLE IF EXISTS `vc_room`;

CREATE TABLE `vc_room` (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) NOT NULL,
  `room_key` varchar(150) NOT NULL,
  `room_label` varchar(250) NOT NULL,
  `room_is_blocked` tinyint(1) NOT NULL,
  `room_reason_blocked` varchar(250) DEFAULT '',
  PRIMARY KEY (`room_id`),
  KEY `COURSE_ID` (`course_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Volcado de tabla vc_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `vc_user`;

CREATE TABLE `vc_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_username` varchar(70) NOT NULL,
  `user_password` varchar(255) DEFAULT NULL,
  `user_firstname` varchar(50) NOT NULL,
  `user_surname` varchar(75) NOT NULL,
  `user_fullname` varchar(150) NOT NULL,
  `user_email` varchar(50) NOT NULL,
  `user_image` varchar(255) DEFAULT NULL,
  `user_blocked` bit(1) NOT NULL DEFAULT b'0',
  `user_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_last_modification` timestamp NULL DEFAULT NULL,
  `token_access` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `USER_ID` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Volcado de tabla vc_usercourse
# ------------------------------------------------------------

DROP TABLE IF EXISTS `vc_usercourse`;

CREATE TABLE `vc_usercourse` (
  `user_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `usercourse_role` varchar(250) NOT NULL,
  `usercourse_is_instructor` tinyint(1) NOT NULL,
  PRIMARY KEY (`user_id`,`course_id`),
  KEY `course_id` (`course_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Volcado de tabla vc_usermeeting
# ------------------------------------------------------------

DROP TABLE IF EXISTS `vc_usermeeting`;

CREATE TABLE `vc_usermeeting` (
  `user_id` int(11) NOT NULL,
  `meeting_id` int(11) NOT NULL,
  `usermeeting_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `usermeeting_stream_key` varchar(255) NOT NULL DEFAULT '',
  `usermeeting_access_confirmed` bit(1) NOT NULL DEFAULT b'0',
  `usermeeting_extra_role` varchar(255) DEFAULT NULL,
  `usermeeting_user_agent` varchar(150) DEFAULT NULL,
  `usermeeting_platform` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`meeting_id`),
  KEY `meeting_id` (`meeting_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Volcado de tabla vc_usermeeting_history
# ------------------------------------------------------------

DROP TABLE IF EXISTS `vc_usermeeting_history`;

CREATE TABLE `vc_usermeeting_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `meeting_id` int(11) NOT NULL,
  `usermeeting_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `usermeeting_deleted` timestamp NULL DEFAULT NULL,
  `usermeeting_extra_role` varchar(255) DEFAULT NULL,
  `usermeeting_user_agent` varchar(150) DEFAULT NULL,
  `usermeeting_platform` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `meeting_id` (`user_id`,`meeting_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
