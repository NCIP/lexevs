SET FOREIGN_KEY_CHECKS=0;



CREATE TABLE @PREFIX@associationEntity
(
	associationEntityGuid VARCHAR(36) NOT NULL,
	entityGuid VARCHAR(36) NOT NULL,
	forwardName VARCHAR(100),
	reverseName VARCHAR(100),
	isNavigable CHAR(1),
	isTransitive CHAR(1),
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (associationEntityGuid),
	KEY (entityGuid)
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@associationPredicate
(
	associationPredicateGuid VARCHAR(36) NOT NULL,
	relationGuid VARCHAR(36) NOT NULL,
	associationEntityGuid VARCHAR(36),
	associationName VARCHAR(100),
	PRIMARY KEY (associationPredicateGuid),
	UNIQUE UQ_association(relationGuid, associationName),
	KEY (associationEntityGuid),
	KEY (relationGuid)
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@codingScheme
(
	codingSchemeGuid VARCHAR(36) NOT NULL,
	codingSchemeName VARCHAR(50) NOT NULL,
	codingSchemeURI VARCHAR(250) NOT NULL,
	representsVersion VARCHAR(50) NOT NULL,
	formalName VARCHAR(250),
	defaultLanguage VARCHAR(32),
	approxNumConcepts DECIMAL(18),
	description TEXT,
	copyright TEXT,
	isActive CHAR(1) DEFAULT 1,
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate DATETIME,
	expirationDate DATETIME,
	releaseGuid VARCHAR(36),
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (codingSchemeGuid),
	UNIQUE UQ_URIVersionPair(codingSchemeURI, representsVersion),
	KEY (releaseGuid),
	INDEX idx_csURI (codingSchemeURI ASC),
	INDEX idx_csURIVersion (codingSchemeURI ASC, representsVersion ASC),
	INDEX idx_csName (codingSchemeName ASC),
	INDEX idx_csNameVersion (codingSchemeName ASC, representsVersion ASC)
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@csMultiAttrib
(
	csMultiAttribGuid VARCHAR(36) NOT NULL,
	codingSchemeGuid VARCHAR(36) NOT NULL,
	attributeType VARCHAR(30) NOT NULL,
	attributeValue VARCHAR(250) NOT NULL,
	subRef VARCHAR(250),
	role VARCHAR(250),
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (csMultiAttribGuid),
	KEY (codingSchemeGuid),
	INDEX idx_csMultiAttrib (codingSchemeGuid ASC, attributeType ASC)
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@csSupportedAttrib
(
	csSuppAttribGuid VARCHAR(36) NOT NULL,
	codingSchemeGuid VARCHAR(36) NOT NULL,
	supportedAttributeTag VARCHAR(30) NOT NULL,
	id VARCHAR(250) NOT NULL,
	uri VARCHAR(250),
	idValue VARCHAR(250),
	rootCode VARCHAR(250),
	isForwardNavigable CHAR(1),
	isImported CHAR(1),
	equivalentCodingScheme VARCHAR(250),
	assemblyRule VARCHAR(250),
	PRIMARY KEY (csSuppAttribGuid),
	UNIQUE UQ_mapping(codingSchemeGuid, supportedAttributeTag, id),
	KEY (codingSchemeGuid)
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@entity
(
	entityGuid VARCHAR(36) NOT NULL,
	codingSchemeGuid VARCHAR(36) NOT NULL,
	entityCode VARCHAR(200) NOT NULL,
	entityCodeNamespace VARCHAR(50) NOT NULL,
	isDefined CHAR(1),
	isAnonymous CHAR(1),
	description TEXT,
	isActive CHAR(1) DEFAULT 1,
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate DATETIME,
	expirationDate DATETIME,
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (entityGuid),
	UNIQUE UQ_entity(codingSchemeGuid, entityCode, entityCodeNamespace),
	KEY (codingSchemeGuid),
	INDEX idx_entity (entityCode ASC, codingSchemeGuid ASC ),
	INDEX idx_entityNS (entityCodeNamespace ASC, entityCode ASC, codingSchemeGuid ASC )
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@entityAssnQuals
(
	entityAssnQualsGuid VARCHAR(36) NOT NULL,
	referenceGuid VARCHAR(36) NOT NULL,
	qualifierName VARCHAR(50) NOT NULL,
	qualifierValue VARCHAR(250),
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (entityAssnQualsGuid),
	UNIQUE UQ_entityAssnQuals(referenceGuid, qualifierName, qualifierValue)
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@entityAssnsToData
(
	entityAssnsDataGuid VARCHAR(36) NOT NULL,
	associationPredicateGuid VARCHAR(36) NOT NULL,
	sourceEntityCode VARCHAR(200) NOT NULL,
	sourceEntityCodeNamespace VARCHAR(50) NOT NULL,
	associationInstanceId VARCHAR(50),
	isDefining CHAR(1),
	isInferred CHAR(1),
	dataValue TEXT,
	isActive CHAR(1),
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate DATETIME,
	expirationDate DATETIME,
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (entityAssnsDataGuid),
	UNIQUE UQ_entAsToData_source(sourceEntityCode, sourceEntityCodeNamespace),
	KEY (associationPredicateGuid),
	INDEX idx_entAsToData_source (associationPredicateGuid ASC, sourceEntityCode ASC)
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@entityAssnsToEntity
(
	entityAssnsGuid VARCHAR(36) NOT NULL,
	associationPredicateGuid VARCHAR(36) NOT NULL,
	sourceEntityCode VARCHAR(200) NOT NULL,
	sourceEntityCodeNamespace VARCHAR(50) NOT NULL,
	targetEntityCode VARCHAR(200) NOT NULL,
	targetEntityCodeNamespace VARCHAR(50) NOT NULL,
	associationInstanceId VARCHAR(50),
	isDefining CHAR(1),
	isInferred CHAR(1),
	isActive CHAR(1),
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate DATETIME,
	expirationDate DATETIME,
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (entityAssnsGuid),
	KEY (associationPredicateGuid),
	INDEX idx_entAsToEnt_source (sourceEntityCode ASC, associationPredicateGuid ASC),
	INDEX idx_entAsToEnt_sourceNS (sourceEntityCodeNamespace ASC, sourceEntityCode ASC, associationPredicateGuid ASC),
	INDEX idx_entAsToEnt_target (targetEntityCode ASC, associationPredicateGuid ASC),
	INDEX idx_entAsToEnt_targetNS (targetEntityCodeNamespace ASC, targetEntityCode ASC, associationPredicateGuid ASC )
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@entityAssnsToEntityTr
(
	entityAssnsTrGuid VARCHAR(36) NOT NULL,
	associationPredicateGuid VARCHAR(36) NOT NULL,
	sourceEntityCode VARCHAR(200) NOT NULL,
	sourceEntityCodeNamespace VARCHAR(50) NOT NULL,
	targetEntityCode VARCHAR(200) NOT NULL,
	targetEntityCodeNamespace VARCHAR(50) NOT NULL,
	PRIMARY KEY (entityAssnsTrGuid),
	UNIQUE UQ_sourceTargetCombo(sourceEntityCode, sourceEntityCodeNamespace, targetEntityCode, targetEntityCodeNamespace, associationPredicateGuid),
	KEY (associationPredicateGuid),
	INDEX idx_entAsToEntTr_source (associationPredicateGuid ASC, sourceEntityCode ASC, sourceEntityCodeNamespace ASC),
	INDEX idx_entAsToEntTr_target (associationPredicateGuid ASC, targetEntityCode ASC, targetEntityCodeNamespace ASC)
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@entityType
(
	entityGuid VARCHAR(36) NOT NULL,
	entityType VARCHAR(50) NOT NULL,
	KEY (entityGuid)
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@entryState
(
	entryStateGuid VARCHAR(36) NOT NULL,
	entryGuid VARCHAR(36) NOT NULL,
	entryType VARCHAR(50) NOT NULL,
	changeType VARCHAR(15) NOT NULL,
	relativeOrder DECIMAL(18) NOT NULL,
	revisionGuid VARCHAR(36),
	prevRevisionGuid VARCHAR(36),
	prevEntryStateGuid VARCHAR(36),
	PRIMARY KEY (entryStateGuid),
	KEY (prevEntryStateGuid),
	KEY (revisionGuid),
	KEY (prevRevisionGuid)
)
TYPE=INNODB
;

CREATE TABLE @PREFIX@property
(
	propertyGuid VARCHAR(36) NOT NULL,
	referenceGuid VARCHAR(36) NOT NULL,
	referenceType VARCHAR(50) NOT NULL,
	propertyId VARCHAR(50),
	propertyType VARCHAR(15),
	propertyName VARCHAR(250) NOT NULL,
	language VARCHAR(32),
	format VARCHAR(50),
	isPreferred CHAR(1),
	matchIfNoContext CHAR(1),
	degreeOfFidelity VARCHAR(50),
	representationalForm VARCHAR(50),
	propertyValue TEXT NOT NULL,
	isActive CHAR(1),
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate DATETIME,
	expirationDate DATETIME,
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (propertyGuid),
	UNIQUE UQ_property(referenceGuid, propertyName, propertyId),
	INDEX idx_referenceGuid (referenceGuid ASC)
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@propertyLinks
(
	propertyLinksGuid VARCHAR(36) NOT NULL,
	sourcePropertyGuid VARCHAR(36) NOT NULL,
	link VARCHAR(250) NOT NULL,
	targetPropertyGuid VARCHAR(36) NOT NULL,
	PRIMARY KEY (propertyLinksGuid),
	UNIQUE UQ_propLinks(sourcePropertyGuid, link, targetPropertyGuid),
	INDEX idx_sourcePropertyGuid (sourcePropertyGuid ASC),
	INDEX idx_targetPropertyGuid (targetPropertyGuid ASC)
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@propertyMultiAttrib
(
	propMultiAttribGuid VARCHAR(36) NOT NULL,
	propertyGuid VARCHAR(36) NOT NULL,
	attributeType VARCHAR(30) NOT NULL,
	attributeId VARCHAR(50),
	attributeValue VARCHAR(250),
	subRef VARCHAR(250),
	role VARCHAR(250),
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (propMultiAttribGuid),
	INDEX idx_guid_attribType_attribId(propertyGuid, attributeType, attributeId),
	INDEX idx_propertyMultiAttrib (propertyGuid ASC)
) 
TYPE=INNODB
;


CREATE TABLE @PREFIX@relation
(
	relationGuid VARCHAR(36) NOT NULL,
	codingSchemeGuid VARCHAR(36) NOT NULL,
	containerName VARCHAR(50) NOT NULL,
	isMapping CHAR(1),
	sourceCodingScheme VARCHAR(50),
	sourceCodingSchemeVersion VARCHAR(50),
	targetCodingScheme VARCHAR(50),
	targetCodingSchemeVersion VARCHAR(50),
	description TEXT,
	isActive CHAR(1),
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate DATETIME,
	expirationDate DATETIME,
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (relationGuid),
	UNIQUE UQ_csGuid_containerName(codingSchemeGuid, containerName),
	KEY (codingSchemeGuid)
) 
TYPE=INNODB
;

SET FOREIGN_KEY_CHECKS=1;


ALTER TABLE @PREFIX@associationEntity ADD CONSTRAINT @PREFIX@FK_associationEntity_entity 
	FOREIGN KEY (entityGuid) REFERENCES @PREFIX@entity (entityGuid)
	ON DELETE CASCADE
;

ALTER TABLE @PREFIX@associationPredicate ADD CONSTRAINT @PREFIX@FK_associationPr_associationEn 
	FOREIGN KEY (associationEntityGuid) REFERENCES @PREFIX@entity (entityGuid)
;

ALTER TABLE @PREFIX@associationPredicate ADD CONSTRAINT @PREFIX@FK_associationPredica_relation 
	FOREIGN KEY (relationGuid) REFERENCES @PREFIX@relation (relationGuid)
	ON DELETE CASCADE
;

ALTER TABLE @PREFIX@codingScheme ADD CONSTRAINT @PREFIX@FK_cs_releaseGuid 
	FOREIGN KEY (releaseGuid) REFERENCES @DEFAULT_PREFIX@systemRelease (releaseGuid)
;

ALTER TABLE @PREFIX@csMultiAttrib ADD CONSTRAINT @PREFIX@FK_csMulti_csGuid 
	FOREIGN KEY (codingSchemeGuid) REFERENCES @PREFIX@codingScheme (codingSchemeGuid)
	ON DELETE CASCADE
;

ALTER TABLE @PREFIX@csSupportedAttrib ADD CONSTRAINT @PREFIX@FK_map_csGuid 
	FOREIGN KEY (codingSchemeGuid) REFERENCES @PREFIX@codingScheme (codingSchemeGuid)
	ON DELETE CASCADE
;

ALTER TABLE @PREFIX@entity ADD CONSTRAINT @PREFIX@FK_ent_csGuid 
	FOREIGN KEY (codingSchemeGuid) REFERENCES @PREFIX@codingScheme (codingSchemeGuid)
	ON DELETE CASCADE
;

ALTER TABLE @PREFIX@entityAssnsToData ADD CONSTRAINT @PREFIX@FK_entAsToData_assnGuid 
	FOREIGN KEY (associationPredicateGuid) REFERENCES @PREFIX@associationPredicate (associationPredicateGuid)
	ON DELETE CASCADE
;

ALTER TABLE @PREFIX@entityAssnsToEntity ADD CONSTRAINT @PREFIX@FK_entAsToEnt_assnGuid 
	FOREIGN KEY (associationPredicateGuid) REFERENCES @PREFIX@associationPredicate (associationPredicateGuid)
	ON DELETE CASCADE
;

ALTER TABLE @PREFIX@entityAssnsToEntityTr ADD CONSTRAINT @PREFIX@FK_entAsToEntTr_assnGuid 
	FOREIGN KEY (associationPredicateGuid) REFERENCES @PREFIX@associationPredicate (associationPredicateGuid)
	ON DELETE CASCADE
;

ALTER TABLE @PREFIX@entityType ADD CONSTRAINT @PREFIX@FK_eType_entityGuid 
	FOREIGN KEY (entityGuid) REFERENCES @PREFIX@entity (entityGuid)
	ON DELETE CASCADE
;

ALTER TABLE @PREFIX@entryState ADD CONSTRAINT @PREFIX@FK_es_prevEntryStateGuid 
	FOREIGN KEY (prevEntryStateGuid) REFERENCES @PREFIX@entryState (entryStateGuid)
;

ALTER TABLE @PREFIX@entryState ADD CONSTRAINT @PREFIX@FK_es_revisionGuid 
	FOREIGN KEY (revisionGuid) REFERENCES @DEFAULT_PREFIX@revision (revisionGuid)
;

ALTER TABLE @PREFIX@entryState ADD CONSTRAINT @PREFIX@FK_es_prevRevisionGuid 
	FOREIGN KEY (prevRevisionGuid) REFERENCES @DEFAULT_PREFIX@revision (revisionGuid)
;

ALTER TABLE @PREFIX@propertyLinks ADD CONSTRAINT @PREFIX@FK_pLinks_sPropGuid 
	FOREIGN KEY (sourcePropertyGuid) REFERENCES @PREFIX@property (propertyGuid)
	ON DELETE CASCADE
;

ALTER TABLE @PREFIX@propertyLinks ADD CONSTRAINT @PREFIX@FK_pLinks_tPropGuid 
	FOREIGN KEY (targetPropertyGuid) REFERENCES @PREFIX@property (propertyGuid)
	ON DELETE CASCADE
;

ALTER TABLE @PREFIX@propertyMultiAttrib ADD CONSTRAINT @PREFIX@FK_pma_propertyGuid 
	FOREIGN KEY (propertyGuid) REFERENCES @PREFIX@property (propertyGuid)
	ON DELETE CASCADE
;

ALTER TABLE @PREFIX@relation ADD CONSTRAINT @PREFIX@FK_relation_codingScheme 
	FOREIGN KEY (codingSchemeGuid) REFERENCES @PREFIX@codingScheme (codingSchemeGuid)
	ON DELETE CASCADE
;

