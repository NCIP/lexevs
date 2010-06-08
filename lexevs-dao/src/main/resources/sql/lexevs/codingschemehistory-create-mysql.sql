SET FOREIGN_KEY_CHECKS=0;



CREATE TABLE @PREFIX@h_codingScheme
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
	effectiveDate TIMESTAMP NULL,
	expirationDate TIMESTAMP NULL,
	releaseGuid VARCHAR(36),
	entryStateGuid VARCHAR(36) NOT NULL,
	PRIMARY KEY (entryStateGuid),
	KEY (releaseGuid),
	INDEX idx_h_codingSchemeURI (codingSchemeURI ASC)
) 
;


CREATE TABLE @PREFIX@h_csMultiAttrib
(
	csMultiAttribGuid VARCHAR(36) NOT NULL,
	codingSchemeGuid VARCHAR(36) NOT NULL,
	attributeType VARCHAR(30) NOT NULL,
	attributeValue VARCHAR(250) NOT NULL,
	subRef VARCHAR(250),
	role VARCHAR(250),
	entryStateGuid VARCHAR(36) NOT NULL,
	INDEX idx_h_csMultiAttrib (codingSchemeGuid ASC, attributeType ASC)
) 
;


CREATE TABLE @PREFIX@h_entity
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
	effectiveDate TIMESTAMP NULL,
	expirationDate TIMESTAMP NULL,
	entryStateGuid VARCHAR(36) NOT NULL,
	forwardName VARCHAR(100),
	reverseName VARCHAR(100),
	isNavigable CHAR(1),
	isTransitive CHAR(1),
	PRIMARY KEY (entryStateGuid),
	INDEX idx_h_entity (codingSchemeGuid ASC, entityCode ASC),
	INDEX idx_h_entityNS (codingSchemeGuid ASC, entityCode ASC, entityCodeNamespace ASC)
) 
;


CREATE TABLE @PREFIX@h_entityAssnQuals
(
	entityAssnQualsGuid VARCHAR(36) NOT NULL,
	referenceGuid VARCHAR(36) NOT NULL,
	qualifierName VARCHAR(50) NOT NULL,
	qualifierValue VARCHAR(250),
	entryStateGuid VARCHAR(36) NOT NULL
) 
;


CREATE TABLE @PREFIX@h_entityAssnsToData
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
	effectiveDate TIMESTAMP NULL,
	expirationDate TIMESTAMP NULL,
	entryStateGuid VARCHAR(36) NOT NULL,
	PRIMARY KEY (entryStateGuid),
	INDEX idx_h_entAsToData_source (associationPredicateGuid ASC, sourceEntityCode ASC)
) 
;


CREATE TABLE @PREFIX@h_entityAssnsToEntity
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
	effectiveDate TIMESTAMP NULL,
	expirationDate TIMESTAMP NULL,
	entryStateGuid VARCHAR(36) NOT NULL,
	PRIMARY KEY (entryStateGuid),
	INDEX idx_h_entAsToEnt_source (associationPredicateGuid ASC, sourceEntityCode ASC),
	INDEX idx_h_entAsToEnt_sourceNS (associationPredicateGuid ASC, sourceEntityCode ASC, sourceEntityCodeNamespace ASC),
	INDEX idx_h_entAsToEnt_target (associationPredicateGuid ASC, targetEntityCode ASC),
	INDEX idx_h_entAsToEnt_targetNS (associationPredicateGuid ASC, targetEntityCode ASC, targetEntityCodeNamespace ASC)
) 
;


CREATE TABLE @PREFIX@h_property
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
	effectiveDate TIMESTAMP NULL,
	expirationDate TIMESTAMP NULL,
	entryStateGuid VARCHAR(36) NOT NULL,
	PRIMARY KEY (entryStateGuid),
	INDEX idx_h_referenceGuid (referenceGuid ASC)
) 
;


CREATE TABLE @PREFIX@h_propertyLinks
(
	propertyLinksGuid VARCHAR(36) NOT NULL,
	sourcePropertyGuid VARCHAR(36) NOT NULL,
	link VARCHAR(250) NOT NULL,
	targetPropertyGuid VARCHAR(36) NOT NULL,
	entryStateGuid VARCHAR(36) NOT NULL,
	PRIMARY KEY (entryStateGuid),
	INDEX idx_h_sourcePropertyGuid (sourcePropertyGuid ASC),
	INDEX idx_h_targetPropertyGuid (targetPropertyGuid ASC)
) 
;


