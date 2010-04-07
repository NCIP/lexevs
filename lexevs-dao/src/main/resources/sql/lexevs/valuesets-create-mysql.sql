SET FOREIGN_KEY_CHECKS=0;



CREATE TABLE valueSetDefinition
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
	PRIMARY KEY (valueSetDefGuid),
	KEY (releaseGuid),
	INDEX idx_vsdName (valueSetDefName ASC),
	INDEX idx_vsdURI (valueSetDefURI ASC)
) 
;


CREATE TABLE vsdEntry
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
	PRIMARY KEY (vsdEntryGuid),
	UNIQUE UQ_vsdEntry_valueSetDefGuid(vsdEntryGuid, valueSetDefGuid),
	INDEX idx_valueSetDefGuid (valueSetDefGuid ASC),
	INDEX idx_vsdEnt_entityCode (entityCode ASC)
) 
;


CREATE TABLE vsEntryState
(
	entryStateGuid VARCHAR(36) NOT NULL,
	entryGuid VARCHAR(36) NOT NULL,
	entryType VARCHAR(50),
	changeType VARCHAR(15),
	relativeOrder DECIMAL(18),
	revisionGuid VARCHAR(36),
	prevRevisionGuid VARCHAR(36),
	prevEntryStateGuid VARCHAR(36),
	PRIMARY KEY (entryStateGuid),
	KEY (prevEntryStateGuid),
	KEY (revisionGuid),
	KEY (prevRevisionGuid)
) 
;


CREATE TABLE vsMultiAttrib
(
	vsMultiAttribGuid VARCHAR(36) NOT NULL,
	referenceGuid VARCHAR(36) NOT NULL,
	referenceType VARCHAR(50),
	attributeType VARCHAR(30) NOT NULL,
	attributeId VARCHAR(50),
	attributeValue VARCHAR(250) NOT NULL,
	subRef VARCHAR(250),
	role VARCHAR(250),
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (vsMultiAttribGuid)
) 
;


CREATE TABLE vsPickList
(
	vsPickListGuid VARCHAR(36) NOT NULL,
	pickListId VARCHAR(50) NOT NULL,
	representsValueSetDefinition VARCHAR(250) NOT NULL,
	completeSet CHAR(1),
	defaultEntityCodeNamespace VARCHAR(50),
	defaultLanguage VARCHAR(50),
	defaultSortOrder VARCHAR(50),
	description TEXT,
	releaseGuid VARCHAR(36),
	isActive CHAR(1),
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate TIMESTAMP,
	expirationDate TIMESTAMP,
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (vsPickListGuid),
	KEY (releaseGuid),
	INDEX idx_pickListId (pickListId ASC),
	INDEX idx_representsVSD (representsValueSetDefinition ASC)
) 
;


CREATE TABLE vsPLEntry
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
	include CHAR(1),
	pickText TEXT,
	isActive CHAR(1),
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate TIMESTAMP,
	expirationDate TIMESTAMP,
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (vsPLEntryGuid),
	UNIQUE UQ_vsPLEntry_vsPickListGuid(plEntryId, vsPickListGuid),
	INDEX idx_vsPickListGuid (vsPickListGuid ASC),
	INDEX idx_entityCode (entityCode ASC)
) 
;


CREATE TABLE vsProperty
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
	isActive CHAR(1) NOT NULL,
	owner VARCHAR(250),
	status VARCHAR(50),
	effectiveDate TIMESTAMP,
	expirationDate TIMESTAMP,
	entryStateGuid VARCHAR(36) NOT NULL,
	PRIMARY KEY (vsPropertyGuid),
	UNIQUE UQ_vsProperty(referenceGuid, propertyId, propertyName),
	INDEX idx_vsProperty (referenceGuid ASC, propertyId ASC, propertyName ASC)
) 
;


CREATE TABLE vsPropertyMultiAttrib
(
	vsPropMultiAttribGuid VARCHAR(36) NOT NULL,
	vsPropertyGuid VARCHAR(36) NOT NULL,
	attributeType VARCHAR(30) NOT NULL,
	attributeId VARCHAR(50),
	attributeValue VARCHAR(250) NOT NULL,
	subRef VARCHAR(250),
	role VARCHAR(250),
	entryStateGuid VARCHAR(36),
	PRIMARY KEY (vsPropMultiAttribGuid),
	INDEX idx_vsPropertyGuid (vsPropertyGuid ASC)
) 
;


CREATE TABLE vsSupportedAttrib
(
	vsSuppAttribGuid VARCHAR(36) NOT NULL,
	referenceGuid VARCHAR(36),
	supportedAttributeTag VARCHAR(30) NOT NULL,
	id VARCHAR(250) NOT NULL,
	uri VARCHAR(250),
	idValue VARCHAR(250),
	rootCode VARCHAR(250),
	isForwardNavigable CHAR(1),
	isImported CHAR(1),
	equivalentCodingScheme VARCHAR(250),
	assemblyRule VARCHAR(250),
	PRIMARY KEY (vsSuppAttribGuid),
	UNIQUE UQ_vsMapping(referenceGuid, supportedAttributeTag, id),
	INDEX idx_saReferenceGuid (referenceGuid ASC)
) 
;



SET FOREIGN_KEY_CHECKS=1;


ALTER TABLE valueSetDefinition ADD CONSTRAINT FK_vsd_releaseGuid 
	FOREIGN KEY (releaseGuid) REFERENCES systemRelease (releaseGuid)
;

ALTER TABLE vsdEntry ADD CONSTRAINT FK_vsdEnt_vsdGuid 
	FOREIGN KEY (valueSetDefGuid) REFERENCES valueSetDefinition (valueSetDefGuid)
	ON DELETE CASCADE
;

ALTER TABLE vsEntryState ADD CONSTRAINT FK_vsES_prevEntryStateGuid 
	FOREIGN KEY (prevEntryStateGuid) REFERENCES vsEntryState (entryStateGuid)
;

ALTER TABLE vsPickList ADD CONSTRAINT FK_vsdPL_releaseGuid 
	FOREIGN KEY (releaseGuid) REFERENCES systemRelease (releaseGuid)
;

ALTER TABLE vsPLEntry ADD CONSTRAINT FK_vsPLEnt_vsPLGuid 
	FOREIGN KEY (vsPickListGuid) REFERENCES vsPickList (vsPickListGuid)
	ON DELETE CASCADE
;

ALTER TABLE vsPropertyMultiAttrib ADD CONSTRAINT FK_vsProperty 
	FOREIGN KEY (vsPropertyGuid) REFERENCES vsProperty (vsPropertyGuid)
	ON DELETE CASCADE
;
