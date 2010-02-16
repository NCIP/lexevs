CREATE TABLE associationEntity ( 
	associationEntityGuid varchar(36) NOT NULL,
	entityGuid varchar(36) NOT NULL,
	forwardName varchar(100),
	reverseName varchar(100),
	isNavigable char(1),
	isTransitive char(1),
	entryStateGuid varchar(36)
)
;

CREATE TABLE @PREFIX@associationPredicate ( 
	associationPredicateGuid varchar(36) NOT NULL,
	relationGuid varchar(36) NOT NULL,
	associationEntityGuid varchar(36),
	associationName varchar(100)
)
;

CREATE TABLE @PREFIX@codingScheme ( 
	codingSchemeGuid varchar(36) NOT NULL,
	codingSchemeName varchar(50) NOT NULL,
	codingSchemeURI varchar(250) NOT NULL,
	representsVersion varchar(50) NOT NULL,
	formalName varchar(250),
	defaultLanguage varchar(32),
	approxNumConcepts decimal(18),
	description varchar,
	copyright varchar,
	isActive char(1) DEFAULT 1,
	owner varchar(250),
	status varchar(50),
	effectiveDate timestamp,
	expirationDate timestamp,
	releaseGuid varchar(36),
	entryStateGuid varchar(36)
)
;

CREATE TABLE @PREFIX@csMultiAttrib ( 
	csMultiAttribGuid varchar(36) NOT NULL,
	codingSchemeGuid varchar(36) NOT NULL,
	attributeType varchar(30) NOT NULL,
	attributeValue varchar(250) NOT NULL,
	subRef varchar(250),
	role varchar(250),
	entryStateGuid varchar(36)
)
;

CREATE TABLE @PREFIX@csSupportedAttrib ( 
	csSuppAttribGuid varchar(36) NOT NULL,
	codingSchemeGuid varchar(36) NOT NULL,
	supportedAttributeTag varchar(30) NOT NULL,
	id varchar(250) NOT NULL,
	uri varchar(250),
	idValue varchar(250),
	rootCode varchar(250),
	isForwardNavigable char(1),
	isImported char(1),
	equivalentCodingScheme varchar(250),
	assemblyRule varchar(250)
)
;

CREATE TABLE @PREFIX@entity ( 
	entityGuid varchar(36) NOT NULL,
	codingSchemeGuid varchar(36) NOT NULL,
	entityCode varchar(200) NOT NULL,
	entityCodeNamespace varchar(50) NOT NULL,
	isDefined char(1),
	isAnonymous char(1),
	description varchar,
	isActive char(1) DEFAULT 1,
	owner varchar(250),
	status varchar(50),
	effectiveDate timestamp,
	expirationDate timestamp,
	entryStateGuid varchar(36)
)
;

CREATE TABLE @PREFIX@entityAssnQuals ( 
	entityAssnQualsGuid varchar(36) NOT NULL,
	referenceGuid varchar(36) NOT NULL,
	qualifierName varchar(50) NOT NULL,
	qualifierValue varchar(250) NOT NULL,
	entryStateGuid varchar(36)
)
;

CREATE TABLE @PREFIX@entityAssnsToData ( 
	entityAssnsDataGuid varchar(36) NOT NULL,
	associationPredicateGuid varchar(36) NOT NULL,
	sourceEntityCode varchar(200) NOT NULL,
	sourceEntityCodeNamespace varchar(50) NOT NULL,
	associationInstanceId varchar(50),
	isDefining char(1),
	isInferred char(1),
	dataValue varchar,
	isActive char(1),
	owner varchar(250),
	status varchar(50),
	effectiveDate timestamp,
	expirationDate timestamp,
	entryStateGuid varchar(36)
)
;

CREATE TABLE @PREFIX@entityAssnsToEntity ( 
	entityAssnsGuid varchar(36) NOT NULL,
	associationPredicateGuid varchar(36) NOT NULL,
	sourceEntityCode varchar(200) NOT NULL,
	sourceEntityCodeNamespace varchar(50) NOT NULL,
	targetEntityCode varchar(200) NOT NULL,
	targetEntityCodeNamespace varchar(50) NOT NULL,
	associationInstanceId varchar(50),
	isDefining char(1),
	isInferred char(1),
	isActive char(1),
	owner varchar(250),
	status varchar(50),
	effectiveDate timestamp,
	expirationDate timestamp,
	entryStateGuid varchar(36)
)
;

