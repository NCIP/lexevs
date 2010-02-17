SET FOREIGN_KEY_CHECKS=0;



CREATE TABLE @PREFIX@registry
(
	registryGuid VARCHAR(36) NOT NULL,
	resourceURI VARCHAR(250),
	resourceVersion VARCHAR(50),
	resourceType VARCHAR(50),
	dbURI VARCHAR(250),
	dbName VARCHAR(50),
	prefix VARCHAR(20),
	status VARCHAR(50),
	tag VARCHAR(50),
	lastUpdateDate DATETIME,
	activationDate DATETIME,
	deactivationDate DATETIME,
	baseRevision VARCHAR(50),
	fixedAtRevision VARCHAR(50),
	isLocked CHAR(1),
	dbSchemaVersion VARCHAR(50),
	dbSchemaDescription VARCHAR(255),
	PRIMARY KEY (registryGuid),
	UNIQUE UQ_registry(resourceURI, resourceVersion, resourceType)
) 
;


CREATE TABLE @PREFIX@registryMetaData
(
	id char(1),
	lastUpdateTime DATETIME,
	lastUsedDBIdentifer VARCHAR(4),
	lastUsedHistoryIdentifer VARCHAR(4)
) 
;

INSERT INTO @PREFIX@registryMetadata VALUES ('0', NOW(), 'aaaa', 'aaaa');


SET FOREIGN_KEY_CHECKS=1;
