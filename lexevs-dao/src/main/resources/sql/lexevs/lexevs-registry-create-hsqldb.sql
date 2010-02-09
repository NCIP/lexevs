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
	lastUpdateTime TIMESTAMP,
	lastUsedDbIdentifer VARCHAR(50) NOT NULL,
	lastUsedHistoryIdentifer VARCHAR(250)
) 
;

INSERT INTO @PREFIX@registry 
(
	id, 
	lastUpdateTime,
	lastUsedDbIdentifer, 
	lastUsedHistoryIdentifer
)
	VALUES (0, CURTIME(), 'aaa','aaa')
;

CREATE TABLE @PREFIX@codingschemeentry
(
	id VARCHAR(50) NOT NULL,
	uri VARCHAR(50) NOT NULL,
	prefix VARCHAR(50),
	status VARCHAR(50),
	tag VARCHAR(50),
	version VARCHAR(50) NOT NULL,
	deactivateDate TIMESTAMP,
	lastUpdateDate TIMESTAMP
) 
;

CREATE TABLE @PREFIX@historyentry
(
	id VARCHAR(50) NOT NULL,
	uri VARCHAR(50) NOT NULL,
	prefix VARCHAR(50),
	version VARCHAR(50) NOT NULL,
	lastUpdateDate TIMESTAMP
) 
;