CREATE TABLE @PREFIX@entityAssnsToEntityTr ( 
	entityAssnsTrGuid varchar(36) NOT NULL,
	associationPredicateGuid varchar(36) NOT NULL,
	sourceEntityCode varchar(200) NOT NULL,
	sourceEntityCodeNamespace varchar(50) NOT NULL,
	targetEntityCode varchar(200) NOT NULL,
	targetEntityCodeNamespace varchar(50) NOT NULL
)
;

CREATE TABLE @PREFIX@entityType ( 
	entityGuid varchar(36) NOT NULL,
	entityType varchar(50) NOT NULL
)
;

CREATE TABLE @PREFIX@entryState ( 
	entryStateGuid varchar(36) NOT NULL,
	entryGuid varchar(36) NOT NULL,
	entryType varchar(50) NOT NULL,
	changeType varchar(15) NOT NULL,
	relativeOrder decimal(18) NOT NULL,
	revisionGuid varchar(36),
	prevRevisionGuid varchar(36),
	prevEntryStateGuid varchar(36)
)
;


CREATE TABLE @PREFIX@property ( 
	propertyGuid varchar(36) NOT NULL,
	referenceGuid varchar(36) NOT NULL,
	referenceType varchar(50) NOT NULL,
	propertyId varchar(50),
	propertyType varchar(15),
	propertyName varchar(250) NOT NULL,
	language varchar(32),
	format varchar(50),
	isPreferred char(1),
	matchIfNoContext char(1),
	degreeOfFidelity varchar(50),
	representationalForm varchar(50),
	propertyValue varchar NOT NULL,
	isActive char(1),
	owner varchar(250),
	status varchar(50),
	effectiveDate timestamp,
	expirationDate timestamp,
	entryStateGuid varchar(36)
)
;

CREATE TABLE @PREFIX@propertyLinks ( 
	propertyLinksGuid varchar(36) NOT NULL,
	sourcePropertyGuid varchar(36) NOT NULL,
	link varchar(250) NOT NULL,
	targetPropertyGuid varchar(36) NOT NULL
)
;

CREATE TABLE @PREFIX@propertyMultiAttrib ( 
	propMultiAttribGuid varchar(36) NOT NULL,
	propertyGuid varchar(36) NOT NULL,
	attributeType varchar(30) NOT NULL,
	attributeId varchar(50),
	attributeValue varchar(250),
	subRef varchar(250),
	role varchar(250),
	entryStateGuid varchar(36)
)
;

CREATE TABLE @PREFIX@relation ( 
	relationGuid varchar(36) NOT NULL,
	codingSchemeGuid varchar(36) NOT NULL,
	containerName varchar(50) NOT NULL,
	isMapping char(1),
	sourceCodingScheme varchar(50),
	sourceCodingSchemeVersion varchar(50),
	targetCodingScheme varchar(50),
	targetCodingSchemeVersion varchar(50),
	description varchar,
	isActive char(1),
	owner varchar(250),
	status varchar(50),
	effectiveDate timestamp,
	expirationDate timestamp,
	entryStateGuid varchar(36)
)
;

ALTER TABLE @PREFIX@associationPredicate
	ADD CONSTRAINT UQ_association UNIQUE (relationGuid, associationName)
;
ALTER TABLE @PREFIX@codingScheme
	ADD CONSTRAINT UQ_URIVersionPair UNIQUE (codingSchemeURI, representsVersion)
;
CREATE INDEX idx_csURI
ON @PREFIX@codingScheme (codingSchemeURI)
;
CREATE INDEX idx_csURIVersion
ON @PREFIX@codingScheme (codingSchemeURI, representsVersion)
;
CREATE INDEX idx_csName
ON @PREFIX@codingScheme (codingSchemeName)
;
CREATE INDEX idx_csNameVersion
ON @PREFIX@codingScheme (codingSchemeName, representsVersion)
;

