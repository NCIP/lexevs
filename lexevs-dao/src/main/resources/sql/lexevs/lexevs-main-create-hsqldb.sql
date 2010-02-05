CREATE TABLE @PREFIX@associationEntity
(
	associationEntityGuid VARCHAR(36) NOT NULL,
	entityGuid VARCHAR(36) NOT NULL,
	forwardName VARCHAR(100),
	reverseName VARCHAR(100),
	isNavigable CHAR(1),
	isTransitive CHAR(1),
	isTranslationAssociation CHAR(1),
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (associationEntityGuid)
) 
;


CREATE TABLE @PREFIX@associationPredicate
(
	associationPredicateGuid VARCHAR(36) NOT NULL,
	relationGuid VARCHAR(36) NOT NULL,
	associationEntityGuid VARCHAR(36),
	associationName VARCHAR(100),
	PRIMARY KEY (associationPredicateGuid)
) 
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
	description VARCHAR,
	copyright VARCHAR,
	isActive CHAR(1) DEFAULT 1,
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate TIMESTAMP,
	expirationDate TIMESTAMP,
	releaseGuid VARCHAR(36),
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (codingSchemeGuid)
) 
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
	PRIMARY KEY (csMultiAttribGuid)
) 
;


CREATE TABLE @PREFIX@entity
(
	entityGuid VARCHAR(36) NOT NULL,
	codingSchemeGuid VARCHAR(36) NOT NULL,
	entityCode VARCHAR(200) NOT NULL,
	entityCodeNamespace VARCHAR(50) NOT NULL,
	isDefined CHAR(1),
	isAnonymous CHAR(1),
	description VARCHAR,
	isActive CHAR(1) DEFAULT 1,
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate TIMESTAMP,
	expirationDate TIMESTAMP,
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (entityGuid)
) 
;


CREATE TABLE @PREFIX@entityAssnQuals
(
	entityAssnQualsGuid VARCHAR(36) NOT NULL,
	referenceGuid VARCHAR(36) NOT NULL,
	qualifierName VARCHAR(50) NOT NULL,
	qualifierValue VARCHAR(250) NOT NULL,
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (entityAssnQualsGuid)
) 
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
	dataValue VARCHAR,
	isActive CHAR(1),
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate TIMESTAMP,
	expirationDate TIMESTAMP,
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (entityAssnsDataGuid)
) 
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
	effectiveDate TIMESTAMP,
	expirationDate TIMESTAMP,
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (entityAssnsGuid)
) 
;


CREATE TABLE @PREFIX@entityAssnsToEntityTr
(
	entityAssnsTrGuid VARCHAR(36) NOT NULL,
	associationPredicateGuid VARCHAR(36) NOT NULL,
	sourceEntityCode VARCHAR(200) NOT NULL,
	sourceEntityCodeNamespace VARCHAR(50) NOT NULL,
	targetEntityCode VARCHAR(200) NOT NULL,
	targetEntityCodeNamespace VARCHAR(50) NOT NULL,
	PRIMARY KEY (entityAssnsTrGuid)
) 
;


CREATE TABLE @PREFIX@entityType
(
	entityGuid VARCHAR(36) NOT NULL,
	entityType VARCHAR(50) NOT NULL
) 
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
	PRIMARY KEY (entryStateGuid)
) 
;


CREATE TABLE @PREFIX@mapping
(
	mappingGuid VARCHAR(36) NOT NULL,
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
	conceptDomainCodingScheme VARCHAR(250),
	PRIMARY KEY (mappingGuid)
) 
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
	propertyValue VARCHAR NOT NULL,
	isActive CHAR(1),
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate TIMESTAMP,
	expirationDate TIMESTAMP,
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (propertyGuid)
) 
;


CREATE TABLE @PREFIX@propertyLinks
(
	propertyLinksGuid VARCHAR(36) NOT NULL,
	sourcePropertyGuid VARCHAR(36) NOT NULL,
	link VARCHAR(250) NOT NULL,
	targetPropertyGuid VARCHAR(36) NOT NULL,
	PRIMARY KEY (propertyLinksGuid)
) 
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
	PRIMARY KEY (propMultiAttribGuid)
) 
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
	description VARCHAR,
	isActive CHAR(1),
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate TIMESTAMP,
	expirationDate TIMESTAMP,
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (relationGuid)
) 
;


CREATE TABLE @PREFIX@revision
(
	revisionGuid VARCHAR(36) NOT NULL,
	releaseGuid VARCHAR(36),
	revisionId VARCHAR(50) NOT NULL,
	changeAgent VARCHAR(50),
	revisionDate TIMESTAMP,
	revAppliedDate TIMESTAMP NOT NULL,
	editOrder DECIMAL(18),
	changeInstructions VARCHAR,
	description VARCHAR,
	PRIMARY KEY (revisionGuid)
) 
;


CREATE TABLE @PREFIX@systemRelease
(
	releaseGuid VARCHAR(36) NOT NULL,
	releaseURI VARCHAR(250) NOT NULL,
	releaseId VARCHAR(50),
	releaseDate TIMESTAMP NOT NULL,
	basedOnRelease VARCHAR(250),
	releaseAgency VARCHAR(250),
	description VARCHAR,
	PRIMARY KEY (releaseGuid)
) 
;





CREATE TABLE @PREFIX@revisionTableMetaData
(
	version VARCHAR(50) NOT NULL,
	description VARCHAR(255)
) 
;


