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

CREATE TABLE @PREFIX@registryentry
(
	id VARCHAR(50) NOT NULL,
	activationDate TIMESTAMP,
	baseRevision VARCHAR(50),
	dbName VARCHAR(50),
	dbSchemaDescription VARCHAR(250),
	dbSchemaVersion VARCHAR(50),
	dbUrl VARCHAR(50),
	deactivationDate TIMESTAMP,
	fixedAtRevision VARCHAR(50),
	isLocked BOOLEAN,
	lastUpdateDate TIMESTAMP,
	prefix VARCHAR(50),
	resourceType VARCHAR(50),
	resourceUri VARCHAR(50) NOT NULL,
	resourceVersion VARCHAR(50) NOT NULL,
	status VARCHAR(50),
	tag VARCHAR(50),
	version VARCHAR(50) NOT NULL
) 
;


