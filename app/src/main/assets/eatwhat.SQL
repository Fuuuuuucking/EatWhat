SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `eatwhat`;
CREATE TABLE `eatwhat` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `breakfast` tinyint(1) default NULL,
  `lunch` tinyint(1) default NULL,
  `dinner` tinyint(1) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `eatwhat` VALUES ('1', '香辣鸡排饭', '0', '1', '1');
INSERT INTO `eatwhat` VALUES ('2', '香辣牛排饭', '0', '1', '1');
INSERT INTO `eatwhat` VALUES ('3', '真味卤肉饭', '0', '1', '1');
INSERT INTO `eatwhat` VALUES ('4', '豆浆油条肉包', '1', '0', '0');
INSERT INTO `eatwhat` VALUES ('5', '蒸饺', '1', '0', '0');
INSERT INTO `eatwhat` VALUES ('6', '三鲜面', '1', '0', '1');
INSERT INTO `eatwhat` VALUES ('7', '辣椒炒肉面', '1', '0', '1');
INSERT INTO `eatwhat` VALUES ('8', '香辣排骨面', '1', '0', '1');
INSERT INTO `eatwhat` VALUES ('9', '皮蛋瘦肉粥', '1', '0', '1');
INSERT INTO `eatwhat` VALUES ('10', '木桶饭', '0', '1', '1');
INSERT INTO `eatwhat` VALUES ('11', '密卤鸭肉饭', '0', '1', '1');
INSERT INTO `eatwhat` VALUES ('12', '螺蛳粉', '0', '0', '1');
INSERT INTO `eatwhat` VALUES ('13', '水煮肉片', '0', '1', '0');
INSERT INTO `eatwhat` VALUES ('14', '西红柿炒蛋面', '1', '0', '0');
INSERT INTO `eatwhat` VALUES ('15', '三鲜粉', '1', '0', '1');
INSERT INTO `eatwhat` VALUES ('16', '辣椒炒肉粉', '1', '0', '1');
INSERT INTO `eatwhat` VALUES ('17', '香辣排骨粉', '1', '0', '1');
INSERT INTO `eatwhat` VALUES ('18', '西红柿炒蛋粉', '1', '0', '0');
INSERT INTO `eatwhat` VALUES ('19', '家常小炒', '0', '1', '1');
INSERT INTO `eatwhat` VALUES ('20', '酸辣粉', '0', '0', '1');
INSERT INTO `eatwhat` VALUES ('21', '茄子豆角饭', '0', '1', '1');
INSERT INTO `eatwhat` VALUES ('22', '石锅拌饭', '0', '1', '1');
INSERT INTO `eatwhat` VALUES ('23', '鸡腿饭', '0', '1', '1');
INSERT INTO `eatwhat` VALUES ('24', '红豆薏米粥', '1', '0', '0');
INSERT INTO `eatwhat` VALUES ('25', '黑米粥', '1', '0', '0');
INSERT INTO `eatwhat` VALUES ('26', '红豆粥', '1', '0', '0');
INSERT INTO `eatwhat` VALUES ('27', '八宝粥', '1', '0', '0');
INSERT INTO `eatwhat` VALUES ('28', '玉米粥', '1', '0', '0');
INSERT INTO `eatwhat` VALUES ('29', '绿豆粥', '1', '0', '0');
INSERT INTO `eatwhat` VALUES ('30', '香菇鸡丝粥', '1', '0', '1');
INSERT INTO `eatwhat` VALUES ('31', '鱼香茄子饭', '0', '1', '1');
INSERT INTO `eatwhat` VALUES ('32', '煎饺', '1', '0', '0');
INSERT INTO `eatwhat` VALUES ('33', '麻辣烫（少吃）+', '0', '0', '1');
INSERT INTO `eatwhat` VALUES ('34', '烧烤（少吃）+', '0', '0', '1');

