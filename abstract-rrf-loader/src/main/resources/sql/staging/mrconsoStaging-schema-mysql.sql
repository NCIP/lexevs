DROP TABLE IF EXISTS `@PREFIX@mrconsoStaging`;
CREATE TABLE `@PREFIX@mrconsoStaging` (
  `CUI` varchar(10) collate utf8_bin default NULL,
  `LAT` varchar(3) collate utf8_bin default NULL,
  `TS` varchar(1) collate utf8_bin default NULL,
  `LUI` varchar(9) collate utf8_bin default NULL,
  `STT` varchar(3) collate utf8_bin default NULL,
  `SUI` varchar(10) collate utf8_bin default NULL,
  `ISPREF` varchar(1) collate utf8_bin default NULL,
  `AUI` varchar(10) collate utf8_bin default NULL,
  `SAUI` varchar(100) collate utf8_bin default NULL,
  `SCUI` varchar(100) collate utf8_bin default NULL,
  `SDUI` varchar(100) collate utf8_bin default NULL,
  `SAB` varchar(40) collate utf8_bin default NULL,
  `TTY` varchar(20) collate utf8_bin default NULL,
  `CODE` varchar(100) collate utf8_bin default NULL,
  `STR` varchar(3000) collate utf8_bin default NULL,
  `SRL` varchar(20) collate utf8_bin default NULL,
  `SUPPRESS` varchar(1) collate utf8_bin default NULL,
  `CVF` varchar(50) collate utf8_bin default NULL,

  KEY `Index_2` (`CUI`,`AUI`),
  KEY `Index_3` (`CODE`),
  KEY `Index_4` (`SAB`,`CODE`),
  KEY `Index_5` (`AUI`)
) ENGINE=InnoDB AUTO_INCREMENT=1077279 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='InnoDB free: 4096 kB';