CREATE INDEX idx_csMultiAttrib
ON @PREFIX@csMultiAttrib (codingSchemeGuid, attributeType)
;
ALTER TABLE @PREFIX@csSupportedAttrib
	ADD CONSTRAINT UQ_mapping UNIQUE (codingSchemeGuid, supportedAttributeTag, id)
;
ALTER TABLE @PREFIX@entity
	ADD CONSTRAINT UQ_entity UNIQUE (codingSchemeGuid, entityCode, entityCodeNamespace)
;
CREATE INDEX idx_entity
ON @PREFIX@entity (codingSchemeGuid, entityCode)
;
CREATE INDEX idx_entityNS
ON @PREFIX@entity (codingSchemeGuid, entityCode, entityCodeNamespace)
;
ALTER TABLE @PREFIX@entityAssnQuals
	ADD CONSTRAINT UQ_entityAssnQuals UNIQUE (referenceGuid, qualifierName, qualifierValue)
;
ALTER TABLE @PREFIX@entityAssnsToData
	ADD CONSTRAINT UQ_entAsToData_source UNIQUE (sourceEntityCode, sourceEntityCodeNamespace)
;
CREATE INDEX idx_entAsToData_source
ON @PREFIX@entityAssnsToData (associationPredicateGuid, sourceEntityCode)
;
CREATE INDEX idx_entAsToEnt_source
ON @PREFIX@entityAssnsToEntity (associationPredicateGuid, sourceEntityCode)
;
CREATE INDEX idx_entAsToEnt_sourceNS
ON @PREFIX@entityAssnsToEntity (associationPredicateGuid, sourceEntityCode, sourceEntityCodeNamespace)
;
CREATE INDEX idx_entAsToEnt_target
ON @PREFIX@entityAssnsToEntity (associationPredicateGuid, targetEntityCode)
;
CREATE INDEX idx_entAsToEnt_targetNS
ON @PREFIX@entityAssnsToEntity (associationPredicateGuid, targetEntityCode, targetEntityCodeNamespace)
;
ALTER TABLE @PREFIX@entityAssnsToEntityTr
	ADD CONSTRAINT UQ_sourceTargetCombo UNIQUE (sourceEntityCode, sourceEntityCodeNamespace, targetEntityCode, targetEntityCodeNamespace)
;
CREATE INDEX idx_entAsToEntTr_source
ON @PREFIX@entityAssnsToEntityTr (associationPredicateGuid, sourceEntityCode, sourceEntityCodeNamespace)
;
CREATE INDEX idx_entAsToEntTr_target
ON @PREFIX@entityAssnsToEntityTr (associationPredicateGuid, targetEntityCode, targetEntityCodeNamespace)
;
ALTER TABLE @PREFIX@property
	ADD CONSTRAINT UQ_property UNIQUE (referenceGuid, propertyName, propertyId)
;
CREATE INDEX idx_referenceGuid
ON @PREFIX@property (referenceGuid)
;
ALTER TABLE @PREFIX@propertyLinks
	ADD CONSTRAINT UQ_propLinks UNIQUE (sourcePropertyGuid, link, targetPropertyGuid)
;
CREATE INDEX idx_sourcePropertyGuid
ON @PREFIX@propertyLinks (sourcePropertyGuid)
;
CREATE INDEX idx_targetPropertyGuid
ON @PREFIX@propertyLinks (targetPropertyGuid)
;
CREATE INDEX idx_propertyMultiAttrib
ON @PREFIX@propertyMultiAttrib (propertyGuid)
;
ALTER TABLE @PREFIX@propertyMultiAttrib
	ADD CONSTRAINT UQ_propertyMultiAttrib UNIQUE (propertyGuid, attributeType, attributeId)
;
ALTER TABLE @PREFIX@relation
	ADD CONSTRAINT UQ_relation_codingSchemeGuid UNIQUE (codingSchemeGuid)
;
ALTER TABLE @PREFIX@relation
	ADD CONSTRAINT UQ_relation_containerName UNIQUE (containerName)
;
