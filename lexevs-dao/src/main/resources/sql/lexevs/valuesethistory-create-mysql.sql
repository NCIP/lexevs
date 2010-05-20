SET FOREIGN_KEY_CHECKS=0;



CREATE TABLE @PREFIX@h_valueSetDefinition
(
	valueSetDefGuid VARCHAR(36) NOT NULL,
	valueSetDefURI VARCHAR(250) NOT NULL,
	valueSetDefName VARCHAR(250) NOT NULL,
	defaultCodingScheme VARCHAR(50),
	conceptDomain VARCHAR(200),
	description TEXT,
	releaseGuid VARCHAR(36),
	isActive CHAR(1),
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate TIMESTAMP,
	expirationDate TIMESTAMP,
	entryStateGuid VARCHAR(36),
	INDEX idx_vsdName (valueSetDefName ASC),
	INDEX idx_vsdURI (valueSetDefURI ASC)
) 
;


CREATE TABLE @PREFIX@h_vsdEntry
(
	vsdEntryGuid VARCHAR(36) NOT NULL,
	valueSetDefGuid VARCHAR(36) NOT NULL,
	ruleOrder DECIMAL(18) NOT NULL,
	operator VARCHAR(15) NOT NULL,
	codingSchemeReference VARCHAR(50),
	valueSetDefReference VARCHAR(250),
	entityCode VARCHAR(200),
	entityCodeNamespace VARCHAR(50),
	leafOnly CHAR(1),
	referenceAssociation VARCHAR(50),
	targetToSource CHAR(1),
	transitiveClosure CHAR(1),
	propertyRefCodingScheme VARCHAR(50),
	propertyName VARCHAR(50),
	propertyMatchValue TEXT,
	matchAlgorithm VARCHAR(250),
	format VARCHAR(50),
	isActive CHAR(1),
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate TIMESTAMP,
	expirationDate TIMESTAMP,
	entryStateGuid VARCHAR(36),
	INDEX idx_valueSetDefGuid (valueSetDefGuid ASC),
	INDEX idx_vsdEnt_entityCode (entityCode ASC)
) 
;

CREATE TABLE @PREFIX@h_vsMultiAttrib
(
	vsMultiAttribGuid VARCHAR(36) NOT NULL,
	referenceGuid VARCHAR(36) NOT NULL,
	referenceType VARCHAR(50) NOT NULL,
	attributeType VARCHAR(30) NOT NULL,
	attributeValue VARCHAR(250) NOT NULL,
	subRef VARCHAR(250),
	role VARCHAR(250),
	entryStateGuid VARCHAR(36)
) 
;


CREATE TABLE @PREFIX@h_vsPickList
(
	vsPickListGuid VARCHAR(36) NOT NULL,
	pickListId VARCHAR(50) NOT NULL,
	representsValueSetDefinition VARCHAR(250) NOT NULL,
	completeSet CHAR(1),
	defaultEntityCodeNamespace VARCHAR(50),
	defaultLanguage VARCHAR(32),
	defaultSortOrder VARCHAR(50),
	description TEXT,
	releaseGuid VARCHAR(36),
	isActive CHAR(1),
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate TIMESTAMP,
	expirationDate TIMESTAMP,
	entryStateGuid VARCHAR(36),
	INDEX idx_pickListId (pickListId ASC),
	INDEX idx_representsVSD (representsValueSetDefinition ASC)
) 
;


CREATE TABLE @PREFIX@h_vsPLEntry
(
	vsPLEntryGuid VARCHAR(36) NOT NULL,
	vsPickListGuid VARCHAR(36) NOT NULL,
	plEntryId VARCHAR(50) NOT NULL,
	entityCodeNamespace VARCHAR(50),
	entityCode VARCHAR(200) NOT NULL,
	entryOrder DECIMAL(18),
	isDefault CHAR(1),
	matchIfNoContext CHAR(1),
	propertyId VARCHAR(50),
	language VARCHAR(32),
	include CHAR(1),
	pickText TEXT,
	isActive CHAR(1),
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate TIMESTAMP,
	expirationDate TIMESTAMP,
	entryStateGuid VARCHAR(36),
	INDEX idx_vsPickListGuid (vsPickListGuid ASC),
	INDEX idx_entityCode (entityCode ASC)
) 
;


CREATE TABLE @PREFIX@h_vsProperty
(
	vsPropertyGuid VARCHAR(36) NOT NULL,
	referenceGuid VARCHAR(36) NOT NULL,
	referenceType VARCHAR(50) NOT NULL,
	propertyId VARCHAR(50),
	propertyType VARCHAR(15),
	propertyName VARCHAR(50) NOT NULL,
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
	effectiveDate TIMESTAMP,
	expirationDate TIMESTAMP,
	entryStateGuid VARCHAR(36) NOT NULL,
	INDEX idx_vsProperty (referenceGuid ASC, propertyId ASC, propertyName ASC)
) 
;


CREATE TABLE @PREFIX@h_vsPropertyMultiAttrib
(
	vsPropMultiAttribGuid VARCHAR(36) NOT NULL,
	vsPropertyGuid VARCHAR(36) NOT NULL,
	attributeType VARCHAR(30) NOT NULL,
	attributeId VARCHAR(50),
	attributeValue VARCHAR(250) NOT NULL,
	subRef VARCHAR(250),
	role VARCHAR(250),
	qualifierType VARCHAR(250),
	entryStateGuid VARCHAR(36),
	INDEX idx_vsPropertyGuid (vsPropertyGuid ASC)
) 
;

SET FOREIGN_KEY_CHECKS=1;
