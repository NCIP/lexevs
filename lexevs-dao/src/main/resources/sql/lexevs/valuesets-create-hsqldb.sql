CREATE TABLE valueDomain ( 
	valueDomainGuid varchar(36) NOT NULL,
	valueDomainURI varchar(250) NOT NULL,
	valueDomainName varchar(250) NOT NULL,
	defaultCodingScheme varchar(50),
	conceptDomain varchar(200),
	description varchar,
	releaseGuid varchar(36),
	isActive char(1),
	owner varchar(250),
	status varchar(50),
	effectiveDate timestamp,
	expirationDate timestamp,
	entryStateGuid varchar(36)
)
;

CREATE TABLE vdEntry ( 
	vdEntryGuid varchar(36) NOT NULL,
	valueDomainGuid varchar(36) NOT NULL,
	ruleOrder decimal(18) NOT NULL,
	operator varchar(15) NOT NULL,
	codingSchemeReference varchar(50),
	valueDomainReference varchar(250),
	entityCode varchar(200),
	entityCodeNamespace varchar(50),
	leafOnly char(1),
	referenceAssociation varchar(50),
	targetToSource char(1),
	transitiveClosure char(1),
	propertyRefCodingScheme varchar(50),
	propertyName varchar(50),
	propertyMatchValue varchar,
	matchAlgorithm varchar(250),
	format varchar(50),
	isActive char(1),
	owner varchar(250),
	status varchar(50),
	effectiveDate timestamp,
	expirationDate timestamp,
	entryStateGuid varchar(36)
)
;

CREATE TABLE vdEntryState ( 
	entryStateGuid varchar(36) NOT NULL,
	entryGuid varchar(36) NOT NULL,
	entryType varchar(50),
	changeType varchar(15),
	relativeOrder decimal(18),
	revisionGuid varchar(36),
	prevRevisionGuid varchar(36),
	prevEntryStateGuid varchar(36)
)
;

CREATE TABLE vdMultiAttrib ( 
	vdMultiAttribGuid varchar(36) NOT NULL,
	referenceGuid varchar(36) NOT NULL,
	referenceType varchar(50),
	attributeType varchar(30) NOT NULL,
	attributeId varchar(50),
	attributeValue varchar(250) NOT NULL,
	subRef varchar(250),
	role varchar(250),
	entryStateGuid varchar(36)
)
;

CREATE TABLE vdPickList ( 
	vdPickListGuid varchar(36) NOT NULL,
	pickListId varchar(50) NOT NULL,
	representsValueDomain varchar(250) NOT NULL,
	completeDomain char(1),
	defaultEntityCodeNamespace varchar(50),
	defaultLanguage varchar(50),
	defaultSortOrder varchar(50),
	description varchar,
	releaseGuid varchar(36),
	isActive char(1),
	owner varchar(250),
	status varchar(50),
	effectiveDate timestamp,
	expirationDate timestamp,
	entryStateGuid varchar(36)
)
;

CREATE TABLE vdPLEntry ( 
	vdPLEntryGuid varchar(36) NOT NULL,
	vdPickListGuid varchar(36) NOT NULL,
	plEntryId varchar(50) NOT NULL,
	entityCodeNamespace varchar(50),
	entityCode varchar(200) NOT NULL,
	entryOrder decimal(18),
	isDefault char(1),
	matchIfNoContext char(1),
	propertyId varchar(50),
	include char(1),
	pickText varchar,
	isActive char(1),
	owner varchar(250),
	status varchar(50),
	effectiveDate timestamp,
	expirationDate timestamp,
	entryStateGuid varchar(36)
)
;

CREATE TABLE vdProperty ( 
	vdPropertyGuid varchar(36) NOT NULL,
	referenceGuid varchar(36) NOT NULL,
	referenceType varchar(50) NOT NULL,
	propertyId varchar(50),
	propertyType varchar(15),
	propertyName varchar(50) NOT NULL,
	language varchar(32),
	format varchar(50),
	isPreferred char(1),
	matchIfNoContext char(1),
	degreeOfFidelity varchar(50),
	representationalForm varchar(50),
	propertyValue varchar NOT NULL,
	isActive char(1) NOT NULL,
	owner varchar(250),
	status varchar(50),
	effectiveDate timestamp,
	expirationDate timestamp,
	entryStateGuid varchar(36) NOT NULL
)
;

CREATE TABLE vdPropertyMultiAttrib ( 
	vdPropMultiAttribGuid varchar(36) NOT NULL,
	vdPropertyGuid varchar(36) NOT NULL,
	attributeType varchar(30) NOT NULL,
	attributeId varchar(50),
	attributeValue varchar(250) NOT NULL,
	subRef varchar(250),
	role varchar(250),
	qualifierType varchar(250),
	entryStateGuid varchar(36)
)
;

