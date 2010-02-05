CREATE TABLE @PREFIX@lexGridTableMetaData
(
	version VARCHAR(50) NOT NULL,
	description VARCHAR(255)
) 
;

INSERT INTO @PREFIX@lexGridTableMetaData (
	version, 
	description)
	VALUES ('2.0','LexGrid Database Table Schema Version 2.0')
;

CREATE TABLE @PREFIX@registry
(
	id INTEGER NOT NULL,
	lastUsedDBIdentifer VARCHAR(50) NOT NULL,
	lastUsedHistoryIdentifer VARCHAR(250)
) 
;

INSERT INTO @PREFIX@registry (
	id, lastUsedDBIdentifer, 
	lastUsedHistoryIdentifer)
	VALUES (0,'000','000')
;


