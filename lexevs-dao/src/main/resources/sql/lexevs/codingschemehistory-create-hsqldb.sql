CREATE TABLE h_associationEntity ( 
	associationEntityGuid varchar(36) NOT NULL,
	entityGuid varchar(36) NOT NULL,
	forwardName varchar(100),
	reverseName varchar(100),
	isNavigable char(1),
	isTransitive char(1),
	isTranslationAssociation char(1),
	entryStateGuid varchar(36) NOT NULL
)
;

CREATE TABLE @PREFIX@h_codingScheme ( 
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
	entryStateGuid varchar(36) NOT NULL
)
;

CREATE TABLE @PREFIX@h_csMultiAttrib ( 
	csMultiAttribGuid varchar(36) NOT NULL,
	codingSchemeGuid varchar(36) NOT NULL,
	attributeType varchar(30) NOT NULL,
	attributeValue varchar(250) NOT NULL,
	subRef varchar(250),
	role varchar(250),
	entryStateGuid varchar(36) NOT NULL
)
;

CREATE TABLE @PREFIX@h_entity ( 
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
	entryStateGuid varchar(36) NOT NULL
)
;

CREATE TABLE @PREFIX@h_entityAssnQuals ( 
	entityAssnQualsGuid varchar(36) NOT NULL,
	referenceGuid varchar(36) NOT NULL,
	qualifierName varchar(50) NOT NULL,
	qualifierValue varchar(250) NOT NULL,
	entryStateGuid varchar(36) NOT NULL
)
;

CREATE TABLE @PREFIX@h_entityAssnsToData ( 
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
	entryStateGuid varchar(36) NOT NULL
)
;

CREATE TABLE @PREFIX@h_entityAssnsToEntity ( 
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
	entryStateGuid varchar(36) NOT NULL
)
;

CREATE TABLE @PREFIX@h_property ( 
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
	entryStateGuid varchar(36) NOT NULL
)
;

CREATE TABLE @PREFIX@h_propertyLinks ( 
	propertyLinksGuid varchar(36) NOT NULL,
	sourcePropertyGuid varchar(36) NOT NULL,
	link varchar(250) NOT NULL,
	targetPropertyGuid varchar(36) NOT NULL,
	entryStateGuid varchar(36) NOT NULL
)
;

CREATE TABLE @PREFIX@h_propertyMultiAttrib ( 
	propMultiAttribGuid varchar(36) NOT NULL,
	propertyGuid varchar(36) NOT NULL,
	attributeType varchar(30) NOT NULL,
	attributeId varchar(50),
	attributeValue varchar(250),
	subRef varchar(250),
	role varchar(250),
	qualifierType varchar(250),
	entryStateGuid varchar(36) NOT NULL
)
;

CREATE TABLE @PREFIX@h_relation ( 
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
	entryStateGuid varchar(36) NOT NULL
)
;


ALTER TABLE h_associationEntity ADD CONSTRAINT PK_h_associationEntity 
	PRIMARY KEY (entryStateGuid)
;


ALTER TABLE h_codingScheme ADD CONSTRAINT PK_h_codingScheme 
	PRIMARY KEY (entryStateGuid)
;


ALTER TABLE h_csMultiAttrib ADD CONSTRAINT PK_h_csMultiAttrib 
	PRIMARY KEY (csMultiAttribGuid,entryStateGuid)
;


ALTER TABLE h_entity ADD CONSTRAINT PK_h_entity 
	PRIMARY KEY (entryStateGuid)
;


ALTER TABLE h_entityAssnQuals ADD CONSTRAINT PK_h_entityAssnQuals 
	PRIMARY KEY (entryStateGuid)
;


ALTER TABLE h_entityAssnsToData ADD CONSTRAINT PK_h_entityAssnsToData 
	PRIMARY KEY (entryStateGuid)
;


ALTER TABLE h_entityAssnsToEntity ADD CONSTRAINT PK_h_entityAssnsToEntity 
	PRIMARY KEY (entryStateGuid)
;


ALTER TABLE h_property ADD CONSTRAINT PK_h_property 
	PRIMARY KEY (entryStateGuid)
;


ALTER TABLE h_propertyLinks ADD CONSTRAINT PK_h_propertyLinks 
	PRIMARY KEY (entryStateGuid)
;


ALTER TABLE h_propertyMultiAttrib ADD CONSTRAINT PK_h_propertyMultiAttrib 
	PRIMARY KEY (entryStateGuid)
;


ALTER TABLE h_relation ADD CONSTRAINT PK_h_relation 
	PRIMARY KEY (entryStateGuid)
;



CREATE INDEX idx_h_codingSchemeURI
ON h_codingScheme (codingSchemeURI)
;
CREATE INDEX idx_h_csMultiAttrib
ON h_csMultiAttrib (codingSchemeGuid, attributeType)
;
CREATE INDEX idx_h_entity
ON h_entity (codingSchemeGuid, entityCode)
;
CREATE INDEX idx_h_entityNS
ON h_entity (codingSchemeGuid, entityCode, entityCodeNamespace)
;
CREATE INDEX idx_h_entAsToData_source
ON h_entityAssnsToData (associationPredicateGuid, sourceEntityCode)
;
CREATE INDEX idx_h_entAsToEnt_source
ON h_entityAssnsToEntity (associationPredicateGuid, sourceEntityCode)
;
CREATE INDEX idx_h_entAsToEnt_sourceNS
ON h_entityAssnsToEntity (associationPredicateGuid, sourceEntityCode, sourceEntityCodeNamespace)
;
CREATE INDEX idx_h_entAsToEnt_target
ON h_entityAssnsToEntity (associationPredicateGuid, targetEntityCode)
;
CREATE INDEX idx_h_entAsToEnt_targetNS
ON h_entityAssnsToEntity (associationPredicateGuid, targetEntityCode, targetEntityCodeNamespace)
;
CREATE INDEX idx_h_referenceGuid
ON h_property (referenceGuid)
;
CREATE INDEX idx_h_sourcePropertyGuid
ON h_propertyLinks (sourcePropertyGuid)
;
CREATE INDEX idx_h_targetPropertyGuid
ON h_propertyLinks (targetPropertyGuid)
;
CREATE INDEX idx_h_propertyMultiAttrib
ON h_propertyMultiAttrib (propertyGuid)
;
