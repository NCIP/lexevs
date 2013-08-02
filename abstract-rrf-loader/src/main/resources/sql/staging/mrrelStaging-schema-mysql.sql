DROP TABLE IF EXISTS `@PREFIX@MRREL`;
CREATE TABLE `@PREFIX@MRREL` (
  `CUI1` varchar(8) collate utf8_bin default NULL,
  `AUI1` varchar(8) collate utf8_bin default NULL,
  `STYPE1` varchar(50) collate utf8_bin default NULL,
  `REL` varchar(4) collate utf8_bin default NULL,
  `CUI2` varchar(8) collate utf8_bin default NULL,
  `AUI2` varchar(8) collate utf8_bin default NULL,
  `STYPE2` varchar(50) collate utf8_bin default NULL,
  `RELA` varchar(100) collate utf8_bin default NULL,
  `RUI` varchar(10) collate utf8_bin default NULL,
  `SRUI` varchar(50) collate utf8_bin default NULL,
  `SAB` varchar(40) collate utf8_bin default NULL,
  `SL` varchar(40) collate utf8_bin default NULL,
  `RG` varchar(10) collate utf8_bin default NULL,
  `DIR` varchar(1) collate utf8_bin default NULL,
  `SUPPRESS` varchar(1) collate utf8_bin default NULL,
  `CVF` varchar(50) collate utf8_bin default NULL,
  KEY `mi4` (`RELA`),
  KEY `mi5` (`REL`),
  KEY `mi6` (`RUI`),
  KEY `mi7` (`SAB`,`RELA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
