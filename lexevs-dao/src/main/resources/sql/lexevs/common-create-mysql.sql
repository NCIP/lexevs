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
TYPE=INNODB
;


CREATE TABLE @PREFIX@registryMetaData
(
	id char(1),
	lastUpdateTime DATETIME,
	lastUsedDBIdentifer VARCHAR(4),
	lastUsedHistoryIdentifer VARCHAR(4)
) 
TYPE=INNODB
;

CREATE TABLE @PREFIX@revision
(
	revisionGuid VARCHAR(36) NOT NULL,
	releaseGuid VARCHAR(36),
	revisionId VARCHAR(50) NOT NULL,
	changeAgent VARCHAR(50),
	revisionDate DATETIME,
	revAppliedDate DATETIME NOT NULL,
	editOrder DECIMAL(18),
	changeInstructions TEXT,
	description TEXT,
	PRIMARY KEY (revisionGuid),
	KEY (releaseGuid)
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@systemRelease
(
	releaseGuid VARCHAR(36) NOT NULL,
	releaseURI VARCHAR(250) NOT NULL,
	releaseId VARCHAR(50),
	releaseDate DATETIME NOT NULL,
	basedOnRelease VARCHAR(250),
	releaseAgency VARCHAR(250),
	description TEXT,
	PRIMARY KEY (releaseGuid)
)
TYPE=INNODB
;

SET FOREIGN_KEY_CHECKS=1;