CREATE TABLE @PREFIX@h_propertyMultiAttrib
(
	propMultiAttribGuid VARCHAR(36) NOT NULL,
	propertyGuid VARCHAR(36) NOT NULL,
	attributeType VARCHAR(30) NOT NULL,
	attributeId VARCHAR(50),
	attributeValue VARCHAR(250),
	subRef VARCHAR(250),
	role VARCHAR(250),
	qualifierType VARCHAR(250),
	entryStateGuid VARCHAR(36) NOT NULL,
	INDEX idx_h_propertyMultiAttrib (propertyGuid ASC)
) 
;


CREATE TABLE @PREFIX@h_relation
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
	effectiveDate TIMESTAMP NULL,
	expirationDate TIMESTAMP NULL,
	entryStateGuid VARCHAR(36) NOT NULL,
	PRIMARY KEY (entryStateGuid)
) 
;



ALTER TABLE @PREFIX@h_codingScheme ADD CONSTRAINT FK_h_codingScheme_codingScheme 
	FOREIGN KEY (codingSchemeGuid) REFERENCES @PREFIX@codingScheme (codingSchemeGuid)
ON DELETE CASCADE
;

ALTER TABLE @PREFIX@h_csMultiAttrib ADD CONSTRAINT FK_h_csMultiAttr_csMultiAttrib 
	FOREIGN KEY (csMultiAttribGuid) REFERENCES @PREFIX@csMultiAttrib (csMultiAttribGuid)
ON DELETE CASCADE
;

ALTER TABLE @PREFIX@h_entity ADD CONSTRAINT FK_h_entity_entity 
	FOREIGN KEY (entityGuid) REFERENCES @PREFIX@entity (entityGuid)
ON DELETE CASCADE
;

ALTER TABLE @PREFIX@h_entityAssnQuals ADD CONSTRAINT FK_h_entityAssnQ_entityAssnQua 
	FOREIGN KEY (entityAssnQualsGuid) REFERENCES @PREFIX@entityAssnQuals (entityAssnQualsGuid)
ON DELETE CASCADE
;

ALTER TABLE @PREFIX@h_entityAssnsToData ADD CONSTRAINT FK_h_entityAssnsD_entityAssnsD 
	FOREIGN KEY (entityAssnsDataGuid) REFERENCES @PREFIX@entityAssnsToData (entityAssnsDataGuid)
ON DELETE CASCADE
;

ALTER TABLE @PREFIX@h_entityAssnsToEntity ADD CONSTRAINT FK_h_entityAssnsE_entityAssnsE 
	FOREIGN KEY (entityAssnsGuid) REFERENCES @PREFIX@entityAssnsToEntity (entityAssnsGuid)
ON DELETE CASCADE
;

ALTER TABLE @PREFIX@h_property ADD CONSTRAINT FK_h_property_property 
	FOREIGN KEY (propertyGuid) REFERENCES @PREFIX@property (propertyGuid)
ON DELETE CASCADE
;

ALTER TABLE @PREFIX@h_propertyLinks ADD CONSTRAINT FK_h_propertyLin_propertyLinks 
	FOREIGN KEY (propertyLinksGuid) REFERENCES @PREFIX@propertyLinks (propertyLinksGuid)
ON DELETE CASCADE
;

ALTER TABLE @PREFIX@h_propertyMultiAttrib ADD CONSTRAINT FK_h_propertyMul_propertyMulti 
	FOREIGN KEY (propMultiAttribGuid) REFERENCES @PREFIX@propertyMultiAttrib (propMultiAttribGuid)
ON DELETE CASCADE
;

ALTER TABLE @PREFIX@h_relation ADD CONSTRAINT FK_h_relation_relation 
	FOREIGN KEY (relationGuid) REFERENCES @PREFIX@relation (relationGuid)
ON DELETE CASCADE
;

SET FOREIGN_KEY_CHECKS=1;