CREATE TABLE vdSupportedAttrib ( 
	vdSuppAttribGuid varchar(36) NOT NULL,
	referenceGuid varchar(36),
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


ALTER TABLE valueDomain ADD CONSTRAINT PK_valueDomain 
	PRIMARY KEY (valueDomainGuid)
;


ALTER TABLE vdEntry ADD CONSTRAINT PK_vdEntry 
	PRIMARY KEY (vdEntryGuid)
;


ALTER TABLE vdEntryState ADD CONSTRAINT PK_vdEntryState 
	PRIMARY KEY (entryStateGuid)
;


ALTER TABLE vdMultiAttrib ADD CONSTRAINT PK_vdMultiAttrib 
	PRIMARY KEY (vdMultiAttribGuid)
;


ALTER TABLE vdPickList ADD CONSTRAINT PK_vdPickList 
	PRIMARY KEY (vdPickListGuid)
;


ALTER TABLE vdPLEntry ADD CONSTRAINT PK_vdPLEntry 
	PRIMARY KEY (vdPLEntryGuid)
;


ALTER TABLE vdProperty ADD CONSTRAINT PK_vdProperty 
	PRIMARY KEY (vdPropertyGuid)
;


ALTER TABLE vdPropertyMultiAttrib ADD CONSTRAINT PK_vdPropertyMultiAttrib 
	PRIMARY KEY (vdPropMultiAttribGuid)
;


ALTER TABLE vdSupportedAttrib ADD CONSTRAINT PK_vdMapping 
	PRIMARY KEY (vdSuppAttribGuid)
;



CREATE INDEX idx_vdName
ON valueDomain (valueDomainName)
;
CREATE INDEX idx_vdURI
ON valueDomain (valueDomainURI)
;
ALTER TABLE vdEntry
	ADD CONSTRAINT UQ_vdEntry UNIQUE (valueDomainGuid, ruleOrder)
;
CREATE INDEX idx_valueDomainGuid
ON vdEntry (valueDomainGuid)
;
CREATE INDEX idx_vdEnt_entityCode
ON vdEntry (entityCode)
;
CREATE INDEX idx_pickListId
ON vdPickList (pickListId)
;
CREATE INDEX idx_representsValueDomain
ON vdPickList (representsValueDomain)
;
ALTER TABLE vdPLEntry
	ADD CONSTRAINT UQ_vdPLEntry UNIQUE (vdPickListGuid, plEntryId)
;
CREATE INDEX idx_vdPickListGuid
ON vdPLEntry (vdPickListGuid)
;
CREATE INDEX idx_entityCode
ON vdPLEntry (entityCode)
;
CREATE INDEX idx_vdProperty
ON vdProperty (referenceGuid, propertyId, propertyName)
;
ALTER TABLE vdProperty
	ADD CONSTRAINT UQ_vdProperty UNIQUE (referenceGuid, propertyId, propertyName)
;
CREATE INDEX idx_vdPropertyGuid
ON vdPropertyMultiAttrib (vdPropertyGuid)
;
ALTER TABLE vdSupportedAttrib
	ADD CONSTRAINT UQ_vdMapping UNIQUE (referenceGuid, supportedAttributeTag, id)
;
CREATE INDEX idx_saReferenceGuid
ON vdSupportedAttrib (referenceGuid)
;

ALTER TABLE valueDomain ADD CONSTRAINT FK_valDom_releaseGuid 
	FOREIGN KEY (releaseGuid) REFERENCES systemRelease (releaseGuid)
;

ALTER TABLE vdEntry ADD CONSTRAINT FK_vdEnt_vdGuid 
	FOREIGN KEY (valueDomainGuid) REFERENCES valueDomain (valueDomainGuid)
ON DELETE CASCADE
;

ALTER TABLE vdEntryState ADD CONSTRAINT FK_vdES_prevEntryStateGuid 
	FOREIGN KEY (prevEntryStateGuid) REFERENCES vdEntryState (entryStateGuid)
;

ALTER TABLE vdEntryState ADD CONSTRAINT FK_vdES_revisionGuid 
	FOREIGN KEY (revisionGuid) REFERENCES revision (revisionGuid)
;

ALTER TABLE vdEntryState ADD CONSTRAINT FK_vdES_prevRevisionGuid 
	FOREIGN KEY (prevRevisionGuid) REFERENCES revision (revisionGuid)
;

ALTER TABLE vdPickList ADD CONSTRAINT FK_vdPL_releaseGuid 
	FOREIGN KEY (releaseGuid) REFERENCES systemRelease (releaseGuid)
;

ALTER TABLE vdPLEntry ADD CONSTRAINT FK_vdPLEnt_vdPLGuid 
	FOREIGN KEY (vdPickListGuid) REFERENCES vdPickList (vdPickListGuid)
ON DELETE CASCADE
;

ALTER TABLE vdPropertyMultiAttrib ADD CONSTRAINT FK_vdProperty 
	FOREIGN KEY (vdPropertyGuid) REFERENCES vdProperty (vdPropertyGuid)
ON DELETE CASCADE
;
