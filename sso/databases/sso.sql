/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50550
Source Host           : 127.0.0.1:3306
Source Database       : sso

Target Server Type    : MYSQL
Target Server Version : 50550
File Encoding         : 65001

Date: 2018-09-27 11:07:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `s_login`
-- ----------------------------
DROP TABLE IF EXISTS `s_login`;
CREATE TABLE `s_login` (
  `id` varchar(32) NOT NULL,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `logintime` datetime DEFAULT NULL,
  `ip` varchar(32) DEFAULT NULL,
  `locked` int(11) DEFAULT NULL,
  `loginnum` int(11) DEFAULT NULL,
  `creatdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_login
-- ----------------------------

-- ----------------------------
-- Table structure for `s_user`
-- ----------------------------
DROP TABLE IF EXISTS `s_user`;
CREATE TABLE `s_user` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `sex` char(2) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `idcard` varchar(18) DEFAULT NULL,
  `portrait` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_user
-- ----------------------------
