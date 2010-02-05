DROP TABLE IF EXISTS `@PREFIX@stagingkeyvalue`;
CREATE TABLE `@PREFIX@stagingkeyvalue` (
  `aui` varchar(15) collate utf8_bin NOT NULL,
  `cui` varchar(15) collate utf8_bin NOT NULL,
  PRIMARY KEY  (`aui`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

